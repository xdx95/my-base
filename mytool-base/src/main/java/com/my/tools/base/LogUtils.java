package com.my.tools.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: xdx
 * @date: 2024/8/16
 * @description: 日志工具类
 */
public class LogUtils {

	/**
	 * 获取当前线程调用栈信息 StackTraceElement[]
	 * 0 是 Thread.getStackTrace() 方法本身，位于顶部
	 * 1 是 get() 当前执行的方法，
	 * 2 是调用入口
	 *
	 * @return Logger 实例
	 */
	public static Logger get() {
		// 获取当前线程的调用栈信息
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		String className = stackTrace[2].getClassName();
		return LoggerFactory.getLogger(className);
	}

}
