// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultSerializer.java

package org.springframework.core.serializer;

import java.io.*;

// Referenced classes of package org.springframework.core.serializer:
//			Serializer

public class DefaultSerializer
	implements Serializer
{

	public DefaultSerializer()
	{
	}

	public void serialize(Object object, OutputStream outputStream)
		throws IOException
	{
		if (!(object instanceof Serializable))
		{
			throw new IllegalArgumentException((new StringBuilder()).append(getClass().getSimpleName()).append(" requires a Serializable payload but received an object of type [").append(object.getClass().getName()).append("]").toString());
		} else
		{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			return;
		}
	}
}
