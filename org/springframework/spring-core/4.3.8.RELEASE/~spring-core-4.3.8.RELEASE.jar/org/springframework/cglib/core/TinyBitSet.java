// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TinyBitSet.java

package org.springframework.cglib.core;


/**
 * @deprecated Class TinyBitSet is deprecated
 */

public class TinyBitSet
{

	private static int T[];
	private int value;

	public TinyBitSet()
	{
		value = 0;
	}

	private static int gcount(int x)
	{
		int c = 0;
		for (; x != 0; x &= x - 1)
			c++;

		return c;
	}

	private static int topbit(int i)
	{
		int j = 0;
		for (; i != 0; i ^= j)
			j = i & -i;

		return j;
	}

	private static int log2(int i)
	{
		int j = 0;
		j = 0;
		for (; i != 0; i >>= 1)
			j++;

		return j;
	}

	public int length()
	{
		return log2(topbit(value));
	}

	public int cardinality()
	{
		int w = value;
		int c = 0;
		for (; w != 0; w >>= 8)
			c += T[w & 0xff];

		return c;
	}

	public boolean get(int index)
	{
		return (value & 1 << index) != 0;
	}

	public void set(int index)
	{
		value |= 1 << index;
	}

	public void clear(int index)
	{
		value &= ~(1 << index);
	}

	static 
	{
		T = new int[256];
		for (int j = 0; j < 256; j++)
			T[j] = gcount(j);

	}
}
