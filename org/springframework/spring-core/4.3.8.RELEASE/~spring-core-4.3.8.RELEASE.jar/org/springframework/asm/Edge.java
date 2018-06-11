// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Edge.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Label

class Edge
{

	static final int NORMAL = 0;
	static final int EXCEPTION = 0x7fffffff;
	int info;
	Label successor;
	Edge next;

	Edge()
	{
	}
}
