// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardAnnotationMetadata.java

package org.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.MultiValueMap;

// Referenced classes of package org.springframework.core.type:
//			StandardClassMetadata, StandardMethodMetadata, AnnotationMetadata

public class StandardAnnotationMetadata extends StandardClassMetadata
	implements AnnotationMetadata
{

	private final Annotation annotations[];
	private final boolean nestedAnnotationsAsMap;

	public StandardAnnotationMetadata(Class introspectedClass)
	{
		this(introspectedClass, false);
	}

	public StandardAnnotationMetadata(Class introspectedClass, boolean nestedAnnotationsAsMap)
	{
		super(introspectedClass);
		annotations = introspectedClass.getAnnotations();
		this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
	}

	public Set getAnnotationTypes()
	{
		Set types = new LinkedHashSet();
		Annotation aannotation[] = annotations;
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation ann = aannotation[j];
			types.add(ann.annotationType().getName());
		}

		return types;
	}

	public Set getMetaAnnotationTypes(String annotationName)
	{
		return annotations.length <= 0 ? null : AnnotatedElementUtils.getMetaAnnotationTypes(getIntrospectedClass(), annotationName);
	}

	public boolean hasAnnotation(String annotationName)
	{
		Annotation aannotation[] = annotations;
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation ann = aannotation[j];
			if (ann.annotationType().getName().equals(annotationName))
				return true;
		}

		return false;
	}

	public boolean hasMetaAnnotation(String annotationName)
	{
		return annotations.length > 0 && AnnotatedElementUtils.hasMetaAnnotationTypes(getIntrospectedClass(), annotationName);
	}

	public boolean isAnnotated(String annotationName)
	{
		return annotations.length > 0 && AnnotatedElementUtils.isAnnotated(getIntrospectedClass(), annotationName);
	}

	public Map getAnnotationAttributes(String annotationName)
	{
		return getAnnotationAttributes(annotationName, false);
	}

	public Map getAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		return annotations.length <= 0 ? null : AnnotatedElementUtils.getMergedAnnotationAttributes(getIntrospectedClass(), annotationName, classValuesAsString, nestedAnnotationsAsMap);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName)
	{
		return getAllAnnotationAttributes(annotationName, false);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		return annotations.length <= 0 ? null : AnnotatedElementUtils.getAllAnnotationAttributes(getIntrospectedClass(), annotationName, classValuesAsString, nestedAnnotationsAsMap);
	}

	public boolean hasAnnotatedMethods(String annotationName)
	{
		Method amethod[];
		int i;
		int j;
		Method methods[] = getIntrospectedClass().getDeclaredMethods();
		amethod = methods;
		i = amethod.length;
		j = 0;
_L1:
		Method method;
		if (j >= i)
			break MISSING_BLOCK_LABEL_64;
		method = amethod[j];
		if (!method.isBridge() && method.getAnnotations().length > 0 && AnnotatedElementUtils.isAnnotated(method, annotationName))
			return true;
		j++;
		  goto _L1
		return false;
		Throwable ex;
		ex;
		throw new IllegalStateException((new StringBuilder()).append("Failed to introspect annotated methods on ").append(getIntrospectedClass()).toString(), ex);
	}

	public Set getAnnotatedMethods(String annotationName)
	{
		Set annotatedMethods;
		Method methods[] = getIntrospectedClass().getDeclaredMethods();
		annotatedMethods = new LinkedHashSet();
		Method amethod[] = methods;
		int i = amethod.length;
		for (int j = 0; j < i; j++)
		{
			Method method = amethod[j];
			if (!method.isBridge() && method.getAnnotations().length > 0 && AnnotatedElementUtils.isAnnotated(method, annotationName))
				annotatedMethods.add(new StandardMethodMetadata(method, nestedAnnotationsAsMap));
		}

		return annotatedMethods;
		Throwable ex;
		ex;
		throw new IllegalStateException((new StringBuilder()).append("Failed to introspect annotated methods on ").append(getIntrospectedClass()).toString(), ex);
	}
}
