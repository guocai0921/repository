// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AnnotatedTypeMetadata.java

package org.springframework.core.type;

import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface AnnotatedTypeMetadata
{

	public abstract boolean isAnnotated(String s);

	public abstract Map getAnnotationAttributes(String s);

	public abstract Map getAnnotationAttributes(String s, boolean flag);

	public abstract MultiValueMap getAllAnnotationAttributes(String s);

	public abstract MultiValueMap getAllAnnotationAttributes(String s, boolean flag);
}
