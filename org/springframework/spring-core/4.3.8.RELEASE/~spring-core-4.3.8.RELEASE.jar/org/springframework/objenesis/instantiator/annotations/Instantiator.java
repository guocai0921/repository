// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Instantiator.java

package org.springframework.objenesis.instantiator.annotations;

import java.lang.annotation.Annotation;

// Referenced classes of package org.springframework.objenesis.instantiator.annotations:
//			Typology

public interface Instantiator
	extends Annotation
{

	public abstract Typology value();
}
