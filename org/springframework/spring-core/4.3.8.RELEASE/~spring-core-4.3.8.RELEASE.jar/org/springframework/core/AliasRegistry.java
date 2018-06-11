// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AliasRegistry.java

package org.springframework.core;


public interface AliasRegistry
{

	public abstract void registerAlias(String s, String s1);

	public abstract void removeAlias(String s);

	public abstract boolean isAlias(String s);

	public abstract String[] getAliases(String s);
}
