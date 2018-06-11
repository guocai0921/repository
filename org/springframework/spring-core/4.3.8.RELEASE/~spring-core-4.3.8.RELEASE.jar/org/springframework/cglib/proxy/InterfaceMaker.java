// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterfaceMaker.java

package org.springframework.cglib.proxy;

import java.lang.reflect.Method;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.*;

public class InterfaceMaker extends AbstractClassGenerator
{

	private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/proxy/InterfaceMaker.getName());
	private Map signatures;

	public InterfaceMaker()
	{
		super(SOURCE);
		signatures = new HashMap();
	}

	public void add(Signature sig, Type exceptions[])
	{
		signatures.put(sig, exceptions);
	}

	public void add(Method method)
	{
		add(ReflectUtils.getSignature(method), ReflectUtils.getExceptionTypes(method));
	}

	public void add(Class clazz)
	{
		Method methods[] = clazz.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method m = methods[i];
			if (!m.getDeclaringClass().getName().equals("java.lang.Object"))
				add(m);
		}

	}

	public Class create()
	{
		setUseCache(false);
		return (Class)super.create(this);
	}

	protected ClassLoader getDefaultClassLoader()
	{
		return null;
	}

	protected Object firstInstance(Class type)
	{
		return type;
	}

	protected Object nextInstance(Object instance)
	{
		throw new IllegalStateException("InterfaceMaker does not cache");
	}

	public void generateClass(ClassVisitor v)
		throws Exception
	{
		ClassEmitter ce = new ClassEmitter(v);
		ce.begin_class(46, 513, getClassName(), null, null, "<generated>");
		Signature sig;
		Type exceptions[];
		for (Iterator it = signatures.keySet().iterator(); it.hasNext(); ce.begin_method(1025, sig, exceptions).end_method())
		{
			sig = (Signature)it.next();
			exceptions = (Type[])(Type[])signatures.get(sig);
		}

		ce.end_class();
	}

}
