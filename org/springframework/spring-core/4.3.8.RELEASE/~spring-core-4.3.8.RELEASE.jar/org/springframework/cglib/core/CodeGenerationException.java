// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CodeGenerationException.java

package org.springframework.cglib.core;


public class CodeGenerationException extends RuntimeException
{

	private Throwable cause;

	public CodeGenerationException(Throwable cause)
	{
		super((new StringBuilder()).append(cause.getClass().getName()).append("-->").append(cause.getMessage()).toString());
		this.cause = cause;
	}

	public Throwable getCause()
	{
		return cause;
	}
}
