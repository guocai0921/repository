// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SyncTaskExecutor.java

package org.springframework.core.task;

import java.io.Serializable;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.task:
//			TaskExecutor

public class SyncTaskExecutor
	implements TaskExecutor, Serializable
{

	public SyncTaskExecutor()
	{
	}

	public void execute(Runnable task)
	{
		Assert.notNull(task, "Runnable must not be null");
		task.run();
	}
}
