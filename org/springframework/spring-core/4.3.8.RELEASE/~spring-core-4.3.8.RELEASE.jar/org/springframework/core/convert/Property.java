// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Property.java

package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.util.*;

public final class Property
{

	private static Map annotationCache = new ConcurrentReferenceHashMap();
	private final Class objectType;
	private final Method readMethod;
	private final Method writeMethod;
	private final String name;
	private final MethodParameter methodParameter;
	private Annotation annotations[];

	public Property(Class objectType, Method readMethod, Method writeMethod)
	{
		this(objectType, readMethod, writeMethod, null);
	}

	public Property(Class objectType, Method readMethod, Method writeMethod, String name)
	{
		this.objectType = objectType;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		methodParameter = resolveMethodParameter();
		this.name = name == null ? resolveName() : name;
	}

	public Class getObjectType()
	{
		return objectType;
	}

	public String getName()
	{
		return name;
	}

	public Class getType()
	{
		return methodParameter.getParameterType();
	}

	public Method getReadMethod()
	{
		return readMethod;
	}

	public Method getWriteMethod()
	{
		return writeMethod;
	}

	MethodParameter getMethodParameter()
	{
		return methodParameter;
	}

	Annotation[] getAnnotations()
	{
		if (annotations == null)
			annotations = resolveAnnotations();
		return annotations;
	}

	private String resolveName()
	{
		int index;
		if (readMethod != null)
		{
			index = readMethod.getName().indexOf("get");
			if (index != -1)
			{
				index += 3;
			} else
			{
				index = readMethod.getName().indexOf("is");
				if (index == -1)
					throw new IllegalArgumentException("Not a getter method");
				index += 2;
			}
			return StringUtils.uncapitalize(readMethod.getName().substring(index));
		}
		index = writeMethod.getName().indexOf("set") + 3;
		if (index == -1)
			throw new IllegalArgumentException("Not a setter method");
		else
			return StringUtils.uncapitalize(writeMethod.getName().substring(index));
	}

	private MethodParameter resolveMethodParameter()
	{
		MethodParameter read = resolveReadMethodParameter();
		MethodParameter write = resolveWriteMethodParameter();
		if (write == null)
			if (read == null)
				throw new IllegalStateException("Property is neither readable nor writeable");
			else
				return read;
		if (read != null)
		{
			Class readType = read.getParameterType();
			Class writeType = write.getParameterType();
			if (!writeType.equals(readType) && writeType.isAssignableFrom(readType))
				return read;
		}
		return write;
	}

	private MethodParameter resolveReadMethodParameter()
	{
		if (getReadMethod() == null)
			return null;
		else
			return resolveParameterType(new MethodParameter(getReadMethod(), -1));
	}

	private MethodParameter resolveWriteMethodParameter()
	{
		if (getWriteMethod() == null)
			return null;
		else
			return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
	}

	private MethodParameter resolveParameterType(MethodParameter parameter)
	{
		GenericTypeResolver.resolveParameterType(parameter, getObjectType());
		return parameter;
	}

	private Annotation[] resolveAnnotations()
	{
		Annotation annotations[] = (Annotation[])annotationCache.get(this);
		if (annotations == null)
		{
			Map annotationMap = new LinkedHashMap();
			addAnnotationsToMap(annotationMap, getReadMethod());
			addAnnotationsToMap(annotationMap, getWriteMethod());
			addAnnotationsToMap(annotationMap, getField());
			annotations = (Annotation[])annotationMap.values().toArray(new Annotation[annotationMap.size()]);
			annotationCache.put(this, annotations);
		}
		return annotations;
	}

	private void addAnnotationsToMap(Map annotationMap, AnnotatedElement object)
	{
		if (object != null)
		{
			Annotation aannotation[] = object.getAnnotations();
			int i = aannotation.length;
			for (int j = 0; j < i; j++)
			{
				Annotation annotation = aannotation[j];
				annotationMap.put(annotation.annotationType(), annotation);
			}

		}
	}

	private Field getField()
	{
		String name = getName();
		if (!StringUtils.hasLength(name))
			return null;
		Class declaringClass = declaringClass();
		Field field = ReflectionUtils.findField(declaringClass, name);
		if (field == null)
		{
			field = ReflectionUtils.findField(declaringClass, (new StringBuilder()).append(name.substring(0, 1).toLowerCase()).append(name.substring(1)).toString());
			if (field == null)
				field = ReflectionUtils.findField(declaringClass, (new StringBuilder()).append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString());
		}
		return field;
	}

	private Class declaringClass()
	{
		if (getReadMethod() != null)
			return getReadMethod().getDeclaringClass();
		else
			return getWriteMethod().getDeclaringClass();
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof Property))
		{
			return false;
		} else
		{
			Property otherProperty = (Property)other;
			return ObjectUtils.nullSafeEquals(objectType, otherProperty.objectType) && ObjectUtils.nullSafeEquals(name, otherProperty.name) && ObjectUtils.nullSafeEquals(readMethod, otherProperty.readMethod) && ObjectUtils.nullSafeEquals(writeMethod, otherProperty.writeMethod);
		}
	}

	public int hashCode()
	{
		return ObjectUtils.nullSafeHashCode(objectType) * 31 + ObjectUtils.nullSafeHashCode(name);
	}

}
