// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListenableFutureTask.java

package org.springframework.util.concurrent;

import java.util.concurrent.*;

// Referenced classes of package org.springframework.util.concurrent:
//			ListenableFutureCallbackRegistry, ListenableFuture, ListenableFutureCallback, SuccessCallback, 
//			FailureCallback

public class ListenableFutureTask extends FutureTask
	implements ListenableFuture
{

	private final ListenableFutureCallbackRegistry callbacks;

	public ListenableFutureTask(Callable callable)
	{
		super(callable);
		callbacks = new ListenableFutureCallbackRegistry();
	}

	public ListenableFutureTask(Runnable runnable, Object result)
	{
		super(runnable, result);
		callbacks = new ListenableFutureCallbackRegistry();
	}

	public void addCallback(ListenableFutureCallback callback)
	{
		callbacks.addCallback(callback);
	}

	public void addCallback(SuccessCallback successCallback, FailureCallback failureCallback)
	{
		callbacks.addSuccessCallback(successCallback);
		callbacks.addFailureCallback(failureCallback);
	}

	protected void done()
	{
		Throwable cause;
		try
		{
			Object result = get();
			callbacks.success(result);
			return;
		}
		catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
			return;
		}
		catch (ExecutionException ex)
		{
			cause = ex.getCause();
			if (cause == null)
				cause = ex;
		}
		catch (Throwable ex)
		{
			cause = ex;
		}
		callbacks.failure(cause);
	}
}
