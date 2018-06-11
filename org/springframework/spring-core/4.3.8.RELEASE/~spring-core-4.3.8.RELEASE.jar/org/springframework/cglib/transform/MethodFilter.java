// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodFilter.java

package org.springframework.cglib.transform;


public interface MethodFilter
{

	public abstract boolean accept(int i, String s, String s1, String s2, String as[]);
}
