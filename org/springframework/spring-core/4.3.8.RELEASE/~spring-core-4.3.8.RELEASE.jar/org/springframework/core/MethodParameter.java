// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodParameter.java

package org.springframework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core:
//			ParameterNameDiscoverer

public class MethodParameter
{

	private static final Class javaUtilOptionalClass;
	private final Method method;
	private final Constructor constructor;
	private final int parameterIndex;
	private int nestingLevel;
	Map typeIndexesPerLevel;
	private volatile Class containingClass;
	private volatile Class parameterType;
	private volatile Type genericParameterType;
	private volatile Annotation parameterAnnotations[];
	private volatile ParameterNameDiscoverer parameterNameDiscoverer;
	private volatile String parameterName;
	private volatile MethodParameter nestedMethodParameter;

	public MethodParameter(Method method, int parameterIndex)
	{
		this(method, parameterIndex, 1);
	}

	public MethodParameter(Method method, int parameterIndex, int nestingLevel)
	{
		this.nestingLevel = 1;
		Assert.notNull(method, "Method must not be null");
		this.method = method;
		this.parameterIndex = parameterIndex;
		this.nestingLevel = nestingLevel;
		constructor = null;
	}

	public MethodParameter(Constructor constructor, int parameterIndex)
	{
		this(constructor, parameterIndex, 1);
	}

	public MethodParameter(Constructor constructor, int parameterIndex, int nestingLevel)
	{
		this.nestingLevel = 1;
		Assert.notNull(constructor, "Constructor must not be null");
		this.constructor = constructor;
		this.parameterIndex = parameterIndex;
		this.nestingLevel = nestingLevel;
		method = null;
	}

	public MethodParameter(MethodParameter original)
	{
		nestingLevel = 1;
		Assert.notNull(original, "Original must not be null");
		method = original.method;
		constructor = original.constructor;
		parameterIndex = original.parameterIndex;
		nestingLevel = original.nestingLevel;
		typeIndexesPerLevel = original.typeIndexesPerLevel;
		containingClass = original.containingClass;
		parameterType = original.parameterType;
		genericParameterType = original.genericParameterType;
		parameterAnnotations = original.parameterAnnotations;
		parameterNameDiscoverer = original.parameterNameDiscoverer;
		parameterName = original.parameterName;
	}

	public Method getMethod()
	{
		return method;
	}

	public Constructor getConstructor()
	{
		return constructor;
	}

	public Class getDeclaringClass()
	{
		return getMember().getDeclaringClass();
	}

	public Member getMember()
	{
		if (method != null)
			return method;
		else
			return constructor;
	}

	public AnnotatedElement getAnnotatedElement()
	{
		if (method != null)
			return method;
		else
			return constructor;
	}

	public int getParameterIndex()
	{
		return parameterIndex;
	}

	public void increaseNestingLevel()
	{
		nestingLevel++;
	}

	public void decreaseNestingLevel()
	{
		getTypeIndexesPerLevel().remove(Integer.valueOf(nestingLevel));
		nestingLevel--;
	}

	public int getNestingLevel()
	{
		return nestingLevel;
	}

	public void setTypeIndexForCurrentLevel(int typeIndex)
	{
		getTypeIndexesPerLevel().put(Integer.valueOf(nestingLevel), Integer.valueOf(typeIndex));
	}

	public Integer getTypeIndexForCurrentLevel()
	{
		return getTypeIndexForLevel(nestingLevel);
	}

	public Integer getTypeIndexForLevel(int nestingLevel)
	{
		return (Integer)getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
	}

	private Map getTypeIndexesPerLevel()
	{
		if (typeIndexesPerLevel == null)
			typeIndexesPerLevel = new HashMap(4);
		return typeIndexesPerLevel;
	}

	public MethodParameter nested()
	{
		if (nestedMethodParameter != null)
		{
			return nestedMethodParameter;
		} else
		{
			MethodParameter nestedParam = clone();
			nestedParam.nestingLevel = nestingLevel + 1;
			nestedMethodParameter = nestedParam;
			return nestedParam;
		}
	}

	public boolean isOptional()
	{
		return getParameterType() == javaUtilOptionalClass;
	}

	public MethodParameter nestedIfOptional()
	{
		return isOptional() ? nested() : this;
	}

	void setContainingClass(Class containingClass)
	{
		this.containingClass = containingClass;
	}

	public Class getContainingClass()
	{
		return containingClass == null ? getDeclaringClass() : containingClass;
	}

	void setParameterType(Class parameterType)
	{
		this.parameterType = parameterType;
	}

	public Class getParameterType()
	{
		if (parameterType == null)
			if (parameterIndex < 0)
				parameterType = method == null ? null : method.getReturnType();
			else
				parameterType = method == null ? constructor.getParameterTypes()[parameterIndex] : method.getParameterTypes()[parameterIndex];
		return parameterType;
	}

