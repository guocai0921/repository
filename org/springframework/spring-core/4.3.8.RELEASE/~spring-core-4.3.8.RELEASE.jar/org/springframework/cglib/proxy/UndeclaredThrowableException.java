// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UndeclaredThrowableException.java

package org.springframework.cglib.proxy;

import org.springframework.cglib.core.CodeGenerationException;

public class UndeclaredThrowableException extends CodeGenerationException
{

	public UndeclaredThrowableException(Throwable t)
	{
		super(t);
	}

	public Throwable getUndeclaredThrowable()
	{
		return getCause();
	}
}
