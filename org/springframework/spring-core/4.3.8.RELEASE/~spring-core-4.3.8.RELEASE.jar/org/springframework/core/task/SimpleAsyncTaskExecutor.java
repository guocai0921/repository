// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleAsyncTaskExecutor.java

package org.springframework.core.task;

import java.io.Serializable;
import java.util.concurrent.*;
import org.springframework.util.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

// Referenced classes of package org.springframework.core.task:
//			AsyncTaskExecutor, AsyncListenableTaskExecutor, TaskDecorator

public class SimpleAsyncTaskExecutor extends CustomizableThreadCreator
	implements AsyncListenableTaskExecutor, Serializable
{
	private class ConcurrencyThrottlingRunnable
		implements Runnable
	{

		private final Runnable target;
		final SimpleAsyncTaskExecutor this$0;

		public void run()
		{
			target.run();
			concurrencyThrottle.afterAccess();
			break MISSING_BLOCK_LABEL_35;
			Exception exception;
			exception;
			concurrencyThrottle.afterAccess();
			throw exception;
		}

		public ConcurrencyThrottlingRunnable(Runnable target)
		{
			this$0 = SimpleAsyncTaskExecutor.this;
			super();
			this.target = target;
		}
	}

	private static class ConcurrencyThrottleAdapter extends ConcurrencyThrottleSupport
	{

		protected void beforeAccess()
		{
			super.beforeAccess();
		}

		protected void afterAccess()
		{
			super.afterAccess();
		}

		private ConcurrencyThrottleAdapter()
		{
		}

	}


	public static final int UNBOUNDED_CONCURRENCY = -1;
	public static final int NO_CONCURRENCY = 0;
	private final ConcurrencyThrottleAdapter concurrencyThrottle;
	private ThreadFactory threadFactory;
	private TaskDecorator taskDecorator;

	public SimpleAsyncTaskExecutor()
	{
		concurrencyThrottle = new ConcurrencyThrottleAdapter();
	}

	public SimpleAsyncTaskExecutor(String threadNamePrefix)
	{
		super(threadNamePrefix);
		concurrencyThrottle = new ConcurrencyThrottleAdapter();
	}

	public SimpleAsyncTaskExecutor(ThreadFactory threadFactory)
	{
		concurrencyThrottle = new ConcurrencyThrottleAdapter();
		this.threadFactory = threadFactory;
	}

	public void setThreadFactory(ThreadFactory threadFactory)
	{
		this.threadFactory = threadFactory;
	}

	public final ThreadFactory getThreadFactory()
	{
		return threadFactory;
	}

	public final void setTaskDecorator(TaskDecorator taskDecorator)
	{
		this.taskDecorator = taskDecorator;
	}

	public void setConcurrencyLimit(int concurrencyLimit)
	{
		concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
	}

	public final int getConcurrencyLimit()
	{
		return concurrencyThrottle.getConcurrencyLimit();
	}

	public final boolean isThrottleActive()
	{
		return concurrencyThrottle.isThrottleActive();
	}

	public void execute(Runnable task)
	{
		execute(task, 0x7fffffffffffffffL);
	}

	public void execute(Runnable task, long startTimeout)
	{
		Assert.notNull(task, "Runnable must not be null");
		Runnable taskToUse = taskDecorator == null ? task : taskDecorator.decorate(task);
		if (isThrottleActive() && startTimeout > 0L)
		{
			concurrencyThrottle.beforeAccess();
			doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
		} else
		{
			doExecute(taskToUse);
		}
	}

	public Future submit(Runnable task)
	{
		FutureTask future = new FutureTask(task, null);
		execute(future, 0x7fffffffffffffffL);
		return future;
	}

	public Future submit(Callable task)
	{
		FutureTask future = new FutureTask(task);
		execute(future, 0x7fffffffffffffffL);
		return future;
	}

	public ListenableFuture submitListenable(Runnable task)
	{
		ListenableFutureTask future = new ListenableFutureTask(task, null);
		execute(future, 0x7fffffffffffffffL);
		return future;
	}

	public ListenableFuture submitListenable(Callable task)
	{
		ListenableFutureTask future = new ListenableFutureTask(task);
		execute(future, 0x7fffffffffffffffL);
		return future;
	}

	protected void doExecute(Runnable task)
	{
		Thread thread = threadFactory == null ? createThread(task) : threadFactory.newThread(task);
		thread.start();
	}

}
