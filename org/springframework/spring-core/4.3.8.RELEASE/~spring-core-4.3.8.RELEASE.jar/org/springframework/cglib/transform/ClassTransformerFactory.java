// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassTransformerFactory.java

package org.springframework.cglib.transform;


// Referenced classes of package org.springframework.cglib.transform:
//			ClassTransformer

public interface ClassTransformerFactory
{

	public abstract ClassTransformer newInstance();
}
