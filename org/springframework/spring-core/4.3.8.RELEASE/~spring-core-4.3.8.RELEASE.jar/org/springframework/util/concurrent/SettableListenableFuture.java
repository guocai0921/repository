// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SettableListenableFuture.java

package org.springframework.util.concurrent;

import java.util.concurrent.*;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.util.concurrent:
//			ListenableFuture, ListenableFutureCallback, SuccessCallback, FailureCallback, 
//			ListenableFutureTask

public class SettableListenableFuture
	implements ListenableFuture
{
	private static class SettableTask extends ListenableFutureTask
	{

		private volatile Thread completingThread;

		public boolean setResultValue(Object value)
		{
			set(value);
			return checkCompletingThread();
		}

		public boolean setExceptionResult(Throwable exception)
		{
			setException(exception);
			return checkCompletingThread();
		}

		protected void done()
		{
			if (!isCancelled())
				completingThread = Thread.currentThread();
			super.done();
		}

		private boolean checkCompletingThread()
		{
			boolean check = completingThread == Thread.currentThread();
			if (check)
				completingThread = null;
			return check;
		}

		public SettableTask()
		{
			super(SettableListenableFuture.DUMMY_CALLABLE);
		}
	}


	private static final Callable DUMMY_CALLABLE = new Callable() {

		public Object call()
			throws Exception
		{
			throw new IllegalStateException("Should never be called");
		}

	};
	private final SettableTask settableTask = new SettableTask();

	public SettableListenableFuture()
	{
	}

	public boolean set(Object value)
	{
		return settableTask.setResultValue(value);
	}

	public boolean setException(Throwable exception)
	{
		Assert.notNull(exception, "Exception must not be null");
		return settableTask.setExceptionResult(exception);
	}

	public void addCallback(ListenableFutureCallback callback)
	{
		settableTask.addCallback(callback);
	}

	public void addCallback(SuccessCallback successCallback, FailureCallback failureCallback)
	{
		settableTask.addCallback(successCallback, failureCallback);
	}

	public boolean cancel(boolean mayInterruptIfRunning)
	{
		boolean cancelled = settableTask.cancel(mayInterruptIfRunning);
		if (cancelled && mayInterruptIfRunning)
			interruptTask();
		return cancelled;
	}

	public boolean isCancelled()
	{
		return settableTask.isCancelled();
	}

	public boolean isDone()
	{
		return settableTask.isDone();
	}

	public Object get()
		throws InterruptedException, ExecutionException
	{
		return settableTask.get();
	}

	public Object get(long timeout, TimeUnit unit)
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return settableTask.get(timeout, unit);
	}

	protected void interruptTask()
	{
	}


}
