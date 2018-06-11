// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TaskExecutorAdapter.java

package org.springframework.core.task.support;

import java.util.concurrent.*;
import org.springframework.core.task.*;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

public class TaskExecutorAdapter
	implements AsyncListenableTaskExecutor
{

	private final Executor concurrentExecutor;
	private TaskDecorator taskDecorator;

	public TaskExecutorAdapter(Executor concurrentExecutor)
	{
		Assert.notNull(concurrentExecutor, "Executor must not be null");
		this.concurrentExecutor = concurrentExecutor;
	}

	public final void setTaskDecorator(TaskDecorator taskDecorator)
	{
		this.taskDecorator = taskDecorator;
	}

	public void execute(Runnable task)
	{
		try
		{
			doExecute(concurrentExecutor, taskDecorator, task);
		}
		catch (RejectedExecutionException ex)
		{
			throw new TaskRejectedException((new StringBuilder()).append("Executor [").append(concurrentExecutor).append("] did not accept task: ").append(task).toString(), ex);
		}
	}

	public void execute(Runnable task, long startTimeout)
	{
		execute(task);
	}

	public Future submit(Runnable task)
	{
		if (taskDecorator == null && (concurrentExecutor instanceof ExecutorService))
			return ((ExecutorService)concurrentExecutor).submit(task);
		FutureTask future;
		future = new FutureTask(task, null);
		doExecute(concurrentExecutor, taskDecorator, future);
		return future;
		RejectedExecutionException ex;
		ex;
		throw new TaskRejectedException((new StringBuilder()).append("Executor [").append(concurrentExecutor).append("] did not accept task: ").append(task).toString(), ex);
	}

	public Future submit(Callable task)
	{
		if (taskDecorator == null && (concurrentExecutor instanceof ExecutorService))
			return ((ExecutorService)concurrentExecutor).submit(task);
		FutureTask future;
		future = new FutureTask(task);
		doExecute(concurrentExecutor, taskDecorator, future);
		return future;
		RejectedExecutionException ex;
		ex;
		throw new TaskRejectedException((new StringBuilder()).append("Executor [").append(concurrentExecutor).append("] did not accept task: ").append(task).toString(), ex);
	}

	public ListenableFuture submitListenable(Runnable task)
	{
		ListenableFutureTask future;
		future = new ListenableFutureTask(task, null);
		doExecute(concurrentExecutor, taskDecorator, future);
		return future;
		RejectedExecutionException ex;
		ex;
		throw new TaskRejectedException((new StringBuilder()).append("Executor [").append(concurrentExecutor).append("] did not accept task: ").append(task).toString(), ex);
	}

	public ListenableFuture submitListenable(Callable task)
	{
		ListenableFutureTask future;
		future = new ListenableFutureTask(task);
		doExecute(concurrentExecutor, taskDecorator, future);
		return future;
		RejectedExecutionException ex;
		ex;
		throw new TaskRejectedException((new StringBuilder()).append("Executor [").append(concurrentExecutor).append("] did not accept task: ").append(task).toString(), ex);
	}

	protected void doExecute(Executor concurrentExecutor, TaskDecorator taskDecorator, Runnable runnable)
		throws RejectedExecutionException
	{
		concurrentExecutor.execute(taskDecorator == null ? runnable : taskDecorator.decorate(runnable));
	}
}
