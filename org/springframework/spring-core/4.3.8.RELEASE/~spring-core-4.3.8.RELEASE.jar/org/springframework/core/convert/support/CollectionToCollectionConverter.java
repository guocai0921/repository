// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionToCollectionConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class CollectionToCollectionConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public CollectionToCollectionConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Collection, java/util/Collection));
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
		boolean copyRequired = !targetType.getType().isInstance(source);
		if (!copyRequired && sourceCollection.isEmpty())
			return source;
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		if (elementDesc == null && !copyRequired)
			return source;
		Collection target = CollectionFactory.createCollection(targetType.getType(), elementDesc == null ? null : elementDesc.getType(), sourceCollection.size());
		if (elementDesc == null)
		{
			target.addAll(sourceCollection);
		} else
		{
			Iterator iterator = sourceCollection.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				Object sourceElement = iterator.next();
				Object targetElement = conversionService.convert(sourceElement, sourceType.elementTypeDescriptor(sourceElement), elementDesc);
				target.add(targetElement);
				if (sourceElement != targetElement)
					copyRequired = true;
			} while (true);
		}
		return copyRequired ? target : source;
	}
}
