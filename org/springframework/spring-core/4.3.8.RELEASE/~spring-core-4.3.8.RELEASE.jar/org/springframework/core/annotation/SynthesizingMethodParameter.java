// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SynthesizingMethodParameter.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;

// Referenced classes of package org.springframework.core.annotation:
//			AnnotationUtils

public class SynthesizingMethodParameter extends MethodParameter
{

	public SynthesizingMethodParameter(Method method, int parameterIndex)
	{
		super(method, parameterIndex);
	}

	public SynthesizingMethodParameter(Method method, int parameterIndex, int nestingLevel)
	{
		super(method, parameterIndex, nestingLevel);
	}

	public SynthesizingMethodParameter(Constructor constructor, int parameterIndex)
	{
		super(constructor, parameterIndex);
	}

	public SynthesizingMethodParameter(Constructor constructor, int parameterIndex, int nestingLevel)
	{
		super(constructor, parameterIndex, nestingLevel);
	}

	protected SynthesizingMethodParameter(SynthesizingMethodParameter original)
	{
		super(original);
	}

	protected Annotation adaptAnnotation(Annotation annotation)
	{
		return AnnotationUtils.synthesizeAnnotation(annotation, getAnnotatedElement());
	}

	protected Annotation[] adaptAnnotationArray(Annotation annotations[])
	{
		return AnnotationUtils.synthesizeAnnotationArray(annotations, getAnnotatedElement());
	}

	public SynthesizingMethodParameter clone()
	{
		return new SynthesizingMethodParameter(this);
	}

	public volatile MethodParameter clone()
	{
		return clone();
	}

	public volatile Object clone()
		throws CloneNotSupportedException
	{
		return clone();
	}
}
