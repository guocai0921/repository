// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToArrayConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.StringUtils;

final class StringToArrayConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public StringToArrayConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/String, [Ljava/lang/Object;));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		String string = (String)source;
		String fields[] = StringUtils.commaDelimitedListToStringArray(string);
		Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), fields.length);
		for (int i = 0; i < fields.length; i++)
		{
			String sourceElement = fields[i];
			Object targetElement = conversionService.convert(sourceElement.trim(), sourceType, targetType.getElementTypeDescriptor());
			Array.set(target, i, targetElement);
		}

		return target;
	}
}
