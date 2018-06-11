// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultConversionService.java

package org.springframework.core.convert.support;

import java.nio.charset.Charset;
import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.convert.support:
//			GenericConversionService, ByteBufferConverter, ObjectToObjectConverter, IdToEntityConverter, 
//			FallbackObjectToStringConverter, ObjectToOptionalConverter, ArrayToCollectionConverter, CollectionToArrayConverter, 
//			ArrayToArrayConverter, CollectionToCollectionConverter, MapToMapConverter, ArrayToStringConverter, 
//			StringToArrayConverter, ArrayToObjectConverter, ObjectToArrayConverter, CollectionToStringConverter, 
//			StringToCollectionConverter, CollectionToObjectConverter, ObjectToCollectionConverter, StreamConverter, 
//			NumberToNumberConverterFactory, StringToNumberConverterFactory, ObjectToStringConverter, StringToCharacterConverter, 
//			NumberToCharacterConverter, CharacterToNumberFactory, StringToBooleanConverter, StringToEnumConverterFactory, 
//			EnumToStringConverter, IntegerToEnumConverterFactory, EnumToIntegerConverter, StringToLocaleConverter, 
//			StringToCharsetConverter, StringToCurrencyConverter, StringToPropertiesConverter, PropertiesToStringConverter, 
//			StringToUUIDConverter, StringToTimeZoneConverter, ZoneIdToTimeZoneConverter, ZonedDateTimeToCalendarConverter

public class DefaultConversionService extends GenericConversionService
{
	private static final class Jsr310ConverterRegistrar
	{

		public static void registerJsr310Converters(ConverterRegistry converterRegistry)
		{
			converterRegistry.addConverter(new StringToTimeZoneConverter());
			converterRegistry.addConverter(new ZoneIdToTimeZoneConverter());
			converterRegistry.addConverter(new ZonedDateTimeToCalendarConverter());
		}

		private Jsr310ConverterRegistrar()
		{
		}
	}


	private static final boolean javaUtilOptionalClassAvailable = ClassUtils.isPresent("java.util.Optional", org/springframework/core/convert/support/DefaultConversionService.getClassLoader());
	private static final boolean jsr310Available = ClassUtils.isPresent("java.time.ZoneId", org/springframework/core/convert/support/DefaultConversionService.getClassLoader());
	private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", org/springframework/core/convert/support/DefaultConversionService.getClassLoader());
	private static volatile DefaultConversionService sharedInstance;

	public static ConversionService getSharedInstance()
	{
		if (sharedInstance == null)
			synchronized (org/springframework/core/convert/support/DefaultConversionService)
			{
				if (sharedInstance == null)
					sharedInstance = new DefaultConversionService();
			}
		return sharedInstance;
	}

	public DefaultConversionService()
	{
		addDefaultConverters(this);
	}

	public static void addDefaultConverters(ConverterRegistry converterRegistry)
	{
		addScalarConverters(converterRegistry);
		addCollectionConverters(converterRegistry);
		converterRegistry.addConverter(new ByteBufferConverter((ConversionService)converterRegistry));
		if (jsr310Available)
			Jsr310ConverterRegistrar.registerJsr310Converters(converterRegistry);
		converterRegistry.addConverter(new ObjectToObjectConverter());
		converterRegistry.addConverter(new IdToEntityConverter((ConversionService)converterRegistry));
		converterRegistry.addConverter(new FallbackObjectToStringConverter());
		if (javaUtilOptionalClassAvailable)
			converterRegistry.addConverter(new ObjectToOptionalConverter((ConversionService)converterRegistry));
	}

	public static void addCollectionConverters(ConverterRegistry converterRegistry)
	{
		ConversionService conversionService = (ConversionService)converterRegistry;
		converterRegistry.addConverter(new ArrayToCollectionConverter(conversionService));
		converterRegistry.addConverter(new CollectionToArrayConverter(conversionService));
		converterRegistry.addConverter(new ArrayToArrayConverter(conversionService));
		converterRegistry.addConverter(new CollectionToCollectionConverter(conversionService));
		converterRegistry.addConverter(new MapToMapConverter(conversionService));
		converterRegistry.addConverter(new ArrayToStringConverter(conversionService));
		converterRegistry.addConverter(new StringToArrayConverter(conversionService));
		converterRegistry.addConverter(new ArrayToObjectConverter(conversionService));
		converterRegistry.addConverter(new ObjectToArrayConverter(conversionService));
		converterRegistry.addConverter(new CollectionToStringConverter(conversionService));
		converterRegistry.addConverter(new StringToCollectionConverter(conversionService));
		converterRegistry.addConverter(new CollectionToObjectConverter(conversionService));
		converterRegistry.addConverter(new ObjectToCollectionConverter(conversionService));
		if (streamAvailable)
			converterRegistry.addConverter(new StreamConverter(conversionService));
	}

	private static void addScalarConverters(ConverterRegistry converterRegistry)
	{
		converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());
		converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
		converterRegistry.addConverter(java/lang/Number, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverter(new StringToCharacterConverter());
		converterRegistry.addConverter(java/lang/Character, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverter(new NumberToCharacterConverter());
		converterRegistry.addConverterFactory(new CharacterToNumberFactory());
		converterRegistry.addConverter(new StringToBooleanConverter());
		converterRegistry.addConverter(java/lang/Boolean, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverterFactory(new StringToEnumConverterFactory());
		converterRegistry.addConverter(new EnumToStringConverter((ConversionService)converterRegistry));
		converterRegistry.addConverterFactory(new IntegerToEnumConverterFactory());
		converterRegistry.addConverter(new EnumToIntegerConverter((ConversionService)converterRegistry));
		converterRegistry.addConverter(new StringToLocaleConverter());
		converterRegistry.addConverter(java/util/Locale, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverter(new StringToCharsetConverter());
		converterRegistry.addConverter(java/nio/charset/Charset, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverter(new StringToCurrencyConverter());
		converterRegistry.addConverter(java/util/Currency, java/lang/String, new ObjectToStringConverter());
		converterRegistry.addConverter(new StringToPropertiesConverter());
		converterRegistry.addConverter(new PropertiesToStringConverter());
		converterRegistry.addConverter(new StringToUUIDConverter());
		converterRegistry.addConverter(java/util/UUID, java/lang/String, new ObjectToStringConverter());
	}

}
