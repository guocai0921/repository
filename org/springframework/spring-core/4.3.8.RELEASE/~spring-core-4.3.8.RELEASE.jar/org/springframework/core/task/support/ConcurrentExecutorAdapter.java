// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConcurrentExecutorAdapter.java

package org.springframework.core.task.support;

import java.util.concurrent.Executor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;

public class ConcurrentExecutorAdapter
	implements Executor
{

	private final TaskExecutor taskExecutor;

	public ConcurrentExecutorAdapter(TaskExecutor taskExecutor)
	{
		Assert.notNull(taskExecutor, "TaskExecutor must not be null");
		this.taskExecutor = taskExecutor;
	}

	public void execute(Runnable command)
	{
		taskExecutor.execute(command);
	}
}
