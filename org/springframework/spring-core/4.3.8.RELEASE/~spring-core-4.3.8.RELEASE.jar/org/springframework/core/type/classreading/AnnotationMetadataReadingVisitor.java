// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationMetadataReadingVisitor.java

package org.springframework.core.type.classreading;

import java.util.*;
import org.springframework.asm.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// Referenced classes of package org.springframework.core.type.classreading:
//			ClassMetadataReadingVisitor, MethodMetadataReadingVisitor, AnnotationAttributesReadingVisitor, AnnotationReadingVisitorUtils

public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor
	implements AnnotationMetadata
{

	protected final ClassLoader classLoader;
	protected final Set annotationSet = new LinkedHashSet(4);
	protected final Map metaAnnotationMap = new LinkedHashMap(4);
	protected final LinkedMultiValueMap attributesMap = new LinkedMultiValueMap(4);
	protected final Set methodMetadataSet = new LinkedHashSet(4);

	public AnnotationMetadataReadingVisitor(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		if ((access & 0x40) != 0)
			return super.visitMethod(access, name, desc, signature, exceptions);
		else
			return new MethodMetadataReadingVisitor(name, access, getClassName(), Type.getReturnType(desc).getClassName(), classLoader, methodMetadataSet);
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		String className = Type.getType(desc).getClassName();
		annotationSet.add(className);
		return new AnnotationAttributesReadingVisitor(className, attributesMap, metaAnnotationMap, classLoader);
	}

	public Set getAnnotationTypes()
	{
		return annotationSet;
	}

	public Set getMetaAnnotationTypes(String annotationName)
	{
		return (Set)metaAnnotationMap.get(annotationName);
	}

	public boolean hasAnnotation(String annotationName)
	{
		return annotationSet.contains(annotationName);
	}

	public boolean hasMetaAnnotation(String metaAnnotationType)
	{
		Collection allMetaTypes = metaAnnotationMap.values();
		for (Iterator iterator = allMetaTypes.iterator(); iterator.hasNext();)
		{
			Set metaTypes = (Set)iterator.next();
			if (metaTypes.contains(metaAnnotationType))
				return true;
		}

		return false;
	}

	public boolean isAnnotated(String annotationName)
	{
		return !AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && attributesMap.containsKey(annotationName);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationName)
	{
		return getAnnotationAttributes(annotationName, false);
	}

	public AnnotationAttributes getAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		AnnotationAttributes raw = AnnotationReadingVisitorUtils.getMergedAnnotationAttributes(attributesMap, metaAnnotationMap, annotationName);
		return AnnotationReadingVisitorUtils.convertClassValues((new StringBuilder()).append("class '").append(getClassName()).append("'").toString(), classLoader, raw, classValuesAsString);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName)
	{
		return getAllAnnotationAttributes(annotationName, false);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		MultiValueMap allAttributes = new LinkedMultiValueMap();
		List attributes = attributesMap.get(annotationName);
		if (attributes == null)
			return null;
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
		{
			AnnotationAttributes raw = (AnnotationAttributes)iterator.next();
			Iterator iterator1 = AnnotationReadingVisitorUtils.convertClassValues((new StringBuilder()).append("class '").append(getClassName()).append("'").toString(), classLoader, raw, classValuesAsString).entrySet().iterator();
			while (iterator1.hasNext()) 
			{
				java.util.Map.Entry entry = (java.util.Map.Entry)iterator1.next();
				allAttributes.add(entry.getKey(), entry.getValue());
			}
		}

		return allAttributes;
	}

	public boolean hasAnnotatedMethods(String annotationName)
	{
		for (Iterator iterator = methodMetadataSet.iterator(); iterator.hasNext();)
		{
			MethodMetadata methodMetadata = (MethodMetadata)iterator.next();
			if (methodMetadata.isAnnotated(annotationName))
				return true;
		}

		return false;
	}

	public Set getAnnotatedMethods(String annotationName)
	{
		Set annotatedMethods = new LinkedHashSet(4);
		Iterator iterator = methodMetadataSet.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			MethodMetadata methodMetadata = (MethodMetadata)iterator.next();
			if (methodMetadata.isAnnotated(annotationName))
				annotatedMethods.add(methodMetadata);
		} while (true);
		return annotatedMethods;
	}

	public volatile String[] getMemberClassNames()
	{
		return super.getMemberClassNames();
	}

	public volatile String[] getInterfaceNames()
	{
		return super.getInterfaceNames();
	}

	public volatile String getSuperClassName()
	{
		return super.getSuperClassName();
	}

	public volatile boolean hasSuperClass()
	{
		return super.hasSuperClass();
	}

	public volatile String getEnclosingClassName()
	{
		return super.getEnclosingClassName();
	}

	public volatile boolean hasEnclosingClass()
	{
		return super.hasEnclosingClass();
	}

	public volatile boolean isIndependent()
	{
		return super.isIndependent();
	}

	public volatile boolean isFinal()
	{
		return super.isFinal();
	}

	public volatile boolean isConcrete()
	{
		return super.isConcrete();
	}

	public volatile boolean isAbstract()
	{
		return super.isAbstract();
	}

	public volatile boolean isAnnotation()
	{
		return super.isAnnotation();
	}

	public volatile boolean isInterface()
	{
		return super.isInterface();
	}

	public volatile String getClassName()
	{
		return super.getClassName();
	}

	public volatile void visitEnd()
	{
		super.visitEnd();
	}

	public volatile FieldVisitor visitField(int i, String s, String s1, String s2, Object obj)
	{
		return super.visitField(i, s, s1, s2, obj);
	}

	public volatile void visitAttribute(Attribute attribute)
	{
		super.visitAttribute(attribute);
	}

	public volatile void visitSource(String s, String s1)
	{
		super.visitSource(s, s1);
	}

	public volatile void visitInnerClass(String s, String s1, String s2, int i)
	{
		super.visitInnerClass(s, s1, s2, i);
	}

	public volatile void visitOuterClass(String s, String s1, String s2)
	{
		super.visitOuterClass(s, s1, s2);
	}

	public volatile void visit(int i, int j, String s, String s1, String s2, String as[])
	{
		super.visit(i, j, s, s1, s2, as);
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
