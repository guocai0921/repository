// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Block.java

package org.springframework.cglib.core;

import org.springframework.asm.Label;

// Referenced classes of package org.springframework.cglib.core:
//			CodeEmitter

public class Block
{

	private CodeEmitter e;
	private Label start;
	private Label end;

	public Block(CodeEmitter e)
	{
		this.e = e;
		start = e.mark();
	}

	public CodeEmitter getCodeEmitter()
	{
		return e;
	}

	public void end()
	{
		if (end != null)
		{
			throw new IllegalStateException("end of label already set");
		} else
		{
			end = e.mark();
			return;
		}
	}

	public Label getStart()
	{
		return start;
	}

	public Label getEnd()
	{
		return end;
	}
}
