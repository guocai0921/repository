// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayToObjectConverter.java

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

final class ArrayToObjectConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public ArrayToObjectConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([Ljava/lang/Object;, java/lang/Object));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		if (sourceType.isAssignableTo(targetType))
			return source;
		if (Array.getLength(source) == 0)
		{
			return null;
		} else
		{
			Object firstElement = Array.get(source, 0);
			return conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
		}
	}
}
