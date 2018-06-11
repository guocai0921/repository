// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NamingPolicy.java

package org.springframework.cglib.core;


// Referenced classes of package org.springframework.cglib.core:
//			Predicate

public interface NamingPolicy
{

	public abstract String getClassName(String s, String s1, Object obj, Predicate predicate);

	public abstract boolean equals(Object obj);
}
