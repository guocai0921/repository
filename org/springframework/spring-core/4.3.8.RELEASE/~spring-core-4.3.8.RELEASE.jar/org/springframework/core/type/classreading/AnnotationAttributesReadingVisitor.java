// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationAttributesReadingVisitor.java

package org.springframework.core.type.classreading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.*;
import org.apache.commons.logging.Log;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.type.classreading:
//			RecursiveAnnotationAttributesVisitor

final class AnnotationAttributesReadingVisitor extends RecursiveAnnotationAttributesVisitor
{

	private final MultiValueMap attributesMap;
	private final Map metaAnnotationMap;

	public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap attributesMap, Map metaAnnotationMap, ClassLoader classLoader)
	{
		super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
		this.attributesMap = attributesMap;
		this.metaAnnotationMap = metaAnnotationMap;
	}

	public void visitEnd()
	{
		super.visitEnd();
		Class annotationClass = attributes.annotationType();
		if (annotationClass != null)
		{
			List attributeList = (List)attributesMap.get(annotationType);
			if (attributeList == null)
				attributesMap.add(annotationType, attributes);
			else
				attributeList.add(0, attributes);
			Set visited = new LinkedHashSet();
			Annotation metaAnnotations[] = AnnotationUtils.getAnnotations(annotationClass);
			if (!ObjectUtils.isEmpty(metaAnnotations))
			{
				Annotation aannotation[] = metaAnnotations;
				int i = aannotation.length;
				for (int j = 0; j < i; j++)
				{
					Annotation metaAnnotation = aannotation[j];
					if (!AnnotationUtils.isInJavaLangAnnotationPackage(metaAnnotation))
						recursivelyCollectMetaAnnotations(visited, metaAnnotation);
				}

			}
			if (metaAnnotationMap != null)
			{
				Set metaAnnotationTypeNames = new LinkedHashSet(visited.size());
				Annotation ann;
				for (Iterator iterator = visited.iterator(); iterator.hasNext(); metaAnnotationTypeNames.add(ann.annotationType().getName()))
					ann = (Annotation)iterator.next();

				metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
			}
		}
	}

	private void recursivelyCollectMetaAnnotations(Set visited, Annotation annotation)
	{
		Class annotationType = annotation.annotationType();
		String annotationName = annotationType.getName();
		if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation))
			try
			{
				if (Modifier.isPublic(annotationType.getModifiers()))
					attributesMap.add(annotationName, AnnotationUtils.getAnnotationAttributes(annotation, false, true));
				Annotation aannotation[] = annotationType.getAnnotations();
				int i = aannotation.length;
				for (int j = 0; j < i; j++)
				{
					Annotation metaMetaAnnotation = aannotation[j];
					recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
				}

			}
			catch (Throwable ex)
			{
				if (logger.isDebugEnabled())
					logger.debug((new StringBuilder()).append("Failed to introspect meta-annotations on [").append(annotation).append("]: ").append(ex).toString());
			}
	}
}
