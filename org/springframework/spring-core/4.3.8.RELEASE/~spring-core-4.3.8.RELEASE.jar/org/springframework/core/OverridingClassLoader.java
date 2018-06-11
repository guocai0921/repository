// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OverridingClassLoader.java

package org.springframework.core;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.util.FileCopyUtils;

// Referenced classes of package org.springframework.core:
//			DecoratingClassLoader

public class OverridingClassLoader extends DecoratingClassLoader
{

	public static final String DEFAULT_EXCLUDED_PACKAGES[] = {
		"java.", "javax.", "sun.", "oracle.", "javassist.", "org.aspectj.", "net.sf.cglib."
	};
	private static final String CLASS_FILE_SUFFIX = ".class";
	private final ClassLoader overrideDelegate;

	public OverridingClassLoader(ClassLoader parent)
	{
		this(parent, null);
	}

	public OverridingClassLoader(ClassLoader parent, ClassLoader overrideDelegate)
	{
		super(parent);
		this.overrideDelegate = overrideDelegate;
		String as[] = DEFAULT_EXCLUDED_PACKAGES;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String packageName = as[j];
			excludePackage(packageName);
		}

	}

	public Class loadClass(String name)
		throws ClassNotFoundException
	{
		if (overrideDelegate != null && isEligibleForOverriding(name))
			return overrideDelegate.loadClass(name);
		else
			return super.loadClass(name);
	}

	protected Class loadClass(String name, boolean resolve)
		throws ClassNotFoundException
	{
		if (isEligibleForOverriding(name))
		{
			Class result = loadClassForOverriding(name);
			if (result != null)
			{
				if (resolve)
					resolveClass(result);
				return result;
			}
		}
		return super.loadClass(name, resolve);
	}

	protected boolean isEligibleForOverriding(String className)
	{
		return !isExcluded(className);
	}

	protected Class loadClassForOverriding(String name)
		throws ClassNotFoundException
	{
		Class result = findLoadedClass(name);
		if (result == null)
		{
			byte bytes[] = loadBytesForClass(name);
			if (bytes != null)
				result = defineClass(name, bytes, 0, bytes.length);
		}
		return result;
	}

	protected byte[] loadBytesForClass(String name)
		throws ClassNotFoundException
	{
		InputStream is;
		is = openStreamForClass(name);
		if (is == null)
			return null;
		byte bytes[] = FileCopyUtils.copyToByteArray(is);
		return transformIfNecessary(name, bytes);
		IOException ex;
		ex;
		throw new ClassNotFoundException((new StringBuilder()).append("Cannot load resource for class [").append(name).append("]").toString(), ex);
	}

	protected InputStream openStreamForClass(String name)
	{
		String internalName = (new StringBuilder()).append(name.replace('.', '/')).append(".class").toString();
		return getParent().getResourceAsStream(internalName);
	}

	protected byte[] transformIfNecessary(String name, byte bytes[])
	{
		return bytes;
	}

	static 
	{
		if (parallelCapableClassLoaderAvailable)
			ClassLoader.registerAsParallelCapable();
	}
}
