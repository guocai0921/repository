// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MapAnnotationAttributeExtractor.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.annotation:
//			AbstractAliasAwareAnnotationAttributeExtractor, AnnotationUtils

class MapAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor
{

	MapAnnotationAttributeExtractor(Map attributes, Class annotationType, AnnotatedElement annotatedElement)
	{
		super(annotationType, annotatedElement, enrichAndValidateAttributes(attributes, annotationType));
	}

	protected Object getRawAttributeValue(Method attributeMethod)
	{
		return getRawAttributeValue(attributeMethod.getName());
	}

	protected Object getRawAttributeValue(String attributeName)
	{
		return ((Map)getSource()).get(attributeName);
	}

	private static Map enrichAndValidateAttributes(Map originalAttributes, Class annotationType)
	{
		Map attributes = new LinkedHashMap(originalAttributes);
		Map attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
		Iterator iterator = AnnotationUtils.getAttributeMethods(annotationType).iterator();
label0:
		do
		{
			Method attributeMethod;
			String attributeName;
			Object attributeValue;
label1:
			{
				if (!iterator.hasNext())
					break label0;
				attributeMethod = (Method)iterator.next();
				attributeName = attributeMethod.getName();
				attributeValue = attributes.get(attributeName);
				if (attributeValue != null)
					break label1;
				List aliasNames = (List)attributeAliasMap.get(attributeName);
				if (aliasNames == null)
					break label1;
				Iterator iterator1 = aliasNames.iterator();
				Object aliasValue;
				do
				{
					if (!iterator1.hasNext())
						break label1;
					String aliasName = (String)iterator1.next();
					aliasValue = attributes.get(aliasName);
				} while (aliasValue == null);
				attributeValue = aliasValue;
				attributes.put(attributeName, attributeValue);
			}
			if (attributeValue == null)
			{
				Object defaultValue = AnnotationUtils.getDefaultValue(annotationType, attributeName);
				if (defaultValue != null)
				{
					attributeValue = defaultValue;
					attributes.put(attributeName, attributeValue);
				}
			}
			if (attributeValue == null)
				throw new IllegalArgumentException(String.format("Attributes map %s returned null for required attribute '%s' defined by annotation type [%s].", new Object[] {
					attributes, attributeName, annotationType.getName()
				}));
			Class requiredReturnType = attributeMethod.getReturnType();
			Class actualReturnType = attributeValue.getClass();
			if (!ClassUtils.isAssignable(requiredReturnType, actualReturnType))
			{
				boolean converted = false;
				if (requiredReturnType.isArray() && requiredReturnType.getComponentType() == actualReturnType)
				{
					Object array = Array.newInstance(requiredReturnType.getComponentType(), 1);
					Array.set(array, 0, attributeValue);
					attributes.put(attributeName, array);
					converted = true;
				} else
				if (java/lang/annotation/Annotation.isAssignableFrom(requiredReturnType) && java/util/Map.isAssignableFrom(actualReturnType))
				{
					Class nestedAnnotationType = requiredReturnType;
					Map map = (Map)attributeValue;
					attributes.put(attributeName, AnnotationUtils.synthesizeAnnotation(map, nestedAnnotationType, null));
					converted = true;
				} else
				if (requiredReturnType.isArray() && actualReturnType.isArray() && java/lang/annotation/Annotation.isAssignableFrom(requiredReturnType.getComponentType()) && java/util/Map.isAssignableFrom(actualReturnType.getComponentType()))
				{
					Class nestedAnnotationType = requiredReturnType.getComponentType();
					Map maps[] = (Map[])(Map[])attributeValue;
					attributes.put(attributeName, AnnotationUtils.synthesizeAnnotationArray(maps, nestedAnnotationType));
					converted = true;
				}
				if (!converted)
					throw new IllegalArgumentException(String.format("Attributes map %s returned a value of type [%s] for attribute '%s', but a value of type [%s] is required as defined by annotation type [%s].", new Object[] {
						attributes, actualReturnType.getName(), attributeName, requiredReturnType.getName(), annotationType.getName()
					}));
			}
		} while (true);
		return attributes;
	}
}
