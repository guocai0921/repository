// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayToCollectionConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.util.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class ArrayToCollectionConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public ArrayToCollectionConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([Ljava/lang/Object;, java/util/Collection));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		int length = Array.getLength(source);
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection target = CollectionFactory.createCollection(targetType.getType(), elementDesc == null ? null : elementDesc.getType(), length);
		if (elementDesc == null)
		{
			for (int i = 0; i < length; i++)
			{
				Object sourceElement = Array.get(source, i);
				target.add(sourceElement);
			}

		} else
		{
			for (int i = 0; i < length; i++)
			{
				Object sourceElement = Array.get(source, i);
				Object targetElement = conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), elementDesc);
				target.add(targetElement);
			}

		}
		return target;
	}
}
