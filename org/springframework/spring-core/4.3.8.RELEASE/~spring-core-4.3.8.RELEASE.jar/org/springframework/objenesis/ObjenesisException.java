// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjenesisException.java

package org.springframework.objenesis;


public class ObjenesisException extends RuntimeException
{

	private static final long serialVersionUID = 0xdad8917c1aaa2ea8L;

	public ObjenesisException(String msg)
	{
		super(msg);
	}

	public ObjenesisException(Throwable cause)
	{
		super(cause);
	}

	public ObjenesisException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
