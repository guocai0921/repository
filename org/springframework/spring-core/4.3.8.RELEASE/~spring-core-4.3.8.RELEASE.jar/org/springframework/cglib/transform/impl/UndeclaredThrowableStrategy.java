// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UndeclaredThrowableStrategy.java

package org.springframework.cglib.transform.impl;

import org.springframework.cglib.core.*;
import org.springframework.cglib.transform.*;

// Referenced classes of package org.springframework.cglib.transform.impl:
//			UndeclaredThrowableTransformer

public class UndeclaredThrowableStrategy extends DefaultGeneratorStrategy
{

	private Class wrapper;
	private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() {

		public boolean accept(int access, String name, String desc, String signature, String exceptions[])
		{
			return !TypeUtils.isPrivate(access) && name.indexOf('$') < 0;
		}

	};

	public UndeclaredThrowableStrategy(Class wrapper)
	{
		this.wrapper = wrapper;
	}

	protected ClassGenerator transform(ClassGenerator cg)
		throws Exception
	{
		org.springframework.cglib.transform.ClassTransformer tr = new UndeclaredThrowableTransformer(wrapper);
		tr = new MethodFilterTransformer(TRANSFORM_FILTER, tr);
		return new TransformingClassGenerator(cg, tr);
	}

}
