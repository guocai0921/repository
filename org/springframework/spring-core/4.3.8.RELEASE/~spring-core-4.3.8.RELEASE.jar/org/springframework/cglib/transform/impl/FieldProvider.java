// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldProvider.java

package org.springframework.cglib.transform.impl;


public interface FieldProvider
{

	public abstract String[] getFieldNames();

	public abstract Class[] getFieldTypes();

	public abstract void setField(int i, Object obj);

	public abstract Object getField(int i);

	public abstract void setField(String s, Object obj);

	public abstract Object getField(String s);
}
