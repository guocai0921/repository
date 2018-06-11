// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CustomizableThreadCreator.java

package org.springframework.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

// Referenced classes of package org.springframework.util:
//			ClassUtils

public class CustomizableThreadCreator
	implements Serializable
{

	private String threadNamePrefix;
	private int threadPriority;
	private boolean daemon;
	private ThreadGroup threadGroup;
	private final AtomicInteger threadCount;

	public CustomizableThreadCreator()
	{
		threadPriority = 5;
		daemon = false;
		threadCount = new AtomicInteger(0);
		threadNamePrefix = getDefaultThreadNamePrefix();
	}

	public CustomizableThreadCreator(String threadNamePrefix)
	{
		threadPriority = 5;
		daemon = false;
		threadCount = new AtomicInteger(0);
		this.threadNamePrefix = threadNamePrefix == null ? getDefaultThreadNamePrefix() : threadNamePrefix;
	}

	public void setThreadNamePrefix(String threadNamePrefix)
	{
		this.threadNamePrefix = threadNamePrefix == null ? getDefaultThreadNamePrefix() : threadNamePrefix;
	}

	public String getThreadNamePrefix()
	{
		return threadNamePrefix;
	}

	public void setThreadPriority(int threadPriority)
	{
		this.threadPriority = threadPriority;
	}

	public int getThreadPriority()
	{
		return threadPriority;
	}

	public void setDaemon(boolean daemon)
	{
		this.daemon = daemon;
	}

	public boolean isDaemon()
	{
		return daemon;
	}

	public void setThreadGroupName(String name)
	{
		threadGroup = new ThreadGroup(name);
	}

	public void setThreadGroup(ThreadGroup threadGroup)
	{
		this.threadGroup = threadGroup;
	}

	public ThreadGroup getThreadGroup()
	{
		return threadGroup;
	}

	public Thread createThread(Runnable runnable)
	{
		Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
		thread.setPriority(getThreadPriority());
		thread.setDaemon(isDaemon());
		return thread;
	}

	protected String nextThreadName()
	{
		return (new StringBuilder()).append(getThreadNamePrefix()).append(threadCount.incrementAndGet()).toString();
	}

	protected String getDefaultThreadNamePrefix()
	{
		return (new StringBuilder()).append(ClassUtils.getShortName(getClass())).append("-").toString();
	}
}
