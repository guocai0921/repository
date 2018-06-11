// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Conventions.java

package org.springframework.core;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core:
//			MethodParameter, ResolvableType

public abstract class Conventions
{

	private static final String PLURAL_SUFFIX = "List";
	private static final Set IGNORED_INTERFACES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new Class[] {
		java/io/Serializable, java/io/Externalizable, java/lang/Cloneable, java/lang/Comparable
	})));

	public Conventions()
	{
	}

	public static String getVariableName(Object value)
	{
		Assert.notNull(value, "Value must not be null");
		boolean pluralize = false;
		Class valueClass;
		if (value.getClass().isArray())
		{
			valueClass = value.getClass().getComponentType();
			pluralize = true;
		} else
		if (value instanceof Collection)
		{
			Collection collection = (Collection)value;
			if (collection.isEmpty())
				throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
			Object valueToCheck = peekAhead(collection);
			valueClass = getClassForValue(valueToCheck);
			pluralize = true;
		} else
		{
			valueClass = getClassForValue(value);
		}
		String name = ClassUtils.getShortNameAsProperty(valueClass);
		return pluralize ? pluralize(name) : name;
	}

	public static String getVariableNameForParameter(MethodParameter parameter)
	{
		Assert.notNull(parameter, "MethodParameter must not be null");
		boolean pluralize = false;
		Class valueClass;
		if (parameter.getParameterType().isArray())
		{
			valueClass = parameter.getParameterType().getComponentType();
			pluralize = true;
		} else
		if (java/util/Collection.isAssignableFrom(parameter.getParameterType()))
		{
			valueClass = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
			if (valueClass == null)
				throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection parameter type");
			pluralize = true;
		} else
		{
			valueClass = parameter.getParameterType();
		}
		String name = ClassUtils.getShortNameAsProperty(valueClass);
		return pluralize ? pluralize(name) : name;
	}

	public static String getVariableNameForReturnType(Method method)
	{
		return getVariableNameForReturnType(method, method.getReturnType(), null);
	}

	public static String getVariableNameForReturnType(Method method, Object value)
	{
		return getVariableNameForReturnType(method, method.getReturnType(), value);
	}

	public static String getVariableNameForReturnType(Method method, Class resolvedType, Object value)
	{
		Assert.notNull(method, "Method must not be null");
		if (java/lang/Object == resolvedType)
			if (value == null)
				throw new IllegalArgumentException("Cannot generate variable name for an Object return type with null value");
			else
				return getVariableName(value);
		boolean pluralize = false;
		Class valueClass;
		if (resolvedType.isArray())
		{
			valueClass = resolvedType.getComponentType();
			pluralize = true;
		} else
		if (java/util/Collection.isAssignableFrom(resolvedType))
		{
			valueClass = ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
			if (valueClass == null)
			{
				if (!(value instanceof Collection))
					throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and a non-Collection value");
				Collection collection = (Collection)value;
				if (collection.isEmpty())
					throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and an empty Collection value");
				Object valueToCheck = peekAhead(collection);
				valueClass = getClassForValue(valueToCheck);
			}
			pluralize = true;
		} else
		{
			valueClass = resolvedType;
		}
		String name = ClassUtils.getShortNameAsProperty(valueClass);
		return pluralize ? pluralize(name) : name;
	}

	public static String attributeNameToPropertyName(String attributeName)
	{
		Assert.notNull(attributeName, "'attributeName' must not be null");
		if (!attributeName.contains("-"))
			return attributeName;
		char chars[] = attributeName.toCharArray();
		char result[] = new char[chars.length - 1];
		int currPos = 0;
		boolean upperCaseNext = false;
		char ac[] = chars;
		int i = ac.length;
		for (int j = 0; j < i; j++)
		{
			char c = ac[j];
			if (c == '-')
			{
				upperCaseNext = true;
				continue;
			}
			if (upperCaseNext)
			{
				result[currPos++] = Character.toUpperCase(c);
				upperCaseNext = false;
			} else
			{
				result[currPos++] = c;
			}
		}

		return new String(result, 0, currPos);
	}

	public static String getQualifiedAttributeName(Class enclosingClass, String attributeName)
	{
		Assert.notNull(enclosingClass, "'enclosingClass' must not be null");
		Assert.notNull(attributeName, "'attributeName' must not be null");
		return (new StringBuilder()).append(enclosingClass.getName()).append('.').append(attributeName).toString();
	}

	private static Class getClassForValue(Object value)
	{
		Class valueClass = value.getClass();
		if (Proxy.isProxyClass(valueClass))
		{
			Class ifcs[] = valueClass.getInterfaces();
			Class aclass[] = ifcs;
			int i = aclass.length;
			for (int j = 0; j < i; j++)
			{
				Class ifc = aclass[j];
				if (!IGNORED_INTERFACES.contains(ifc))
					return ifc;
			}

		} else
		if (valueClass.getName().lastIndexOf('$') != -1 && valueClass.getDeclaringClass() == null)
			valueClass = valueClass.getSuperclass();
		return valueClass;
	}

	private static String pluralize(String name)
	{
		return (new StringBuilder()).append(name).append("List").toString();
	}

	private static Object peekAhead(Collection collection)
	{
		Iterator it = collection.iterator();
		if (!it.hasNext())
			throw new IllegalStateException("Unable to peek ahead in non-empty collection - no element found");
		Object value = it.next();
		if (value == null)
			throw new IllegalStateException("Unable to peek ahead in non-empty collection - only null element found");
		else
			return value;
	}

}
