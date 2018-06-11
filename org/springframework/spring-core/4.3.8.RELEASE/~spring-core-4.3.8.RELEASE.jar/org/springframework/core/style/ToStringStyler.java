// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ToStringStyler.java

package org.springframework.core.style;


public interface ToStringStyler
{

	public abstract void styleStart(StringBuilder stringbuilder, Object obj);

	public abstract void styleEnd(StringBuilder stringbuilder, Object obj);

	public abstract void styleField(StringBuilder stringbuilder, String s, Object obj);

	public abstract void styleValue(StringBuilder stringbuilder, Object obj);

	public abstract void styleFieldSeparator(StringBuilder stringbuilder);
}
