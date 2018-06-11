// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectToCollectionConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class ObjectToCollectionConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public ObjectToCollectionConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/util/Collection));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), conversionService);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection target = CollectionFactory.createCollection(targetType.getType(), elementDesc == null ? null : elementDesc.getType(), 1);
		if (elementDesc == null || elementDesc.isCollection())
		{
			target.add(source);
		} else
		{
			Object singleElement = conversionService.convert(source, sourceType, elementDesc);
			target.add(singleElement);
		}
		return target;
	}
}
