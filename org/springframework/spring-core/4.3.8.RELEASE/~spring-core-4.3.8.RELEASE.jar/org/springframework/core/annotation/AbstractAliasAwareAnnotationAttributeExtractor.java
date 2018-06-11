// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractAliasAwareAnnotationAttributeExtractor.java

package org.springframework.core.annotation;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationConfigurationException, AnnotationAttributeExtractor, AnnotationUtils

abstract class AbstractAliasAwareAnnotationAttributeExtractor
	implements AnnotationAttributeExtractor
{

	private final Class annotationType;
	private final Object annotatedElement;
	private final Object source;
	private final Map attributeAliasMap;

	AbstractAliasAwareAnnotationAttributeExtractor(Class annotationType, Object annotatedElement, Object source)
	{
		Assert.notNull(annotationType, "annotationType must not be null");
		Assert.notNull(source, "source must not be null");
		this.annotationType = annotationType;
		this.annotatedElement = annotatedElement;
		this.source = source;
		attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
	}

	public final Class getAnnotationType()
	{
		return annotationType;
	}

	public final Object getAnnotatedElement()
	{
		return annotatedElement;
	}

	public final Object getSource()
	{
		return source;
	}

	public final Object getAttributeValue(Method attributeMethod)
	{
		String attributeName = attributeMethod.getName();
		Object attributeValue = getRawAttributeValue(attributeMethod);
		List aliasNames = (List)attributeAliasMap.get(attributeName);
		if (aliasNames != null)
		{
			Object defaultValue = AnnotationUtils.getDefaultValue(annotationType, attributeName);
			Iterator iterator = aliasNames.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				String aliasName = (String)iterator.next();
				Object aliasValue = getRawAttributeValue(aliasName);
				if (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue) && !ObjectUtils.nullSafeEquals(attributeValue, defaultValue) && !ObjectUtils.nullSafeEquals(aliasValue, defaultValue))
				{
					String elementName = annotatedElement == null ? "unknown element" : annotatedElement.toString();
					throw new AnnotationConfigurationException(String.format("In annotation [%s] declared on %s and synthesized from [%s], attribute '%s' and its alias '%s' are present with values of [%s] and [%s], but only one is permitted.", new Object[] {
						annotationType.getName(), elementName, source, attributeName, aliasName, ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue)
					}));
				}
				if (ObjectUtils.nullSafeEquals(attributeValue, defaultValue))
					attributeValue = aliasValue;
			} while (true);
		}
		return attributeValue;
	}

	protected abstract Object getRawAttributeValue(Method method);

	protected abstract Object getRawAttributeValue(String s);
}
