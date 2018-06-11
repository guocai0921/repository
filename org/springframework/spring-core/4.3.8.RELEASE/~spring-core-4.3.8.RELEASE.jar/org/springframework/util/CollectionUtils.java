// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionUtils.java

package org.springframework.util;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			ObjectUtils, Assert, MultiValueMap

public abstract class CollectionUtils
{
	private static class MultiValueMapAdapter
		implements MultiValueMap, Serializable
	{

		private final Map map;

		public void add(Object key, Object value)
		{
			List values = (List)map.get(key);
			if (values == null)
			{
				values = new LinkedList();
				map.put(key, values);
			}
			values.add(value);
		}

		public Object getFirst(Object key)
		{
			List values = (List)map.get(key);
			return values == null ? null : values.get(0);
		}

		public void set(Object key, Object value)
		{
			List values = new LinkedList();
			values.add(value);
			map.put(key, values);
		}

		public void setAll(Map values)
		{
			java.util.Map.Entry entry;
			for (Iterator iterator = values.entrySet().iterator(); iterator.hasNext(); set(entry.getKey(), entry.getValue()))
				entry = (java.util.Map.Entry)iterator.next();

		}

		public Map toSingleValueMap()
		{
			LinkedHashMap singleValueMap = new LinkedHashMap(map.size());
			java.util.Map.Entry entry;
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0)))
				entry = (java.util.Map.Entry)iterator.next();

			return singleValueMap;
		}

		public int size()
		{
			return map.size();
		}

		public boolean isEmpty()
		{
			return map.isEmpty();
		}

		public boolean containsKey(Object key)
		{
			return map.containsKey(key);
		}

		public boolean containsValue(Object value)
		{
			return map.containsValue(value);
		}

		public List get(Object key)
		{
			return (List)map.get(key);
		}

		public List put(Object key, List value)
		{
			return (List)map.put(key, value);
		}

		public List remove(Object key)
		{
			return (List)map.remove(key);
		}

		public void putAll(Map map)
		{
			this.map.putAll(map);
		}

		public void clear()
		{
			map.clear();
		}

		public Set keySet()
		{
			return map.keySet();
		}

		public Collection values()
		{
			return map.values();
		}

		public Set entrySet()
		{
			return map.entrySet();
		}

		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			else
				return map.equals(other);
		}

		public int hashCode()
		{
			return map.hashCode();
		}

		public String toString()
		{
			return map.toString();
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

		public MultiValueMapAdapter(Map map)
		{
			Assert.notNull(map, "'map' must not be null");
			this.map = map;
		}
	}

	private static class EnumerationIterator
		implements Iterator
	{

		private final Enumeration enumeration;

		public boolean hasNext()
		{
			return enumeration.hasMoreElements();
		}

		public Object next()
		{
			return enumeration.nextElement();
		}

		public void remove()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException("Not supported");
		}

		public EnumerationIterator(Enumeration enumeration)
		{
			this.enumeration = enumeration;
		}
	}


	public CollectionUtils()
	{
	}

	public static boolean isEmpty(Collection collection)
	{
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Map map)
	{
		return map == null || map.isEmpty();
	}

	public static List arrayToList(Object source)
	{
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	public static void mergeArrayIntoCollection(Object array, Collection collection)
	{
		if (collection == null)
			throw new IllegalArgumentException("Collection must not be null");
		Object arr[] = ObjectUtils.toObjectArray(array);
		Object aobj[] = arr;
		int i = aobj.length;
		for (int j = 0; j < i; j++)
		{
			Object elem = aobj[j];
			collection.add(elem);
		}

	}

	public static void mergePropertiesIntoMap(Properties props, Map map)
	{
		if (map == null)
			throw new IllegalArgumentException("Map must not be null");
		if (props != null)
		{
			String key;
			Object value;
			for (Enumeration en = props.propertyNames(); en.hasMoreElements(); map.put(key, value))
			{
				key = (String)en.nextElement();
				value = props.get(key);
				if (value == null)
					value = props.getProperty(key);
			}

		}
	}

	public static boolean contains(Iterator iterator, Object element)
	{
		if (iterator != null)
			while (iterator.hasNext()) 
			{
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element))
					return true;
			}
		return false;
	}

	public static boolean contains(Enumeration enumeration, Object element)
	{
		if (enumeration != null)
			while (enumeration.hasMoreElements()) 
			{
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element))
					return true;
			}
		return false;
	}

	public static boolean containsInstance(Collection collection, Object element)
	{
label0:
		{
			if (collection == null)
				break label0;
			Iterator iterator = collection.iterator();
			Object candidate;
			do
			{
				if (!iterator.hasNext())
					break label0;
				candidate = iterator.next();
			} while (candidate != element);
			return true;
		}
		return false;
	}

	public static boolean containsAny(Collection source, Collection candidates)
	{
		if (isEmpty(source) || isEmpty(candidates))
			return false;
		for (Iterator iterator = candidates.iterator(); iterator.hasNext();)
		{
			Object candidate = iterator.next();
			if (source.contains(candidate))
				return true;
		}

		return false;
	}

	public static Object findFirstMatch(Collection source, Collection candidates)
	{
		if (isEmpty(source) || isEmpty(candidates))
			return null;
		for (Iterator iterator = candidates.iterator(); iterator.hasNext();)
		{
			Object candidate = iterator.next();
			if (source.contains(candidate))
				return candidate;
		}

		return null;
	}

	public static Object findValueOfType(Collection collection, Class type)
	{
		if (isEmpty(collection))
			return null;
		Object value = null;
		Iterator iterator = collection.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			Object element = iterator.next();
			if (type == null || type.isInstance(element))
			{
				if (value != null)
					return null;
				value = element;
			}
		} while (true);
		return value;
	}

	public static Object findValueOfType(Collection collection, Class types[])
	{
		if (isEmpty(collection) || ObjectUtils.isEmpty(types))
			return null;
		Class aclass[] = types;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class type = aclass[j];
			Object value = findValueOfType(collection, type);
			if (value != null)
				return value;
		}

		return null;
	}

	public static boolean hasUniqueObject(Collection collection)
	{
label0:
		{
			if (isEmpty(collection))
				return false;
			boolean hasCandidate = false;
			Object candidate = null;
			Object elem;
label1:
			do
			{
				for (Iterator iterator = collection.iterator(); iterator.hasNext();)
				{
					elem = iterator.next();
					if (hasCandidate)
						continue label1;
					hasCandidate = true;
					candidate = elem;
				}

				break label0;
			} while (candidate == elem);
			return false;
		}
		return true;
	}

	public static Class findCommonElementType(Collection collection)
	{
		Class candidate;
label0:
		{
			if (isEmpty(collection))
				return null;
			candidate = null;
			Object val;
label1:
			do
			{
				for (Iterator iterator = collection.iterator(); iterator.hasNext();)
				{
					val = iterator.next();
					if (val != null)
					{
						if (candidate != null)
							continue label1;
						candidate = val.getClass();
					}
				}

				break label0;
			} while (candidate == val.getClass());
			return null;
		}
		return candidate;
	}

	public static Object[] toArray(Enumeration enumeration, Object array[])
	{
		ArrayList elements = new ArrayList();
		for (; enumeration.hasMoreElements(); elements.add(enumeration.nextElement()));
		return elements.toArray(array);
	}

	public static Iterator toIterator(Enumeration enumeration)
	{
		return new EnumerationIterator(enumeration);
	}

	public static MultiValueMap toMultiValueMap(Map map)
	{
		return new MultiValueMapAdapter(map);
	}

	public static MultiValueMap unmodifiableMultiValueMap(MultiValueMap map)
	{
		Assert.notNull(map, "'map' must not be null");
		Map result = new LinkedHashMap(map.size());
		java.util.Map.Entry entry;
		List values;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); result.put(entry.getKey(), values))
		{
			entry = (java.util.Map.Entry)iterator.next();
			values = Collections.unmodifiableList((List)entry.getValue());
		}

		Map unmodifiableMap = Collections.unmodifiableMap(result);
		return toMultiValueMap(unmodifiableMap);
	}
}
