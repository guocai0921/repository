// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LocalVariablesSorter.java

package org.springframework.cglib.core;

import org.springframework.asm.*;

public class LocalVariablesSorter extends MethodVisitor
{
	private static class State
	{

		int mapping[];
		int nextLocal;

		private State()
		{
			mapping = new int[40];
		}

	}


	protected final int firstLocal;
	private final State state;

	public LocalVariablesSorter(int access, String desc, MethodVisitor mv)
	{
		super(0x50000, mv);
		state = new State();
		Type args[] = Type.getArgumentTypes(desc);
		state.nextLocal = (8 & access) == 0 ? 1 : 0;
		for (int i = 0; i < args.length; i++)
			state.nextLocal += args[i].getSize();

		firstLocal = state.nextLocal;
	}

	public LocalVariablesSorter(LocalVariablesSorter lvs)
	{
		super(0x50000, lvs.mv);
		state = lvs.state;
		firstLocal = lvs.firstLocal;
	}

	public void visitVarInsn(int opcode, int var)
	{
		int size;
		switch (opcode)
		{
		case 22: // '\026'
		case 24: // '\030'
		case 55: // '7'
		case 57: // '9'
			size = 2;
			break;

		default:
			size = 1;
			break;
		}
		mv.visitVarInsn(opcode, remap(var, size));
	}

	public void visitIincInsn(int var, int increment)
	{
		mv.visitIincInsn(remap(var, 1), increment);
	}

	public void visitMaxs(int maxStack, int maxLocals)
	{
		mv.visitMaxs(maxStack, state.nextLocal);
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		mv.visitLocalVariable(name, desc, signature, start, end, remap(index));
	}

	protected int newLocal(int size)
	{
		int var = state.nextLocal;
		state.nextLocal += size;
		return var;
	}

	private int remap(int var, int size)
	{
		if (var < firstLocal)
			return var;
		int key = (2 * var + size) - 1;
		int length = state.mapping.length;
		if (key >= length)
		{
			int newMapping[] = new int[Math.max(2 * length, key + 1)];
			System.arraycopy(state.mapping, 0, newMapping, 0, length);
			state.mapping = newMapping;
		}
		int value = state.mapping[key];
		if (value == 0)
		{
			value = state.nextLocal + 1;
			state.mapping[key] = value;
			state.nextLocal += size;
		}
		return value - 1;
	}

	private int remap(int var)
	{
		if (var < firstLocal)
			return var;
		int key = 2 * var;
		int value = key >= state.mapping.length ? 0 : state.mapping[key];
		if (value == 0)
			value = key + 1 >= state.mapping.length ? 0 : state.mapping[key + 1];
		if (value == 0)
			throw new IllegalStateException((new StringBuilder()).append("Unknown local variable ").append(var).toString());
		else
			return value - 1;
	}
}
