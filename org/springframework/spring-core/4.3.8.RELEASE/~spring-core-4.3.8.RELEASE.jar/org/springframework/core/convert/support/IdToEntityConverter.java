// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IdToEntityConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

final class IdToEntityConverter
	implements ConditionalGenericConverter
{

	private final ConversionService conversionService;

	public IdToEntityConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/lang/Object));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Method finder = getFinder(targetType.getType());
		return finder != null && conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
		{
			return null;
		} else
		{
			Method finder = getFinder(targetType.getType());
			Object id = conversionService.convert(source, sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
			return ReflectionUtils.invokeMethod(finder, source, new Object[] {
				id
			});
		}
	}

	private Method getFinder(Class entityClass)
	{
		String finderMethod = (new StringBuilder()).append("find").append(getEntityName(entityClass)).toString();
		Method methods[];
		boolean localOnlyFiltered;
		try
		{
			methods = entityClass.getDeclaredMethods();
			localOnlyFiltered = true;
		}
		catch (SecurityException ex)
		{
			methods = entityClass.getMethods();
			localOnlyFiltered = false;
		}
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(finderMethod) && method.getParameterTypes().length == 1 && method.getReturnType().equals(entityClass) && (localOnlyFiltered || method.getDeclaringClass().equals(entityClass)))
				return method;
		}

		return null;
	}

	private String getEntityName(Class entityClass)
	{
		String shortName = ClassUtils.getShortName(entityClass);
		int lastDot = shortName.lastIndexOf('.');
		if (lastDot != -1)
			return shortName.substring(lastDot + 1);
		else
			return shortName;
	}
}
