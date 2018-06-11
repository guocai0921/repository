// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassGenerator.java

package org.springframework.cglib.core;

import org.springframework.asm.ClassVisitor;

public interface ClassGenerator
{

	public abstract void generateClass(ClassVisitor classvisitor)
		throws Exception;
}
