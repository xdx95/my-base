package com.my.tools.monitor;

/**
 * @author: xdx
 * @date: 2024/8/29
 * @description: 监控配置
 */
public class MonitorConfig {

	// 应用编码
	private String appCode = "MY";
	// 开启监控
	private boolean enable = true;
	// 是否监控自己
	private boolean self = true;
	// 监控器
	private String[] types = {MonitorType.THREAD.name().toLowerCase(),MonitorType.TASK.name().toLowerCase()};
	// 任务调度间隔
	private int period = 5;
	// 慢任务
	private int slowTaskTime = 3000;
	// 监控平台服务地址
	private String[] monitorServers = {};

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}

	public int getSlowTaskTime() {
		return slowTaskTime;
	}

	public void setSlowTaskTime(int slowTaskTime) {
		this.slowTaskTime = slowTaskTime;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String[] getMonitorServers() {
		return monitorServers;
	}

	public void setMonitorServers(String[] monitorServers) {
		this.monitorServers = monitorServers;
	}
}
