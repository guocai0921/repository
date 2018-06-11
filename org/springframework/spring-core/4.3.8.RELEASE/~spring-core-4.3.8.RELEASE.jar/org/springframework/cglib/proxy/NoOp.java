// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NoOp.java

package org.springframework.cglib.proxy;


// Referenced classes of package org.springframework.cglib.proxy:
//			Callback

public interface NoOp
	extends Callback
{

	public static final NoOp INSTANCE = new NoOp() {

	};

}
