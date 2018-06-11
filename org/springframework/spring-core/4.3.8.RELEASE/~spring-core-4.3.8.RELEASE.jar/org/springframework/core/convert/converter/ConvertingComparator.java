// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConvertingComparator.java

package org.springframework.core.convert.converter;

import java.util.Comparator;
import java.util.Map;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.comparator.ComparableComparator;

// Referenced classes of package org.springframework.core.convert.converter:
//			Converter

public class ConvertingComparator
	implements Comparator
{
	private static class ConversionServiceConverter
		implements Converter
	{

		private final ConversionService conversionService;
		private final Class targetType;

		public Object convert(Object source)
		{
			return conversionService.convert(source, targetType);
		}

		public ConversionServiceConverter(ConversionService conversionService, Class targetType)
		{
			Assert.notNull(conversionService, "ConversionService must not be null");
			Assert.notNull(targetType, "TargetType must not be null");
			this.conversionService = conversionService;
			this.targetType = targetType;
		}
	}


	private final Comparator comparator;
	private final Converter converter;

	public ConvertingComparator(Converter converter)
	{
		this(((Comparator) (ComparableComparator.INSTANCE)), converter);
	}

	public ConvertingComparator(Comparator comparator, Converter converter)
	{
		Assert.notNull(comparator, "Comparator must not be null");
		Assert.notNull(converter, "Converter must not be null");
		this.comparator = comparator;
		this.converter = converter;
	}

	public ConvertingComparator(Comparator comparator, ConversionService conversionService, Class targetType)
	{
		this(comparator, ((Converter) (new ConversionServiceConverter(conversionService, targetType))));
	}

	public int compare(Object o1, Object o2)
	{
		Object c1 = converter.convert(o1);
		Object c2 = converter.convert(o2);
		return comparator.compare(c1, c2);
	}

	public static ConvertingComparator mapEntryKeys(Comparator comparator)
	{
		return new ConvertingComparator(comparator, new Converter() {

			public Object convert(java.util.Map.Entry source)
			{
				return source.getKey();
			}

			public volatile Object convert(Object obj)
			{
				return convert((java.util.Map.Entry)obj);
			}

		});
	}

	public static ConvertingComparator mapEntryValues(Comparator comparator)
	{
		return new ConvertingComparator(comparator, new Converter() {

			public Object convert(java.util.Map.Entry source)
			{
				return source.getValue();
			}

			public volatile Object convert(Object obj)
			{
				return convert((java.util.Map.Entry)obj);
			}

		});
	}
}
