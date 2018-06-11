// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListenableFuture.java

package org.springframework.util.concurrent;

import java.util.concurrent.Future;

// Referenced classes of package org.springframework.util.concurrent:
//			ListenableFutureCallback, SuccessCallback, FailureCallback

public interface ListenableFuture
	extends Future
{

	public abstract void addCallback(ListenableFutureCallback listenablefuturecallback);

	public abstract void addCallback(SuccessCallback successcallback, FailureCallback failurecallback);
}
