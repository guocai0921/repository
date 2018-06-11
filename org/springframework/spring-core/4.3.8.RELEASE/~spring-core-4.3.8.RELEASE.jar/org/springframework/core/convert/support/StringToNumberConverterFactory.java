// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToNumberConverterFactory.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;

final class StringToNumberConverterFactory
	implements ConverterFactory
{
	private static final class StringToNumber
		implements Converter
	{

		private final Class targetType;

		public Number convert(String source)
		{
			if (source.isEmpty())
				return null;
			else
				return NumberUtils.parseNumber(source, targetType);
		}

		public volatile Object convert(Object obj)
		{
			return convert((String)obj);
		}

		public StringToNumber(Class targetType)
		{
			this.targetType = targetType;
		}
	}


	StringToNumberConverterFactory()
	{
	}

	public Converter getConverter(Class targetType)
	{
		return new StringToNumber(targetType);
	}
}
