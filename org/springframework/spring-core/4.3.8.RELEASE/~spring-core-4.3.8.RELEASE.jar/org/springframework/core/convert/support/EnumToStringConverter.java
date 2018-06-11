// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   EnumToStringConverter.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

// Referenced classes of package org.springframework.core.convert.support:
//			AbstractConditionalEnumConverter

final class EnumToStringConverter extends AbstractConditionalEnumConverter
	implements Converter
{

	public EnumToStringConverter(ConversionService conversionService)
	{
		super(conversionService);
	}

	public String convert(Enum source)
	{
		return source.name();
	}

	public volatile Object convert(Object obj)
	{
		return convert((Enum)obj);
	}
}
