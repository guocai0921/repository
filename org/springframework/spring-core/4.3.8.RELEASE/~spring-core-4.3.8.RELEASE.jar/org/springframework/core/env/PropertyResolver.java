// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertyResolver.java

package org.springframework.core.env;


public interface PropertyResolver
{

	public abstract boolean containsProperty(String s);

	public abstract String getProperty(String s);

	public abstract String getProperty(String s, String s1);

	public abstract Object getProperty(String s, Class class1);

	public abstract Object getProperty(String s, Class class1, Object obj);

	/**
	 * @deprecated Method getPropertyAsClass is deprecated
	 */

	public abstract Class getPropertyAsClass(String s, Class class1);

	public abstract String getRequiredProperty(String s)
		throws IllegalStateException;

	public abstract Object getRequiredProperty(String s, Class class1)
		throws IllegalStateException;

	public abstract String resolvePlaceholders(String s);

	public abstract String resolveRequiredPlaceholders(String s)
		throws IllegalArgumentException;
}
