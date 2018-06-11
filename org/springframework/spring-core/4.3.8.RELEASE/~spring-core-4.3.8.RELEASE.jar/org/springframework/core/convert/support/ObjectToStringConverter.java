// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectToStringConverter.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

final class ObjectToStringConverter
	implements Converter
{

	ObjectToStringConverter()
	{
	}

	public String convert(Object source)
	{
		return source.toString();
	}

	public volatile Object convert(Object obj)
	{
		return convert(obj);
	}
}
