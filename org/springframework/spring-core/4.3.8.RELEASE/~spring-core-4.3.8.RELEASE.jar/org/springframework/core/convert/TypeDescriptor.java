// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeDescriptor.java

package org.springframework.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.convert:
//			Property

public class TypeDescriptor
	implements Serializable
{
	private static class StreamDelegate
	{

		public static boolean isStream(Class type)
		{
			return java/util/stream/Stream.isAssignableFrom(type);
		}

		public static TypeDescriptor getStreamElementType(TypeDescriptor source)
		{
			return TypeDescriptor.getRelatedIfResolvable(source, source.getResolvableType().as(java/util/stream/Stream).getGeneric(new int[] {
				0
			}));
		}

		private StreamDelegate()
		{
		}
	}

	private class AnnotatedElementAdapter
		implements AnnotatedElement, Serializable
	{

		private final Annotation annotations[];
		final TypeDescriptor this$0;

		public boolean isAnnotationPresent(Class annotationClass)
		{
			Annotation aannotation[] = getAnnotations();
			int i = aannotation.length;
			for (int j = 0; j < i; j++)
			{
				Annotation annotation = aannotation[j];
				if (annotation.annotationType() == annotationClass)
					return true;
			}

			return false;
		}

		public Annotation getAnnotation(Class annotationClass)
		{
			Annotation aannotation[] = getAnnotations();
			int i = aannotation.length;
			for (int j = 0; j < i; j++)
			{
				Annotation annotation = aannotation[j];
				if (annotation.annotationType() == annotationClass)
					return annotation;
			}

			return null;
		}

		public Annotation[] getAnnotations()
		{
			return annotations == null ? TypeDescriptor.EMPTY_ANNOTATION_ARRAY : annotations;
		}

		public Annotation[] getDeclaredAnnotations()
		{
			return getAnnotations();
		}

		public boolean isEmpty()
		{
			return ObjectUtils.isEmpty(annotations);
		}

		public boolean equals(Object other)
		{
			return this == other || (other instanceof AnnotatedElementAdapter) && Arrays.equals(annotations, ((AnnotatedElementAdapter)other).annotations);
		}

		public int hashCode()
		{
			return Arrays.hashCode(annotations);
		}

		public String toString()
		{
			return TypeDescriptor.this.toString();
		}

		public AnnotatedElementAdapter(Annotation annotations[])
		{
			this$0 = TypeDescriptor.this;
			super();
			this.annotations = annotations;
		}
	}


	static final Annotation EMPTY_ANNOTATION_ARRAY[] = new Annotation[0];
	private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", org/springframework/core/convert/TypeDescriptor.getClassLoader());
	private static final Map commonTypesCache;
	private static final Class CACHED_COMMON_TYPES[];
	private final Class type;
	private final ResolvableType resolvableType;
	private final AnnotatedElementAdapter annotatedElement;

	public TypeDescriptor(MethodParameter methodParameter)
	{
		resolvableType = ResolvableType.forMethodParameter(methodParameter);
		type = resolvableType.resolve(methodParameter.getParameterType());
		annotatedElement = new AnnotatedElementAdapter(methodParameter.getParameterIndex() != -1 ? methodParameter.getParameterAnnotations() : methodParameter.getMethodAnnotations());
	}

	public TypeDescriptor(Field field)
	{
		resolvableType = ResolvableType.forField(field);
		type = resolvableType.resolve(field.getType());
		annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
	}

	public TypeDescriptor(Property property)
	{
		Assert.notNull(property, "Property must not be null");
		resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
		type = resolvableType.resolve(property.getType());
		annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
	}

	protected TypeDescriptor(ResolvableType resolvableType, Class type, Annotation annotations[])
	{
		this.resolvableType = resolvableType;
		this.type = type == null ? resolvableType.resolve(java/lang/Object) : type;
		annotatedElement = new AnnotatedElementAdapter(annotations);
	}

	public Class getObjectType()
	{
		return ClassUtils.resolvePrimitiveIfNecessary(getType());
	}

	public Class getType()
	{
		return type;
	}

	public ResolvableType getResolvableType()
	{
		return resolvableType;
	}

	public Object getSource()
	{
		return resolvableType == null ? null : resolvableType.getSource();
	}

	public TypeDescriptor narrow(Object value)
	{
		if (value == null)
		{
			return this;
		} else
		{
			ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
			return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
		}
	}

	public TypeDescriptor upcast(Class superType)
	{
		if (superType == null)
		{
			return null;
		} else
		{
			Assert.isAssignable(superType, getType());
			return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
		}
	}

	public String getName()
	{
		return ClassUtils.getQualifiedName(getType());
	}

	public boolean isPrimitive()
	{
		return getType().isPrimitive();
	}

	public Annotation[] getAnnotations()
	{
		return annotatedElement.getAnnotations();
	}

	public boolean hasAnnotation(Class annotationType)
	{
		if (annotatedElement.isEmpty())
			return false;
		else
			return AnnotatedElementUtils.isAnnotated(annotatedElement, annotationType);
	}

	public Annotation getAnnotation(Class annotationType)
	{
		if (annotatedElement.isEmpty())
			return null;
		else
			return AnnotatedElementUtils.getMergedAnnotation(annotatedElement, annotationType);
	}

	public boolean isAssignableTo(TypeDescriptor typeDescriptor)
	{
		boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
		if (!typesAssignable)
			return false;
		if (isArray() && typeDescriptor.isArray())
			return getElementTypeDescriptor().isAssignableTo(typeDescriptor.getElementTypeDescriptor());
		if (isCollection() && typeDescriptor.isCollection())
			return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
		if (isMap() && typeDescriptor.isMap())
			return isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor());
		else
			return true;
	}

	private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor)
	{
		if (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null)
			return true;
		else
			return nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor);
	}

	public boolean isCollection()
	{
		return java/util/Collection.isAssignableFrom(getType());
	}

	public boolean isArray()
	{
		return getType().isArray();
	}

	public TypeDescriptor getElementTypeDescriptor()
	{
		if (getResolvableType().isArray())
			return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
		if (streamAvailable && StreamDelegate.isStream(getType()))
			return StreamDelegate.getStreamElementType(this);
		else
			return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(new int[] {
				0
			}));
	}

	public TypeDescriptor elementTypeDescriptor(Object element)
	{
		return narrow(element, getElementTypeDescriptor());
	}

	public boolean isMap()
	{
		return java/util/Map.isAssignableFrom(getType());
	}

	public TypeDescriptor getMapKeyTypeDescriptor()
	{
		Assert.state(isMap(), "Not a [java.util.Map]");
		return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] {
			0
		}));
	}

	public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey)
	{
		return narrow(mapKey, getMapKeyTypeDescriptor());
	}

	public TypeDescriptor getMapValueTypeDescriptor()
	{
		Assert.state(isMap(), "Not a [java.util.Map]");
		return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] {
			1
		}));
	}

	public TypeDescriptor getMapValueTypeDescriptor(Object mapValue)
	{
		return narrow(mapValue, getMapValueTypeDescriptor());
	}

	private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor)
	{
		if (typeDescriptor != null)
			return typeDescriptor.narrow(value);
		if (value != null)
			return narrow(value);
		else
			return null;
	}

	public boolean equals(Object other)
	{
		if (this == other)
			return true;
		if (!(other instanceof TypeDescriptor))
			return false;
		TypeDescriptor otherDesc = (TypeDescriptor)other;
		if (getType() != otherDesc.getType())
			return false;
		if (!annotationsMatch(otherDesc))
			return false;
		if (isCollection() || isArray())
			return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
		if (isMap())
			return ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) && ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor());
		else
			return true;
	}

	private boolean annotationsMatch(TypeDescriptor otherDesc)
	{
		Annotation anns[] = getAnnotations();
		Annotation otherAnns[] = otherDesc.getAnnotations();
		if (anns == otherAnns)
			return true;
		if (anns.length != otherAnns.length)
			return false;
		if (anns.length > 0)
		{
			for (int i = 0; i < anns.length; i++)
				if (!annotationEquals(anns[i], otherAnns[i]))
					return false;

		}
		return true;
	}

	private boolean annotationEquals(Annotation ann, Annotation otherAnn)
	{
		return ann == otherAnn || ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn);
	}

	public int hashCode()
	{
		return getType().hashCode();
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		Annotation aannotation[] = getAnnotations();
		int i = aannotation.length;
		for (int j = 0; j < i; j++)
		{
			Annotation ann = aannotation[j];
			builder.append("@").append(ann.annotationType().getName()).append(' ');
		}

		builder.append(getResolvableType().toString());
		return builder.toString();
	}

	public static TypeDescriptor forObject(Object source)
	{
		return source == null ? null : valueOf(source.getClass());
	}

	public static TypeDescriptor valueOf(Class type)
	{
		if (type == null)
			type = java/lang/Object;
		TypeDescriptor desc = (TypeDescriptor)commonTypesCache.get(type);
		return desc == null ? new TypeDescriptor(ResolvableType.forClass(type), null, null) : desc;
	}

	public static TypeDescriptor collection(Class collectionType, TypeDescriptor elementTypeDescriptor)
	{
		Assert.notNull(collectionType, "Collection type must not be null");
		if (!java/util/Collection.isAssignableFrom(collectionType))
		{
			throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
		} else
		{
			ResolvableType element = elementTypeDescriptor == null ? null : elementTypeDescriptor.resolvableType;
			return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, new ResolvableType[] {
				element
			}), null, null);
		}
	}

	public static TypeDescriptor map(Class mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor)
	{
		Assert.notNull(mapType, "Map type must not be null");
		if (!java/util/Map.isAssignableFrom(mapType))
		{
			throw new IllegalArgumentException("Map type must be a [java.util.Map]");
		} else
		{
			ResolvableType key = keyTypeDescriptor == null ? null : keyTypeDescriptor.resolvableType;
			ResolvableType value = valueTypeDescriptor == null ? null : valueTypeDescriptor.resolvableType;
			return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, new ResolvableType[] {
				key, value
			}), null, null);
		}
	}

	public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor)
	{
		if (elementTypeDescriptor == null)
			return null;
		else
			return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType), null, elementTypeDescriptor.getAnnotations());
	}

	public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel)
	{
		if (methodParameter.getNestingLevel() != 1)
			throw new IllegalArgumentException("MethodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
		else
			return nested(new TypeDescriptor(methodParameter), nestingLevel);
	}

	public static TypeDescriptor nested(Field field, int nestingLevel)
	{
		return nested(new TypeDescriptor(field), nestingLevel);
	}

	public static TypeDescriptor nested(Property property, int nestingLevel)
	{
		return nested(new TypeDescriptor(property), nestingLevel);
	}

	private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel)
	{
		ResolvableType nested = typeDescriptor.resolvableType;
		for (int i = 0; i < nestingLevel; i++)
			if (java/lang/Object != nested.getType())
				nested = nested.getNested(2);

		if (nested == ResolvableType.NONE)
			return null;
		else
			return getRelatedIfResolvable(typeDescriptor, nested);
	}

	private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type)
	{
		if (type.resolve() == null)
			return null;
		else
			return new TypeDescriptor(type, null, source.getAnnotations());
	}

	static 
	{
		commonTypesCache = new HashMap(18);
		CACHED_COMMON_TYPES = (new Class[] {
			Boolean.TYPE, java/lang/Boolean, Byte.TYPE, java/lang/Byte, Character.TYPE, java/lang/Character, Double.TYPE, java/lang/Double, Integer.TYPE, java/lang/Integer, 
			Long.TYPE, java/lang/Long, Float.TYPE, java/lang/Float, Short.TYPE, java/lang/Short, java/lang/String, java/lang/Object
		});
		Class aclass[] = CACHED_COMMON_TYPES;
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class preCachedClass = aclass[j];
			commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
		}

	}

}
