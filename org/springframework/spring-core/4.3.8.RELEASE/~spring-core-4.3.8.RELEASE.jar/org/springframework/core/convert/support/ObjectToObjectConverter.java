// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectToObjectConverter.java

package org.springframework.core.convert.support;

import java.lang.reflect.*;
import java.util.*;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.*;

final class ObjectToObjectConverter
	implements ConditionalGenericConverter
{

	private static final Map conversionMemberCache = new ConcurrentReferenceHashMap(32);

	ObjectToObjectConverter()
	{
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/lang/Object));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return sourceType.getType() != targetType.getType() && hasConversionMethodOrConstructor(targetType.getType(), sourceType.getType());
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Class sourceClass;
		Class targetClass;
		Member member;
		if (source == null)
			return null;
		sourceClass = sourceType.getType();
		targetClass = targetType.getType();
		member = getValidatedMember(targetClass, sourceClass);
		Method method;
		if (!(member instanceof Method))
			break MISSING_BLOCK_LABEL_84;
		method = (Method)member;
		ReflectionUtils.makeAccessible(method);
		if (!Modifier.isStatic(method.getModifiers()))
			return method.invoke(source, new Object[0]);
		return method.invoke(null, new Object[] {
			source
		});
		Constructor ctor;
		if (!(member instanceof Constructor))
			break MISSING_BLOCK_LABEL_154;
		ctor = (Constructor)member;
		ReflectionUtils.makeAccessible(ctor);
		return ctor.newInstance(new Object[] {
			source
		});
		InvocationTargetException ex;
		ex;
		throw new ConversionFailedException(sourceType, targetType, source, ex.getTargetException());
		ex;
		throw new ConversionFailedException(sourceType, targetType, source, ex);
		throw new IllegalStateException(String.format("No to%3$s() method exists on %1$s, and no static valueOf/of/from(%1$s) method or %3$s(%1$s) constructor exists on %2$s.", new Object[] {
			sourceClass.getName(), targetClass.getName(), targetClass.getSimpleName()
		}));
	}

	static boolean hasConversionMethodOrConstructor(Class targetClass, Class sourceClass)
	{
		return getValidatedMember(targetClass, sourceClass) != null;
	}

	private static Member getValidatedMember(Class targetClass, Class sourceClass)
	{
		Member member = (Member)conversionMemberCache.get(targetClass);
		if (isApplicable(member, sourceClass))
			return member;
		member = determineToMethod(targetClass, sourceClass);
		if (member == null)
		{
			member = determineFactoryMethod(targetClass, sourceClass);
			if (member == null)
			{
				member = determineFactoryConstructor(targetClass, sourceClass);
				if (member == null)
					return null;
			}
		}
		conversionMemberCache.put(targetClass, member);
		return member;
	}

	private static boolean isApplicable(Member member, Class sourceClass)
	{
		if (member instanceof Method)
		{
			Method method = (Method)member;
			return Modifier.isStatic(method.getModifiers()) ? method.getParameterTypes()[0] == sourceClass : ClassUtils.isAssignable(method.getDeclaringClass(), sourceClass);
		}
		if (member instanceof Constructor)
		{
			Constructor ctor = (Constructor)member;
			return ctor.getParameterTypes()[0] == sourceClass;
		} else
		{
			return false;
		}
	}

	private static Method determineToMethod(Class targetClass, Class sourceClass)
	{
		if (java/lang/String == targetClass || java/lang/String == sourceClass)
		{
			return null;
		} else
		{
			Method method = ClassUtils.getMethodIfAvailable(sourceClass, (new StringBuilder()).append("to").append(targetClass.getSimpleName()).toString(), new Class[0]);
			return method == null || Modifier.isStatic(method.getModifiers()) || !ClassUtils.isAssignable(targetClass, method.getReturnType()) ? null : method;
		}
	}

	private static Method determineFactoryMethod(Class targetClass, Class sourceClass)
	{
		if (java/lang/String == targetClass)
			return null;
		Method method = ClassUtils.getStaticMethod(targetClass, "valueOf", new Class[] {
			sourceClass
		});
		if (method == null)
		{
			method = ClassUtils.getStaticMethod(targetClass, "of", new Class[] {
				sourceClass
			});
			if (method == null)
				method = ClassUtils.getStaticMethod(targetClass, "from", new Class[] {
					sourceClass
				});
		}
		return method;
	}

	private static Constructor determineFactoryConstructor(Class targetClass, Class sourceClass)
	{
		return ClassUtils.getConstructorIfAvailable(targetClass, new Class[] {
			sourceClass
		});
	}

}
