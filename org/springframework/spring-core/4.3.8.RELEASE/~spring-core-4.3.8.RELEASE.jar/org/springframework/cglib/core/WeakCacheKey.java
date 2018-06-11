// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   WeakCacheKey.java

package org.springframework.cglib.core;

import java.lang.ref.WeakReference;

public class WeakCacheKey extends WeakReference
{

	private final int hash;

	public WeakCacheKey(Object referent)
	{
		super(referent);
		hash = referent.hashCode();
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof WeakCacheKey))
		{
			return false;
		} else
		{
			Object ours = get();
			Object theirs = ((WeakCacheKey)obj).get();
			return ours != null && theirs != null && ours.equals(theirs);
		}
	}

	public int hashCode()
	{
		return hash;
	}

	public String toString()
	{
		Object t = get();
		return t != null ? t.toString() : (new StringBuilder()).append("Clean WeakIdentityKey, hash: ").append(hash).toString();
	}
}
