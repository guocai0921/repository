// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ReadOnlySystemAttributesMap.java

package org.springframework.core.env;

import java.util.*;

abstract class ReadOnlySystemAttributesMap
	implements Map
{

	ReadOnlySystemAttributesMap()
	{
	}

	public boolean containsKey(Object key)
	{
		return get(key) != null;
	}

	public String get(Object key)
	{
		if (!(key instanceof String))
			throw new IllegalArgumentException((new StringBuilder()).append("Type of key [").append(key == null ? "null" : key.getClass().getName()).append("] must be java.lang.String.").toString());
		else
			return getSystemAttribute((String)key);
	}

	public boolean isEmpty()
	{
		return false;
	}

	protected abstract String getSystemAttribute(String s);

	public int size()
	{
		throw new UnsupportedOperationException();
	}

	public String put(String key, String value)
	{
		throw new UnsupportedOperationException();
	}

	public boolean containsValue(Object value)
	{
		throw new UnsupportedOperationException();
	}

	public String remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	public Set keySet()
	{
		return Collections.emptySet();
	}

	public void putAll(Map map)
	{
		throw new UnsupportedOperationException();
	}

	public Collection values()
	{
		return Collections.emptySet();
	}

	public Set entrySet()
	{
		return Collections.emptySet();
	}

	public volatile Object remove(Object obj)
	{
		return remove(obj);
	}

	public volatile Object put(Object obj, Object obj1)
	{
		return put((String)obj, (String)obj1);
	}

	public volatile Object get(Object obj)
	{
		return get(obj);
	}
}
