// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToLocaleConverter.java

package org.springframework.core.convert.support;

import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

final class StringToLocaleConverter
	implements Converter
{

	StringToLocaleConverter()
	{
	}

	public Locale convert(String source)
	{
		return StringUtils.parseLocaleString(source);
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}
}
