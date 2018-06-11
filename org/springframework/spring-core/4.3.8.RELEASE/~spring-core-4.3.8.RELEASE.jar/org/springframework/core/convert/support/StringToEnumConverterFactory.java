// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToEnumConverterFactory.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class StringToEnumConverterFactory
	implements ConverterFactory
{
	private class StringToEnum
		implements Converter
	{

		private final Class enumType;
		final StringToEnumConverterFactory this$0;

		public Enum convert(String source)
		{
			if (source.isEmpty())
				return null;
			else
				return Enum.valueOf(enumType, source.trim());
		}

		public volatile Object convert(Object obj)
		{
			return convert((String)obj);
		}

		public StringToEnum(Class enumType)
		{
			this$0 = StringToEnumConverterFactory.this;
			super();
			this.enumType = enumType;
		}
	}


	StringToEnumConverterFactory()
	{
	}

	public Converter getConverter(Class targetType)
	{
		return new StringToEnum(ConversionUtils.getEnumType(targetType));
	}
}
