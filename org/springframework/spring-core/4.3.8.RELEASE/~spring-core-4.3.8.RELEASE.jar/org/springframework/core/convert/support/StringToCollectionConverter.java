// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringToCollectionConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.StringUtils;

final class StringToCollectionConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public StringToCollectionConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/String, java/util/Collection));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return targetType.getElementTypeDescriptor() == null || conversionService.canConvert(sourceType, targetType.getElementTypeDescriptor());
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		String string = (String)source;
		String fields[] = StringUtils.commaDelimitedListToStringArray(string);
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection target = CollectionFactory.createCollection(targetType.getType(), elementDesc == null ? null : elementDesc.getType(), fields.length);
		if (elementDesc == null)
		{
			String as[] = fields;
			int i = as.length;
			for (int k = 0; k < i; k++)
			{
				String field = as[k];
				target.add(field.trim());
			}

		} else
		{
			String as1[] = fields;
			int j = as1.length;
			for (int l = 0; l < j; l++)
			{
				String field = as1[l];
				Object targetElement = conversionService.convert(field.trim(), sourceType, elementDesc);
				target.add(targetElement);
			}

		}
		return target;
	}
}
