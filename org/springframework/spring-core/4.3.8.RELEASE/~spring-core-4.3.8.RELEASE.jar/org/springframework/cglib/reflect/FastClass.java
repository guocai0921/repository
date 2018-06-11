// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FastClass.java

package org.springframework.cglib.reflect;

import java.lang.reflect.*;
import java.security.ProtectionDomain;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.reflect:
//			FastMethod, FastConstructor, FastClassEmitter

public abstract class FastClass
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/reflect/FastClass.getName());
		private Class type;

		public void setType(Class type)
		{
			this.type = type;
		}

		public FastClass create()
		{
			setNamePrefix(type.getName());
			return (FastClass)super.create(type.getName());
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return type.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(type);
		}

		public void generateClass(ClassVisitor v)
			throws Exception
		{
			new FastClassEmitter(v, getClassName(), type);
		}

		protected Object firstInstance(Class type)
		{
			return ReflectUtils.newInstance(type, new Class[] {
				java/lang/Class
			}, new Object[] {
				this.type
			});
		}

		protected Object nextInstance(Object instance)
		{
			return instance;
		}


		public Generator()
		{
			super(SOURCE);
		}
	}


	private Class type;

	protected FastClass()
	{
		throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
	}

	protected FastClass(Class type)
	{
		this.type = type;
	}

	public static FastClass create(Class type)
	{
		return create(type.getClassLoader(), type);
	}

	public static FastClass create(ClassLoader loader, Class type)
	{
		Generator gen = new Generator();
		gen.setType(type);
		gen.setClassLoader(loader);
		return gen.create();
	}

	public Object invoke(String name, Class parameterTypes[], Object obj, Object args[])
		throws InvocationTargetException
	{
		return invoke(getIndex(name, parameterTypes), obj, args);
	}

	public Object newInstance()
		throws InvocationTargetException
	{
		return newInstance(getIndex(Constants.EMPTY_CLASS_ARRAY), null);
	}

	public Object newInstance(Class parameterTypes[], Object args[])
		throws InvocationTargetException
	{
		return newInstance(getIndex(parameterTypes), args);
	}

	public FastMethod getMethod(Method method)
	{
		return new FastMethod(this, method);
	}

	public FastConstructor getConstructor(Constructor constructor)
	{
		return new FastConstructor(this, constructor);
	}

	public FastMethod getMethod(String name, Class parameterTypes[])
	{
		return getMethod(type.getMethod(name, parameterTypes));
		NoSuchMethodException e;
		e;
		throw new NoSuchMethodError(e.getMessage());
	}

	public FastConstructor getConstructor(Class parameterTypes[])
	{
		return getConstructor(type.getConstructor(parameterTypes));
		NoSuchMethodException e;
		e;
		throw new NoSuchMethodError(e.getMessage());
	}

	public String getName()
	{
		return type.getName();
	}

	public Class getJavaClass()
	{
		return type;
	}

	public String toString()
	{
		return type.toString();
	}

	public int hashCode()
	{
		return type.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof FastClass))
			return false;
		else
			return type.equals(((FastClass)o).type);
	}

	public abstract int getIndex(String s, Class aclass[]);

	public abstract int getIndex(Class aclass[]);

	public abstract Object invoke(int i, Object obj, Object aobj[])
		throws InvocationTargetException;

	public abstract Object newInstance(int i, Object aobj[])
		throws InvocationTargetException;

	public abstract int getIndex(Signature signature);

	public abstract int getMaxIndex();

	protected static String getSignatureWithoutReturnType(String name, Class parameterTypes[])
	{
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append('(');
		for (int i = 0; i < parameterTypes.length; i++)
			sb.append(Type.getDescriptor(parameterTypes[i]));

		sb.append(')');
		return sb.toString();
	}
}
