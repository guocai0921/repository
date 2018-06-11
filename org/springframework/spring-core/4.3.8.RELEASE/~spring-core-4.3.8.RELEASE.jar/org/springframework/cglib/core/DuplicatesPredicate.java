// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DuplicatesPredicate.java

package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

// Referenced classes of package org.springframework.cglib.core:
//			Predicate, MethodWrapper

public class DuplicatesPredicate
	implements Predicate
{

	private Set unique;

	public DuplicatesPredicate()
	{
		unique = new HashSet();
	}

	public boolean evaluate(Object arg)
	{
		return unique.add(MethodWrapper.create((Method)arg));
	}
}
