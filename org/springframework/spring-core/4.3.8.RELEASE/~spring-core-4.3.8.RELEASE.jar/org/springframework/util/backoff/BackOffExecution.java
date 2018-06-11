// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BackOffExecution.java

package org.springframework.util.backoff;


public interface BackOffExecution
{

	public static final long STOP = -1L;

	public abstract long nextBackOff();
}
