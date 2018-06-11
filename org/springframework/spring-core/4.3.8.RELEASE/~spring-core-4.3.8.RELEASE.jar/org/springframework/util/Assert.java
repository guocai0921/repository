// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Assert.java

package org.springframework.util;

import java.util.Collection;
import java.util.Map;

// Referenced classes of package org.springframework.util:
//			StringUtils, ObjectUtils, CollectionUtils

public abstract class Assert
{

	public Assert()
	{
	}

	public static void state(boolean expression, String message)
	{
		if (!expression)
			throw new IllegalStateException(message);
		else
			return;
	}

	/**
	 * @deprecated Method state is deprecated
	 */

	public static void state(boolean expression)
	{
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	public static void isTrue(boolean expression, String message)
	{
		if (!expression)
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method isTrue is deprecated
	 */

	public static void isTrue(boolean expression)
	{
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message)
	{
		if (object != null)
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method isNull is deprecated
	 */

	public static void isNull(Object object)
	{
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message)
	{
		if (object == null)
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method notNull is deprecated
	 */

	public static void notNull(Object object)
	{
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text, String message)
	{
		if (!StringUtils.hasLength(text))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method hasLength is deprecated
	 */

	public static void hasLength(String text)
	{
		hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text, String message)
	{
		if (!StringUtils.hasText(text))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method hasText is deprecated
	 */

	public static void hasText(String text)
	{
		hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void doesNotContain(String textToSearch, String substring, String message)
	{
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method doesNotContain is deprecated
	 */

	public static void doesNotContain(String textToSearch, String substring)
	{
		doesNotContain(textToSearch, substring, (new StringBuilder()).append("[Assertion failed] - this String argument must not contain the substring [").append(substring).append("]").toString());
	}

	public static void notEmpty(Object array[], String message)
	{
		if (ObjectUtils.isEmpty(array))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method notEmpty is deprecated
	 */

	public static void notEmpty(Object array[])
	{
		notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static void noNullElements(Object array[], String message)
	{
		if (array != null)
		{
			Object aobj[] = array;
			int i = aobj.length;
			for (int j = 0; j < i; j++)
			{
				Object element = aobj[j];
				if (element == null)
					throw new IllegalArgumentException(message);
			}

		}
	}

	/**
	 * @deprecated Method noNullElements is deprecated
	 */

	public static void noNullElements(Object array[])
	{
		noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static void notEmpty(Collection collection, String message)
	{
		if (CollectionUtils.isEmpty(collection))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method notEmpty is deprecated
	 */

	public static void notEmpty(Collection collection)
	{
		notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static void notEmpty(Map map, String message)
	{
		if (CollectionUtils.isEmpty(map))
			throw new IllegalArgumentException(message);
		else
			return;
	}

	/**
	 * @deprecated Method notEmpty is deprecated
	 */

	public static void notEmpty(Map map)
	{
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	public static void isInstanceOf(Class type, Object obj, String message)
	{
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj))
			instanceCheckFailed(type, obj, message);
	}

	public static void isInstanceOf(Class type, Object obj)
	{
		isInstanceOf(type, obj, "");
	}

	public static void isAssignable(Class superType, Class subType, String message)
	{
		notNull(superType, "Super type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType))
			assignableCheckFailed(superType, subType, message);
	}

	public static void isAssignable(Class superType, Class subType)
	{
		isAssignable(superType, subType, "");
	}

	private static void instanceCheckFailed(Class type, Object obj, String msg)
	{
		String className = obj == null ? "null" : obj.getClass().getName();
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg))
			if (endsWithSeparator(msg))
			{
				result = (new StringBuilder()).append(msg).append(" ").toString();
			} else
			{
				result = messageWithTypeName(msg, className);
				defaultMessage = false;
			}
		if (defaultMessage)
			result = (new StringBuilder()).append(result).append("Object of class [").append(className).append("] must be an instance of ").append(type).toString();
		throw new IllegalArgumentException(result);
	}

	private static void assignableCheckFailed(Class superType, Class subType, String msg)
	{
		String result = "";
		boolean defaultMessage = true;
		if (StringUtils.hasLength(msg))
			if (endsWithSeparator(msg))
			{
				result = (new StringBuilder()).append(msg).append(" ").toString();
			} else
			{
				result = messageWithTypeName(msg, subType);
				defaultMessage = false;
			}
		if (defaultMessage)
			result = (new StringBuilder()).append(result).append(subType).append(" is not assignable to ").append(superType).toString();
		throw new IllegalArgumentException(result);
	}

	private static boolean endsWithSeparator(String msg)
	{
		return msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith(".");
	}

	private static String messageWithTypeName(String msg, Object typeName)
	{
		return (new StringBuilder()).append(msg).append(msg.endsWith(" ") ? "" : ": ").append(typeName).toString();
	}
}
