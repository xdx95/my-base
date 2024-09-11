package com.my.tools.monitor;

import com.my.tools.base.JsonUtils;
import com.my.tools.base.LogUtils;
import java.util.Map;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/9/3
 * @description: 任务监控器
 */
public class TaskMonitor extends AbstractMonitor {

	public static final Logger log = LogUtils.get();

	private static final TaskMonitor INSTANCE = new TaskMonitor();

	private static final int slowTaskTime = MonitorManager.getInstance().getConfig()
		.getSlowTaskTime();

	private TaskMonitor() {
	}

	/**
	 * 获取单例实例
	 */
	public static TaskMonitor getInstance() {
		return INSTANCE;
	}


	/**
	 * 注册任务
	 */
	public void register(TaskInfo taskInfo) {
		long duration = taskInfo.getEndTime() - taskInfo.getStartTime();
		if (duration > slowTaskTime) {
			log.info("{}", JsonUtils.format(taskInfo));
		}
	}

	@Override
	public Map<String, Object> collect() {
		return null;
	}

	@Override
	public MonitorType type() {
		return MonitorType.TASK;
	}
}
