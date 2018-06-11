// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConversionUtils.java

package org.springframework.core.convert.support;

import org.springframework.core.convert.*;
import org.springframework.core.convert.converter.GenericConverter;

abstract class ConversionUtils
{

	ConversionUtils()
	{
	}

	public static Object invokeConverter(GenericConverter converter, Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return converter.convert(source, sourceType, targetType);
		ConversionFailedException ex;
		ex;
		throw ex;
		ex;
		throw new ConversionFailedException(sourceType, targetType, source, ex);
	}

	public static boolean canConvertElements(TypeDescriptor sourceElementType, TypeDescriptor targetElementType, ConversionService conversionService)
	{
		if (targetElementType == null)
			return true;
		if (sourceElementType == null)
			return true;
		if (conversionService.canConvert(sourceElementType, targetElementType))
			return true;
		return sourceElementType.getType().isAssignableFrom(targetElementType.getType());
	}

	public static Class getEnumType(Class targetType)
	{
		Class enumType;
		for (enumType = targetType; enumType != null && !enumType.isEnum(); enumType = enumType.getSuperclass());
		if (enumType == null)
			throw new IllegalArgumentException((new StringBuilder()).append("The target type ").append(targetType.getName()).append(" does not refer to an enum").toString());
		else
			return enumType;
	}
}
