// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TaskRejectedException.java

package org.springframework.core.task;

import java.util.concurrent.RejectedExecutionException;

public class TaskRejectedException extends RejectedExecutionException
{

	public TaskRejectedException(String msg)
	{
		super(msg);
	}

	public TaskRejectedException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
