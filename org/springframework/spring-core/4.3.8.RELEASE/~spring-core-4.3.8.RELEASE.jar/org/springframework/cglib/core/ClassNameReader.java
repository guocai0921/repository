// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassNameReader.java

package org.springframework.cglib.core;

import java.util.ArrayList;
import java.util.List;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;

public class ClassNameReader
{
	private static class EarlyExitException extends RuntimeException
	{

		private EarlyExitException()
		{
		}

	}


	private static final EarlyExitException EARLY_EXIT = new EarlyExitException();

	private ClassNameReader()
	{
	}

	public static String getClassName(ClassReader r)
	{
		return getClassInfo(r)[0];
	}

	public static String[] getClassInfo(ClassReader r)
	{
		List array = new ArrayList();
		try
		{
			r.accept(new ClassVisitor(0x50000, null, array) {

				final List val$array;

				public void visit(int version, int access, String name, String signature, String superName, String interfaces[])
				{
					array.add(name.replace('/', '.'));
					if (superName != null)
						array.add(superName.replace('/', '.'));
					for (int i = 0; i < interfaces.length; i++)
						array.add(interfaces[i].replace('/', '.'));

					throw ClassNameReader.EARLY_EXIT;
				}

			
			{
				array = list;
				super(x0, x1);
			}
			}, 6);
		}
		catch (EarlyExitException earlyexitexception) { }
		return (String[])(String[])array.toArray(new String[0]);
	}


}
