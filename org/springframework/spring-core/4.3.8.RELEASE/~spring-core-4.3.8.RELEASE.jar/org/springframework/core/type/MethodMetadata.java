// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodMetadata.java

package org.springframework.core.type;


// Referenced classes of package org.springframework.core.type:
//			AnnotatedTypeMetadata

public interface MethodMetadata
	extends AnnotatedTypeMetadata
{

	public abstract String getMethodName();

	public abstract String getDeclaringClassName();

	public abstract String getReturnTypeName();

	public abstract boolean isAbstract();

	public abstract boolean isStatic();

	public abstract boolean isFinal();

	public abstract boolean isOverridable();
}
