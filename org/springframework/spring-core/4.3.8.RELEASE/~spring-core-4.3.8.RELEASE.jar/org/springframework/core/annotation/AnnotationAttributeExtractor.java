// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationAttributeExtractor.java

package org.springframework.core.annotation;

import java.lang.reflect.Method;

interface AnnotationAttributeExtractor
{

	public abstract Class getAnnotationType();

	public abstract Object getAnnotatedElement();

	public abstract Object getSource();

	public abstract Object getAttributeValue(Method method);
}
