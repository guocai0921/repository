// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PercSerializationInstantiator.java

package org.springframework.objenesis.instantiator.perc;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public class PercSerializationInstantiator
	implements ObjectInstantiator
{

	private Object typeArgs[];
	private final Method newInstanceMethod;

	public PercSerializationInstantiator(Class type)
	{
		Class unserializableType;
		for (unserializableType = type; java/io/Serializable.isAssignableFrom(unserializableType); unserializableType = unserializableType.getSuperclass());
		try
		{
			Class percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
			newInstanceMethod = java/io/ObjectInputStream.getDeclaredMethod("noArgConstruct", new Class[] {
				java/lang/Class, java/lang/Object, percMethodClass
			});
			newInstanceMethod.setAccessible(true);
			Class percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
			Method getPercClassMethod = percClassClass.getDeclaredMethod("getPercClass", new Class[] {
				java/lang/Class
			});
			Object someObject = getPercClassMethod.invoke(null, new Object[] {
				unserializableType
			});
			Method findMethodMethod = someObject.getClass().getDeclaredMethod("findMethod", new Class[] {
				java/lang/String
			});
			Object percMethod = findMethodMethod.invoke(someObject, new Object[] {
				"<init>()V"
			});
			typeArgs = (new Object[] {
				unserializableType, type, percMethod
			});
		}
		catch (ClassNotFoundException e)
		{
			throw new ObjenesisException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new ObjenesisException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new ObjenesisException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new ObjenesisException(e);
		}
	}

	public Object newInstance()
	{
		return newInstanceMethod.invoke(null, typeArgs);
		IllegalAccessException e;
		e;
		throw new ObjenesisException(e);
		e;
		throw new ObjenesisException(e);
	}
}
