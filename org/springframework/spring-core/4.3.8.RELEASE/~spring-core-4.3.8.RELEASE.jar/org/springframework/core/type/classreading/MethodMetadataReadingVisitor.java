// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodMetadataReadingVisitor.java

package org.springframework.core.type.classreading;

import java.util.*;
import org.springframework.asm.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// Referenced classes of package org.springframework.core.type.classreading:
//			AnnotationAttributesReadingVisitor, AnnotationReadingVisitorUtils

public class MethodMetadataReadingVisitor extends MethodVisitor
	implements MethodMetadata
{

	protected final String methodName;
	protected final int access;
	protected final String declaringClassName;
	protected final String returnTypeName;
	protected final ClassLoader classLoader;
	protected final Set methodMetadataSet;
	protected final Map metaAnnotationMap = new LinkedHashMap(4);
	protected final LinkedMultiValueMap attributesMap = new LinkedMultiValueMap(4);

	public MethodMetadataReadingVisitor(String methodName, int access, String declaringClassName, String returnTypeName, ClassLoader classLoader, Set methodMetadataSet)
	{
		super(0x50000);
		this.methodName = methodName;
		this.access = access;
		this.declaringClassName = declaringClassName;
		this.returnTypeName = returnTypeName;
		this.classLoader = classLoader;
		this.methodMetadataSet = methodMetadataSet;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		String className = Type.getType(desc).getClassName();
		methodMetadataSet.add(this);
		return new AnnotationAttributesReadingVisitor(className, attributesMap, metaAnnotationMap, classLoader);
	}

	public String getMethodName()
	{
		return methodName;
	}

	public boolean isAbstract()
	{
		return (access & 0x400) != 0;
	}

	public boolean isStatic()
	{
		return (access & 8) != 0;
	}

	public boolean isFinal()
	{
		return (access & 0x10) != 0;
	}

	public boolean isOverridable()
	{
		return !isStatic() && !isFinal() && (access & 2) == 0;
	}

	public boolean isAnnotated(String annotationName)
	{
		return attributesMap.containsKey(annotationName);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationName)
	{
		return getAnnotationAttributes(annotationName, false);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(attributesMap, metaAnnotationMap, annotationName);
		return AnnotationReadingVisitorUtils.convertClassValues((new StringBuilder()).append("method '").append(getMethodName()).append("'").toString(), classLoader, raw, classValuesAsString);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName)
	{
		return getAllAnnotationAttributes(annotationName, false);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		if (!attributesMap.containsKey(annotationName))
			return null;
		MultiValueMap allAttributes = new LinkedMultiValueMap();
		for (Iterator iterator = attributesMap.get(annotationName).iterator(); iterator.hasNext();)
		{
			AnnotationAttributes annotationAttributes = (AnnotationAttributes)iterator.next();
			AnnotationAttributes convertedAttributes = AnnotationReadingVisitorUtils.convertClassValues((new StringBuilder()).append("method '").append(getMethodName()).append("'").toString(), classLoader, annotationAttributes, classValuesAsString);
			Iterator iterator1 = convertedAttributes.entrySet().iterator();
			while (iterator1.hasNext()) 
			{
				java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
				allAttributes.add(entry.getKey(), entry.getValue());
			}
		}

		return allAttributes;
	}

	public String getDeclaringClassName()
	{
		return declaringClassName;
	}

	public String getReturnTypeName()
	{
		return returnTypeName;
	}

	public volatile Map getAnnotationAttributes(String s, boolean flag)
	{
		return getAnnotationAttributes(s, flag);
	}

	public volatile Map getAnnotationAttributes(String s)
	{
		return getAnnotationAttributes(s);
	}
}
