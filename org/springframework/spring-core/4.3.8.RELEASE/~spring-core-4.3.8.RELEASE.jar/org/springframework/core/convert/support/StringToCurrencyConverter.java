// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToCurrencyConverter.java

package org.springframework.core.convert.support;

import java.util.Currency;
import org.springframework.core.convert.converter.Converter;

class StringToCurrencyConverter
	implements Converter
{

	StringToCurrencyConverter()
	{
	}

	public Currency convert(String source)
	{
		return Currency.getInstance(source);
	}

	public volatile Object convert(Object obj)
	{
		return convert((String)obj);
	}
}
