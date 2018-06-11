// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IntegerToEnumConverterFactory.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class IntegerToEnumConverterFactory
	implements ConverterFactory
{
	private class IntegerToEnum
		implements Converter
	{

		private final Class enumType;
		final IntegerToEnumConverterFactory this$0;

		public Enum convert(Integer source)
		{
			return ((Enum[])enumType.getEnumConstants())[source.intValue()];
		}

		public volatile Object convert(Object obj)
		{
			return convert((Integer)obj);
		}

		public IntegerToEnum(Class enumType)
		{
			this$0 = IntegerToEnumConverterFactory.this;
			super();
			this.enumType = enumType;
		}
	}


	IntegerToEnumConverterFactory()
	{
	}

	public Converter getConverter(Class targetType)
	{
		return new IntegerToEnum(ConversionUtils.getEnumType(targetType));
	}
}
