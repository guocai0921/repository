// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SpringFactoriesLoader.java

package org.springframework.core.io.support;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.io.support:
//			PropertiesLoaderUtils

public abstract class SpringFactoriesLoader
{

	private static final Log logger = LogFactory.getLog(org/springframework/core/io/support/SpringFactoriesLoader);
	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

	public SpringFactoriesLoader()
	{
	}

	public static List loadFactories(Class factoryClass, ClassLoader classLoader)
	{
		Assert.notNull(factoryClass, "'factoryClass' must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null)
			classLoaderToUse = org/springframework/core/io/support/SpringFactoriesLoader.getClassLoader();
		List factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
		if (logger.isTraceEnabled())
			logger.trace((new StringBuilder()).append("Loaded [").append(factoryClass.getName()).append("] names: ").append(factoryNames).toString());
		List result = new ArrayList(factoryNames.size());
		String factoryName;
		for (Iterator iterator = factoryNames.iterator(); iterator.hasNext(); result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse)))
			factoryName = (String)iterator.next();

		AnnotationAwareOrderComparator.sort(result);
		return result;
	}

	public static List loadFactoryNames(Class factoryClass, ClassLoader classLoader)
	{
		String factoryClassName = factoryClass.getName();
		List result;
		Enumeration urls = classLoader == null ? ClassLoader.getSystemResources("META-INF/spring.factories") : classLoader.getResources("META-INF/spring.factories");
		result = new ArrayList();
		String factoryClassNames;
		for (; urls.hasMoreElements(); result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames))))
		{
			URL url = (URL)urls.nextElement();
			Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
			factoryClassNames = properties.getProperty(factoryClassName);
		}

		return result;
		IOException ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Unable to load [").append(factoryClass.getName()).append("] factories from location [").append("META-INF/spring.factories").append("]").toString(), ex);
	}

	private static Object instantiateFactory(String instanceClassName, Class factoryClass, ClassLoader classLoader)
	{
		Constructor constructor;
		Class instanceClass = ClassUtils.forName(instanceClassName, classLoader);
		if (!factoryClass.isAssignableFrom(instanceClass))
			throw new IllegalArgumentException((new StringBuilder()).append("Class [").append(instanceClassName).append("] is not assignable to [").append(factoryClass.getName()).append("]").toString());
		constructor = instanceClass.getDeclaredConstructor(new Class[0]);
		ReflectionUtils.makeAccessible(constructor);
		return constructor.newInstance(new Object[0]);
		Throwable ex;
		ex;
		throw new IllegalArgumentException((new StringBuilder()).append("Unable to instantiate factory class: ").append(factoryClass.getName()).toString(), ex);
	}

}
