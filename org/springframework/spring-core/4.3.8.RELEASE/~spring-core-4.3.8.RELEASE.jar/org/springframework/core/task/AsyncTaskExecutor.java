// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AsyncTaskExecutor.java

package org.springframework.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

// Referenced classes of package org.springframework.core.task:
//			TaskExecutor

public interface AsyncTaskExecutor
	extends TaskExecutor
{

	public static final long TIMEOUT_IMMEDIATE = 0L;
	public static final long TIMEOUT_INDEFINITE = 0x7fffffffffffffffL;

	public abstract void execute(Runnable runnable, long l);

	public abstract Future submit(Runnable runnable);

	public abstract Future submit(Callable callable);
}
