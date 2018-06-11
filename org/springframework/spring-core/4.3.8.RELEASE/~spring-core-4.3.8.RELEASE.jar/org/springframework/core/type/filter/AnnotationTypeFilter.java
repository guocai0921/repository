// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationTypeFilter.java

package org.springframework.core.type.filter;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.type.filter:
//			AbstractTypeHierarchyTraversingFilter

public class AnnotationTypeFilter extends AbstractTypeHierarchyTraversingFilter
{

	private final Class annotationType;
	private final boolean considerMetaAnnotations;

	public AnnotationTypeFilter(Class annotationType)
	{
		this(annotationType, true, false);
	}

	public AnnotationTypeFilter(Class annotationType, boolean considerMetaAnnotations)
	{
		this(annotationType, considerMetaAnnotations, false);
	}

	public AnnotationTypeFilter(Class annotationType, boolean considerMetaAnnotations, boolean considerInterfaces)
	{
		super(annotationType.isAnnotationPresent(java/lang/annotation/Inherited), considerInterfaces);
		this.annotationType = annotationType;
		this.considerMetaAnnotations = considerMetaAnnotations;
	}

	protected boolean matchSelf(MetadataReader metadataReader)
	{
		AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
		return metadata.hasAnnotation(annotationType.getName()) || considerMetaAnnotations && metadata.hasMetaAnnotation(annotationType.getName());
	}

	protected Boolean matchSuperClass(String superClassName)
	{
		return hasAnnotation(superClassName);
	}

	protected Boolean matchInterface(String interfaceName)
	{
		return hasAnnotation(interfaceName);
	}

	protected Boolean hasAnnotation(String typeName)
	{
		if (java/lang/Object.getName().equals(typeName))
			return Boolean.valueOf(false);
		if (!typeName.startsWith("java"))
			break MISSING_BLOCK_LABEL_77;
		Class clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
		return Boolean.valueOf((considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, annotationType) : clazz.getAnnotation(annotationType)) != null);
		Throwable throwable;
		throwable;
		return null;
	}
}
