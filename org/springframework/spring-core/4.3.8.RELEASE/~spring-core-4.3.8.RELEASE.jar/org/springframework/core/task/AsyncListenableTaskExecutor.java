// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AsyncListenableTaskExecutor.java

package org.springframework.core.task;

import java.util.concurrent.Callable;
import org.springframework.util.concurrent.ListenableFuture;

// Referenced classes of package org.springframework.core.task:
//			AsyncTaskExecutor

public interface AsyncListenableTaskExecutor
	extends AsyncTaskExecutor
{

	public abstract ListenableFuture submitListenable(Runnable runnable);

	public abstract ListenableFuture submitListenable(Callable callable);
}
