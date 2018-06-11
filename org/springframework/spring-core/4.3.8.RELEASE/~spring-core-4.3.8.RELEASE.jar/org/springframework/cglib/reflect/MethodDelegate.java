// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodDelegate.java

package org.springframework.cglib.reflect;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public abstract class MethodDelegate
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/reflect/MethodDelegate.getName());
		private static final Type METHOD_DELEGATE;
		private static final Signature NEW_INSTANCE;
		private Object target;
		private Class targetClass;
		private String methodName;
		private Class iface;

		public void setTarget(Object target)
		{
			this.target = target;
			targetClass = target.getClass();
		}

		public void setTargetClass(Class targetClass)
		{
			this.targetClass = targetClass;
		}

		public void setMethodName(String methodName)
		{
			this.methodName = methodName;
		}

		public void setInterface(Class iface)
		{
			this.iface = iface;
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return targetClass.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(targetClass);
		}

		public MethodDelegate create()
		{
			setNamePrefix(targetClass.getName());
			Object key = MethodDelegate.KEY_FACTORY.newInstance(targetClass, methodName, iface);
			return (MethodDelegate)super.create(key);
		}

		protected Object firstInstance(Class type)
		{
			return ((MethodDelegate)ReflectUtils.newInstance(type)).newInstance(target);
		}

		protected Object nextInstance(Object instance)
		{
			return ((MethodDelegate)instance).newInstance(target);
		}

		public void generateClass(ClassVisitor v)
			throws NoSuchMethodException
		{
			Method proxy = ReflectUtils.findInterfaceMethod(iface);
			Method method = targetClass.getMethod(methodName, proxy.getParameterTypes());
			if (!proxy.getReturnType().isAssignableFrom(method.getReturnType()))
				throw new IllegalArgumentException("incompatible return types");
			MethodInfo methodInfo = ReflectUtils.getMethodInfo(method);
			boolean isStatic = TypeUtils.isStatic(methodInfo.getModifiers());
			if ((target == null) ^ isStatic)
				throw new IllegalArgumentException((new StringBuilder()).append("Static method ").append(isStatic ? "not " : "").append("expected").toString());
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(46, 1, getClassName(), METHOD_DELEGATE, new Type[] {
				Type.getType(iface)
			}, "<generated>");
			ce.declare_field(26, "eqMethod", Constants.TYPE_STRING, null);
			EmitUtils.null_constructor(ce);
			MethodInfo proxied = ReflectUtils.getMethodInfo(iface.getDeclaredMethods()[0]);
			int modifiers = 1;
			if ((proxied.getModifiers() & 0x80) == 128)
				modifiers |= 0x80;
			CodeEmitter e = EmitUtils.begin_method(ce, proxied, modifiers);
			e.load_this();
			e.super_getfield("target", Constants.TYPE_OBJECT);
			e.checkcast(methodInfo.getClassInfo().getType());
			e.load_args();
			e.invoke(methodInfo);
			e.return_value();
			e.end_method();
			e = ce.begin_method(1, NEW_INSTANCE, null);
			e.new_instance_this();
			e.dup();
			e.dup2();
			e.invoke_constructor_this();
			e.getfield("eqMethod");
			e.super_putfield("eqMethod", Constants.TYPE_STRING);
			e.load_arg(0);
			e.super_putfield("target", Constants.TYPE_OBJECT);
			e.return_value();
			e.end_method();
			e = ce.begin_static();
			e.push(methodInfo.getSignature().toString());
			e.putfield("eqMethod");
			e.return_value();
			e.end_method();
			ce.end_class();
		}

		static 
		{
			METHOD_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.MethodDelegate");
			NEW_INSTANCE = new Signature("newInstance", METHOD_DELEGATE, new Type[] {
				Constants.TYPE_OBJECT
			});
		}

		public Generator()
		{
			super(SOURCE);
		}
	}

	static interface MethodDelegateKey
	{

		public abstract Object newInstance(Class class1, String s, Class class2);
	}


	private static final MethodDelegateKey KEY_FACTORY;
	protected Object target;
	protected String eqMethod;

	public MethodDelegate()
	{
	}

	public static MethodDelegate createStatic(Class targetClass, String methodName, Class iface)
	{
		Generator gen = new Generator();
		gen.setTargetClass(targetClass);
		gen.setMethodName(methodName);
		gen.setInterface(iface);
		return gen.create();
	}

	public static MethodDelegate create(Object target, String methodName, Class iface)
	{
		Generator gen = new Generator();
		gen.setTarget(target);
		gen.setMethodName(methodName);
		gen.setInterface(iface);
		return gen.create();
	}

	public boolean equals(Object obj)
	{
		MethodDelegate other = (MethodDelegate)obj;
		return other != null && target == other.target && eqMethod.equals(other.eqMethod);
	}

	public int hashCode()
	{
		return target.hashCode() ^ eqMethod.hashCode();
	}

	public Object getTarget()
	{
		return target;
	}

	public abstract MethodDelegate newInstance(Object obj);

	static 
	{
		KEY_FACTORY = (MethodDelegateKey)KeyFactory.create(org/springframework/cglib/reflect/MethodDelegate$MethodDelegateKey, KeyFactory.CLASS_BY_NAME);
	}

}
