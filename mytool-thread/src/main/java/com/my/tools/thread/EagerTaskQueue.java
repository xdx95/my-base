package com.my.tools.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: xdx
 * @date: 2024/8/30
 * @description:
 */
public class EagerTaskQueue extends LinkedBlockingQueue<Runnable> {

	private static final long serialVersionUID = -1L;

	private transient EagerThreadPoolExecutor executor;

	public EagerTaskQueue(int queueCapacity) {
		super(queueCapacity);
	}

	public void setExecutor(EagerThreadPoolExecutor executor) {
		this.executor = executor;
	}

	@Override
	public boolean offer(Runnable runnable) {
		if (executor == null) {
			throw new RejectedExecutionException("The task queue does not have executor.");
		}
		if (executor.getPoolSize() == executor.getMaximumPoolSize()) {
			return super.offer(runnable);
		}
		if (executor.getSubmittedTaskCount() <= executor.getPoolSize()) {
			return super.offer(runnable);
		}
		if (executor.getPoolSize() < executor.getMaximumPoolSize()) {
			return false;
		}
		return super.offer(runnable);
	}

	/**
	 * 尝试强制加入队列
	 */
	public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
		if (executor.isShutdown()) {
			throw new RejectedExecutionException("Executor is shutdown.");
		}
		return super.offer(o, timeout, unit);
	}
}