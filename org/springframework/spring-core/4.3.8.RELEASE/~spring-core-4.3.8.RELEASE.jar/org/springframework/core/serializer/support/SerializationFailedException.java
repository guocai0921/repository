// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializationFailedException.java

package org.springframework.core.serializer.support;

import org.springframework.core.NestedRuntimeException;

public class SerializationFailedException extends NestedRuntimeException
{

	public SerializationFailedException(String message)
	{
		super(message);
	}

	public SerializationFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
