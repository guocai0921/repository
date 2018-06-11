// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MultiValueMap.java

package org.springframework.util;

import java.util.Map;

public interface MultiValueMap
	extends Map
{

	public abstract Object getFirst(Object obj);

	public abstract void add(Object obj, Object obj1);

	public abstract void set(Object obj, Object obj1);

	public abstract void setAll(Map map);

	public abstract Map toSingleValueMap();
}
