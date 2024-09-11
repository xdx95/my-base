package com.my.tools.balancer.checker;

import com.my.tools.base.LogUtils;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/1
 * @description: socket 服务健康检查器
 */
public class SocketServerHealthChecker extends AbstractHealthChecker {

	private static final Logger log = LogUtils.get();

	@Override
	public CheckerEnums type() {
		return CheckerEnums.SOCKET;
	}

	@Override
	public boolean healthCheck(String host, int port, int timeout) {
		try (Socket socket = new Socket()) {
			// 创建一个Socket实例，并设置连接的超时时间
			socket.connect(new InetSocketAddress(host, port), timeout);
			// 如果连接成功，则认为服务可用
			log.info("socket server health check result,{}:{}, {}", host, port, true);
			return true;
		} catch (Exception e) {
			log.error("socket server health check exception,{}:{}, {}", host, port, e.getMessage());
		}
		return false;
	}
}
