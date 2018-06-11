// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassesKey.java

package org.springframework.cglib.core;


// Referenced classes of package org.springframework.cglib.core:
//			KeyFactory

public class ClassesKey
{
	static interface Key
	{

		public abstract Object newInstance(Object aobj[]);
	}


	private static final Key FACTORY = (Key)KeyFactory.create(org/springframework/cglib/core/ClassesKey$Key);

	private ClassesKey()
	{
	}

	public static Object create(Object array[])
	{
		return FACTORY.newInstance(classNames(array));
	}

	private static String[] classNames(Object objects[])
	{
		if (objects == null)
			return null;
		String classNames[] = new String[objects.length];
		for (int i = 0; i < objects.length; i++)
		{
			Object object = objects[i];
			if (object != null)
			{
				Class aClass = object.getClass();
				classNames[i] = aClass != null ? aClass.getName() : null;
			}
		}

		return classNames;
	}

}
