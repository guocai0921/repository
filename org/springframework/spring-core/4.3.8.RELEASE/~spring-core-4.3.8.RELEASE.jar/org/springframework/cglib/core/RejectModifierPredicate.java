// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RejectModifierPredicate.java

package org.springframework.cglib.core;

import java.lang.reflect.Member;

// Referenced classes of package org.springframework.cglib.core:
//			Predicate

public class RejectModifierPredicate
	implements Predicate
{

	private int rejectMask;

	public RejectModifierPredicate(int rejectMask)
	{
		this.rejectMask = rejectMask;
	}

	public boolean evaluate(Object arg)
	{
		return (((Member)arg).getModifiers() & rejectMask) == 0;
	}
}
