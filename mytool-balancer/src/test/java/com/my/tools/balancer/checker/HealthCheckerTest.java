package com.my.tools.balancer.checker;

import com.my.tools.base.LogUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

class HealthCheckerTest {

	public static final Logger log = LogUtils.get();
	public HealthChecker healthChecker;

	@BeforeEach
	void setUp() {
		healthChecker = new HttpServerHealthChecker();
	}

	@Test
	void type() {
		log.info("health checker type:{}",healthChecker.type().name().toLowerCase());
	}

	@Test
	void healthCheck() {
		healthChecker.healthCheck("127.0.0.1",8080,5000);
	}
}