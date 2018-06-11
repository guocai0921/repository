// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConversionException.java

package org.springframework.core.convert;

import org.springframework.core.NestedRuntimeException;

public abstract class ConversionException extends NestedRuntimeException
{

	public ConversionException(String message)
	{
		super(message);
	}

	public ConversionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
