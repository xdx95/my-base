package com.my.tools.thread;

import com.my.tools.base.LogUtils;
import java.util.HashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/16
 * @description: 命名线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {

	public static final Logger log = LogUtils.get();

	// 线程名集合
	private static final HashMap<String, AtomicInteger> name_prefix_map = new HashMap<>();

	// 线程计数器
	private final AtomicInteger thread_number = new AtomicInteger(1);
	// 线程组
	private final ThreadGroup group;
	// 线程名前缀
	private final String name_prefix;


	public NamedThreadFactory(String namePrefix) {
		SecurityManager securityManager = System.getSecurityManager();
		this.group = (securityManager != null) ? securityManager.getThreadGroup()
			: Thread.currentThread().getThreadGroup();
		synchronized (name_prefix_map) {
			if (name_prefix_map.containsKey(namePrefix)) {
				this.name_prefix =
					namePrefix + "-" + name_prefix_map.get(namePrefix).getAndIncrement();
			} else {
				this.name_prefix = namePrefix;
			}
			name_prefix_map.put(this.name_prefix, new AtomicInteger(2));
		}
	}

	/**
	 * 创建一个新的线程
	 */
	@Override
	public Thread newThread(Runnable runnable) {
		String threadName = this.name_prefix + "-" + thread_number.getAndIncrement();
		Thread thread = new Thread(group, runnable, threadName, 0);
		thread.setDaemon(false);
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}

	public String getNamePrefix() {
		return name_prefix;
	}
}

