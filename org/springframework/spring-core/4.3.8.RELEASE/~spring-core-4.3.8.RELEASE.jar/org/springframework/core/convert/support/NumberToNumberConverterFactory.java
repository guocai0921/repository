// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NumberToNumberConverterFactory.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.*;
import org.springframework.util.NumberUtils;

final class NumberToNumberConverterFactory
	implements ConverterFactory, ConditionalConverter
{
	private static final class NumberToNumber
		implements Converter
	{

		private final Class targetType;

		public Number convert(Number source)
		{
			return NumberUtils.convertNumberToTargetClass(source, targetType);
		}

		public volatile Object convert(Object obj)
		{
			return convert((Number)obj);
		}

		public NumberToNumber(Class targetType)
		{
			this.targetType = targetType;
		}
	}


	NumberToNumberConverterFactory()
	{
	}

	public Converter getConverter(Class targetType)
	{
		return new NumberToNumber(targetType);
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return !sourceType.equals(targetType);
	}
}
