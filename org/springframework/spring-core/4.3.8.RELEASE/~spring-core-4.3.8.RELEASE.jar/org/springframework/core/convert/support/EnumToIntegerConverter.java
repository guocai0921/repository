// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EnumToIntegerConverter.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

// Referenced classes of package org.springframework.core.convert.support:
//			AbstractConditionalEnumConverter

final class EnumToIntegerConverter extends AbstractConditionalEnumConverter
	implements Converter
{

	public EnumToIntegerConverter(ConversionService conversionService)
	{
		super(conversionService);
	}

	public Integer convert(Enum source)
	{
		return Integer.valueOf(source.ordinal());
	}

	public volatile Object convert(Object obj)
	{
		return convert((Enum)obj);
	}
}
