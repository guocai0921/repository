// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConstructorDelegate.java

package org.springframework.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public abstract class ConstructorDelegate
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/reflect/ConstructorDelegate.getName());
		private static final Type CONSTRUCTOR_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.ConstructorDelegate");
		private Class iface;
		private Class targetClass;

		public void setInterface(Class iface)
		{
			this.iface = iface;
		}

		public void setTargetClass(Class targetClass)
		{
			this.targetClass = targetClass;
		}

		public ConstructorDelegate create()
		{
			setNamePrefix(targetClass.getName());
			Object key = ConstructorDelegate.KEY_FACTORY.newInstance(iface.getName(), targetClass.getName());
			return (ConstructorDelegate)super.create(key);
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return targetClass.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(targetClass);
		}

		public void generateClass(ClassVisitor v)
		{
			setNamePrefix(targetClass.getName());
			Method newInstance = ReflectUtils.findNewInstance(iface);
			if (!newInstance.getReturnType().isAssignableFrom(targetClass))
				throw new IllegalArgumentException("incompatible return type");
			Constructor constructor;
			try
			{
				constructor = targetClass.getDeclaredConstructor(newInstance.getParameterTypes());
			}
			catch (NoSuchMethodException e)
			{
				throw new IllegalArgumentException("interface does not match any known constructor");
			}
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(46, 1, getClassName(), CONSTRUCTOR_DELEGATE, new Type[] {
				Type.getType(iface)
			}, "<generated>");
			Type declaring = Type.getType(constructor.getDeclaringClass());
			EmitUtils.null_constructor(ce);
			CodeEmitter e = ce.begin_method(1, ReflectUtils.getSignature(newInstance), ReflectUtils.getExceptionTypes(newInstance));
			e.new_instance(declaring);
			e.dup();
			e.load_args();
			e.invoke_constructor(declaring, ReflectUtils.getSignature(constructor));
			e.return_value();
			e.end_method();
			ce.end_class();
		}

		protected Object firstInstance(Class type)
		{
			return ReflectUtils.newInstance(type);
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

	static interface ConstructorKey
	{

		public abstract Object newInstance(String s, String s1);
	}


	private static final ConstructorKey KEY_FACTORY;

	protected ConstructorDelegate()
	{
	}

	public static ConstructorDelegate create(Class targetClass, Class iface)
	{
		Generator gen = new Generator();
		gen.setTargetClass(targetClass);
		gen.setInterface(iface);
		return gen.create();
	}

	static 
	{
		KEY_FACTORY = (ConstructorKey)KeyFactory.create(org/springframework/cglib/reflect/ConstructorDelegate$ConstructorKey, KeyFactory.CLASS_BY_NAME);
	}

}
