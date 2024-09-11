package com.my.tools.monitor;

import java.util.Map;

/**
 * @author: xdx
 * @date: 2024/8/27
 * @description: 监控器
 */
public interface Monitor {

	// 数据采集
	Map<String, Object> collect();
    // 类型
	MonitorType type();

	void start();

	void stop();
}
