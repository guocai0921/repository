// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VisibilityPredicate.java

package org.springframework.cglib.core;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.springframework.asm.Type;

// Referenced classes of package org.springframework.cglib.core:
//			Predicate, TypeUtils

public class VisibilityPredicate
	implements Predicate
{

	private boolean protectedOk;
	private String pkg;
	private boolean samePackageOk;

	public VisibilityPredicate(Class source, boolean protectedOk)
	{
		this.protectedOk = protectedOk;
		samePackageOk = source.getClassLoader() != null;
		pkg = TypeUtils.getPackageName(Type.getType(source));
	}

	public boolean evaluate(Object arg)
	{
		Member member = (Member)arg;
		int mod = member.getModifiers();
		if (Modifier.isPrivate(mod))
			return false;
		if (Modifier.isPublic(mod))
			return true;
		if (Modifier.isProtected(mod) && protectedOk)
			return true;
		else
			return samePackageOk && pkg.equals(TypeUtils.getPackageName(Type.getType(member.getDeclaringClass())));
	}
}
