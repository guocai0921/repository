// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodWrapper.java

package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package org.springframework.cglib.core:
//			ReflectUtils, KeyFactory

public class MethodWrapper
{
	public static interface MethodWrapperKey
	{

		public abstract Object newInstance(String s, String as[], String s1);
	}


	private static final MethodWrapperKey KEY_FACTORY = (MethodWrapperKey)KeyFactory.create(org/springframework/cglib/core/MethodWrapper$MethodWrapperKey);

	private MethodWrapper()
	{
	}

	public static Object create(Method method)
	{
		return KEY_FACTORY.newInstance(method.getName(), ReflectUtils.getNames(method.getParameterTypes()), method.getReturnType().getName());
	}

	public static Set createSet(Collection methods)
	{
		Set set = new HashSet();
		for (Iterator it = methods.iterator(); it.hasNext(); set.add(create((Method)it.next())));
		return set;
	}

}
