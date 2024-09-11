package com.my.tools.monitor;

/**
 * @author: xdx
 * @date: 2024/9/5
 * @description: 任务详情
 */
public class TaskInfo {

	private  Long id;
	private  String name;
	private  String message;
	private  Long startTime;
	private  Long endTime;

	public TaskInfo() {
	}

	public TaskInfo(Long id, String name, Long startTime) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
