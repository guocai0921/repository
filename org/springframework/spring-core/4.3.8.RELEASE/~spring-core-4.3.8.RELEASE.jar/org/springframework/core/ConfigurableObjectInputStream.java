// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConfigurableObjectInputStream.java

package org.springframework.core;

import java.io.*;
import org.springframework.util.ClassUtils;

public class ConfigurableObjectInputStream extends ObjectInputStream
{

	private final ClassLoader classLoader;
	private final boolean acceptProxyClasses;

	public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader)
		throws IOException
	{
		this(in, classLoader, true);
	}

	public ConfigurableObjectInputStream(InputStream in, ClassLoader classLoader, boolean acceptProxyClasses)
		throws IOException
	{
		super(in);
		this.classLoader = classLoader;
		this.acceptProxyClasses = acceptProxyClasses;
	}

	protected Class resolveClass(ObjectStreamClass classDesc)
		throws IOException, ClassNotFoundException
	{
		if (classLoader != null)
			return ClassUtils.forName(classDesc.getName(), classLoader);
		return super.resolveClass(classDesc);
		ClassNotFoundException ex;
		ex;
		return resolveFallbackIfPossible(classDesc.getName(), ex);
	}

	protected Class resolveProxyClass(String interfaces[])
		throws IOException, ClassNotFoundException
	{
		Class resolvedInterfaces[];
		if (!acceptProxyClasses)
			throw new NotSerializableException("Not allowed to accept serialized proxy classes");
		if (classLoader == null)
			break MISSING_BLOCK_LABEL_94;
		resolvedInterfaces = new Class[interfaces.length];
		for (int i = 0; i < interfaces.length; i++)
			try
			{
				resolvedInterfaces[i] = ClassUtils.forName(interfaces[i], classLoader);
			}
			catch (ClassNotFoundException ex)
			{
				resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);
			}

		return ClassUtils.createCompositeInterface(resolvedInterfaces, classLoader);
		IllegalArgumentException ex;
		ex;
		throw new ClassNotFoundException(null, ex);
		return super.resolveProxyClass(interfaces);
		ClassNotFoundException ex;
		ex;
		Class resolvedInterfaces[] = new Class[interfaces.length];
		for (int i = 0; i < interfaces.length; i++)
			resolvedInterfaces[i] = resolveFallbackIfPossible(interfaces[i], ex);

		return ClassUtils.createCompositeInterface(resolvedInterfaces, getFallbackClassLoader());
	}

	protected Class resolveFallbackIfPossible(String className, ClassNotFoundException ex)
		throws IOException, ClassNotFoundException
	{
		throw ex;
	}

	protected ClassLoader getFallbackClassLoader()
		throws IOException
	{
		return null;
	}
}
