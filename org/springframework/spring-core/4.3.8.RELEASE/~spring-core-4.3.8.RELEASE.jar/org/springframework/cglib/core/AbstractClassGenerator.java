// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractClassGenerator.java

package org.springframework.cglib.core;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.internal.Function;
import org.springframework.cglib.core.internal.LoadingCache;

// Referenced classes of package org.springframework.cglib.core:
//			ClassGenerator, DefaultGeneratorStrategy, DefaultNamingPolicy, NamingPolicy, 
//			CodeGenerationException, GeneratorStrategy, ClassNameReader, ReflectUtils, 
//			Predicate

public abstract class AbstractClassGenerator
	implements ClassGenerator
{
	protected static class Source
	{

		String name;

		public Source(String name)
		{
			this.name = name;
		}
	}

	protected static class ClassLoaderData
	{

		private final Set reservedClassNames = new HashSet();
		private final LoadingCache generatedClasses;
		private final WeakReference classLoader;
		private final Predicate uniqueNamePredicate = new Predicate() {

			final ClassLoaderData this$0;

			public boolean evaluate(Object name)
			{
				return reservedClassNames.contains(name);
			}

				
				{
					this.this$0 = ClassLoaderData.this;
					super();
				}
		};
		private static final Function GET_KEY = new Function() {

			public Object apply(AbstractClassGenerator gen)
			{
				return gen.key;
			}

			public volatile Object apply(Object obj)
			{
				return apply((AbstractClassGenerator)obj);
			}

		};

		public ClassLoader getClassLoader()
		{
			return (ClassLoader)classLoader.get();
		}

		public void reserveName(String name)
		{
			reservedClassNames.add(name);
		}

		public Predicate getUniqueNamePredicate()
		{
			return uniqueNamePredicate;
		}

		public Object get(AbstractClassGenerator gen, boolean useCache)
		{
			if (!useCache)
			{
				return gen.generate(this);
			} else
			{
				Object cachedValue = generatedClasses.get(gen);
				return gen.unwrapCachedValue(cachedValue);
			}
		}



		public ClassLoaderData(ClassLoader classLoader)
		{
			if (classLoader == null)
			{
				throw new IllegalArgumentException("classLoader == null is not yet supported");
			} else
			{
				this.classLoader = new WeakReference(classLoader);
				Function load = new Function() {

					final ClassLoaderData this$0;

					public Object apply(AbstractClassGenerator gen)
					{
						Class klass = gen.generate(ClassLoaderData.this);
						return gen.wrapCachedClass(klass);
					}

					public volatile Object apply(Object obj)
					{
						return apply((AbstractClassGenerator)obj);
					}

				
				{
					this.this$0 = ClassLoaderData.this;
					super();
				}
				};
				generatedClasses = new LoadingCache(GET_KEY, load);
				return;
			}
		}
	}


	private static final ThreadLocal CURRENT = new ThreadLocal();
	private static volatile Map CACHE = new WeakHashMap();
	private GeneratorStrategy strategy;
	private NamingPolicy namingPolicy;
	private Source source;
	private ClassLoader classLoader;
	private String namePrefix;
	private Object key;
	private boolean useCache;
	private String className;
	private boolean attemptLoad;

	protected Object wrapCachedClass(Class klass)
	{
		return new WeakReference(klass);
	}

	protected Object unwrapCachedValue(Object cached)
	{
		return ((WeakReference)cached).get();
	}

	protected AbstractClassGenerator(Source source)
	{
		strategy = DefaultGeneratorStrategy.INSTANCE;
		namingPolicy = DefaultNamingPolicy.INSTANCE;
		useCache = true;
		this.source = source;
	}

	protected void setNamePrefix(String namePrefix)
	{
		this.namePrefix = namePrefix;
	}

	protected final String getClassName()
	{
		return className;
	}

	private void setClassName(String className)
	{
		this.className = className;
	}

	private String generateClassName(Predicate nameTestPredicate)
	{
		return namingPolicy.getClassName(namePrefix, source.name, key, nameTestPredicate);
	}

	public void setClassLoader(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	public void setNamingPolicy(NamingPolicy namingPolicy)
	{
		if (namingPolicy == null)
			namingPolicy = DefaultNamingPolicy.INSTANCE;
		this.namingPolicy = namingPolicy;
	}

	public NamingPolicy getNamingPolicy()
	{
		return namingPolicy;
	}

	public void setUseCache(boolean useCache)
	{
		this.useCache = useCache;
	}

	public boolean getUseCache()
	{
		return useCache;
	}

	public void setAttemptLoad(boolean attemptLoad)
	{
		this.attemptLoad = attemptLoad;
	}

	public boolean getAttemptLoad()
	{
		return attemptLoad;
	}

	public void setStrategy(GeneratorStrategy strategy)
	{
		if (strategy == null)
			strategy = DefaultGeneratorStrategy.INSTANCE;
		this.strategy = strategy;
	}

	public GeneratorStrategy getStrategy()
	{
		return strategy;
	}

	public static AbstractClassGenerator getCurrent()
	{
		return (AbstractClassGenerator)CURRENT.get();
	}

	public ClassLoader getClassLoader()
	{
		ClassLoader t = classLoader;
		if (t == null)
			t = getDefaultClassLoader();
		if (t == null)
			t = getClass().getClassLoader();
		if (t == null)
			t = Thread.currentThread().getContextClassLoader();
		if (t == null)
			throw new IllegalStateException("Cannot determine classloader");
		else
			return t;
	}

	protected abstract ClassLoader getDefaultClassLoader();

	protected ProtectionDomain getProtectionDomain()
	{
		return null;
	}

	protected Object create(Object key)
	{
		Object obj;
		ClassLoader loader = getClassLoader();
		Map cache = CACHE;
		ClassLoaderData data = (ClassLoaderData)cache.get(loader);
		if (data == null)
			synchronized (org/springframework/cglib/core/AbstractClassGenerator)
			{
				cache = CACHE;
				data = (ClassLoaderData)cache.get(loader);
				if (data == null)
				{
					Map newCache = new WeakHashMap(cache);
					data = new ClassLoaderData(loader);
					newCache.put(loader, data);
					CACHE = newCache;
				}
			}
		this.key = key;
		obj = data.get(this, getUseCache());
		if (obj instanceof Class)
			return firstInstance((Class)obj);
		return nextInstance(obj);
		RuntimeException e;
		e;
		throw e;
		e;
		throw e;
		e;
		throw new CodeGenerationException(e);
	}

	protected Class generate(ClassLoaderData data)
	{
		Object save;
		save = CURRENT.get();
		CURRENT.set(this);
		ClassLoader classLoader;
		classLoader = data.getClassLoader();
		if (classLoader == null)
			throw new IllegalStateException((new StringBuilder()).append("ClassLoader is null while trying to define class ").append(getClassName()).append(". It seems that the loader has been expired from a weak reference somehow. Please file an issue at cglib's issue tracker.").toString());
		synchronized (classLoader)
		{
			String name = generateClassName(data.getUniqueNamePredicate());
			data.reserveName(name);
			setClassName(name);
		}
		if (!attemptLoad)
			break MISSING_BLOCK_LABEL_132;
		Class class1;
		Class gen = classLoader.loadClass(getClassName());
		class1 = gen;
		CURRENT.set(save);
		return class1;
		ClassNotFoundException classnotfoundexception;
		classnotfoundexception;
		Class class2;
		byte b[] = strategy.generate(this);
		String className = ClassNameReader.getClassName(new ClassReader(b));
		ProtectionDomain protectionDomain = getProtectionDomain();
		Class gen;
		synchronized (classLoader)
		{
			if (protectionDomain == null)
				gen = ReflectUtils.defineClass(className, b, classLoader);
			else
				gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain);
		}
		class2 = gen;
		CURRENT.set(save);
		return class2;
		RuntimeException e;
		e;
		throw e;
		e;
		throw e;
		e;
		throw new CodeGenerationException(e);
		Exception exception2;
		exception2;
		CURRENT.set(save);
		throw exception2;
	}

	protected abstract Object firstInstance(Class class1)
		throws Exception;

	protected abstract Object nextInstance(Object obj)
		throws Exception;


}
