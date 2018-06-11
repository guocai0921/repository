// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodClassKey.java

package org.springframework.core;

import java.lang.reflect.Method;
import org.springframework.util.ObjectUtils;

public final class MethodClassKey
	implements Comparable
{

	private final Method method;
	private final Class targetClass;

	public MethodClassKey(Method method, Class targetClass)
	{
		this.method = method;
		this.targetClass = targetClass;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof MethodClassKey))
		{
			return false;
		} else
		{
			MethodClassKey otherKey = (MethodClassKey)other;
			return method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(targetClass, otherKey.targetClass);
		}
	}

	public int hashCode()
	{
		return method.hashCode() + (targetClass == null ? 0 : targetClass.hashCode() * 29);
	}

	public String toString()
	{
		return (new StringBuilder()).append(method).append(targetClass == null ? "" : (new StringBuilder()).append(" on ").append(targetClass).toString()).toString();
	}

	public int compareTo(MethodClassKey other)
	{
		int result = method.getName().compareTo(other.method.getName());
		if (result == 0)
		{
			result = method.toString().compareTo(other.method.toString());
			if (result == 0 && targetClass != null)
				result = targetClass.getName().compareTo(other.targetClass.getName());
		}
		return result;
	}

	public volatile int compareTo(Object obj)
	{
		return compareTo((MethodClassKey)obj);
	}
}
