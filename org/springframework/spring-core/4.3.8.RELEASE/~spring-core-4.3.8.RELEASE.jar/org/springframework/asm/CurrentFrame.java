// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CurrentFrame.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Frame, Label, ClassWriter, Item

class CurrentFrame extends Frame
{

	CurrentFrame()
	{
	}

	void execute(int opcode, int arg, ClassWriter cw, Item item)
	{
		super.execute(opcode, arg, cw, item);
		Frame successor = new Frame();
		merge(cw, successor, 0);
		set(successor);
		owner.inputStackTop = 0;
	}
}
