// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AccessibleInstantiator.java

package org.springframework.objenesis.instantiator.basic;

import java.lang.reflect.Constructor;

// Referenced classes of package org.springframework.objenesis.instantiator.basic:
//			ConstructorInstantiator

public class AccessibleInstantiator extends ConstructorInstantiator
{

	public AccessibleInstantiator(Class type)
	{
		super(type);
		if (constructor != null)
			constructor.setAccessible(true);
	}
}
