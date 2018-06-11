// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LinkedMultiValueMap.java

package org.springframework.util;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			MultiValueMap

public class LinkedMultiValueMap
	implements MultiValueMap, Serializable, Cloneable
{

	private static final long serialVersionUID = 0x34c04e5c106cc0fbL;
	private final Map targetMap;

	public LinkedMultiValueMap()
	{
		targetMap = new LinkedHashMap();
	}

	public LinkedMultiValueMap(int initialCapacity)
	{
		targetMap = new LinkedHashMap(initialCapacity);
	}

	public LinkedMultiValueMap(Map otherMap)
	{
		targetMap = new LinkedHashMap(otherMap);
	}

	public void add(Object key, Object value)
	{
		List values = (List)targetMap.get(key);
		if (values == null)
		{
			values = new LinkedList();
			targetMap.put(key, values);
		}
		values.add(value);
	}

	public Object getFirst(Object key)
	{
		List values = (List)targetMap.get(key);
		return values == null ? null : values.get(0);
	}

	public void set(Object key, Object value)
	{
		List values = new LinkedList();
		values.add(value);
		targetMap.put(key, values);
	}

	public void setAll(Map values)
	{
		java.util.Map.Entry entry;
		for (Iterator iterator = values.entrySet().iterator(); iterator.hasNext(); set(entry.getKey(), entry.getValue()))
			entry = (java.util.Map.Entry)iterator.next();

	}

	public Map toSingleValueMap()
	{
		LinkedHashMap singleValueMap = new LinkedHashMap(targetMap.size());
		java.util.Map.Entry entry;
		for (Iterator iterator = targetMap.entrySet().iterator(); iterator.hasNext(); singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0)))
			entry = (java.util.Map.Entry)iterator.next();

		return singleValueMap;
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
		return targetMap.containsKey(key);
	}

	public boolean containsValue(Object value)
	{
		return targetMap.containsValue(value);
	}

	public List get(Object key)
	{
		return (List)targetMap.get(key);
	}

	public List put(Object key, List value)
	{
		return (List)targetMap.put(key, value);
	}

	public List remove(Object key)
	{
		return (List)targetMap.remove(key);
	}

	public void putAll(Map map)
	{
		targetMap.putAll(map);
	}

	public void clear()
	{
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

	public LinkedMultiValueMap deepCopy()
	{
		LinkedMultiValueMap copy = new LinkedMultiValueMap(targetMap.size());
		java.util.Map.Entry entry;
		for (Iterator iterator = targetMap.entrySet().iterator(); iterator.hasNext(); copy.put(entry.getKey(), new LinkedList((Collection)entry.getValue())))
			entry = (java.util.Map.Entry)iterator.next();

		return copy;
	}

	public LinkedMultiValueMap clone()
	{
		return new LinkedMultiValueMap(this);
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

	public volatile Object clone()
		throws CloneNotSupportedException
	{
		return clone();
	}

	public volatile Object remove(Object obj)
	{
		return remove(obj);
	}

	public volatile Object put(Object obj, Object obj1)
	{
		return put(obj, (List)obj1);
	}

	public volatile Object get(Object obj)
	{
		return get(obj);
	}
}
