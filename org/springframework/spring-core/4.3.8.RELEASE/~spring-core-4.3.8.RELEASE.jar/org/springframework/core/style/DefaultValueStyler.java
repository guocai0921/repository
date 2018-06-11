// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultValueStyler.java

package org.springframework.core.style;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.style:
//			ValueStyler

public class DefaultValueStyler
	implements ValueStyler
{

	private static final String EMPTY = "[empty]";
	private static final String NULL = "[null]";
	private static final String COLLECTION = "collection";
	private static final String SET = "set";
	private static final String LIST = "list";
	private static final String MAP = "map";
	private static final String ARRAY = "array";

	public DefaultValueStyler()
	{
	}

	public String style(Object value)
	{
		if (value == null)
			return "[null]";
		if (value instanceof String)
			return (new StringBuilder()).append("'").append(value).append("'").toString();
		if (value instanceof Class)
			return ClassUtils.getShortName((Class)value);
		if (value instanceof Method)
		{
			Method method = (Method)value;
			return (new StringBuilder()).append(method.getName()).append("@").append(ClassUtils.getShortName(method.getDeclaringClass())).toString();
		}
		if (value instanceof Map)
			return style((Map)value);
		if (value instanceof java.util.Map.Entry)
			return style((java.util.Map.Entry)value);
		if (value instanceof Collection)
			return style((Collection)value);
		if (value.getClass().isArray())
			return styleArray(ObjectUtils.toObjectArray(value));
		else
			return String.valueOf(value);
	}

	private String style(Map value)
	{
		StringBuilder result = new StringBuilder(value.size() * 8 + 16);
		result.append("map[");
		Iterator it = value.entrySet().iterator();
		do
		{
			if (!it.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
			result.append(style(entry));
			if (it.hasNext())
				result.append(',').append(' ');
		} while (true);
		if (value.isEmpty())
			result.append("[empty]");
		result.append("]");
		return result.toString();
	}

	private String style(java.util.Map.Entry value)
	{
		return (new StringBuilder()).append(style(value.getKey())).append(" -> ").append(style(value.getValue())).toString();
	}

	private String style(Collection value)
	{
		StringBuilder result = new StringBuilder(value.size() * 8 + 16);
		result.append(getCollectionTypeString(value)).append('[');
		Iterator i = value.iterator();
		do
		{
			if (!i.hasNext())
				break;
			result.append(style(i.next()));
			if (i.hasNext())
				result.append(',').append(' ');
		} while (true);
		if (value.isEmpty())
			result.append("[empty]");
		result.append("]");
		return result.toString();
	}

	private String getCollectionTypeString(Collection value)
	{
		if (value instanceof List)
			return "list";
		if (value instanceof Set)
			return "set";
		else
			return "collection";
	}

	private String styleArray(Object array[])
	{
		StringBuilder result = new StringBuilder(array.length * 8 + 16);
		result.append("array<").append(ClassUtils.getShortName(((Object) (array)).getClass().getComponentType())).append(">[");
		for (int i = 0; i < array.length - 1; i++)
		{
			result.append(style(array[i]));
			result.append(',').append(' ');
		}

		if (array.length > 0)
			result.append(style(array[array.length - 1]));
		else
			result.append("[empty]");
		result.append("]");
		return result.toString();
	}
}
