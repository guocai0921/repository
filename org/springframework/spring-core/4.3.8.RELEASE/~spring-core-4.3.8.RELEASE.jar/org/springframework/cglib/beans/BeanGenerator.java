// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BeanGenerator.java

package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public class BeanGenerator extends AbstractClassGenerator
{
	static interface BeanGeneratorKey
	{

		public abstract Object newInstance(String s, Map map);
	}


	private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/beans/BeanGenerator.getName());
	private static final BeanGeneratorKey KEY_FACTORY = (BeanGeneratorKey)KeyFactory.create(org/springframework/cglib/beans/BeanGenerator$BeanGeneratorKey);
	private Class superclass;
	private Map props;
	private boolean classOnly;

	public BeanGenerator()
	{
		super(SOURCE);
		props = new HashMap();
	}

	public void setSuperclass(Class superclass)
	{
		if (superclass != null && superclass.equals(java/lang/Object))
			superclass = null;
		this.superclass = superclass;
	}

	public void addProperty(String name, Class type)
	{
		if (props.containsKey(name))
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Duplicate property name \"").append(name).append("\"").toString());
		} else
		{
			props.put(name, Type.getType(type));
			return;
		}
	}

	protected ClassLoader getDefaultClassLoader()
	{
		if (superclass != null)
			return superclass.getClassLoader();
		else
			return null;
	}

	protected ProtectionDomain getProtectionDomain()
	{
		return ReflectUtils.getProtectionDomain(superclass);
	}

	public Object create()
	{
		classOnly = false;
		return createHelper();
	}

	public Object createClass()
	{
		classOnly = true;
		return createHelper();
	}

	private Object createHelper()
	{
		if (superclass != null)
			setNamePrefix(superclass.getName());
		String superName = superclass == null ? "java.lang.Object" : superclass.getName();
		Object key = KEY_FACTORY.newInstance(superName, props);
		return super.create(key);
	}

	public void generateClass(ClassVisitor v)
		throws Exception
	{
		int size = props.size();
		String names[] = (String[])(String[])props.keySet().toArray(new String[size]);
		Type types[] = new Type[size];
		for (int i = 0; i < size; i++)
			types[i] = (Type)props.get(names[i]);

		ClassEmitter ce = new ClassEmitter(v);
		ce.begin_class(46, 1, getClassName(), superclass == null ? Constants.TYPE_OBJECT : Type.getType(superclass), null, null);
		EmitUtils.null_constructor(ce);
		EmitUtils.add_properties(ce, names, types);
		ce.end_class();
	}

	protected Object firstInstance(Class type)
	{
		if (classOnly)
			return type;
		else
			return ReflectUtils.newInstance(type);
	}

	protected Object nextInstance(Object instance)
	{
		Class protoclass = (instance instanceof Class) ? (Class)instance : instance.getClass();
		if (classOnly)
			return protoclass;
		else
			return ReflectUtils.newInstance(protoclass);
	}

	public static void addProperties(BeanGenerator gen, Map props)
	{
		String name;
		for (Iterator it = props.keySet().iterator(); it.hasNext(); gen.addProperty(name, (Class)props.get(name)))
			name = (String)it.next();

	}

	public static void addProperties(BeanGenerator gen, Class type)
	{
		addProperties(gen, ReflectUtils.getBeanProperties(type));
	}

	public static void addProperties(BeanGenerator gen, PropertyDescriptor descriptors[])
	{
		for (int i = 0; i < descriptors.length; i++)
			gen.addProperty(descriptors[i].getName(), descriptors[i].getPropertyType());

	}

}
