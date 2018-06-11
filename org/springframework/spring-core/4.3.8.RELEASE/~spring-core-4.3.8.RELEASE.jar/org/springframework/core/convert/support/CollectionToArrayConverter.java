// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionToArrayConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class CollectionToArrayConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public CollectionToArrayConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Collection, [Ljava/lang/Object;));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		Collection sourceCollection = (Collection)source;
		Object array = Array.newInstance(targetType.getElementTypeDescriptor().getType(), sourceCollection.size());
		int i = 0;
		Object targetElement;
		for (Iterator iterator = sourceCollection.iterator(); iterator.hasNext(); Array.set(array, i++, targetElement))
		{
			Object sourceElement = iterator.next();
			targetElement = conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType.getElementTypeDescriptor());
		}

		return array;
	}
}
