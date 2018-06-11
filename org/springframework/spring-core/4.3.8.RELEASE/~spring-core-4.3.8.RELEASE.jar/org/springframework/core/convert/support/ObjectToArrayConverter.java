// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectToArrayConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class ObjectToArrayConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public ObjectToArrayConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, [Ljava/lang/Object;));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
		{
			return null;
		} else
		{
			Object target = Array.newInstance(targetType.getElementTypeDescriptor().getType(), 1);
			Object targetElement = conversionService.convert(source, sourceType, targetType.getElementTypeDescriptor());
			Array.set(target, 0, targetElement);
			return target;
		}
	}
}
