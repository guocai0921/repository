// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectToOptionalConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

final class ObjectToOptionalConverter
	implements ConditionalGenericConverter
{
	private static class GenericTypeDescriptor extends TypeDescriptor
	{

		public GenericTypeDescriptor(TypeDescriptor typeDescriptor)
		{
			super(typeDescriptor.getResolvableType().getGeneric(new int[] {
				0
			}), null, typeDescriptor.getAnnotations());
		}
	}


	private final ConversionService conversionService;

	public ObjectToOptionalConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/util/Optional));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (targetType.getResolvableType() != null)
			return conversionService.canConvert(sourceType, new GenericTypeDescriptor(targetType));
		else
			return true;
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return Optional.empty();
		if (source instanceof Optional)
			return source;
		if (targetType.getResolvableType() != null)
		{
			Object target = conversionService.convert(source, sourceType, new GenericTypeDescriptor(targetType));
			return Optional.ofNullable(target);
		} else
		{
			return Optional.of(source);
		}
	}
}
