// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterceptFieldFilter.java

package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

public interface InterceptFieldFilter
{

	public abstract boolean acceptRead(Type type, String s);

	public abstract boolean acceptWrite(Type type, String s);
}
