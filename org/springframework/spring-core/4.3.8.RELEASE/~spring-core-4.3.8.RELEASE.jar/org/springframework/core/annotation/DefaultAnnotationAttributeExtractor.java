// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultAnnotationAttributeExtractor.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.util.ReflectionUtils;

// Referenced classes of package org.springframework.core.annotation:
//			AbstractAliasAwareAnnotationAttributeExtractor

class DefaultAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor
{

	DefaultAnnotationAttributeExtractor(Annotation annotation, Object annotatedElement)
	{
		super(annotation.annotationType(), annotatedElement, annotation);
	}

	protected Object getRawAttributeValue(Method attributeMethod)
	{
		ReflectionUtils.makeAccessible(attributeMethod);
		return ReflectionUtils.invokeMethod(attributeMethod, getSource());
	}

	protected Object getRawAttributeValue(String attributeName)
	{
		Method attributeMethod = ReflectionUtils.findMethod(getAnnotationType(), attributeName);
		return getRawAttributeValue(attributeMethod);
	}
}
