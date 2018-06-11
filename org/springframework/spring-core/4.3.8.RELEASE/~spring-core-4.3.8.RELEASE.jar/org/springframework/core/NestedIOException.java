// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NestedIOException.java

package org.springframework.core;

import java.io.IOException;

// Referenced classes of package org.springframework.core:
//			NestedExceptionUtils

public class NestedIOException extends IOException
{

	public NestedIOException(String msg)
	{
		super(msg);
	}

	public NestedIOException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public String getMessage()
	{
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	static 
	{
		org/springframework/core/NestedExceptionUtils.getName();
	}
}
