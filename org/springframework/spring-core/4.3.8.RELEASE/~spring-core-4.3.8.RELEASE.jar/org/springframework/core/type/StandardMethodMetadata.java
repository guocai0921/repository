// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardMethodMetadata.java

package org.springframework.core.type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

// Referenced classes of package org.springframework.core.type:
//			MethodMetadata

public class StandardMethodMetadata
	implements MethodMetadata
{

	private final Method introspectedMethod;
	private final boolean nestedAnnotationsAsMap;

	public StandardMethodMetadata(Method introspectedMethod)
	{
		this(introspectedMethod, false);
	}

	public StandardMethodMetadata(Method introspectedMethod, boolean nestedAnnotationsAsMap)
	{
		Assert.notNull(introspectedMethod, "Method must not be null");
		this.introspectedMethod = introspectedMethod;
		this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
	}

	public final Method getIntrospectedMethod()
	{
		return introspectedMethod;
	}

	public String getMethodName()
	{
		return introspectedMethod.getName();
	}

	public String getDeclaringClassName()
	{
		return introspectedMethod.getDeclaringClass().getName();
	}

	public String getReturnTypeName()
	{
		return introspectedMethod.getReturnType().getName();
	}

	public boolean isAbstract()
	{
		return Modifier.isAbstract(introspectedMethod.getModifiers());
	}

	public boolean isStatic()
	{
		return Modifier.isStatic(introspectedMethod.getModifiers());
	}

	public boolean isFinal()
	{
		return Modifier.isFinal(introspectedMethod.getModifiers());
	}

	public boolean isOverridable()
	{
		return !isStatic() && !isFinal() && !Modifier.isPrivate(introspectedMethod.getModifiers());
	}

	public boolean isAnnotated(String annotationName)
	{
		return AnnotatedElementUtils.isAnnotated(introspectedMethod, annotationName);
	}

	public Map getAnnotationAttributes(String annotationName)
	{
		return getAnnotationAttributes(annotationName, false);
	}

	public Map getAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		return AnnotatedElementUtils.getMergedAnnotationAttributes(introspectedMethod, annotationName, classValuesAsString, nestedAnnotationsAsMap);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName)
	{
		return getAllAnnotationAttributes(annotationName, false);
	}

	public MultiValueMap getAllAnnotationAttributes(String annotationName, boolean classValuesAsString)
	{
		return AnnotatedElementUtils.getAllAnnotationAttributes(introspectedMethod, annotationName, classValuesAsString, nestedAnnotationsAsMap);
	}
}
