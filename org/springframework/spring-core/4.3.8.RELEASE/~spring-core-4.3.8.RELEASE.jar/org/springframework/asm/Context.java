// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Context.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Attribute, Label, TypePath

class Context
{

	Attribute attrs[];
	int flags;
	char buffer[];
	int bootstrapMethods[];
	int access;
	String name;
	String desc;
	Label labels[];
	int typeRef;
	TypePath typePath;
	int offset;
	Label start[];
	Label end[];
	int index[];
	int mode;
	int localCount;
	int localDiff;
	Object local[];
	int stackCount;
	Object stack[];

	Context()
	{
	}
}
