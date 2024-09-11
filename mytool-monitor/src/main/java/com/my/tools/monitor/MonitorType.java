package com.my.tools.monitor;

/**
 * @author: xdx
 * @date: 2024/8/28
 * @description: 监控器类型
 */
public enum MonitorType {

	THREAD(ThreadPoolMonitor.getInstance()),
	TASK(TaskMonitor.getInstance()),
	JVM(JvmMonitor.getInstance());

	private final Monitor monitor;

	MonitorType(Monitor monitor) {
		this.monitor = monitor;
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public static MonitorType getMonitorType(String name) {
		for (MonitorType monitorType : MonitorType.values()) {
			if (monitorType.name().equalsIgnoreCase(name)) {
				return monitorType;
			}
		}
		return null;
	}
}
