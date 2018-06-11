// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ArrayToStringConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.convert.support:
//			CollectionToStringConverter

final class ArrayToStringConverter
	implements ConditionalGenericConverter
{

	private final CollectionToStringConverter helperConverter;

	public ArrayToStringConverter(ConversionService conversionService)
	{
		helperConverter = new CollectionToStringConverter(conversionService);
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([Ljava/lang/Object;, java/lang/String));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return helperConverter.matches(sourceType, targetType);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return helperConverter.convert(Arrays.asList(ObjectUtils.toObjectArray(source)), sourceType, targetType);
	}
}