	public Type getGenericParameterType()
	{
		if (genericParameterType == null)
			if (parameterIndex < 0)
				genericParameterType = method == null ? null : method.getGenericReturnType();
			else
				genericParameterType = method == null ? constructor.getGenericParameterTypes()[parameterIndex] : method.getGenericParameterTypes()[parameterIndex];
		return genericParameterType;
	}

	public Class getNestedParameterType()
	{
		if (nestingLevel > 1)
		{
			Type type = getGenericParameterType();
			for (int i = 2; i <= nestingLevel; i++)
				if (type instanceof ParameterizedType)
				{
					Type args[] = ((ParameterizedType)type).getActualTypeArguments();
					Integer index = getTypeIndexForLevel(i);
					type = args[index == null ? args.length - 1 : index.intValue()];
				}

			if (type instanceof Class)
				return (Class)type;
			if (type instanceof ParameterizedType)
			{
				Type arg = ((ParameterizedType)type).getRawType();
				if (arg instanceof Class)
					return (Class)arg;
			}
			return java/lang/Object;
		} else
		{
			return getParameterType();
		}
	}

	public Type getNestedGenericParameterType()
	{
		if (nestingLevel > 1)
		{
			Type type = getGenericParameterType();
			for (int i = 2; i <= nestingLevel; i++)
				if (type instanceof ParameterizedType)
				{
					Type args[] = ((ParameterizedType)type).getActualTypeArguments();
					Integer index = getTypeIndexForLevel(i);
					type = args[index == null ? args.length - 1 : index.intValue()];
				}

			return type;
		} else
		{
			return getGenericParameterType();
		}
	}

	public Annotation[] getMethodAnnotations()
	{
		return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
	}

	public Annotation getMethodAnnotation(Class annotationType)
	{
		return adaptAnnotation(getAnnotatedElement().getAnnotation(annotationType));
	}

	public boolean hasMethodAnnotation(Class annotationType)
	{
		return getAnnotatedElement().isAnnotationPresent(annotationType);
	}

	public Annotation[] getParameterAnnotations()
	{
		if (parameterAnnotations == null)
		{
			Annotation annotationArray[][] = method == null ? constructor.getParameterAnnotations() : method.getParameterAnnotations();
			if (parameterIndex >= 0 && parameterIndex < annotationArray.length)
				parameterAnnotations = adaptAnnotationArray(annotationArray[parameterIndex]);
			else
				parameterAnnotations = new Annotation[0];
		}
		return parameterAnnotations;
	}

	public boolean hasParameterAnnotations()
	{
		return getParameterAnnotations().length != 0;
	}

	public Annotation getParameterAnnotation(Class annotationType)
	{
		Annotation anns[] = getParameterAnnotations();
		Annotation aannotation[] = anns;
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation ann = aannotation[j];
			if (annotationType.isInstance(ann))
				return ann;
		}

		return null;
	}

	public boolean hasParameterAnnotation(Class annotationType)
	{
		return getParameterAnnotation(annotationType) != null;
	}

	public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
	{
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	public String getParameterName()
	{
		ParameterNameDiscoverer discoverer = parameterNameDiscoverer;
		if (discoverer != null)
		{
			String parameterNames[] = method == null ? discoverer.getParameterNames(constructor) : discoverer.getParameterNames(method);
			if (parameterNames != null)
				parameterName = parameterNames[parameterIndex];
			parameterNameDiscoverer = null;
		}
		return parameterName;
	}

	protected Annotation adaptAnnotation(Annotation annotation)
	{
		return annotation;
	}

	protected Annotation[] adaptAnnotationArray(Annotation annotations[])
	{
		return annotations;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof MethodParameter))
		{
			return false;
		} else
		{
			MethodParameter otherParam = (MethodParameter)other;
			return parameterIndex == otherParam.parameterIndex && getMember().equals(otherParam.getMember());
		}
	}

	public int hashCode()
	{
		return getMember().hashCode() * 31 + parameterIndex;
	}

	public String toString()
	{
		return (new StringBuilder()).append(method == null ? "constructor" : (new StringBuilder()).append("method '").append(method.getName()).append("'").toString()).append(" parameter ").append(parameterIndex).toString();
	}

	public MethodParameter clone()
	{
		return new MethodParameter(this);
	}

	public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex)
	{
		if (methodOrConstructor instanceof Method)
			return new MethodParameter((Method)methodOrConstructor, parameterIndex);
		if (methodOrConstructor instanceof Constructor)
			return new MethodParameter((Constructor)methodOrConstructor, parameterIndex);
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Given object [").append(methodOrConstructor).append("] is neither a Method nor a Constructor").toString());
	}

	public volatile Object clone()
		throws CloneNotSupportedException
	{
		return clone();
	}

	static 
	{
		Class clazz;
		try
		{
			clazz = ClassUtils.forName("java.util.Optional", org/springframework/core/MethodParameter.getClassLoader());
		}
		catch (ClassNotFoundException ex)
		{
			clazz = null;
		}
		javaUtilOptionalClass = clazz;
	}
}
