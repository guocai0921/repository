// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToCharsetConverter.java

package org.springframework.core.convert.support;

import java.nio.charset.Charset;
import org.springframework.core.convert.converter.Converter;

class StringToCharsetConverter
	implements Converter
{

	StringToCharsetConverter()
	{
	}

	public Charset convert(String source)
	{
		return Charset.forName(source);
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}
}
