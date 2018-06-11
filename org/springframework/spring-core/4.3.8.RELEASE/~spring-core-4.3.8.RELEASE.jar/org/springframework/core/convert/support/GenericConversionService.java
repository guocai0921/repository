// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericConversionService.java

package org.springframework.core.convert.support;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import org.springframework.core.DecoratingProxy;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.*;
import org.springframework.core.convert.converter.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.convert.support:
//			ConfigurableConversionService, ConversionUtils

public class GenericConversionService
	implements ConfigurableConversionService
{
	private static class NoOpConverter
		implements GenericConverter
	{

		private final String name;

		public Set getConvertibleTypes()
		{
			return null;
		}

		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			return source;
		}

		public String toString()
		{
			return name;
		}

		public NoOpConverter(String name)
		{
			this.name = name;
		}
	}

	private static class ConvertersForPair
	{

		private final LinkedList converters;

		public void add(GenericConverter converter)
		{
			converters.addFirst(converter);
		}

		public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			for (Iterator iterator = converters.iterator(); iterator.hasNext();)
			{
				GenericConverter converter = (GenericConverter)iterator.next();
				if (!(converter instanceof ConditionalGenericConverter) || ((ConditionalGenericConverter)converter).matches(sourceType, targetType))
					return converter;
			}

			return null;
		}

		public String toString()
		{
			return StringUtils.collectionToCommaDelimitedString(converters);
		}

		private ConvertersForPair()
		{
			converters = new LinkedList();
		}

	}

	private static class Converters
	{

		private final Set globalConverters;
		private final Map converters;

		public void add(GenericConverter converter)
		{
			Set convertibleTypes = converter.getConvertibleTypes();
			if (convertibleTypes == null)
			{
				Assert.state(converter instanceof ConditionalConverter, "Only conditional converters may return null convertible types");
				globalConverters.add(converter);
			} else
			{
				ConvertersForPair convertersForPair;
				for (Iterator iterator = convertibleTypes.iterator(); iterator.hasNext(); convertersForPair.add(converter))
				{
					org.springframework.core.convert.converter.GenericConverter.ConvertiblePair convertiblePair = (org.springframework.core.convert.converter.GenericConverter.ConvertiblePair)iterator.next();
					convertersForPair = getMatchableConverters(convertiblePair);
				}

			}
		}

		private ConvertersForPair getMatchableConverters(org.springframework.core.convert.converter.GenericConverter.ConvertiblePair convertiblePair)
		{
			ConvertersForPair convertersForPair = (ConvertersForPair)converters.get(convertiblePair);
			if (convertersForPair == null)
			{
				convertersForPair = new ConvertersForPair();
				converters.put(convertiblePair, convertersForPair);
			}
			return convertersForPair;
		}

		public void remove(Class sourceType, Class targetType)
		{
			converters.remove(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(sourceType, targetType));
		}

		public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			List sourceCandidates = getClassHierarchy(sourceType.getType());
			List targetCandidates = getClassHierarchy(targetType.getType());
			Iterator iterator = sourceCandidates.iterator();
			GenericConverter converter;
label0:
			do
				if (iterator.hasNext())
				{
					Class sourceCandidate = (Class)iterator.next();
					Iterator iterator1 = targetCandidates.iterator();
					do
					{
						if (!iterator1.hasNext())
							continue label0;
						Class targetCandidate = (Class)iterator1.next();
						org.springframework.core.convert.converter.GenericConverter.ConvertiblePair convertiblePair = new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
						converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
					} while (converter == null);
					break;
				} else
				{
					return null;
				}
			while (true);
			return converter;
		}

		private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, org.springframework.core.convert.converter.GenericConverter.ConvertiblePair convertiblePair)
		{
			ConvertersForPair convertersForPair = (ConvertersForPair)converters.get(convertiblePair);
			if (convertersForPair != null)
			{
				GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
				if (converter != null)
					return converter;
			}
			for (Iterator iterator = globalConverters.iterator(); iterator.hasNext();)
			{
				GenericConverter globalConverter = (GenericConverter)iterator.next();
				if (((ConditionalConverter)globalConverter).matches(sourceType, targetType))
					return globalConverter;
			}

			return null;
		}

		private List getClassHierarchy(Class type)
		{
			List hierarchy = new ArrayList(20);
			Set visited = new HashSet(20);
			addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
			boolean array = type.isArray();
			for (int i = 0; i < hierarchy.size(); i++)
			{
				Class candidate = (Class)hierarchy.get(i);
				candidate = array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate);
				Class superclass = candidate.getSuperclass();
				if (superclass != null && superclass != java/lang/Object && superclass != java/lang/Enum)
					addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
				addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
			}

			if (java/lang/Enum.isAssignableFrom(type))
			{
				addToClassHierarchy(hierarchy.size(), java/lang/Enum, array, hierarchy, visited);
				addToClassHierarchy(hierarchy.size(), java/lang/Enum, false, hierarchy, visited);
				addInterfacesToClassHierarchy(java/lang/Enum, array, hierarchy, visited);
			}
			addToClassHierarchy(hierarchy.size(), java/lang/Object, array, hierarchy, visited);
			addToClassHierarchy(hierarchy.size(), java/lang/Object, false, hierarchy, visited);
			return hierarchy;
		}

		private void addInterfacesToClassHierarchy(Class type, boolean asArray, List hierarchy, Set visited)
		{
			Class aclass[] = type.getInterfaces();
			int i = aclass.length;
			for (int j = 0; j < i; j++)
			{
				Class implementedInterface = aclass[j];
				addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
			}

		}

		private void addToClassHierarchy(int index, Class type, boolean asArray, List hierarchy, Set visited)
		{
			if (asArray)
				type = Array.newInstance(type, 0).getClass();
			if (visited.add(type))
				hierarchy.add(index, type);
		}

		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("ConversionService converters =\n");
			String converterString;
			for (Iterator iterator = getConverterStrings().iterator(); iterator.hasNext(); builder.append('\t').append(converterString).append('\n'))
				converterString = (String)iterator.next();

			return builder.toString();
		}

		private List getConverterStrings()
		{
			List converterStrings = new ArrayList();
			ConvertersForPair convertersForPair;
			for (Iterator iterator = converters.values().iterator(); iterator.hasNext(); converterStrings.add(convertersForPair.toString()))
				convertersForPair = (ConvertersForPair)iterator.next();

			Collections.sort(converterStrings);
			return converterStrings;
		}

		private Converters()
		{
			globalConverters = new LinkedHashSet();
			converters = new LinkedHashMap(36);
		}

	}

	private static final class ConverterCacheKey
		implements Comparable
	{

		private final TypeDescriptor sourceType;
		private final TypeDescriptor targetType;

		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (!(other instanceof ConverterCacheKey))
			{
				return false;
			} else
			{
				ConverterCacheKey otherKey = (ConverterCacheKey)other;
				return ObjectUtils.nullSafeEquals(sourceType, otherKey.sourceType) && ObjectUtils.nullSafeEquals(targetType, otherKey.targetType);
			}
		}

		public int hashCode()
		{
			return ObjectUtils.nullSafeHashCode(sourceType) * 29 + ObjectUtils.nullSafeHashCode(targetType);
		}

		public String toString()
		{
			return (new StringBuilder()).append("ConverterCacheKey [sourceType = ").append(sourceType).append(", targetType = ").append(targetType).append("]").toString();
		}

		public int compareTo(ConverterCacheKey other)
		{
			int result = sourceType.getResolvableType().toString().compareTo(other.sourceType.getResolvableType().toString());
			if (result == 0)
				result = targetType.getResolvableType().toString().compareTo(other.targetType.getResolvableType().toString());
			return result;
		}

		public volatile int compareTo(Object obj)
		{
			return compareTo((ConverterCacheKey)obj);
		}

		public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			this.sourceType = sourceType;
			this.targetType = targetType;
		}
	}

	private final class ConverterFactoryAdapter
		implements ConditionalGenericConverter
	{

		private final ConverterFactory converterFactory;
		private final org.springframework.core.convert.converter.GenericConverter.ConvertiblePair typeInfo;
		final GenericConversionService this$0;

		public Set getConvertibleTypes()
		{
			return Collections.singleton(typeInfo);
		}

		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			boolean matches = true;
			if (converterFactory instanceof ConditionalConverter)
				matches = ((ConditionalConverter)converterFactory).matches(sourceType, targetType);
			if (matches)
			{
				Converter converter = converterFactory.getConverter(targetType.getType());
				if (converter instanceof ConditionalConverter)
					matches = ((ConditionalConverter)converter).matches(sourceType, targetType);
			}
			return matches;
		}

		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			if (source == null)
				return convertNullSource(sourceType, targetType);
			else
				return converterFactory.getConverter(targetType.getObjectType()).convert(source);
		}

		public String toString()
		{
			return (new StringBuilder()).append(typeInfo).append(" : ").append(converterFactory).toString();
		}

		public ConverterFactoryAdapter(ConverterFactory converterFactory, org.springframework.core.convert.converter.GenericConverter.ConvertiblePair typeInfo)
		{
			this$0 = GenericConversionService.this;
			super();
			this.converterFactory = converterFactory;
			this.typeInfo = typeInfo;
		}
	}

	private final class ConverterAdapter
		implements ConditionalGenericConverter
	{

		private final Converter converter;
		private final org.springframework.core.convert.converter.GenericConverter.ConvertiblePair typeInfo;
		private final ResolvableType targetType;
		final GenericConversionService this$0;

		public Set getConvertibleTypes()
		{
			return Collections.singleton(typeInfo);
		}

		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			if (typeInfo.getTargetType() != targetType.getObjectType())
				return false;
			ResolvableType rt = targetType.getResolvableType();
			if (!(rt.getType() instanceof Class) && !rt.isAssignableFrom(this.targetType) && !this.targetType.hasUnresolvableGenerics())
				return false;
			else
				return !(converter instanceof ConditionalConverter) || ((ConditionalConverter)converter).matches(sourceType, targetType);
		}

		public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
		{
			if (source == null)
				return convertNullSource(sourceType, targetType);
			else
				return converter.convert(source);
		}

		public String toString()
		{
			return (new StringBuilder()).append(typeInfo).append(" : ").append(converter).toString();
		}

		public ConverterAdapter(Converter converter, ResolvableType sourceType, ResolvableType targetType)
		{
			this$0 = GenericConversionService.this;
			super();
			this.converter = converter;
			typeInfo = new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(sourceType.resolve(java/lang/Object), targetType.resolve(java/lang/Object));
			this.targetType = targetType;
		}
	}


	private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
	private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
	private static Object javaUtilOptionalEmpty = null;
	private final Converters converters = new Converters();
	private final Map converterCache = new ConcurrentReferenceHashMap(64);

	public GenericConversionService()
	{
	}

	public void addConverter(Converter converter)
	{
		ResolvableType typeInfo[] = getRequiredTypeInfo(converter.getClass(), org/springframework/core/convert/converter/Converter);
		if (typeInfo == null && (converter instanceof DecoratingProxy))
			typeInfo = getRequiredTypeInfo(((DecoratingProxy)converter).getDecoratedClass(), org/springframework/core/convert/converter/Converter);
		if (typeInfo == null)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Unable to determine source type <S> and target type <T> for your Converter [").append(converter.getClass().getName()).append("]; does the class parameterize those types?").toString());
		} else
		{
			addConverter(((GenericConverter) (new ConverterAdapter(converter, typeInfo[0], typeInfo[1]))));
			return;
		}
	}

	public void addConverter(Class sourceType, Class targetType, Converter converter)
	{
		addConverter(((GenericConverter) (new ConverterAdapter(converter, ResolvableType.forClass(sourceType), ResolvableType.forClass(targetType)))));
	}

	public void addConverter(GenericConverter converter)
	{
		converters.add(converter);
		invalidateCache();
	}

	public void addConverterFactory(ConverterFactory factory)
	{
		ResolvableType typeInfo[] = getRequiredTypeInfo(factory.getClass(), org/springframework/core/convert/converter/ConverterFactory);
		if (typeInfo == null && (factory instanceof DecoratingProxy))
			typeInfo = getRequiredTypeInfo(((DecoratingProxy)factory).getDecoratedClass(), org/springframework/core/convert/converter/ConverterFactory);
		if (typeInfo == null)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Unable to determine source type <S> and target type <T> for your ConverterFactory [").append(factory.getClass().getName()).append("]; does the class parameterize those types?").toString());
		} else
		{
			addConverter(new ConverterFactoryAdapter(factory, new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(typeInfo[0].resolve(), typeInfo[1].resolve())));
			return;
		}
	}

	public void removeConvertible(Class sourceType, Class targetType)
	{
		converters.remove(sourceType, targetType);
		invalidateCache();
	}

	public boolean canConvert(Class sourceType, Class targetType)
	{
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		return canConvert(sourceType == null ? null : TypeDescriptor.valueOf(sourceType), TypeDescriptor.valueOf(targetType));
	}

	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null)
		{
			return true;
		} else
		{
			GenericConverter converter = getConverter(sourceType, targetType);
			return converter != null;
		}
	}

	public boolean canBypassConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null)
		{
			return true;
		} else
		{
			GenericConverter converter = getConverter(sourceType, targetType);
			return converter == NO_OP_CONVERTER;
		}
	}

	public Object convert(Object source, Class targetType)
	{
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		return convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null)
		{
			Assert.isTrue(source == null, "Source must be [null] if source type == [null]");
			return handleResult(null, targetType, convertNullSource(null, targetType));
		}
		if (source != null && !sourceType.getObjectType().isInstance(source))
			throw new IllegalArgumentException((new StringBuilder()).append("Source to convert from must be an instance of [").append(sourceType).append("]; instead it was a [").append(source.getClass().getName()).append("]").toString());
		GenericConverter converter = getConverter(sourceType, targetType);
		if (converter != null)
		{
			Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
			return handleResult(sourceType, targetType, result);
		} else
		{
			return handleConverterNotFound(source, sourceType, targetType);
		}
	}

	public Object convert(Object source, TypeDescriptor targetType)
	{
		return convert(source, TypeDescriptor.forObject(source), targetType);
	}

	public String toString()
	{
		return converters.toString();
	}

	protected Object convertNullSource(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (javaUtilOptionalEmpty != null && targetType.getObjectType() == javaUtilOptionalEmpty.getClass())
			return javaUtilOptionalEmpty;
		else
			return null;
	}

	protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
		GenericConverter converter = (GenericConverter)converterCache.get(key);
		if (converter != null)
			return converter == NO_MATCH ? null : converter;
		converter = converters.find(sourceType, targetType);
		if (converter == null)
			converter = getDefaultConverter(sourceType, targetType);
		if (converter != null)
		{
			converterCache.put(key, converter);
			return converter;
		} else
		{
			converterCache.put(key, NO_MATCH);
			return null;
		}
	}

	protected GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
	}

	private ResolvableType[] getRequiredTypeInfo(Class converterClass, Class genericIfc)
	{
		ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
		ResolvableType generics[] = resolvableType.getGenerics();
		if (generics.length < 2)
			return null;
		Class sourceType = generics[0].resolve();
		Class targetType = generics[1].resolve();
		if (sourceType == null || targetType == null)
			return null;
		else
			return generics;
	}

	private void invalidateCache()
	{
		converterCache.clear();
	}

	private Object handleConverterNotFound(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (source == null)
		{
			assertNotPrimitiveTargetType(sourceType, targetType);
			return null;
		}
		if (sourceType.isAssignableTo(targetType) && targetType.getObjectType().isInstance(source))
			return source;
		else
			throw new ConverterNotFoundException(sourceType, targetType);
	}

	private Object handleResult(TypeDescriptor sourceType, TypeDescriptor targetType, Object result)
	{
		if (result == null)
			assertNotPrimitiveTargetType(sourceType, targetType);
		return result;
	}

	private void assertNotPrimitiveTargetType(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		if (targetType.isPrimitive())
			throw new ConversionFailedException(sourceType, targetType, null, new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
		else
			return;
	}

	static 
	{
		try
		{
			Class clazz = ClassUtils.forName("java.util.Optional", org/springframework/core/convert/support/GenericConversionService.getClassLoader());
			javaUtilOptionalEmpty = ClassUtils.getMethod(clazz, "empty", new Class[0]).invoke(null, new Object[0]);
		}
		catch (Exception exception) { }
	}
}
