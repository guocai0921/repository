// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StopWatch.java

package org.springframework.util;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

public class StopWatch
{
	public static final class TaskInfo
	{

		private final String taskName;
		private final long timeMillis;

		public String getTaskName()
		{
			return taskName;
		}

		public long getTimeMillis()
		{
			return timeMillis;
		}

		public double getTimeSeconds()
		{
			return (double)timeMillis / 1000D;
		}

		TaskInfo(String taskName, long timeMillis)
		{
			this.taskName = taskName;
			this.timeMillis = timeMillis;
		}
	}


	private final String id;
	private boolean keepTaskList;
	private final List taskList;
	private long startTimeMillis;
	private boolean running;
	private String currentTaskName;
	private TaskInfo lastTaskInfo;
	private int taskCount;
	private long totalTimeMillis;

	public StopWatch()
	{
		this("");
	}

	public StopWatch(String id)
	{
		keepTaskList = true;
		taskList = new LinkedList();
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setKeepTaskList(boolean keepTaskList)
	{
		this.keepTaskList = keepTaskList;
	}

	public void start()
		throws IllegalStateException
	{
		start("");
	}

	public void start(String taskName)
		throws IllegalStateException
	{
		if (running)
		{
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		} else
		{
			running = true;
			currentTaskName = taskName;
			startTimeMillis = System.currentTimeMillis();
			return;
		}
	}

	public void stop()
		throws IllegalStateException
	{
		if (!running)
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		long lastTime = System.currentTimeMillis() - startTimeMillis;
		totalTimeMillis += lastTime;
		lastTaskInfo = new TaskInfo(currentTaskName, lastTime);
		if (keepTaskList)
			taskList.add(lastTaskInfo);
		taskCount++;
		running = false;
		currentTaskName = null;
	}

	public boolean isRunning()
	{
		return running;
	}

	public String currentTaskName()
	{
		return currentTaskName;
	}

	public long getLastTaskTimeMillis()
		throws IllegalStateException
	{
		if (lastTaskInfo == null)
			throw new IllegalStateException("No tasks run: can't get last task interval");
		else
			return lastTaskInfo.getTimeMillis();
	}

	public String getLastTaskName()
		throws IllegalStateException
	{
		if (lastTaskInfo == null)
			throw new IllegalStateException("No tasks run: can't get last task name");
		else
			return lastTaskInfo.getTaskName();
	}

	public TaskInfo getLastTaskInfo()
		throws IllegalStateException
	{
		if (lastTaskInfo == null)
			throw new IllegalStateException("No tasks run: can't get last task info");
		else
			return lastTaskInfo;
	}

	public long getTotalTimeMillis()
	{
		return totalTimeMillis;
	}

	public double getTotalTimeSeconds()
	{
		return (double)totalTimeMillis / 1000D;
	}

	public int getTaskCount()
	{
		return taskCount;
	}

	public TaskInfo[] getTaskInfo()
	{
		if (!keepTaskList)
			throw new UnsupportedOperationException("Task info is not being kept!");
		else
			return (TaskInfo[])taskList.toArray(new TaskInfo[taskList.size()]);
	}

	public String shortSummary()
	{
		return (new StringBuilder()).append("StopWatch '").append(getId()).append("': running time (millis) = ").append(getTotalTimeMillis()).toString();
	}

	public String prettyPrint()
	{
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		if (!keepTaskList)
		{
			sb.append("No task info kept");
		} else
		{
			sb.append("-----------------------------------------\n");
			sb.append("ms     %     Task name\n");
			sb.append("-----------------------------------------\n");
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(5);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			TaskInfo ataskinfo[] = getTaskInfo();
			int i = ataskinfo.length;
			for (int j = 0; j < i; j++)
			{
				TaskInfo task = ataskinfo[j];
				sb.append(nf.format(task.getTimeMillis())).append("  ");
				sb.append(pf.format(task.getTimeSeconds() / getTotalTimeSeconds())).append("  ");
				sb.append(task.getTaskName()).append("\n");
			}

		}
		return sb.toString();
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder(shortSummary());
		if (keepTaskList)
		{
			TaskInfo ataskinfo[] = getTaskInfo();
			int i = ataskinfo.length;
			for (int j = 0; j < i; j++)
			{
				TaskInfo task = ataskinfo[j];
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeMillis());
				long percent = Math.round((100D * task.getTimeSeconds()) / getTotalTimeSeconds());
				sb.append(" = ").append(percent).append("%");
			}

		} else
		{
			sb.append("; no task info kept");
		}
		return sb.toString();
	}
}
