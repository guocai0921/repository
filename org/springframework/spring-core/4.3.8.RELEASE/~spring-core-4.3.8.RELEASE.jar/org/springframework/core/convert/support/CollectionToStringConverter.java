// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionToStringConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class CollectionToStringConverter
	implements ConditionalGenericConverter
{

	private static final String DELIMITER = ",";
	private final ConversionService conversionService;

	public CollectionToStringConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Collection, java/lang/String));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		Collection sourceCollection = (Collection)source;
		if (sourceCollection.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Iterator iterator = sourceCollection.iterator(); iterator.hasNext();)
		{
			Object sourceElement = iterator.next();
			if (i > 0)
				sb.append(",");
			Object targetElement = conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType);
			sb.append(targetElement);
			i++;
		}

		return sb.toString();
	}
}
