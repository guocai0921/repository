// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConcurrencyThrottleSupport.java

package org.springframework.util;

import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ConcurrencyThrottleSupport
	implements Serializable
{

	public static final int UNBOUNDED_CONCURRENCY = -1;
	public static final int NO_CONCURRENCY = 0;
	protected transient Log logger;
	private transient Object monitor;
	private int concurrencyLimit;
	private int concurrencyCount;

	public ConcurrencyThrottleSupport()
	{
		logger = LogFactory.getLog(getClass());
		monitor = new Object();
		concurrencyLimit = -1;
		concurrencyCount = 0;
	}

	public void setConcurrencyLimit(int concurrencyLimit)
	{
		this.concurrencyLimit = concurrencyLimit;
	}

	public int getConcurrencyLimit()
	{
		return concurrencyLimit;
	}

	public boolean isThrottleActive()
	{
		return concurrencyLimit > 0;
	}

	protected void beforeAccess()
	{
		if (concurrencyLimit == 0)
			throw new IllegalStateException("Currently no invocations allowed - concurrency limit set to NO_CONCURRENCY");
		if (concurrencyLimit > 0)
		{
			boolean debug = logger.isDebugEnabled();
			synchronized (monitor)
			{
				boolean interrupted = false;
				while (concurrencyCount >= concurrencyLimit) 
				{
					if (interrupted)
						throw new IllegalStateException("Thread was interrupted while waiting for invocation access, but concurrency limit still does not allow for entering");
					if (debug)
						logger.debug((new StringBuilder()).append("Concurrency count ").append(concurrencyCount).append(" has reached limit ").append(concurrencyLimit).append(" - blocking").toString());
					try
					{
						monitor.wait();
					}
					catch (InterruptedException ex)
					{
						Thread.currentThread().interrupt();
						interrupted = true;
					}
				}
				if (debug)
					logger.debug((new StringBuilder()).append("Entering throttle at concurrency count ").append(concurrencyCount).toString());
				concurrencyCount++;
			}
		}
	}

	protected void afterAccess()
	{
		if (concurrencyLimit >= 0)
			synchronized (monitor)
			{
				concurrencyCount--;
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Returning from throttle at concurrency count ").append(concurrencyCount).toString());
				monitor.notify();
			}
	}

	private void readObject(ObjectInputStream ois)
		throws IOException, ClassNotFoundException
	{
		ois.defaultReadObject();
		logger = LogFactory.getLog(getClass());
		monitor = new Object();
	}
}
