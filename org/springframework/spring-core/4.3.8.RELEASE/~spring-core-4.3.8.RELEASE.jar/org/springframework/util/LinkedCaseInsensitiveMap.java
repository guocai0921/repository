// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LinkedCaseInsensitiveMap.java

package org.springframework.util;

import java.io.Serializable;
import java.util.*;

public class LinkedCaseInsensitiveMap
	implements Map, Serializable, Cloneable
{

	private final LinkedHashMap targetMap;
	private final HashMap caseInsensitiveKeys;
	private final Locale locale;

	public LinkedCaseInsensitiveMap()
	{
		this((Locale)null);
	}

	public LinkedCaseInsensitiveMap(Locale locale)
	{
		this(16, locale);
	}

	public LinkedCaseInsensitiveMap(int initialCapacity)
	{
		this(initialCapacity, null);
	}

	public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale)
	{
		targetMap = new LinkedHashMap(initialCapacity) {

			final LinkedCaseInsensitiveMap this$0;

			public boolean containsKey(Object key)
			{
				return LinkedCaseInsensitiveMap.this.containsKey(key);
			}

			protected boolean removeEldestEntry(java.util.Map.Entry eldest)
			{
				boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
				if (doRemove)
					caseInsensitiveKeys.remove(convertKey((String)eldest.getKey()));
				return doRemove;
			}

			
			{
				this.this$0 = LinkedCaseInsensitiveMap.this;
				super(x0);
			}
		};
		caseInsensitiveKeys = new HashMap(initialCapacity);
		this.locale = locale == null ? Locale.getDefault() : locale;
	}

	private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap other)
	{
		targetMap = (LinkedHashMap)other.targetMap.clone();
		caseInsensitiveKeys = (HashMap)other.caseInsensitiveKeys.clone();
		locale = other.locale;
	}

	public int size()
	{
		return targetMap.size();
	}

	public boolean isEmpty()
	{
		return targetMap.isEmpty();
	}

	public boolean containsKey(Object key)
	{
		return (key instanceof String) && caseInsensitiveKeys.containsKey(convertKey((String)key));
	}

	public boolean containsValue(Object value)
	{
		return targetMap.containsValue(value);
	}

	public Object get(Object key)
	{
		if (key instanceof String)
		{
			String caseInsensitiveKey = (String)caseInsensitiveKeys.get(convertKey((String)key));
			if (caseInsensitiveKey != null)
				return targetMap.get(caseInsensitiveKey);
		}
		return null;
	}

	public Object getOrDefault(Object key, Object defaultValue)
	{
		if (key instanceof String)
		{
			String caseInsensitiveKey = (String)caseInsensitiveKeys.get(convertKey((String)key));
			if (caseInsensitiveKey != null)
				return targetMap.get(caseInsensitiveKey);
		}
		return defaultValue;
	}

	public Object put(String key, Object value)
	{
		String oldKey = (String)caseInsensitiveKeys.put(convertKey(key), key);
		if (oldKey != null && !oldKey.equals(key))
			targetMap.remove(oldKey);
		return targetMap.put(key, value);
	}

	public void putAll(Map map)
	{
		if (map.isEmpty())
			return;
		java.util.Map.Entry entry;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); put((String)entry.getKey(), entry.getValue()))
			entry = (java.util.Map.Entry)iterator.next();

	}

	public Object remove(Object key)
	{
		if (key instanceof String)
		{
			String caseInsensitiveKey = (String)caseInsensitiveKeys.remove(convertKey((String)key));
			if (caseInsensitiveKey != null)
				return targetMap.remove(caseInsensitiveKey);
		}
		return null;
	}

	public void clear()
	{
		caseInsensitiveKeys.clear();
		targetMap.clear();
	}

	public Set keySet()
	{
		return targetMap.keySet();
	}

	public Collection values()
	{
		return targetMap.values();
	}

	public Set entrySet()
	{
		return targetMap.entrySet();
	}

	public LinkedCaseInsensitiveMap clone()
	{
		return new LinkedCaseInsensitiveMap(this);
	}

	public boolean equals(Object obj)
	{
		return targetMap.equals(obj);
	}

	public int hashCode()
	{
		return targetMap.hashCode();
	}

	public String toString()
	{
		return targetMap.toString();
	}

	protected String convertKey(String key)
	{
		return key.toLowerCase(locale);
	}

	protected boolean removeEldestEntry(java.util.Map.Entry eldest)
	{
		return false;
	}

	public volatile Object clone()
		throws CloneNotSupportedException
	{
		return clone();
	}

	public volatile Object put(Object obj, Object obj1)
	{
		return put((String)obj, obj1);
	}

}
