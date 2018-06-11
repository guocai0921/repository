// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AssignableTypeFilter.java

package org.springframework.core.type.filter;

import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.type.filter:
//			AbstractTypeHierarchyTraversingFilter

public class AssignableTypeFilter extends AbstractTypeHierarchyTraversingFilter
{

	private final Class targetType;

	public AssignableTypeFilter(Class targetType)
	{
		super(true, true);
		this.targetType = targetType;
	}

	protected boolean matchClassName(String className)
	{
		return targetType.getName().equals(className);
	}

	protected Boolean matchSuperClass(String superClassName)
	{
		return matchTargetType(superClassName);
	}

	protected Boolean matchInterface(String interfaceName)
	{
		return matchTargetType(interfaceName);
	}

	protected Boolean matchTargetType(String typeName)
	{
		if (targetType.getName().equals(typeName))
			return Boolean.valueOf(true);
		if (java/lang/Object.getName().equals(typeName))
			return Boolean.valueOf(false);
		if (!typeName.startsWith("java"))
			break MISSING_BLOCK_LABEL_70;
		Class clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
		return Boolean.valueOf(targetType.isAssignableFrom(clazz));
		Throwable throwable;
		throwable;
		return null;
	}
}
