// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Objenesis.java

package org.springframework.objenesis;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

public interface Objenesis
{

	public abstract Object newInstance(Class class1);

	public abstract ObjectInstantiator getInstantiatorOf(Class class1);
}
