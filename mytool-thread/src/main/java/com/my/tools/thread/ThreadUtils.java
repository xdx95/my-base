package com.my.tools.thread;

import com.my.tools.base.LogUtils;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/16
 * @description: 线程工具类
 */
public class ThreadUtils {

	public static final Logger log = LogUtils.get();

	/**
	 * 默认任务队列容量
	 */
	public static final int DEFAULT_QUEUE_CAPACITY = 1024;
	/**
	 * 默认最大线程数
	 */
	public static final int DEFAULT_MAX_POOL_SIZE = 1024;

	public static ThreadFactory newThreadFactory(String namePrefix) {
		return new NamedThreadFactory(namePrefix);
	}

	public static ThreadPoolExecutor newSingleThreadPool(String namePrefix) {
		return ThreadPoolBuilder.create()
			.corePoolSize(1)
			.maxPoolSize(1)
			.keepAliveTime(0L)
			.timeUnit(TimeUnit.SECONDS)
			.threadFactory(newThreadFactory(namePrefix))
			.workQueue(new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY))
			.rejectedHandler(RejectPolicy.ABORT.value())
			.build();
	}

	public static ThreadPoolExecutor newFixedThreadPool(int nThread, String namePrefix) {
		return ThreadPoolBuilder.create()
			.corePoolSize(nThread)
			.maxPoolSize(nThread)
			.keepAliveTime(0L)
			.timeUnit(TimeUnit.SECONDS)
			.threadFactory(newThreadFactory(namePrefix))
			.workQueue(new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY))
			.rejectedHandler(RejectPolicy.ABORT.value())
			.build();
	}

	public static ScheduledThreadPoolExecutor newScheduledThreadPool(int nThread,
		String namePrefix) {
		return ThreadPoolBuilder.create()
			.corePoolSize(nThread)
			.maxPoolSize(nThread)
			.keepAliveTime(0L)
			.timeUnit(TimeUnit.SECONDS)
			.threadFactory(newThreadFactory(namePrefix))
			.buildScheduled();
	}

	public static ThreadPoolExecutor newCachedThreadPool(String namePrefix) {
		return ThreadPoolBuilder.create()
			.corePoolSize(0)
			.maxPoolSize(1024)
			.keepAliveTime(60L)
			.timeUnit(TimeUnit.SECONDS)
			.threadFactory(newThreadFactory(namePrefix))
			.workQueue(new SynchronousQueue<>())
			.rejectedHandler(RejectPolicy.ABORT.value())
			.buildScheduled();
	}

	public static ThreadPoolExecutor newEagerThreadPool(String namePrefix) {
		return ThreadPoolBuilder.create()
			.corePoolSize(1)
			.maxPoolSize(25)
			.keepAliveTime(60L)
			.timeUnit(TimeUnit.SECONDS)
			.threadFactory(newThreadFactory(namePrefix))
			.workQueue(new EagerTaskQueue(1024))
			.rejectedHandler(RejectPolicy.ABORT.value())
			.buildEager();
	}


	public static void sleep(long millis) {
		if (millis > 0) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException ignored) {
			}
		}
	}

	public static String getThreadName() {
		return Thread.currentThread().getName();
	}

	public static String getThreadPoolName(ThreadPoolExecutor executor) {
		ThreadFactory threadFactory = executor.getThreadFactory();
		if (threadFactory instanceof NamedThreadFactory) {
			NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
			return namedThreadFactory.getNamePrefix();
		} else {
			log.warn("unsupported thread factory types:{}", threadFactory.toString());
		}
		return "";
	}

	private ThreadUtils() {

	}
}
