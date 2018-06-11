// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RecursiveAnnotationAttributesVisitor.java

package org.springframework.core.type.classreading;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;

// Referenced classes of package org.springframework.core.type.classreading:
//			AbstractRecursiveAnnotationVisitor

class RecursiveAnnotationAttributesVisitor extends AbstractRecursiveAnnotationVisitor
{

	protected final String annotationType;

	public RecursiveAnnotationAttributesVisitor(String annotationType, AnnotationAttributes attributes, ClassLoader classLoader)
	{
		super(classLoader, attributes);
		this.annotationType = annotationType;
	}

	public void visitEnd()
	{
		AnnotationUtils.registerDefaultValues(attributes);
	}
}
