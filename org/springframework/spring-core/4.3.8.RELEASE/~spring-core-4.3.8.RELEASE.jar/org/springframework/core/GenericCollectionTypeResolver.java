// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericCollectionTypeResolver.java

package org.springframework.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

// Referenced classes of package org.springframework.core:
//			ResolvableType, MethodParameter

/**
 * @deprecated Class GenericCollectionTypeResolver is deprecated
 */

public abstract class GenericCollectionTypeResolver
{

	public GenericCollectionTypeResolver()
	{
	}

	public static Class getCollectionType(Class collectionClass)
	{
		return ResolvableType.forClass(collectionClass).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getMapKeyType(Class mapClass)
	{
		return ResolvableType.forClass(mapClass).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapValueType(Class mapClass)
	{
		return ResolvableType.forClass(mapClass).asMap().resolveGeneric(new int[] {
			1
		});
	}

	public static Class getCollectionFieldType(Field collectionField)
	{
		return ResolvableType.forField(collectionField).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getCollectionFieldType(Field collectionField, int nestingLevel)
	{
		return ResolvableType.forField(collectionField).getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
	}

	/**
	 * @deprecated Method getCollectionFieldType is deprecated
	 */

	public static Class getCollectionFieldType(Field collectionField, int nestingLevel, Map typeIndexesPerLevel)
	{
		return ResolvableType.forField(collectionField).getNested(nestingLevel, typeIndexesPerLevel).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getMapKeyFieldType(Field mapField)
	{
		return ResolvableType.forField(mapField).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapKeyFieldType(Field mapField, int nestingLevel)
	{
		return ResolvableType.forField(mapField).getNested(nestingLevel).asMap().resolveGeneric(new int[] {
			0
		});
	}

	/**
	 * @deprecated Method getMapKeyFieldType is deprecated
	 */

	public static Class getMapKeyFieldType(Field mapField, int nestingLevel, Map typeIndexesPerLevel)
	{
		return ResolvableType.forField(mapField).getNested(nestingLevel, typeIndexesPerLevel).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapValueFieldType(Field mapField)
	{
		return ResolvableType.forField(mapField).asMap().resolveGeneric(new int[] {
			1
		});
	}

	public static Class getMapValueFieldType(Field mapField, int nestingLevel)
	{
		return ResolvableType.forField(mapField).getNested(nestingLevel).asMap().resolveGeneric(new int[] {
			1
		});
	}

	/**
	 * @deprecated Method getMapValueFieldType is deprecated
	 */

	public static Class getMapValueFieldType(Field mapField, int nestingLevel, Map typeIndexesPerLevel)
	{
		return ResolvableType.forField(mapField).getNested(nestingLevel, typeIndexesPerLevel).asMap().resolveGeneric(new int[] {
			1
		});
	}

	public static Class getCollectionParameterType(MethodParameter methodParam)
	{
		return ResolvableType.forMethodParameter(methodParam).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getMapKeyParameterType(MethodParameter methodParam)
	{
		return ResolvableType.forMethodParameter(methodParam).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapValueParameterType(MethodParameter methodParam)
	{
		return ResolvableType.forMethodParameter(methodParam).asMap().resolveGeneric(new int[] {
			1
		});
	}

	public static Class getCollectionReturnType(Method method)
	{
		return ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getCollectionReturnType(Method method, int nestingLevel)
	{
		return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
	}

	public static Class getMapKeyReturnType(Method method)
	{
		return ResolvableType.forMethodReturnType(method).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapKeyReturnType(Method method, int nestingLevel)
	{
		return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asMap().resolveGeneric(new int[] {
			0
		});
	}

	public static Class getMapValueReturnType(Method method)
	{
		return ResolvableType.forMethodReturnType(method).asMap().resolveGeneric(new int[] {
			1
		});
	}

	public static Class getMapValueReturnType(Method method, int nestingLevel)
	{
		return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asMap().resolveGeneric(new int[] {
			1
		});
	}
}
