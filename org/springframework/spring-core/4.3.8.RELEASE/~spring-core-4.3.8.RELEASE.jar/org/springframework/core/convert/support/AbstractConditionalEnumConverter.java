// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractConditionalEnumConverter.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.util.ClassUtils;

abstract class AbstractConditionalEnumConverter
	implements ConditionalConverter
{

	private final ConversionService conversionService;

	protected AbstractConditionalEnumConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Class aclass[] = ClassUtils.getAllInterfacesForClass(sourceType.getType());
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class interfaceType = aclass[j];
			if (conversionService.canConvert(TypeDescriptor.valueOf(interfaceType), targetType))
				return false;
		}

		return true;
	}
}
