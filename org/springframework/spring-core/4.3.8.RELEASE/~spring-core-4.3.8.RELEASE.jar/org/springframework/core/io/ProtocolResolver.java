// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ProtocolResolver.java

package org.springframework.core.io;


// Referenced classes of package org.springframework.core.io:
//			ResourceLoader, Resource

public interface ProtocolResolver
{

	public abstract Resource resolve(String s, ResourceLoader resourceloader);
}
