// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MapToMapConverter.java

package org.springframework.core.convert.support;

import java.util.*;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

// Referenced classes of package org.springframework.core.convert.support:
//			ConversionUtils

final class MapToMapConverter
	implements ConditionalGenericConverter
{
	private static class MapEntry
	{

		private final Object key;
		private final Object value;

		public void addToMap(Map map)
		{
			map.put(key, value);
		}

		public MapEntry(Object key, Object value)
		{
			this.key = key;
			this.value = value;
		}
	}


	private final ConversionService conversionService;

	public MapToMapConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return Collections.singleton(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/util/Map, java/util/Map));
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return canConvertKey(sourceType, targetType) && canConvertValue(sourceType, targetType);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
			return null;
		Map sourceMap = (Map)source;
		boolean copyRequired = !targetType.getType().isInstance(source);
		if (!copyRequired && sourceMap.isEmpty())
			return sourceMap;
		TypeDescriptor keyDesc = targetType.getMapKeyTypeDescriptor();
		TypeDescriptor valueDesc = targetType.getMapValueTypeDescriptor();
		List targetEntries = new ArrayList(sourceMap.size());
		Map targetMap = sourceMap.entrySet().iterator();
		do
		{
			if (!targetMap.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry)targetMap.next();
			Object sourceKey = entry.getKey();
			Object sourceValue = entry.getValue();
			Object targetKey = convertKey(sourceKey, sourceType, keyDesc);
			Object targetValue = convertValue(sourceValue, sourceType, valueDesc);
			targetEntries.add(new MapEntry(targetKey, targetValue));
			if (sourceKey != targetKey || sourceValue != targetValue)
				copyRequired = true;
		} while (true);
		if (!copyRequired)
			return sourceMap;
		targetMap = CollectionFactory.createMap(targetType.getType(), keyDesc == null ? null : keyDesc.getType(), sourceMap.size());
		MapEntry entry;
		for (Iterator iterator = targetEntries.iterator(); iterator.hasNext(); entry.addToMap(targetMap))
			entry = (MapEntry)iterator.next();

		return targetMap;
	}

	private boolean canConvertKey(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getMapKeyTypeDescriptor(), targetType.getMapKeyTypeDescriptor(), conversionService);
	}

	private boolean canConvertValue(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return ConversionUtils.canConvertElements(sourceType.getMapValueTypeDescriptor(), targetType.getMapValueTypeDescriptor(), conversionService);
	}

	private Object convertKey(Object sourceKey, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (targetType == null)
			return sourceKey;
		else
			return conversionService.convert(sourceKey, sourceType.getMapKeyTypeDescriptor(sourceKey), targetType);
	}

	private Object convertValue(Object sourceValue, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (targetType == null)
			return sourceValue;
		else
			return conversionService.convert(sourceValue, sourceType.getMapValueTypeDescriptor(sourceValue), targetType);
	}
}
