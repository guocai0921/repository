// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListenableFutureAdapter.java

package org.springframework.util.concurrent;

import java.util.concurrent.ExecutionException;

// Referenced classes of package org.springframework.util.concurrent:
//			FutureAdapter, ListenableFuture, ListenableFutureCallback, SuccessCallback, 
//			FailureCallback

public abstract class ListenableFutureAdapter extends FutureAdapter
	implements ListenableFuture
{

	protected ListenableFutureAdapter(ListenableFuture adaptee)
	{
		super(adaptee);
	}

	public void addCallback(ListenableFutureCallback callback)
	{
		addCallback(((SuccessCallback) (callback)), ((FailureCallback) (callback)));
	}

	public void addCallback(final SuccessCallback successCallback, final FailureCallback failureCallback)
	{
		ListenableFuture listenableAdaptee = (ListenableFuture)getAdaptee();
		listenableAdaptee.addCallback(new ListenableFutureCallback() {

			final SuccessCallback val$successCallback;
			final FailureCallback val$failureCallback;
			final ListenableFutureAdapter this$0;

			public void onSuccess(Object result)
			{
				Object adapted;
				try
				{
					adapted = adaptInternal(result);
				}
				catch (ExecutionException ex)
				{
					Throwable cause = ex.getCause();
					onFailure(((Throwable) (cause == null ? ((Throwable) (ex)) : cause)));
					return;
				}
				catch (Throwable ex)
				{
					onFailure(ex);
					return;
				}
				successCallback.onSuccess(adapted);
			}

			public void onFailure(Throwable ex)
			{
				failureCallback.onFailure(ex);
			}

			
			{
				this.this$0 = ListenableFutureAdapter.this;
				successCallback = successcallback;
				failureCallback = failurecallback;
				super();
			}
		});
	}
}
