// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FallbackObjectToStringConverter.java

package org.springframework.core.convert.support;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ObjectToObjectConverter

final class FallbackObjectToStringConverter
	implements ConditionalGenericConverter
{

	FallbackObjectToStringConverter()
	{
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/lang/String));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Class sourceClass = sourceType.getObjectType();
		if (java/lang/String == sourceClass)
			return false;
		else
			return java/lang/CharSequence.isAssignableFrom(sourceClass) || java/io/StringWriter.isAssignableFrom(sourceClass) || ObjectToObjectConverter.hasConversionMethodOrConstructor(sourceClass, java/lang/String);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return source == null ? null : source.toString();
	}
}
