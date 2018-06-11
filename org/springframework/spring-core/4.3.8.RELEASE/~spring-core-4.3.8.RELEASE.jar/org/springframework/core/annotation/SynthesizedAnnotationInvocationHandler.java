// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SynthesizedAnnotationInvocationHandler.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationConfigurationException, AnnotationUtils, AnnotationAttributeExtractor

class SynthesizedAnnotationInvocationHandler
	implements InvocationHandler
{

	private final AnnotationAttributeExtractor attributeExtractor;
	private final Map valueCache = new ConcurrentHashMap(8);

	SynthesizedAnnotationInvocationHandler(AnnotationAttributeExtractor attributeExtractor)
	{
		Assert.notNull(attributeExtractor, "AnnotationAttributeExtractor must not be null");
		this.attributeExtractor = attributeExtractor;
	}

	public Object invoke(Object proxy, Method method, Object args[])
		throws Throwable
	{
		if (ReflectionUtils.isEqualsMethod(method))
			return Boolean.valueOf(annotationEquals(args[0]));
		if (ReflectionUtils.isHashCodeMethod(method))
			return Integer.valueOf(annotationHashCode());
		if (ReflectionUtils.isToStringMethod(method))
			return annotationToString();
		if (AnnotationUtils.isAnnotationTypeMethod(method))
			return annotationType();
		if (!AnnotationUtils.isAttributeMethod(method))
			throw new AnnotationConfigurationException(String.format("Method [%s] is unsupported for synthesized annotation type [%s]", new Object[] {
				method, annotationType()
			}));
		else
			return getAttributeValue(method);
	}

	private Class annotationType()
	{
		return attributeExtractor.getAnnotationType();
	}

	private Object getAttributeValue(Method attributeMethod)
	{
		String attributeName = attributeMethod.getName();
		Object value = valueCache.get(attributeName);
		if (value == null)
		{
			value = attributeExtractor.getAttributeValue(attributeMethod);
			if (value == null)
			{
				String msg = String.format("%s returned null for attribute name [%s] from attribute source [%s]", new Object[] {
					attributeExtractor.getClass().getName(), attributeName, attributeExtractor.getSource()
				});
				throw new IllegalStateException(msg);
			}
			if (value instanceof Annotation)
				value = AnnotationUtils.synthesizeAnnotation((Annotation)value, attributeExtractor.getAnnotatedElement());
			else
			if (value instanceof Annotation[])
				value = AnnotationUtils.synthesizeAnnotationArray((Annotation[])(Annotation[])value, attributeExtractor.getAnnotatedElement());
			valueCache.put(attributeName, value);
		}
		if (value.getClass().isArray())
			value = cloneArray(value);
		return value;
	}

	private Object cloneArray(Object array)
	{
		if (array instanceof boolean[])
			return ((boolean[])(boolean[])array).clone();
		if (array instanceof byte[])
			return ((byte[])(byte[])array).clone();
		if (array instanceof char[])
			return ((char[])(char[])array).clone();
		if (array instanceof double[])
			return ((double[])(double[])array).clone();
		if (array instanceof float[])
			return ((float[])(float[])array).clone();
		if (array instanceof int[])
			return ((int[])(int[])array).clone();
		if (array instanceof long[])
			return ((long[])(long[])array).clone();
		if (array instanceof short[])
			return ((short[])(short[])array).clone();
		else
			return ((Object []) ((Object[])(Object[])array)).clone();
	}

	private boolean annotationEquals(Object other)
	{
		if (this == other)
			return true;
		if (!annotationType().isInstance(other))
			return false;
		for (Iterator iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator(); iterator.hasNext();)
		{
			Method attributeMethod = (Method)iterator.next();
			Object thisValue = getAttributeValue(attributeMethod);
			Object otherValue = ReflectionUtils.invokeMethod(attributeMethod, other);
			if (!ObjectUtils.nullSafeEquals(thisValue, otherValue))
				return false;
		}

		return true;
	}

	private int annotationHashCode()
	{
		int result = 0;
		for (Iterator iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator(); iterator.hasNext();)
		{
			Method attributeMethod = (Method)iterator.next();
			Object value = getAttributeValue(attributeMethod);
			int hashCode;
			if (value.getClass().isArray())
				hashCode = hashCodeForArray(value);
			else
				hashCode = value.hashCode();
			result += 127 * attributeMethod.getName().hashCode() ^ hashCode;
		}

		return result;
	}

	private int hashCodeForArray(Object array)
	{
		if (array instanceof boolean[])
			return Arrays.hashCode((boolean[])(boolean[])array);
		if (array instanceof byte[])
			return Arrays.hashCode((byte[])(byte[])array);
		if (array instanceof char[])
			return Arrays.hashCode((char[])(char[])array);
		if (array instanceof double[])
			return Arrays.hashCode((double[])(double[])array);
		if (array instanceof float[])
			return Arrays.hashCode((float[])(float[])array);
		if (array instanceof int[])
			return Arrays.hashCode((int[])(int[])array);
		if (array instanceof long[])
			return Arrays.hashCode((long[])(long[])array);
		if (array instanceof short[])
			return Arrays.hashCode((short[])(short[])array);
		else
			return Arrays.hashCode((Object[])(Object[])array);
	}

	private String annotationToString()
	{
		StringBuilder sb = (new StringBuilder("@")).append(annotationType().getName()).append("(");
		for (Iterator iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator(); iterator.hasNext(); sb.append(iterator.hasNext() ? ", " : ""))
		{
			Method attributeMethod = (Method)iterator.next();
			sb.append(attributeMethod.getName());
			sb.append('=');
			sb.append(attributeValueToString(getAttributeValue(attributeMethod)));
		}

		return sb.append(")").toString();
	}

	private String attributeValueToString(Object value)
	{
		if (value instanceof Object[])
			return (new StringBuilder()).append("[").append(StringUtils.arrayToDelimitedString((Object[])(Object[])value, ", ")).append("]").toString();
		else
			return String.valueOf(value);
	}
}
