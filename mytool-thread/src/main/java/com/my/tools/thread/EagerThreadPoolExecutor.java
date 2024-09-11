package com.my.tools.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xdx
 * @date: 2024/8/30
 * @description: IO 特性线程池 优先创建线程去执行任务，当满足最大线程池后，进入任务队列
 */
public class EagerThreadPoolExecutor extends MonitorThreadPoolExecutor {

	private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

	public EagerThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
		TimeUnit unit, BlockingQueue<Runnable> workQueue,
		ThreadFactory threadFactory,
		RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
			handler);
	}

	public int getSubmittedTaskCount() {
		return submittedTaskCount.get();
	}

	@Override
	public void execute(Runnable command) {
		if (command == null) {
			throw new NullPointerException();
		}
		submittedTaskCount.incrementAndGet();
		try {
			super.execute(command);
		} catch (RejectedExecutionException rx) {
			EagerTaskQueue queue = (EagerTaskQueue) getQueue();
			try {
				if (!queue.force(command, 0, TimeUnit.MILLISECONDS)) {
					submittedTaskCount.decrementAndGet();
					throw new RejectedExecutionException("Queue capacity is full.", rx);
				}
			} catch (InterruptedException x) {
				submittedTaskCount.decrementAndGet();
				throw new RejectedExecutionException(x);
			}
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		submittedTaskCount.decrementAndGet();
		super.afterExecute(r, t);
	}
}



