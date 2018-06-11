// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Ordered.java

package org.springframework.core;


public interface Ordered
{

	public static final int HIGHEST_PRECEDENCE = 0x80000000;
	public static final int LOWEST_PRECEDENCE = 0x7fffffff;

	public abstract int getOrder();
}
