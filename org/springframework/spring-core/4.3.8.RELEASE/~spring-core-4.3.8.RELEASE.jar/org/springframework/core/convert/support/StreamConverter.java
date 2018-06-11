// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StreamConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

class StreamConverter
	implements ConditionalGenericConverter
{

	private static final TypeDescriptor STREAM_TYPE = TypeDescriptor.valueOf(java/util/stream/Stream);
	private static final Set CONVERTIBLE_TYPES = createConvertibleTypes();
	private final ConversionService conversionService;

	public StreamConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return CONVERTIBLE_TYPES;
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (sourceType.isAssignableTo(STREAM_TYPE))
			return matchesFromStream(sourceType.getElementTypeDescriptor(), targetType);
		if (targetType.isAssignableTo(STREAM_TYPE))
			return matchesToStream(targetType.getElementTypeDescriptor(), sourceType);
		else
			return false;
	}

	public boolean matchesFromStream(TypeDescriptor elementType, TypeDescriptor targetType)
	{
		TypeDescriptor collectionOfElement = TypeDescriptor.collection(java/util/Collection, elementType);
		return conversionService.canConvert(collectionOfElement, targetType);
	}

	public boolean matchesToStream(TypeDescriptor elementType, TypeDescriptor sourceType)
	{
		TypeDescriptor collectionOfElement = TypeDescriptor.collection(java/util/Collection, elementType);
		return conversionService.canConvert(sourceType, collectionOfElement);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (sourceType.isAssignableTo(STREAM_TYPE))
			return convertFromStream((Stream)source, sourceType, targetType);
		if (targetType.isAssignableTo(STREAM_TYPE))
			return convertToStream(source, sourceType, targetType);
		else
			throw new IllegalStateException("Unexpected source/target types");
	}

	private Object convertFromStream(Stream source, TypeDescriptor streamType, TypeDescriptor targetType)
	{
		List content = (List)source.collect(Collectors.toList());
		TypeDescriptor listType = TypeDescriptor.collection(java/util/List, streamType.getElementTypeDescriptor());
		return conversionService.convert(content, listType, targetType);
	}

	private Object convertToStream(Object source, TypeDescriptor sourceType, TypeDescriptor streamType)
	{
		TypeDescriptor targetCollection = TypeDescriptor.collection(java/util/List, streamType.getElementTypeDescriptor());
		List target = (List)conversionService.convert(source, sourceType, targetCollection);
		return target.stream();
	}

	private static Set createConvertibleTypes()
	{
		Set convertiblePairs = new HashSet();
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/stream/Stream, java/util/Collection));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/stream/Stream, [Ljava/lang/Object;));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Collection, java/util/stream/Stream));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([Ljava/lang/Object;, java/util/stream/Stream));
		return convertiblePairs;
	}

}
