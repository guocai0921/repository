// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayToArrayConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.convert.support:
//			CollectionToArrayConverter, GenericConversionService

final class ArrayToArrayConverter
	implements ConditionalGenericConverter
{

	private final CollectionToArrayConverter helperConverter;
	private final ConversionService conversionService;

	public ArrayToArrayConverter(ConversionService conversionService)
	{
		helperConverter = new CollectionToArrayConverter(conversionService);
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([Ljava/lang/Object;, [Ljava/lang/Object;));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return helperConverter.matches(sourceType, targetType);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if ((conversionService instanceof GenericConversionService) && ((GenericConversionService)conversionService).canBypassConvert(sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor()))
		{
			return source;
		} else
		{
			java.util.List sourceList = Arrays.asList(ObjectUtils.toObjectArray(source));
			return helperConverter.convert(sourceList, sourceType, targetType);
		}
	}
}
