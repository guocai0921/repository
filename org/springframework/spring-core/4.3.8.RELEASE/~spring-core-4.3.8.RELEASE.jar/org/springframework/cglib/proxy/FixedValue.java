// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FixedValue.java

package org.springframework.cglib.proxy;


// Referenced classes of package org.springframework.cglib.proxy:
//			Callback

public interface FixedValue
	extends Callback
{

	public abstract Object loadObject()
		throws Exception;
}
