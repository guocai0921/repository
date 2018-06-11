// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationReadingVisitorUtils.java

package org.springframework.core.type.classreading;

import java.io.Serializable;
import java.util.*;
import org.springframework.asm.Type;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

abstract class AnnotationReadingVisitorUtils
{

	AnnotationReadingVisitorUtils()
	{
	}

	public static AnnotationAttributes convertClassValues(Object annotatedElement, ClassLoader classLoader, AnnotationAttributes original, boolean classValuesAsString)
	{
		if (original == null)
			return null;
		AnnotationAttributes result = new AnnotationAttributes(original);
		AnnotationUtils.postProcessAnnotationAttributes(annotatedElement, result, classValuesAsString);
		for (Iterator iterator = result.entrySet().iterator(); iterator.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			try
			{
				Object value = entry.getValue();
				if (value instanceof AnnotationAttributes)
					value = convertClassValues(annotatedElement, classLoader, (AnnotationAttributes)value, classValuesAsString);
				else
				if (value instanceof AnnotationAttributes[])
				{
					AnnotationAttributes values[] = (AnnotationAttributes[])(AnnotationAttributes[])value;
					for (int i = 0; i < values.length; i++)
						values[i] = convertClassValues(annotatedElement, classLoader, values[i], classValuesAsString);

					value = values;
				} else
				if (value instanceof Type)
					value = classValuesAsString ? ((Object) (((Type)value).getClassName())) : ((Object) (classLoader.loadClass(((Type)value).getClassName())));
				else
				if (value instanceof Type[])
				{
					Type array[] = (Type[])(Type[])value;
					Object convArray[] = classValuesAsString ? ((Object []) (new String[array.length])) : ((Object []) (new Class[array.length]));
					for (int i = 0; i < array.length; i++)
						convArray[i] = classValuesAsString ? ((Object) (array[i].getClassName())) : ((Object) (classLoader.loadClass(array[i].getClassName())));

					value = ((Object) (convArray));
				} else
				if (classValuesAsString)
					if (value instanceof Class)
						value = ((Class)value).getName();
					else
					if (value instanceof Class[])
					{
						Class clazzArray[] = (Class[])(Class[])value;
						String newValue[] = new String[clazzArray.length];
						for (int i = 0; i < clazzArray.length; i++)
							newValue[i] = clazzArray[i].getName();

						value = newValue;
					}
				entry.setValue(value);
			}
			catch (Throwable ex)
			{
				result.put(entry.getKey(), ex);
			}
		}

		return result;
	}

	public static AnnotationAttributes getMergedAnnotationAttributes(LinkedMultiValueMap attributesMap, Map metaAnnotationMap, String annotationName)
	{
		List attributesList = attributesMap.get(annotationName);
		if (attributesList == null || attributesList.isEmpty())
			return null;
		AnnotationAttributes result = new AnnotationAttributes((AnnotationAttributes)attributesList.get(0));
		Set overridableAttributeNames = new HashSet(result.keySet());
		overridableAttributeNames.remove("value");
		List annotationTypes = new ArrayList(attributesMap.keySet());
		Collections.reverse(annotationTypes);
		annotationTypes.remove(annotationName);
		for (Iterator iterator = annotationTypes.iterator(); iterator.hasNext();)
		{
			String currentAnnotationType = (String)iterator.next();
			List currentAttributesList = attributesMap.get(currentAnnotationType);
			if (!ObjectUtils.isEmpty(currentAttributesList))
			{
				Set metaAnns = (Set)metaAnnotationMap.get(currentAnnotationType);
				if (metaAnns != null && metaAnns.contains(annotationName))
				{
					AnnotationAttributes currentAttributes = (AnnotationAttributes)currentAttributesList.get(0);
					Iterator iterator1 = overridableAttributeNames.iterator();
					while (iterator1.hasNext()) 
					{
						String overridableAttributeName = (String)iterator1.next();
						Object value = currentAttributes.get(overridableAttributeName);
						if (value != null)
							result.put(overridableAttributeName, value);
					}
				}
			}
		}

		return result;
	}
}
