// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GeneratorStrategy.java

package org.springframework.cglib.core;


// Referenced classes of package org.springframework.cglib.core:
//			ClassGenerator

public interface GeneratorStrategy
{

	public abstract byte[] generate(ClassGenerator classgenerator)
		throws Exception;

	public abstract boolean equals(Object obj);
}
