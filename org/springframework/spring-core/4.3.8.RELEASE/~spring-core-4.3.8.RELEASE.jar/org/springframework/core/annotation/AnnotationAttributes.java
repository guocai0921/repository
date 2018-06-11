// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationAttributes.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.*;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationConfigurationException, AnnotationUtils

public class AnnotationAttributes extends LinkedHashMap
{

	private static final String UNKNOWN = "unknown";
	private final Class annotationType;
	private final String displayName;
	boolean validated;

	public AnnotationAttributes()
	{
		validated = false;
		annotationType = null;
		displayName = "unknown";
	}

	public AnnotationAttributes(int initialCapacity)
	{
		super(initialCapacity);
		validated = false;
		annotationType = null;
		displayName = "unknown";
	}

	public AnnotationAttributes(Class annotationType)
	{
		validated = false;
		Assert.notNull(annotationType, "'annotationType' must not be null");
		this.annotationType = annotationType;
		displayName = annotationType.getName();
	}

	public AnnotationAttributes(String annotationType, ClassLoader classLoader)
	{
		validated = false;
		Assert.notNull(annotationType, "'annotationType' must not be null");
		this.annotationType = getAnnotationType(annotationType, classLoader);
		displayName = annotationType;
	}

	private static Class getAnnotationType(String annotationType, ClassLoader classLoader)
	{
		if (classLoader == null)
			break MISSING_BLOCK_LABEL_11;
		return classLoader.loadClass(annotationType);
		ClassNotFoundException classnotfoundexception;
		classnotfoundexception;
		return null;
	}

	public AnnotationAttributes(Map map)
	{
		super(map);
		validated = false;
		annotationType = null;
		displayName = "unknown";
	}

	public AnnotationAttributes(AnnotationAttributes other)
	{
		super(other);
		validated = false;
		annotationType = other.annotationType;
		displayName = other.displayName;
		validated = other.validated;
	}

	public Class annotationType()
	{
		return annotationType;
	}

	public String getString(String attributeName)
	{
		return (String)getRequiredAttribute(attributeName, java/lang/String);
	}

	/**
	 * @deprecated Method getAliasedString is deprecated
	 */

