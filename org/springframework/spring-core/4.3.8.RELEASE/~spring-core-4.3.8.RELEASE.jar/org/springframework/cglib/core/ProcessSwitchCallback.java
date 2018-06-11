// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ProcessSwitchCallback.java

package org.springframework.cglib.core;

import org.springframework.asm.Label;

public interface ProcessSwitchCallback
{

	public abstract void processCase(int i, Label label)
		throws Exception;

	public abstract void processDefault()
		throws Exception;
}
