// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InterceptFieldEnabled.java

package org.springframework.cglib.transform.impl;


// Referenced classes of package org.springframework.cglib.transform.impl:
//			InterceptFieldCallback

public interface InterceptFieldEnabled
{

	public abstract void setInterceptFieldCallback(InterceptFieldCallback interceptfieldcallback);

	public abstract InterceptFieldCallback getInterceptFieldCallback();
}
