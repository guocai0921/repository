// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StandardClassMetadata.java

package org.springframework.core.type;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.type:
//			ClassMetadata

public class StandardClassMetadata
	implements ClassMetadata
{

	private final Class introspectedClass;

	public StandardClassMetadata(Class introspectedClass)
	{
		Assert.notNull(introspectedClass, "Class must not be null");
		this.introspectedClass = introspectedClass;
	}

	public final Class getIntrospectedClass()
	{
		return introspectedClass;
	}

	public String getClassName()
	{
		return introspectedClass.getName();
	}

	public boolean isInterface()
	{
		return introspectedClass.isInterface();
	}

	public boolean isAnnotation()
	{
		return introspectedClass.isAnnotation();
	}

	public boolean isAbstract()
	{
		return Modifier.isAbstract(introspectedClass.getModifiers());
	}

	public boolean isConcrete()
	{
		return !isInterface() && !isAbstract();
	}

	public boolean isFinal()
	{
		return Modifier.isFinal(introspectedClass.getModifiers());
	}

	public boolean isIndependent()
	{
		return !hasEnclosingClass() || introspectedClass.getDeclaringClass() != null && Modifier.isStatic(introspectedClass.getModifiers());
	}

	public boolean hasEnclosingClass()
	{
		return introspectedClass.getEnclosingClass() != null;
	}

	public String getEnclosingClassName()
	{
		Class enclosingClass = introspectedClass.getEnclosingClass();
		return enclosingClass == null ? null : enclosingClass.getName();
	}

	public boolean hasSuperClass()
	{
		return introspectedClass.getSuperclass() != null;
	}

	public String getSuperClassName()
	{
		Class superClass = introspectedClass.getSuperclass();
		return superClass == null ? null : superClass.getName();
	}

	public String[] getInterfaceNames()
	{
		Class ifcs[] = introspectedClass.getInterfaces();
		String ifcNames[] = new String[ifcs.length];
		for (int i = 0; i < ifcs.length; i++)
			ifcNames[i] = ifcs[i].getName();

		return ifcNames;
	}

	public String[] getMemberClassNames()
	{
		LinkedHashSet memberClassNames = new LinkedHashSet();
		Class aclass[] = introspectedClass.getDeclaredClasses();
		int i = aclass.length;
		for (int j = 0; j < i; j++)
		{
			Class nestedClass = aclass[j];
			memberClassNames.add(nestedClass.getName());
		}

		return (String[])memberClassNames.toArray(new String[memberClassNames.size()]);
	}
}
