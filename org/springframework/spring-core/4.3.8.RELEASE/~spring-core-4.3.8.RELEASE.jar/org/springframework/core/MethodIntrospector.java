// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodIntrospector.java

package org.springframework.core;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

// Referenced classes of package org.springframework.core:
//			BridgeMethodResolver

public abstract class MethodIntrospector
{
	public static interface MetadataLookup
	{

		public abstract Object inspect(Method method);
	}


	public MethodIntrospector()
	{
	}

	public static Map selectMethods(Class targetType, MetadataLookup metadataLookup)
	{
		Map methodMap = new LinkedHashMap();
		Set handlerTypes = new LinkedHashSet();
		Class specificHandlerType = null;
		if (!Proxy.isProxyClass(targetType))
		{
			handlerTypes.add(targetType);
			specificHandlerType = targetType;
		}
		handlerTypes.addAll(Arrays.asList(targetType.getInterfaces()));
		Class currentHandlerType;
		Class targetClass;
		for (Iterator iterator = handlerTypes.iterator(); iterator.hasNext(); ReflectionUtils.doWithMethods(currentHandlerType, new org.springframework.util.ReflectionUtils.MethodCallback(targetClass, metadataLookup, methodMap) {

		final Class val$targetClass;
		final MetadataLookup val$metadataLookup;
		final Map val$methodMap;

		public void doWith(Method method)
		{
			Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
			Object result = metadataLookup.inspect(specificMethod);
			if (result != null)
			{
				Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
				if (bridgedMethod == specificMethod || metadataLookup.inspect(bridgedMethod) == null)
					methodMap.put(specificMethod, result);
			}
		}

			
			{
				targetClass = class1;
				metadataLookup = metadatalookup;
				methodMap = map;
				super();
			}
	}, ReflectionUtils.USER_DECLARED_METHODS))
		{
			currentHandlerType = (Class)iterator.next();
			targetClass = specificHandlerType == null ? currentHandlerType : specificHandlerType;
		}

		return methodMap;
	}

	public static Set selectMethods(Class targetType, org.springframework.util.ReflectionUtils.MethodFilter methodFilter)
	{
		return selectMethods(targetType, new MetadataLookup(methodFilter) {

			final org.springframework.util.ReflectionUtils.MethodFilter val$methodFilter;

			public Boolean inspect(Method method)
			{
				return methodFilter.matches(method) ? Boolean.TRUE : null;
			}

			public volatile Object inspect(Method method)
			{
				return inspect(method);
			}

			
			{
				methodFilter = methodfilter;
				super();
			}
		}).keySet();
	}

	public static Method selectInvocableMethod(Method method, Class targetType)
	{
		if (method.getDeclaringClass().isAssignableFrom(targetType))
			return method;
		String methodName;
		Class parameterTypes[];
		Class aclass[];
		int i;
		int j;
		methodName = method.getName();
		parameterTypes = method.getParameterTypes();
		aclass = targetType.getInterfaces();
		i = aclass.length;
		j = 0;
_L1:
		Class ifc;
		if (j >= i)
			break MISSING_BLOCK_LABEL_67;
		ifc = aclass[j];
		return ifc.getMethod(methodName, parameterTypes);
		NoSuchMethodException nosuchmethodexception;
		nosuchmethodexception;
		j++;
		  goto _L1
		return targetType.getMethod(methodName, parameterTypes);
		NoSuchMethodException ex;
		ex;
		throw new IllegalStateException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in any interface(s) of the exposed proxy type. Either pull the method up to an interface or switch to CGLIB proxies by enforcing proxy-target-class mode in your configuration.", new Object[] {
			method.getName(), method.getDeclaringClass().getSimpleName()
		}));
	}
}
