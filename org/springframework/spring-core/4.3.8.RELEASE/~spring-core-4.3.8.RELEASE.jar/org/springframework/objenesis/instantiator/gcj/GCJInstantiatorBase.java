// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GCJInstantiatorBase.java

package org.springframework.objenesis.instantiator.gcj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

public abstract class GCJInstantiatorBase
	implements ObjectInstantiator
{
	private static class DummyStream extends ObjectInputStream
	{

		public DummyStream()
			throws IOException
		{
		}
	}


	static Method newObjectMethod = null;
	static ObjectInputStream dummyStream;
	protected final Class type;

	private static void initialize()
	{
		if (newObjectMethod == null)
			try
			{
				newObjectMethod = java/io/ObjectInputStream.getDeclaredMethod("newObject", new Class[] {
					java/lang/Class, java/lang/Class
				});
				newObjectMethod.setAccessible(true);
				dummyStream = new DummyStream();
			}
			catch (RuntimeException e)
			{
				throw new ObjenesisException(e);
			}
			catch (NoSuchMethodException e)
			{
				throw new ObjenesisException(e);
			}
			catch (IOException e)
			{
				throw new ObjenesisException(e);
			}
	}

	public GCJInstantiatorBase(Class type)
	{
		this.type = type;
		initialize();
	}

	public abstract Object newInstance();

}
