// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AliasFor.java

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;

public interface AliasFor
	extends Annotation
{

	public abstract String value();

	public abstract String attribute();

	public abstract Class annotation();
}
