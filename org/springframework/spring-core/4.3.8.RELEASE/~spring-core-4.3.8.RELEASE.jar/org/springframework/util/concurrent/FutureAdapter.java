// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FutureAdapter.java

package org.springframework.util.concurrent;

import java.util.concurrent.*;
import org.springframework.util.Assert;

public abstract class FutureAdapter
	implements Future
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
			return (State)Enum.valueOf(org/springframework/util/concurrent/FutureAdapter$State, name);
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


	private final Future adaptee;
	private Object result;
	private State state;
	private final Object mutex = new Object();

	protected FutureAdapter(Future adaptee)
	{
		result = null;
		state = State.NEW;
		Assert.notNull(adaptee, "'delegate' must not be null");
		this.adaptee = adaptee;
	}

	protected Future getAdaptee()
	{
		return adaptee;
	}

	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return adaptee.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled()
	{
		return adaptee.isCancelled();
	}

	public boolean isDone()
	{
		return adaptee.isDone();
	}

	public Object get()
		throws InterruptedException, ExecutionException
	{
		return adaptInternal(adaptee.get());
	}

	public Object get(long timeout, TimeUnit unit)
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return adaptInternal(adaptee.get(timeout, unit));
	}

	final Object adaptInternal(Object adapteeResult)
		throws ExecutionException
	{
		Object obj = mutex;
		JVM INSTR monitorenter ;
		static class 1
		{

			static final int $SwitchMap$org$springframework$util$concurrent$FutureAdapter$State[];

			static 
			{
				$SwitchMap$org$springframework$util$concurrent$FutureAdapter$State = new int[State.values().length];
				try
				{
					$SwitchMap$org$springframework$util$concurrent$FutureAdapter$State[State.SUCCESS.ordinal()] = 1;
				}
				catch (NoSuchFieldError nosuchfielderror) { }
				try
				{
					$SwitchMap$org$springframework$util$concurrent$FutureAdapter$State[State.FAILURE.ordinal()] = 2;
				}
				catch (NoSuchFieldError nosuchfielderror1) { }
				try
				{
					$SwitchMap$org$springframework$util$concurrent$FutureAdapter$State[State.NEW.ordinal()] = 3;
				}
				catch (NoSuchFieldError nosuchfielderror2) { }
			}
		}

		1..SwitchMap.org.springframework.util.concurrent.FutureAdapter.State[state.ordinal()];
		JVM INSTR tableswitch 1 3: default 123
	//	               1 44
	//	               2 51
	//	               3 59;
		   goto _L1 _L2 _L3 _L4
_L2:
		return result;
_L3:
		throw (ExecutionException)result;
_L4:
		Object adapted;
		adapted = adapt(adapteeResult);
		result = adapted;
		state = State.SUCCESS;
		adapted;
		obj;
		JVM INSTR monitorexit ;
		return;
		ExecutionException ex;
		ex;
		result = ex;
		state = State.FAILURE;
		throw ex;
		ex;
		ExecutionException execEx = new ExecutionException(ex);
		result = execEx;
		state = State.FAILURE;
		throw execEx;
_L1:
		throw new IllegalStateException();
		Exception exception;
		exception;
		throw exception;
	}

	protected abstract Object adapt(Object obj)
		throws ExecutionException;
}
