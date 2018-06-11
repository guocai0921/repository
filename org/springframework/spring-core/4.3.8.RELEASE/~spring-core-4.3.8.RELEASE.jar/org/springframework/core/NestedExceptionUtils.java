// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NestedExceptionUtils.java

package org.springframework.core;


public abstract class NestedExceptionUtils
{

	public NestedExceptionUtils()
	{
	}

	public static String buildMessage(String message, Throwable cause)
	{
		if (cause != null)
		{
			StringBuilder sb = new StringBuilder();
			if (message != null)
				sb.append(message).append("; ");
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		} else
		{
			return message;
		}
	}
}
