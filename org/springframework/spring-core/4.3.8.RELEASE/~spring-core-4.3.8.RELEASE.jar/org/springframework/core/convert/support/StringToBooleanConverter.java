// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToBooleanConverter.java

package org.springframework.core.convert.support;

import java.util.HashSet;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;

final class StringToBooleanConverter
	implements Converter
{

	private static final Set trueValues;
	private static final Set falseValues;

	StringToBooleanConverter()
	{
	}

	public Boolean convert(String source)
	{
		String value = source.trim();
		if ("".equals(value))
			return null;
		value = value.toLowerCase();
		if (trueValues.contains(value))
			return Boolean.TRUE;
		if (falseValues.contains(value))
			return Boolean.FALSE;
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid boolean value '").append(source).append("'").toString());
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}

	static 
	{
		trueValues = new HashSet(4);
		falseValues = new HashSet(4);
		trueValues.add("true");
		trueValues.add("on");
		trueValues.add("yes");
		trueValues.add("1");
		falseValues.add("false");
		falseValues.add("off");
		falseValues.add("no");
		falseValues.add("0");
	}
}
