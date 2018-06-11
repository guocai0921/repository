// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotationMetadata.java

package org.springframework.core.type;

import java.util.Set;

// Referenced classes of package org.springframework.core.type:
//			ClassMetadata, AnnotatedTypeMetadata

public interface AnnotationMetadata
	extends ClassMetadata, AnnotatedTypeMetadata
{

	public abstract Set getAnnotationTypes();

	public abstract Set getMetaAnnotationTypes(String s);

	public abstract boolean hasAnnotation(String s);

	public abstract boolean hasMetaAnnotation(String s);

	public abstract boolean hasAnnotatedMethods(String s);

	public abstract Set getAnnotatedMethods(String s);
}
