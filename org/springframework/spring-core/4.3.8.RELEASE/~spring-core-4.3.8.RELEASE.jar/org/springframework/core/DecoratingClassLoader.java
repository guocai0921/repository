// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DecoratingClassLoader.java

package org.springframework.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public abstract class DecoratingClassLoader extends ClassLoader
{

	protected static final boolean parallelCapableClassLoaderAvailable;
	private final Set excludedPackages;
	private final Set excludedClasses;

	public DecoratingClassLoader()
	{
		excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap(8));
		excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap(8));
	}

	public DecoratingClassLoader(ClassLoader parent)
	{
		super(parent);
		excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap(8));
		excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap(8));
	}

	public void excludePackage(String packageName)
	{
		Assert.notNull(packageName, "Package name must not be null");
		excludedPackages.add(packageName);
	}

	public void excludeClass(String className)
	{
		Assert.notNull(className, "Class name must not be null");
		excludedClasses.add(className);
	}

	protected boolean isExcluded(String className)
	{
		if (excludedClasses.contains(className))
			return true;
		for (Iterator iterator = excludedPackages.iterator(); iterator.hasNext();)
		{
			String packageName = (String)iterator.next();
			if (className.startsWith(packageName))
				return true;
		}

		return false;
	}

	static 
	{
		parallelCapableClassLoaderAvailable = ClassUtils.hasMethod(java/lang/ClassLoader, "registerAsParallelCapable", new Class[0]);
		if (parallelCapableClassLoaderAvailable)
			ClassLoader.registerAsParallelCapable();
	}
}
