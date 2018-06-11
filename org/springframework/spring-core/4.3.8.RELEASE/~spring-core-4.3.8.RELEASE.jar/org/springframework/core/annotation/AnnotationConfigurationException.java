// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationConfigurationException.java

package org.springframework.core.annotation;

import org.springframework.core.NestedRuntimeException;

public class AnnotationConfigurationException extends NestedRuntimeException
{

	public AnnotationConfigurationException(String message)
	{
		super(message);
	}

	public AnnotationConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
