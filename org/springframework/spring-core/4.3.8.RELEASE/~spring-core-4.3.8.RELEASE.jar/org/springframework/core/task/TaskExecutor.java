// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TaskExecutor.java

package org.springframework.core.task;

import java.util.concurrent.Executor;

public interface TaskExecutor
	extends Executor
{

	public abstract void execute(Runnable runnable);
}
