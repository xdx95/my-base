package com.my.tools.balancer.checker;

import com.my.tools.base.LogUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/1
 * @description: http 服务健康检查器
 */
public class HttpServerHealthChecker extends AbstractHealthChecker {

	private static final Logger log = LogUtils.get();

	private String protocol = "http";
	private String healthCheckEndpoint = "/health";

	public HttpServerHealthChecker() {
	}

	public HttpServerHealthChecker(String healthCheckEndpoint) {
		this.healthCheckEndpoint = healthCheckEndpoint;
	}

	public HttpServerHealthChecker(String protocol, String healthCheckEndpoint) {
		this.protocol = protocol;
		this.healthCheckEndpoint = healthCheckEndpoint;
	}

	@Override
	public CheckerEnums type() {
		return CheckerEnums.HTTP;
	}

	@Override
	public boolean healthCheck(String host, int port, int timeout) {
		boolean health = false;
		String serverUrl = protocol + "://" + host + ":" + port + healthCheckEndpoint;
		try {
			URL url = new URL(serverUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			int responseCode = connection.getResponseCode();
			log.info("http server health check result,{}, {}", serverUrl, responseCode);
			health = responseCode == 200;
		} catch (Exception e) {
			log.error("http server health check exception,{}, {}", serverUrl, e.getMessage());
		}
		return health;
	}
}
