// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AttributeAccessor.java

package org.springframework.core;


public interface AttributeAccessor
{

	public abstract void setAttribute(String s, Object obj);

	public abstract Object getAttribute(String s);

	public abstract Object removeAttribute(String s);

	public abstract boolean hasAttribute(String s);

	public abstract String[] attributeNames();
}
