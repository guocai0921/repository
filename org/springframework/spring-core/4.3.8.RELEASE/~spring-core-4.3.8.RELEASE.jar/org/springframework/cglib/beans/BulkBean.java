// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BulkBean.java

package org.springframework.cglib.beans;

import java.security.ProtectionDomain;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.beans:
//			BulkBeanEmitter

public abstract class BulkBean
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/beans/BulkBean.getName());
		private Class target;
		private String getters[];
		private String setters[];
		private Class types[];

		public void setTarget(Class target)
		{
			this.target = target;
		}

		public void setGetters(String getters[])
		{
			this.getters = getters;
		}

		public void setSetters(String setters[])
		{
			this.setters = setters;
		}

		public void setTypes(Class types[])
		{
			this.types = types;
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return target.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(target);
		}

		public BulkBean create()
		{
			setNamePrefix(target.getName());
			String targetClassName = target.getName();
			String typeClassNames[] = ReflectUtils.getNames(types);
			Object key = BulkBean.KEY_FACTORY.newInstance(targetClassName, getters, setters, typeClassNames);
			return (BulkBean)super.create(key);
		}

		public void generateClass(ClassVisitor v)
			throws Exception
		{
			new BulkBeanEmitter(v, getClassName(), target, getters, setters, types);
		}

		protected Object firstInstance(Class type)
		{
			BulkBean instance = (BulkBean)ReflectUtils.newInstance(type);
			instance.target = target;
			int length = getters.length;
			instance.getters = new String[length];
			System.arraycopy(getters, 0, instance.getters, 0, length);
			instance.setters = new String[length];
			System.arraycopy(setters, 0, instance.setters, 0, length);
			instance.types = new Class[types.length];
			System.arraycopy(types, 0, instance.types, 0, types.length);
			return instance;
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

	static interface BulkBeanKey
	{

		public abstract Object newInstance(String s, String as[], String as1[], String as2[]);
	}


	private static final BulkBeanKey KEY_FACTORY = (BulkBeanKey)KeyFactory.create(org/springframework/cglib/beans/BulkBean$BulkBeanKey);
	protected Class target;
	protected String getters[];
	protected String setters[];
	protected Class types[];

	protected BulkBean()
	{
	}

	public abstract void getPropertyValues(Object obj, Object aobj[]);

	public abstract void setPropertyValues(Object obj, Object aobj[]);

	public Object[] getPropertyValues(Object bean)
	{
		Object values[] = new Object[getters.length];
		getPropertyValues(bean, values);
		return values;
	}

	public Class[] getPropertyTypes()
	{
		return (Class[])(Class[])types.clone();
	}

	public String[] getGetters()
	{
		return (String[])(String[])getters.clone();
	}

	public String[] getSetters()
	{
		return (String[])(String[])setters.clone();
	}

	public static BulkBean create(Class target, String getters[], String setters[], Class types[])
	{
		Generator gen = new Generator();
		gen.setTarget(target);
		gen.setGetters(getters);
		gen.setSetters(setters);
		gen.setTypes(types);
		return gen.create();
	}


}
