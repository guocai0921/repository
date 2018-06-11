// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ImmutableBean.java

package org.springframework.cglib.beans;

import java.security.ProtectionDomain;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public class ImmutableBean
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/beans/ImmutableBean.getName());
		private Object bean;
		private Class target;

		public void setBean(Object bean)
		{
			this.bean = bean;
			target = bean.getClass();
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return target.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(target);
		}

		public Object create()
		{
			String name = target.getName();
			setNamePrefix(name);
			return super.create(name);
		}

		public void generateClass(ClassVisitor v)
		{
			Type targetType = Type.getType(target);
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(46, 1, getClassName(), targetType, null, "<generated>");
			ce.declare_field(18, "CGLIB$RWBean", targetType, null);
			CodeEmitter e = ce.begin_method(1, ImmutableBean.CSTRUCT_OBJECT, null);
			e.load_this();
			e.super_invoke_constructor();
			e.load_this();
			e.load_arg(0);
			e.checkcast(targetType);
			e.putfield("CGLIB$RWBean");
			e.return_value();
			e.end_method();
			java.beans.PropertyDescriptor descriptors[] = ReflectUtils.getBeanProperties(target);
			java.lang.reflect.Method getters[] = ReflectUtils.getPropertyMethods(descriptors, true, false);
			java.lang.reflect.Method setters[] = ReflectUtils.getPropertyMethods(descriptors, false, true);
			for (int i = 0; i < getters.length; i++)
			{
				org.springframework.cglib.core.MethodInfo getter = ReflectUtils.getMethodInfo(getters[i]);
				e = EmitUtils.begin_method(ce, getter, 1);
				e.load_this();
				e.getfield("CGLIB$RWBean");
				e.invoke(getter);
				e.return_value();
				e.end_method();
			}

			for (int i = 0; i < setters.length; i++)
			{
				org.springframework.cglib.core.MethodInfo setter = ReflectUtils.getMethodInfo(setters[i]);
				e = EmitUtils.begin_method(ce, setter, 1);
				e.throw_exception(ImmutableBean.ILLEGAL_STATE_EXCEPTION, "Bean is immutable");
				e.end_method();
			}

			ce.end_class();
		}

		protected Object firstInstance(Class type)
		{
			return ReflectUtils.newInstance(type, ImmutableBean.OBJECT_CLASSES, new Object[] {
				bean
			});
		}

		protected Object nextInstance(Object instance)
		{
			return firstInstance(instance.getClass());
		}


		public Generator()
		{
			super(SOURCE);
		}
	}


	private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
	private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
	private static final Class OBJECT_CLASSES[] = {
		java/lang/Object
	};
	private static final String FIELD_NAME = "CGLIB$RWBean";

	private ImmutableBean()
	{
	}

	public static Object create(Object bean)
	{
		Generator gen = new Generator();
		gen.setBean(bean);
		return gen.create();
	}




}
