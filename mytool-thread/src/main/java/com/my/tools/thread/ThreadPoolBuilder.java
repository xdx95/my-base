package com.my.tools.thread;

import com.my.tools.monitor.TaskMonitor;
import com.my.tools.monitor.ThreadPoolMonitor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: xdx
 * @date: 2024/8/20
 * @description: 线程池执行器构造
 */
public class ThreadPoolBuilder {

	/**
	 * 线程池名
	 */
	private String poolName;

	/**
	 * 初始池大小
	 */
	private int corePoolSize;

	/**
	 * 最大池大小
	 */
	private int maxPoolSize;

	/**
	 * 非核心线程存活时间
	 */
	private long keepAliveTime;

	/**
	 * 线程存活时间单位
	 */
	private TimeUnit timeUnit;

	/**
	 * 任务队列，用于存放未执行的线程
	 */
	private BlockingQueue<Runnable> workQueue;

	/**
	 * 线程工厂，用于自定义线程创建
	 */
	private ThreadFactory threadFactory;

	/**
	 * 当线程阻塞（block）时的异常处理器，所谓线程阻塞即线程池和等待队列已满，无法处理线程时采取的策略
	 */
	private RejectedExecutionHandler rejectedHandler;


	public static ThreadPoolBuilder create() {
		return new ThreadPoolBuilder();
	}

	public ThreadPoolBuilder corePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}

	public ThreadPoolBuilder maxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	public ThreadPoolBuilder keepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
		return this;
	}

	public ThreadPoolBuilder timeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

	public ThreadPoolBuilder threadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
		if (threadFactory instanceof NamedThreadFactory) {
			NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
			this.poolName = namedThreadFactory.getNamePrefix();
		}
		return this;
	}

	public ThreadPoolBuilder workQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
		return this;
	}

	public ThreadPoolBuilder rejectedHandler(RejectedExecutionHandler rejectedHandler) {
		this.rejectedHandler = rejectedHandler;
		return this;
	}

	public ThreadPoolExecutor build() {
		ThreadPoolExecutor threadPoolExecutor = new MonitorThreadPoolExecutor(
			corePoolSize, maxPoolSize,
			keepAliveTime, timeUnit,
			workQueue, threadFactory, rejectedHandler);
		// 注册到线程池监控器
		ThreadPoolMonitor.getInstance().register(poolName, threadPoolExecutor);
		return threadPoolExecutor;
	}

	/**
	 * ScheduledThreadPoolExecutor
	 */
	public ScheduledThreadPoolExecutor buildScheduled() {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
			new ScheduledThreadPoolExecutor(
				corePoolSize, threadFactory, rejectedHandler);
		scheduledThreadPoolExecutor.setMaximumPoolSize(maxPoolSize);
		scheduledThreadPoolExecutor.setKeepAliveTime(keepAliveTime, timeUnit);
		// 注册到线程池监控器
		ThreadPoolMonitor.getInstance().register(poolName, scheduledThreadPoolExecutor);
		return scheduledThreadPoolExecutor;
	}

	/**
	 * EagerThreadPoolExecutor
	 */
	public EagerThreadPoolExecutor buildEager() {
		EagerThreadPoolExecutor eagerThreadPoolExecutor = new EagerThreadPoolExecutor(
			corePoolSize, maxPoolSize,
			keepAliveTime, timeUnit,
			workQueue, threadFactory, rejectedHandler);
		((EagerTaskQueue) workQueue).setExecutor(eagerThreadPoolExecutor);
		// 注册到线程池监控器
		ThreadPoolMonitor.getInstance().register(poolName, eagerThreadPoolExecutor);
		return eagerThreadPoolExecutor;
	}

}
