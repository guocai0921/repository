// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FixedKeySet.java

package org.springframework.cglib.beans;

import java.util.*;

public class FixedKeySet extends AbstractSet
{

	private Set set;
	private int size;

	public FixedKeySet(String keys[])
	{
		size = keys.length;
		set = Collections.unmodifiableSet(new HashSet(Arrays.asList(keys)));
	}

	public Iterator iterator()
	{
		return set.iterator();
	}

	public int size()
	{
		return size;
	}
}
