// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConstantException.java

package org.springframework.core;


public class ConstantException extends IllegalArgumentException
{

	public ConstantException(String className, String field, String message)
	{
		super((new StringBuilder()).append("Field '").append(field).append("' ").append(message).append(" in class [").append(className).append("]").toString());
	}

	public ConstantException(String className, String namePrefix, Object value)
	{
		super((new StringBuilder()).append("No '").append(namePrefix).append("' field with value '").append(value).append("' found in class [").append(className).append("]").toString());
	}
}
