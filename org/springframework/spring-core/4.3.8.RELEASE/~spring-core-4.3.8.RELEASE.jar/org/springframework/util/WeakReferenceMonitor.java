// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   WeakReferenceMonitor.java

package org.springframework.util;

import java.lang.ref.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @deprecated Class WeakReferenceMonitor is deprecated
 */

public class WeakReferenceMonitor
{
	public static interface ReleaseListener
	{

		public abstract void released();
	}

	private static class MonitoringProcess
		implements Runnable
	{

		public void run()
		{
			WeakReferenceMonitor.logger.debug("Starting reference monitor thread");
_L2:
			if (!WeakReferenceMonitor.keepMonitoringThreadAlive())
				break; /* Loop/switch isn't completed */
			Reference reference = WeakReferenceMonitor.handleQueue.remove();
			ReleaseListener entry = WeakReferenceMonitor.removeEntry(reference);
			if (entry != null)
				try
				{
					entry.released();
				}
				catch (Throwable ex)
				{
					WeakReferenceMonitor.logger.warn("Reference release listener threw exception", ex);
				}
			if (true) goto _L2; else goto _L1
			InterruptedException ex;
			ex;
			synchronized (org/springframework/util/WeakReferenceMonitor)
			{
				WeakReferenceMonitor.monitoringThread = null;
			}
			WeakReferenceMonitor.logger.debug("Reference monitor thread interrupted", ex);
_L1:
		}

		private MonitoringProcess()
		{
		}

	}


	private static final Log logger = LogFactory.getLog(org/springframework/util/WeakReferenceMonitor);
	private static final ReferenceQueue handleQueue = new ReferenceQueue();
	private static final Map trackedEntries = new HashMap();
	private static Thread monitoringThread = null;

	public WeakReferenceMonitor()
	{
	}

	public static void monitor(Object handle, ReleaseListener listener)
	{
		if (logger.isDebugEnabled())
			logger.debug((new StringBuilder()).append("Monitoring handle [").append(handle).append("] with release listener [").append(listener).append("]").toString());
		WeakReference weakRef = new WeakReference(handle, handleQueue);
		addEntry(weakRef, listener);
	}

	private static void addEntry(Reference ref, ReleaseListener entry)
	{
		synchronized (org/springframework/util/WeakReferenceMonitor)
		{
			trackedEntries.put(ref, entry);
			if (monitoringThread == null)
			{
				monitoringThread = new Thread(new MonitoringProcess(), org/springframework/util/WeakReferenceMonitor.getName());
				monitoringThread.setDaemon(true);
				monitoringThread.start();
			}
		}
	}

	private static ReleaseListener removeEntry(Reference reference)
	{
		/*<invalid signature>*/java.lang.Object local = org/springframework/util/WeakReferenceMonitor;
		JVM INSTR monitorenter ;
		return (ReleaseListener)trackedEntries.remove(reference);
		Exception exception;
		exception;
		throw exception;
	}

	private static boolean keepMonitoringThreadAlive()
	{
		/*<invalid signature>*/java.lang.Object local = org/springframework/util/WeakReferenceMonitor;
		JVM INSTR monitorenter ;
		if (!trackedEntries.isEmpty())
			return true;
		logger.debug("No entries left to track - stopping reference monitor thread");
		monitoringThread = null;
		false;
		local;
		JVM INSTR monitorexit ;
		return;
		Exception exception;
		exception;
		throw exception;
	}






}
