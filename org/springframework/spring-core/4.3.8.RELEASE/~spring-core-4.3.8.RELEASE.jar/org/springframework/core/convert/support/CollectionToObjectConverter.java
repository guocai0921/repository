// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionToObjectConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class CollectionToObjectConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public CollectionToObjectConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Collection, java/lang/Object));
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
		Collection sourceCollection = (Collection)source;
		if (sourceCollection.isEmpty())
		{
			return null;
		} else
		{
			Object firstElement = sourceCollection.iterator().next();
			return conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
		}
	}
}
