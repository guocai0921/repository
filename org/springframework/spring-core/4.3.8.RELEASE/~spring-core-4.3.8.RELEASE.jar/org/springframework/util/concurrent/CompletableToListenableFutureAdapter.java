// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CompletableToListenableFutureAdapter.java

package org.springframework.util.concurrent;

import java.util.concurrent.*;
import java.util.function.BiFunction;

// Referenced classes of package org.springframework.util.concurrent:
//			ListenableFutureCallbackRegistry, ListenableFuture, ListenableFutureCallback, SuccessCallback, 
//			FailureCallback

public class CompletableToListenableFutureAdapter
	implements ListenableFuture
{

	private final CompletableFuture completableFuture;
	private final ListenableFutureCallbackRegistry callbacks;

	public CompletableToListenableFutureAdapter(CompletionStage completionStage)
	{
		this(completionStage.toCompletableFuture());
	}

	public CompletableToListenableFutureAdapter(CompletableFuture completableFuture)
	{
		callbacks = new ListenableFutureCallbackRegistry();
		this.completableFuture = completableFuture;
		this.completableFuture.handle(new BiFunction() {

			final CompletableToListenableFutureAdapter this$0;

			public Object apply(Object result, Throwable ex)
			{
				if (ex != null)
					callbacks.failure(ex);
				else
					callbacks.success(result);
				return null;
			}

			public volatile Object apply(Object obj, Object obj1)
			{
				return apply(obj, (Throwable)obj1);
			}

			
			{
				this.this$0 = CompletableToListenableFutureAdapter.this;
				super();
			}
		});
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

	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return completableFuture.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled()
	{
		return completableFuture.isCancelled();
	}

	public boolean isDone()
	{
		return completableFuture.isDone();
	}

	public Object get()
		throws InterruptedException, ExecutionException
	{
		return completableFuture.get();
	}

	public Object get(long timeout, TimeUnit unit)
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return completableFuture.get(timeout, unit);
	}

}
