// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Constants.java

package org.springframework.core;

import java.lang.reflect.Field;
import java.util.*;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

// Referenced classes of package org.springframework.core:
//			ConstantException

public class Constants
{

	private final String className;
	private final Map fieldCache = new HashMap();

	public Constants(Class clazz)
	{
		Assert.notNull(clazz, "Class must not be null");
		className = clazz.getName();
		Field fields[] = clazz.getFields();
		Field afield[] = fields;
		int i = afield.length;
		for (int j = 0; j < i; j++)
		{
			Field field = afield[j];
			if (!ReflectionUtils.isPublicStaticFinal(field))
				continue;
			String name = field.getName();
			try
			{
				Object value = field.get(null);
				fieldCache.put(name, value);
			}
			catch (IllegalAccessException illegalaccessexception) { }
		}

	}

	public final String getClassName()
	{
		return className;
	}

	public final int getSize()
	{
		return fieldCache.size();
	}

	protected final Map getFieldCache()
	{
		return fieldCache;
	}

	public Number asNumber(String code)
		throws ConstantException
	{
		Object obj = asObject(code);
		if (!(obj instanceof Number))
			throw new ConstantException(className, code, "not a Number");
		else
			return (Number)obj;
	}

	public String asString(String code)
		throws ConstantException
	{
		return asObject(code).toString();
	}

	public Object asObject(String code)
		throws ConstantException
	{
		Assert.notNull(code, "Code must not be null");
		String codeToUse = code.toUpperCase(Locale.ENGLISH);
		Object val = fieldCache.get(codeToUse);
		if (val == null)
			throw new ConstantException(className, codeToUse, "not found");
		else
			return val;
	}

	public Set getNames(String namePrefix)
	{
		String prefixToUse = namePrefix == null ? "" : namePrefix.trim().toUpperCase(Locale.ENGLISH);
		Set names = new HashSet();
		Iterator iterator = fieldCache.keySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String code = (String)iterator.next();
			if (code.startsWith(prefixToUse))
				names.add(code);
		} while (true);
		return names;
	}

	public Set getNamesForProperty(String propertyName)
	{
		return getNames(propertyToConstantNamePrefix(propertyName));
	}

	public Set getNamesForSuffix(String nameSuffix)
	{
		String suffixToUse = nameSuffix == null ? "" : nameSuffix.trim().toUpperCase(Locale.ENGLISH);
		Set names = new HashSet();
		Iterator iterator = fieldCache.keySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String code = (String)iterator.next();
			if (code.endsWith(suffixToUse))
				names.add(code);
		} while (true);
		return names;
	}

	public Set getValues(String namePrefix)
	{
		String prefixToUse = namePrefix == null ? "" : namePrefix.trim().toUpperCase(Locale.ENGLISH);
		Set values = new HashSet();
		Iterator iterator = fieldCache.keySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String code = (String)iterator.next();
			if (code.startsWith(prefixToUse))
				values.add(fieldCache.get(code));
		} while (true);
		return values;
	}

	public Set getValuesForProperty(String propertyName)
	{
		return getValues(propertyToConstantNamePrefix(propertyName));
	}

	public Set getValuesForSuffix(String nameSuffix)
	{
		String suffixToUse = nameSuffix == null ? "" : nameSuffix.trim().toUpperCase(Locale.ENGLISH);
		Set values = new HashSet();
		Iterator iterator = fieldCache.keySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String code = (String)iterator.next();
			if (code.endsWith(suffixToUse))
				values.add(fieldCache.get(code));
		} while (true);
		return values;
	}

	public String toCode(Object value, String namePrefix)
		throws ConstantException
	{
		String prefixToUse = namePrefix == null ? "" : namePrefix.trim().toUpperCase(Locale.ENGLISH);
		for (Iterator iterator = fieldCache.entrySet().iterator(); iterator.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			if (((String)entry.getKey()).startsWith(prefixToUse) && entry.getValue().equals(value))
				return (String)entry.getKey();
		}

		throw new ConstantException(className, prefixToUse, value);
	}

	public String toCodeForProperty(Object value, String propertyName)
		throws ConstantException
	{
		return toCode(value, propertyToConstantNamePrefix(propertyName));
	}

	public String toCodeForSuffix(Object value, String nameSuffix)
		throws ConstantException
	{
		String suffixToUse = nameSuffix == null ? "" : nameSuffix.trim().toUpperCase(Locale.ENGLISH);
		for (Iterator iterator = fieldCache.entrySet().iterator(); iterator.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			if (((String)entry.getKey()).endsWith(suffixToUse) && entry.getValue().equals(value))
				return (String)entry.getKey();
		}

		throw new ConstantException(className, suffixToUse, value);
	}

	public String propertyToConstantNamePrefix(String propertyName)
	{
		StringBuilder parsedPrefix = new StringBuilder();
		for (int i = 0; i < propertyName.length(); i++)
		{
			char c = propertyName.charAt(i);
			if (Character.isUpperCase(c))
			{
				parsedPrefix.append("_");
				parsedPrefix.append(c);
			} else
			{
				parsedPrefix.append(Character.toUpperCase(c));
			}
		}

		return parsedPrefix.toString();
	}
}
