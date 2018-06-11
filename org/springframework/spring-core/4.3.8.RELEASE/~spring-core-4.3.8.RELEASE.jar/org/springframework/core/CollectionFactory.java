// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionFactory.java

package org.springframework.core;

import java.util.*;
import org.springframework.util.*;

public abstract class CollectionFactory
{

	private static final Set approximableCollectionTypes;
	private static final Set approximableMapTypes;

	public CollectionFactory()
	{
	}

	public static boolean isApproximableCollectionType(Class collectionType)
	{
		return collectionType != null && approximableCollectionTypes.contains(collectionType);
	}

	public static Collection createApproximateCollection(Object collection, int capacity)
	{
		if (collection instanceof LinkedList)
			return new LinkedList();
		if (collection instanceof List)
			return new ArrayList(capacity);
		if (collection instanceof EnumSet)
		{
			Collection enumSet = EnumSet.copyOf((EnumSet)collection);
			enumSet.clear();
			return enumSet;
		}
		if (collection instanceof SortedSet)
			return new TreeSet(((SortedSet)collection).comparator());
		else
			return new LinkedHashSet(capacity);
	}

	public static Collection createCollection(Class collectionType, int capacity)
	{
		return createCollection(collectionType, null, capacity);
	}

	public static Collection createCollection(Class collectionType, Class elementType, int capacity)
	{
		Assert.notNull(collectionType, "Collection type must not be null");
		if (collectionType.isInterface())
		{
			if (java/util/Set == collectionType || java/util/Collection == collectionType)
				return new LinkedHashSet(capacity);
			if (java/util/List == collectionType)
				return new ArrayList(capacity);
			if (java/util/SortedSet == collectionType || java/util/NavigableSet == collectionType)
				return new TreeSet();
			else
				throw new IllegalArgumentException((new StringBuilder()).append("Unsupported Collection interface: ").append(collectionType.getName()).toString());
		}
		if (java/util/EnumSet == collectionType)
		{
			Assert.notNull(elementType, "Cannot create EnumSet for unknown element type");
			return EnumSet.noneOf(asEnumType(elementType));
		}
		if (!java/util/Collection.isAssignableFrom(collectionType))
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported Collection type: ").append(collectionType.getName()).toString());
		return (Collection)collectionType.newInstance();
		Throwable ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Could not instantiate Collection type: ").append(collectionType.getName()).toString(), ex);
	}

	public static boolean isApproximableMapType(Class mapType)
	{
		return mapType != null && approximableMapTypes.contains(mapType);
	}

	public static Map createApproximateMap(Object map, int capacity)
	{
		if (map instanceof EnumMap)
		{
			EnumMap enumMap = new EnumMap((EnumMap)map);
			enumMap.clear();
			return enumMap;
		}
		if (map instanceof SortedMap)
			return new TreeMap(((SortedMap)map).comparator());
		else
			return new LinkedHashMap(capacity);
	}

	public static Map createMap(Class mapType, int capacity)
	{
		return createMap(mapType, null, capacity);
	}

	public static Map createMap(Class mapType, Class keyType, int capacity)
	{
		Assert.notNull(mapType, "Map type must not be null");
		if (mapType.isInterface())
		{
			if (java/util/Map == mapType)
				return new LinkedHashMap(capacity);
			if (java/util/SortedMap == mapType || java/util/NavigableMap == mapType)
				return new TreeMap();
			if (org/springframework/util/MultiValueMap == mapType)
				return new LinkedMultiValueMap();
			else
				throw new IllegalArgumentException((new StringBuilder()).append("Unsupported Map interface: ").append(mapType.getName()).toString());
		}
		if (java/util/EnumMap == mapType)
		{
			Assert.notNull(keyType, "Cannot create EnumMap for unknown key type");
			return new EnumMap(asEnumType(keyType));
		}
		if (!java/util/Map.isAssignableFrom(mapType))
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported Map type: ").append(mapType.getName()).toString());
		return (Map)mapType.newInstance();
		Throwable ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Could not instantiate Map type: ").append(mapType.getName()).toString(), ex);
	}

	public static Properties createStringAdaptingProperties()
	{
		return new Properties() {

			public String getProperty(String key)
			{
				Object value = get(key);
				return value == null ? null : value.toString();
			}

		};
	}

	private static Class asEnumType(Class enumType)
	{
		Assert.notNull(enumType, "Enum type must not be null");
		if (!java/lang/Enum.isAssignableFrom(enumType))
			throw new IllegalArgumentException((new StringBuilder()).append("Supplied type is not an enum: ").append(enumType.getName()).toString());
		else
			return enumType.asSubclass(java/lang/Enum);
	}

	static 
	{
		approximableCollectionTypes = new HashSet();
		approximableMapTypes = new HashSet();
		approximableCollectionTypes.add(java/util/Collection);
		approximableCollectionTypes.add(java/util/List);
		approximableCollectionTypes.add(java/util/Set);
		approximableCollectionTypes.add(java/util/SortedSet);
		approximableCollectionTypes.add(java/util/NavigableSet);
		approximableMapTypes.add(java/util/Map);
		approximableMapTypes.add(java/util/SortedMap);
		approximableMapTypes.add(java/util/NavigableMap);
		approximableCollectionTypes.add(java/util/ArrayList);
		approximableCollectionTypes.add(java/util/LinkedList);
		approximableCollectionTypes.add(java/util/HashSet);
		approximableCollectionTypes.add(java/util/LinkedHashSet);
		approximableCollectionTypes.add(java/util/TreeSet);
		approximableCollectionTypes.add(java/util/EnumSet);
		approximableMapTypes.add(java/util/HashMap);
		approximableMapTypes.add(java/util/LinkedHashMap);
		approximableMapTypes.add(java/util/TreeMap);
		approximableMapTypes.add(java/util/EnumMap);
	}
}
