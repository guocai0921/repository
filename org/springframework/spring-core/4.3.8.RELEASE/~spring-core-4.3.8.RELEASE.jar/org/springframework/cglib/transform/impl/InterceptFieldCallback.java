// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterceptFieldCallback.java

package org.springframework.cglib.transform.impl;


public interface InterceptFieldCallback
{

	public abstract int writeInt(Object obj, String s, int i, int j);

	public abstract char writeChar(Object obj, String s, char c, char c1);

	public abstract byte writeByte(Object obj, String s, byte byte0, byte byte1);

	public abstract boolean writeBoolean(Object obj, String s, boolean flag, boolean flag1);

	public abstract short writeShort(Object obj, String s, short word0, short word1);

	public abstract float writeFloat(Object obj, String s, float f, float f1);

	public abstract double writeDouble(Object obj, String s, double d, double d1);

	public abstract long writeLong(Object obj, String s, long l, long l1);

	public abstract Object writeObject(Object obj, String s, Object obj1, Object obj2);

	public abstract int readInt(Object obj, String s, int i);

	public abstract char readChar(Object obj, String s, char c);

	public abstract byte readByte(Object obj, String s, byte byte0);

	public abstract boolean readBoolean(Object obj, String s, boolean flag);

	public abstract short readShort(Object obj, String s, short word0);

	public abstract float readFloat(Object obj, String s, float f);

	public abstract double readDouble(Object obj, String s, double d);

	public abstract long readLong(Object obj, String s, long l);

	public abstract Object readObject(Object obj, String s, Object obj1);
}
