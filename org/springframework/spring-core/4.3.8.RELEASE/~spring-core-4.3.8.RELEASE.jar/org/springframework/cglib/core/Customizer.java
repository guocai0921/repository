// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Customizer.java

package org.springframework.cglib.core;

import org.springframework.asm.Type;

// Referenced classes of package org.springframework.cglib.core:
//			KeyFactoryCustomizer, CodeEmitter

public interface Customizer
	extends KeyFactoryCustomizer
{

	public abstract void customize(CodeEmitter codeemitter, Type type);
}
