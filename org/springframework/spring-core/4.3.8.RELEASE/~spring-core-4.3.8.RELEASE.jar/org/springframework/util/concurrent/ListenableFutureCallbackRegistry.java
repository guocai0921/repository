// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListenableFutureCallbackRegistry.java

package org.springframework.util.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.util.concurrent:
//			SuccessCallback, FailureCallback, ListenableFutureCallback

public class ListenableFutureCallbackRegistry
{
	private static final class State extends Enum
	{

		public static final State NEW;
		public static final State SUCCESS;
		public static final State FAILURE;
		private static final State $VALUES[];

		public static State[] values()
		{
			return (State[])$VALUES.clone();
		}

		public static State valueOf(String name)
		{
			return (State)Enum.valueOf(org/springframework/util/concurrent/ListenableFutureCallbackRegistry$State, name);
		}

		static 
		{
			NEW = new State("NEW", 0);
			SUCCESS = new State("SUCCESS", 1);
			FAILURE = new State("FAILURE", 2);
			$VALUES = (new State[] {
				NEW, SUCCESS, FAILURE
			});
		}

		private State(String s, int i)
		{
			super(s, i);
		}
	}


	private final Queue successCallbacks = new LinkedList();
	private final Queue failureCallbacks = new LinkedList();
	private State state;
	private Object result;
	private final Object mutex = new Object();

	public ListenableFutureCallbackRegistry()
	{
		state = State.NEW;
		result = null;
	}

	public void addCallback(ListenableFutureCallback callback)
	{
		Assert.notNull(callback, "'callback' must not be null");
		static class 1
		{

			static final int $SwitchMap$org$springframework$util$concurrent$ListenableFutureCallbackRegistry$State[];

			static 
			{
				$SwitchMap$org$springframework$util$concurrent$ListenableFutureCallbackRegistry$State = new int[State.values().length];
				try
				{
					$SwitchMap$org$springframework$util$concurrent$ListenableFutureCallbackRegistry$State[State.NEW.ordinal()] = 1;
				}
				catch (NoSuchFieldError nosuchfielderror) { }
				try
				{
					$SwitchMap$org$springframework$util$concurrent$ListenableFutureCallbackRegistry$State[State.SUCCESS.ordinal()] = 2;
				}
				catch (NoSuchFieldError nosuchfielderror1) { }
				try
				{
					$SwitchMap$org$springframework$util$concurrent$ListenableFutureCallbackRegistry$State[State.FAILURE.ordinal()] = 3;
				}
				catch (NoSuchFieldError nosuchfielderror2) { }
			}
		}

		synchronized (mutex)
		{
			switch (1..SwitchMap.org.springframework.util.concurrent.ListenableFutureCallbackRegistry.State[state.ordinal()])
			{
			case 1: // '\001'
				successCallbacks.add(callback);
				failureCallbacks.add(callback);
				break;

			case 2: // '\002'
				notifySuccess(callback);
				break;

			case 3: // '\003'
				notifyFailure(callback);
				break;
			}
		}
	}

	private void notifySuccess(SuccessCallback callback)
	{
		try
		{
			callback.onSuccess(result);
		}
		catch (Throwable throwable) { }
	}

	private void notifyFailure(FailureCallback callback)
	{
		try
		{
			callback.onFailure((Throwable)result);
		}
		catch (Throwable throwable) { }
	}

	public void addSuccessCallback(SuccessCallback callback)
	{
		Assert.notNull(callback, "'callback' must not be null");
		synchronized (mutex)
		{
			switch (1..SwitchMap.org.springframework.util.concurrent.ListenableFutureCallbackRegistry.State[state.ordinal()])
			{
			case 1: // '\001'
				successCallbacks.add(callback);
				break;

			case 2: // '\002'
				notifySuccess(callback);
				break;
			}
		}
	}

	public void addFailureCallback(FailureCallback callback)
	{
		Assert.notNull(callback, "'callback' must not be null");
		synchronized (mutex)
		{
			switch (1..SwitchMap.org.springframework.util.concurrent.ListenableFutureCallbackRegistry.State[state.ordinal()])
			{
			case 1: // '\001'
				failureCallbacks.add(callback);
				break;

			case 3: // '\003'
				notifyFailure(callback);
				break;
			}
		}
	}

	public void success(Object result)
	{
		synchronized (mutex)
		{
			state = State.SUCCESS;
			this.result = result;
			for (; !successCallbacks.isEmpty(); notifySuccess((SuccessCallback)successCallbacks.poll()));
		}
	}

	public void failure(Throwable ex)
	{
		synchronized (mutex)
		{
			state = State.FAILURE;
			result = ex;
			for (; !failureCallbacks.isEmpty(); notifyFailure((FailureCallback)failureCallbacks.poll()));
		}
	}
}
