// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Factory.java

package org.springframework.cglib.proxy;


// Referenced classes of package org.springframework.cglib.proxy:
//			Callback

public interface Factory
{

	public abstract Object newInstance(Callback callback);

	public abstract Object newInstance(Callback acallback[]);

	public abstract Object newInstance(Class aclass[], Object aobj[], Callback acallback[]);

	public abstract Callback getCallback(int i);

	public abstract void setCallback(int i, Callback callback);

	public abstract void setCallbacks(Callback acallback[]);

	public abstract Callback[] getCallbacks();
}
