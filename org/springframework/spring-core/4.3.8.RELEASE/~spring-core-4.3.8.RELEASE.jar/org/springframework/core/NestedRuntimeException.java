// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NestedRuntimeException.java

package org.springframework.core;


// Referenced classes of package org.springframework.core:
//			NestedExceptionUtils

public abstract class NestedRuntimeException extends RuntimeException
{

	private static final long serialVersionUID = 0x4b7e7648cb8f9f00L;

	public NestedRuntimeException(String msg)
	{
		super(msg);
	}

	public NestedRuntimeException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public String getMessage()
	{
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	public Throwable getRootCause()
	{
		Throwable rootCause = null;
		for (Throwable cause = getCause(); cause != null && cause != rootCause; cause = cause.getCause())
			rootCause = cause;

		return rootCause;
	}

	public Throwable getMostSpecificCause()
	{
		Throwable rootCause = getRootCause();
		return ((Throwable) (rootCause == null ? this : rootCause));
	}

	public boolean contains(Class exType)
	{
		if (exType == null)
			return false;
		if (exType.isInstance(this))
			return true;
		Throwable cause = getCause();
		if (cause == this)
			return false;
		if (cause instanceof NestedRuntimeException)
			return ((NestedRuntimeException)cause).contains(exType);
		do
		{
			if (cause == null)
				break;
			if (exType.isInstance(cause))
				return true;
			if (cause.getCause() == cause)
				break;
			cause = cause.getCause();
		} while (true);
		return false;
	}

	static 
	{
		org/springframework/core/NestedExceptionUtils.getName();
	}
}
