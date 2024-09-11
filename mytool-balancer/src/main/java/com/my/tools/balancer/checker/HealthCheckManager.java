package com.my.tools.balancer.checker;

import com.my.tools.balancer.context.BalancerContext;
import com.my.tools.balancer.server.Server;
import com.my.tools.balancer.strategy.StrategyEnums;
import com.my.tools.base.LogUtils;
import com.my.tools.thread.ThreadUtils;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/9
 * @description: 健康检查管理器
 */
public class HealthCheckManager {

	private static final Logger log = LogUtils.get();

	private final ScheduledExecutorService
		executor = Executors.newScheduledThreadPool(1, ThreadUtils.newThreadFactory("check"));

	private final BalancerContext context;
	private final HealthChecker checker;

	public HealthCheckManager(BalancerContext context, HealthChecker checker) {
		this.context = context;
		this.checker = checker;
	}

	public void start() {
		if (context.config().isEnableHealthCheck()) {
			log.info("health check manager start scheduling.....");
			scheduledCheck();
		}
	}

	public void scheduledCheck() {
		executor.scheduleWithFixedDelay(() -> {
			context.servers().forEach(server -> {
				boolean health = checker.healthCheck(server.getHost(), server.getPort(),
					context.config().getHealthCheckTimeout());
				if (health != server.isHealthy()) {
					changesStatus(server, health);
				}
			});
		}, 0, context.config().getHealthCheckInterval(), TimeUnit.MILLISECONDS);
	}

	/**
	 * @param server 服务
	 * @param health 健康状态
	 * @description: 加权轮询和最小连接数，需要根据服务状态，动态变更数值。 服务上线:避免短时间命中内大量请求，设置预热连接数为当前最小连接数。
	 * 服务上下线:保证加权轮询的均衡性，需要重新计算 total weights ;
	 * @author: xdx
	 * @date: 2024/8/8
	 */
	public void changesStatus(Server server, boolean health) {
		log.info("Health-check,changes server status,{},{}", server.getName(), health);
		// 服务上线，预热当前连接数 = 可用服务的最小链接数
		if (health && StrategyEnums.LEAST_CONNECTIONS == context.getStrategy()) {
			Server minServer = context.servers().stream().filter(Server::isHealthy).min(
				Comparator.comparingInt(Server::getCurrentConnections)
			).orElse(null);
			assert minServer != null;
			server.currentConnections().set(minServer.getCurrentConnections());
		}
		// 服务上下线，重新计算总权重
		if (StrategyEnums.WEIGHTED_ROUND_ROBIN == context.getStrategy()) {
			int newTotal = context.totalWeights()
				.addAndGet(health ? server.getWeight() : -server.getWeight());
			log.info("Health-check,server {} {} , total weights:{}", server.getName(), health,
				newTotal);
		}
		server.setHealthy(health);
	}

	public BalancerContext getContext() {
		return context;
	}

	public HealthChecker getChecker() {
		return checker;
	}
}
