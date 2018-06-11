// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BeanCopier.java

package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public abstract class BeanCopier
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/beans/BeanCopier.getName());
		private Class source;
		private Class target;
		private boolean useConverter;

		public void setSource(Class source)
		{
			if (!Modifier.isPublic(source.getModifiers()))
				setNamePrefix(source.getName());
			this.source = source;
		}

		public void setTarget(Class target)
		{
			if (!Modifier.isPublic(target.getModifiers()))
				setNamePrefix(target.getName());
			this.target = target;
		}

		public void setUseConverter(boolean useConverter)
		{
			this.useConverter = useConverter;
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return source.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(source);
		}

		public BeanCopier create()
		{
			Object key = BeanCopier.KEY_FACTORY.newInstance(source.getName(), target.getName(), useConverter);
			return (BeanCopier)super.create(key);
		}

		public void generateClass(ClassVisitor v)
		{
			Type sourceType = Type.getType(source);
			Type targetType = Type.getType(target);
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(46, 1, getClassName(), BeanCopier.BEAN_COPIER, null, "<generated>");
			EmitUtils.null_constructor(ce);
			CodeEmitter e = ce.begin_method(1, BeanCopier.COPY, null);
			PropertyDescriptor getters[] = ReflectUtils.getBeanGetters(source);
			PropertyDescriptor setters[] = ReflectUtils.getBeanSetters(target);
			Map names = new HashMap();
			for (int i = 0; i < getters.length; i++)
				names.put(getters[i].getName(), getters[i]);

			org.springframework.cglib.core.Local targetLocal = e.make_local();
			org.springframework.cglib.core.Local sourceLocal = e.make_local();
			if (useConverter)
			{
				e.load_arg(1);
				e.checkcast(targetType);
				e.store_local(targetLocal);
				e.load_arg(0);
				e.checkcast(sourceType);
				e.store_local(sourceLocal);
			} else
			{
				e.load_arg(1);
				e.checkcast(targetType);
				e.load_arg(0);
				e.checkcast(sourceType);
			}
			for (int i = 0; i < setters.length; i++)
			{
				PropertyDescriptor setter = setters[i];
				PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
				if (getter == null)
					continue;
				MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
				MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
				if (useConverter)
				{
					Type setterType = write.getSignature().getArgumentTypes()[0];
					e.load_local(targetLocal);
					e.load_arg(2);
					e.load_local(sourceLocal);
					e.invoke(read);
					e.box(read.getSignature().getReturnType());
					EmitUtils.load_class(e, setterType);
					e.push(write.getSignature().getName());
					e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
					e.unbox_or_zero(setterType);
					e.invoke(write);
					continue;
				}
				if (compatible(getter, setter))
				{
					e.dup2();
					e.invoke(read);
					e.invoke(write);
				}
			}

			e.return_value();
			e.end_method();
			ce.end_class();
		}

		private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter)
		{
			return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
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

	static interface BeanCopierKey
	{

		public abstract Object newInstance(String s, String s1, boolean flag);
	}


	private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey)KeyFactory.create(org/springframework/cglib/beans/BeanCopier$BeanCopierKey);
	private static final Type CONVERTER;
	private static final Type BEAN_COPIER = TypeUtils.parseType("org.springframework.cglib.beans.BeanCopier");
	private static final Signature COPY;
	private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");

	public BeanCopier()
	{
	}

	public static BeanCopier create(Class source, Class target, boolean useConverter)
	{
		Generator gen = new Generator();
		gen.setSource(source);
		gen.setTarget(target);
		gen.setUseConverter(useConverter);
		return gen.create();
	}

	public abstract void copy(Object obj, Object obj1, Converter converter);

	static 
	{
		CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
		COPY = new Signature("copy", Type.VOID_TYPE, new Type[] {
			Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER
		});
	}





}
