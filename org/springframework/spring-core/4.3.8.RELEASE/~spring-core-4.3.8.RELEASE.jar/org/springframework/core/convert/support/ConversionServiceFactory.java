// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConversionServiceFactory.java

package org.springframework.core.convert.support;

import java.util.Iterator;
import java.util.Set;
import org.springframework.core.convert.converter.*;

public abstract class ConversionServiceFactory
{

	public ConversionServiceFactory()
	{
	}

	public static void registerConverters(Set converters, ConverterRegistry registry)
	{
		if (converters != null)
		{
			for (Iterator iterator = converters.iterator(); iterator.hasNext();)
			{
				Object converter = iterator.next();
				if (converter instanceof GenericConverter)
					registry.addConverter((GenericConverter)converter);
				else
				if (converter instanceof Converter)
					registry.addConverter((Converter)converter);
				else
				if (converter instanceof ConverterFactory)
					registry.addConverterFactory((ConverterFactory)converter);
				else
					throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
			}

		}
	}
}
