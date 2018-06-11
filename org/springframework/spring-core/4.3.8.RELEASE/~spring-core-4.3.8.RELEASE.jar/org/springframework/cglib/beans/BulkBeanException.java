// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BulkBeanException.java

package org.springframework.cglib.beans;


public class BulkBeanException extends RuntimeException
{

	private int index;
	private Throwable cause;

	public BulkBeanException(String message, int index)
	{
		super(message);
		this.index = index;
	}

	public BulkBeanException(Throwable cause, int index)
	{
		super(cause.getMessage());
		this.index = index;
		this.cause = cause;
	}

	public int getIndex()
	{
		return index;
	}

	public Throwable getCause()
	{
		return cause;
	}
}