	public String getAliasedString(String attributeName, Class annotationType, Object annotationSource)
	{
		return (String)getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, java/lang/String);
	}

	public String[] getStringArray(String attributeName)
	{
		return (String[])getRequiredAttribute(attributeName, [Ljava/lang/String;);
	}

	/**
	 * @deprecated Method getAliasedStringArray is deprecated
	 */

	public String[] getAliasedStringArray(String attributeName, Class annotationType, Object annotationSource)
	{
		return (String[])getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, [Ljava/lang/String;);
	}

	public boolean getBoolean(String attributeName)
	{
		return ((Boolean)getRequiredAttribute(attributeName, java/lang/Boolean)).booleanValue();
	}

	public Number getNumber(String attributeName)
	{
		return (Number)getRequiredAttribute(attributeName, java/lang/Number);
	}

	public Enum getEnum(String attributeName)
	{
		return (Enum)getRequiredAttribute(attributeName, java/lang/Enum);
	}

	public Class getClass(String attributeName)
	{
		return (Class)getRequiredAttribute(attributeName, java/lang/Class);
	}

	public Class[] getClassArray(String attributeName)
	{
		return (Class[])getRequiredAttribute(attributeName, [Ljava/lang/Class;);
	}

	/**
	 * @deprecated Method getAliasedClassArray is deprecated
	 */

	public Class[] getAliasedClassArray(String attributeName, Class annotationType, Object annotationSource)
	{
		return (Class[])getRequiredAttributeWithAlias(attributeName, annotationType, annotationSource, [Ljava/lang/Class;);
	}

	public AnnotationAttributes getAnnotation(String attributeName)
	{
		return (AnnotationAttributes)getRequiredAttribute(attributeName, org/springframework/core/annotation/AnnotationAttributes);
	}

	public Annotation getAnnotation(String attributeName, Class annotationType)
	{
		return (Annotation)getRequiredAttribute(attributeName, annotationType);
	}

	public AnnotationAttributes[] getAnnotationArray(String attributeName)
	{
		return (AnnotationAttributes[])getRequiredAttribute(attributeName, [Lorg/springframework/core/annotation/AnnotationAttributes;);
	}

	public Annotation[] getAnnotationArray(String attributeName, Class annotationType)
	{
		Object array = Array.newInstance(annotationType, 0);
		return (Annotation[])(Annotation[])getRequiredAttribute(attributeName, array.getClass());
	}

	private Object getRequiredAttribute(String attributeName, Class expectedType)
	{
		Assert.hasText(attributeName, "'attributeName' must not be null or empty");
		Object value = get(attributeName);
		assertAttributePresence(attributeName, value);
		assertNotException(attributeName, value);
		if (!expectedType.isInstance(value) && expectedType.isArray() && expectedType.getComponentType().isInstance(value))
		{
			Object array = Array.newInstance(expectedType.getComponentType(), 1);
			Array.set(array, 0, value);
			value = array;
		}
		assertAttributeType(attributeName, value, expectedType);
		return value;
	}

	private Object getRequiredAttributeWithAlias(String attributeName, Class annotationType, Object annotationSource, Class expectedType)
	{
		Assert.hasText(attributeName, "'attributeName' must not be null or empty");
		Assert.notNull(annotationType, "'annotationType' must not be null");
		Assert.notNull(expectedType, "'expectedType' must not be null");
		Object attributeValue = getAttribute(attributeName, expectedType);
		List aliasNames = (List)AnnotationUtils.getAttributeAliasMap(annotationType).get(attributeName);
		if (aliasNames != null)
		{
			Iterator iterator = aliasNames.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				String aliasName = (String)iterator.next();
				Object aliasValue = getAttribute(aliasName, expectedType);
				boolean attributeEmpty = ObjectUtils.isEmpty(attributeValue);
				boolean aliasEmpty = ObjectUtils.isEmpty(aliasValue);
				if (!attributeEmpty && !aliasEmpty && !ObjectUtils.nullSafeEquals(attributeValue, aliasValue))
				{
					String elementName = annotationSource != null ? annotationSource.toString() : "unknown element";
					String msg = String.format("In annotation [%s] declared on [%s], attribute [%s] and its alias [%s] are present with values of [%s] and [%s], but only one is permitted.", new Object[] {
						annotationType.getName(), elementName, attributeName, aliasName, ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue)
					});
					throw new AnnotationConfigurationException(msg);
				}
				if (expectedType.isArray() && attributeValue == null && aliasValue != null)
					attributeValue = aliasValue;
				else
				if (attributeEmpty && !aliasEmpty)
					attributeValue = aliasValue;
			} while (true);
			assertAttributePresence(attributeName, aliasNames, attributeValue);
		}
		return attributeValue;
	}

	private Object getAttribute(String attributeName, Class expectedType)
	{
		Object value = get(attributeName);
		if (value != null)
		{
			assertNotException(attributeName, value);
			assertAttributeType(attributeName, value, expectedType);
		}
		return value;
	}

	private void assertAttributePresence(String attributeName, Object attributeValue)
	{
		if (attributeValue == null)
			throw new IllegalArgumentException(String.format("Attribute '%s' not found in attributes for annotation [%s]", new Object[] {
				attributeName, displayName
			}));
		else
			return;
	}

	private void assertAttributePresence(String attributeName, List aliases, Object attributeValue)
	{
		if (attributeValue == null)
			throw new IllegalArgumentException(String.format("Neither attribute '%s' nor one of its aliases %s was found in attributes for annotation [%s]", new Object[] {
				attributeName, aliases, displayName
			}));
		else
			return;
	}

	private void assertNotException(String attributeName, Object attributeValue)
	{
		if (attributeValue instanceof Exception)
			throw new IllegalArgumentException(String.format("Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]", new Object[] {
				attributeName, displayName, attributeValue
			}), (Exception)attributeValue);
		else
			return;
	}

	private void assertAttributeType(String attributeName, Object attributeValue, Class expectedType)
	{
		if (!expectedType.isInstance(attributeValue))
			throw new IllegalArgumentException(String.format("Attribute '%s' is of type [%s], but [%s] was expected in attributes for annotation [%s]", new Object[] {
				attributeName, attributeValue.getClass().getSimpleName(), expectedType.getSimpleName(), displayName
			}));
		else
			return;
	}

	public Object putIfAbsent(String key, Object value)
	{
		Object obj = get(key);
		if (obj == null)
			obj = put(key, value);
		return obj;
	}

	public String toString()
	{
		Iterator entries = entrySet().iterator();
		StringBuilder sb = new StringBuilder("{");
		for (; entries.hasNext(); sb.append(entries.hasNext() ? ", " : ""))
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)entries.next();
			sb.append((String)entry.getKey());
			sb.append('=');
			sb.append(valueToString(entry.getValue()));
		}

		sb.append("}");
		return sb.toString();
	}

	private String valueToString(Object value)
	{
		if (value == this)
			return "(this Map)";
		if (value instanceof Object[])
			return (new StringBuilder()).append("[").append(StringUtils.arrayToDelimitedString((Object[])(Object[])value, ", ")).append("]").toString();
		else
			return String.valueOf(value);
	}

	public static AnnotationAttributes fromMap(Map map)
	{
		if (map == null)
			return null;
		if (map instanceof AnnotationAttributes)
			return (AnnotationAttributes)map;
		else
			return new AnnotationAttributes(map);
	}

	public volatile Object putIfAbsent(Object obj, Object obj1)
	{
		return putIfAbsent((String)obj, obj1);
	}
}
