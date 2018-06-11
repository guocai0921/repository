// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BeanMap.java

package org.springframework.cglib.beans;

import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.beans:
//			BeanMapEmitter

public abstract class BeanMap
	implements Map
{
	public static class Generator extends AbstractClassGenerator
	{
		static interface BeanMapKey
		{

			public abstract Object newInstance(Class class1, int i);
		}


		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/beans/BeanMap.getName());
		private static final BeanMapKey KEY_FACTORY;
		private Object bean;
		private Class beanClass;
		private int require;

		public void setBean(Object bean)
		{
			this.bean = bean;
			if (bean != null)
				beanClass = bean.getClass();
		}

		public void setBeanClass(Class beanClass)
		{
			this.beanClass = beanClass;
		}

		public void setRequire(int require)
		{
			this.require = require;
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return beanClass.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(beanClass);
		}

		public BeanMap create()
		{
			if (beanClass == null)
			{
				throw new IllegalArgumentException("Class of bean unknown");
			} else
			{
				setNamePrefix(beanClass.getName());
				return (BeanMap)super.create(KEY_FACTORY.newInstance(beanClass, require));
			}
		}

		public void generateClass(ClassVisitor v)
			throws Exception
		{
			new BeanMapEmitter(v, getClassName(), beanClass, require);
		}

		protected Object firstInstance(Class type)
		{
			return ((BeanMap)ReflectUtils.newInstance(type)).newInstance(bean);
		}

		protected Object nextInstance(Object instance)
		{
			return ((BeanMap)instance).newInstance(bean);
		}

		static 
		{
			KEY_FACTORY = (BeanMapKey)KeyFactory.create(org/springframework/cglib/beans/BeanMap$Generator$BeanMapKey, KeyFactory.CLASS_BY_NAME);
		}

		public Generator()
		{
			super(SOURCE);
		}
	}


	public static final int REQUIRE_GETTER = 1;
	public static final int REQUIRE_SETTER = 2;
	protected Object bean;

	public static BeanMap create(Object bean)
	{
		Generator gen = new Generator();
		gen.setBean(bean);
		return gen.create();
	}

	public abstract BeanMap newInstance(Object obj);

	public abstract Class getPropertyType(String s);

	protected BeanMap()
	{
	}

	protected BeanMap(Object bean)
	{
		setBean(bean);
	}

	public Object get(Object key)
	{
		return get(bean, key);
	}

	public Object put(Object key, Object value)
	{
		return put(bean, key, value);
	}

	public abstract Object get(Object obj, Object obj1);

	public abstract Object put(Object obj, Object obj1, Object obj2);

	public void setBean(Object bean)
	{
		this.bean = bean;
	}

	public Object getBean()
	{
		return bean;
	}

	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key)
	{
		return keySet().contains(key);
	}

	public boolean containsValue(Object value)
	{
		for (Iterator it = keySet().iterator(); it.hasNext();)
		{
			Object v = get(it.next());
			if (value == null && v == null || value != null && value.equals(v))
				return true;
		}

		return false;
	}

	public int size()
	{
		return keySet().size();
	}

	public boolean isEmpty()
	{
		return size() == 0;
	}

	public Object remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	public void putAll(Map t)
	{
		Object key;
		for (Iterator it = t.keySet().iterator(); it.hasNext(); put(key, t.get(key)))
			key = it.next();

	}

	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Map))
			return false;
		Map other = (Map)o;
		if (size() != other.size())
			return false;
		for (Iterator it = keySet().iterator(); it.hasNext();)
		{
			Object key = it.next();
			if (!other.containsKey(key))
				return false;
			Object v1 = get(key);
			Object v2 = other.get(key);
			if (v1 != null ? !v1.equals(v2) : v2 != null)
				return false;
		}

		return true;
	}

	public int hashCode()
	{
		int code = 0;
		for (Iterator it = keySet().iterator(); it.hasNext();)
		{
			Object key = it.next();
			Object value = get(key);
			code += (key != null ? key.hashCode() : 0) ^ (value != null ? value.hashCode() : 0);
		}

		return code;
	}

	public Set entrySet()
	{
		HashMap copy = new HashMap();
		Object key;
		for (Iterator it = keySet().iterator(); it.hasNext(); copy.put(key, get(key)))
			key = it.next();

		return Collections.unmodifiableMap(copy).entrySet();
	}

	public Collection values()
	{
		Set keys = keySet();
		List values = new ArrayList(keys.size());
		for (Iterator it = keys.iterator(); it.hasNext(); values.add(get(it.next())));
		return Collections.unmodifiableCollection(values);
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		Iterator it = keySet().iterator();
		do
		{
			if (!it.hasNext())
				break;
			Object key = it.next();
			sb.append(key);
			sb.append('=');
			sb.append(get(key));
			if (it.hasNext())
				sb.append(", ");
		} while (true);
		sb.append('}');
		return sb.toString();
	}
}
