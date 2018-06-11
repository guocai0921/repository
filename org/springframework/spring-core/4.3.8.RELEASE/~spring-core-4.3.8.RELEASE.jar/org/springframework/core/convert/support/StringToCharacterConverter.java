// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToCharacterConverter.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

final class StringToCharacterConverter
	implements Converter
{

	StringToCharacterConverter()
	{
	}

	public Character convert(String source)
	{
		if (source.isEmpty())
			return null;
		if (source.length() > 1)
			throw new IllegalArgumentException((new StringBuilder()).append("Can only convert a [String] with length of 1 to a [Character]; string value '").append(source).append("'  has length of ").append(source.length()).toString());
		else
			return Character.valueOf(source.charAt(0));
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}
}
