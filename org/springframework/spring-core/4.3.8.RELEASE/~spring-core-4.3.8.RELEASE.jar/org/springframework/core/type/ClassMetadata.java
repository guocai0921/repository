// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassMetadata.java

package org.springframework.core.type;


public interface ClassMetadata
{

	public abstract String getClassName();

	public abstract boolean isInterface();

	public abstract boolean isAnnotation();

	public abstract boolean isAbstract();

	public abstract boolean isConcrete();

	public abstract boolean isFinal();

	public abstract boolean isIndependent();

	public abstract boolean hasEnclosingClass();

	public abstract String getEnclosingClassName();

	public abstract boolean hasSuperClass();

	public abstract String getSuperClassName();

	public abstract String[] getInterfaceNames();

	public abstract String[] getMemberClassNames();
}
