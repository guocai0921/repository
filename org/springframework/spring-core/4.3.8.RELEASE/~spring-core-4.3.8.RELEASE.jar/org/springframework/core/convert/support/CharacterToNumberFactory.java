// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CharacterToNumberFactory.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;

final class CharacterToNumberFactory
	implements ConverterFactory
{
	private static final class CharacterToNumber
		implements Converter
	{

		private final Class targetType;

		public Number convert(Character source)
		{
			return NumberUtils.convertNumberToTargetClass(Short.valueOf((short)source.charValue()), targetType);
		}

		public volatile Object convert(Object obj)
		{
			return convert((Character)obj);
		}

		public CharacterToNumber(Class targetType)
		{
			this.targetType = targetType;
		}
	}


	CharacterToNumberFactory()
	{
	}

	public Converter getConverter(Class targetType)
	{
		return new CharacterToNumber(targetType);
	}
}
