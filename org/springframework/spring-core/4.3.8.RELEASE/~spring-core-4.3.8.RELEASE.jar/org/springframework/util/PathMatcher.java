// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PathMatcher.java

package org.springframework.util;

import java.util.Comparator;
import java.util.Map;

public interface PathMatcher
{

	public abstract boolean isPattern(String s);

	public abstract boolean match(String s, String s1);

	public abstract boolean matchStart(String s, String s1);

	public abstract String extractPathWithinPattern(String s, String s1);

	public abstract Map extractUriTemplateVariables(String s, String s1);

	public abstract Comparator getPatternComparator(String s);

	public abstract String combine(String s, String s1);
}
