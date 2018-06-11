// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SerializingInstantiatorStrategy.java

package org.springframework.objenesis.strategy;

import java.io.NotSerializableException;
import java.io.Serializable;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.springframework.objenesis.instantiator.basic.ObjectStreamClassInstantiator;
import org.springframework.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import org.springframework.objenesis.instantiator.perc.PercSerializationInstantiator;
import org.springframework.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;

// Referenced classes of package org.springframework.objenesis.strategy:
//			BaseInstantiatorStrategy, PlatformDescription

public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy
{

	public SerializingInstantiatorStrategy()
	{
	}

	public ObjectInstantiator newInstantiatorOf(Class type)
	{
		if (!java/io/Serializable.isAssignableFrom(type))
			throw new ObjenesisException(new NotSerializableException((new StringBuilder()).append(type).append(" not serializable").toString()));
		if (PlatformDescription.JVM_NAME.startsWith("Java HotSpot") || PlatformDescription.isThisJVM("OpenJDK"))
		{
			if (PlatformDescription.isGoogleAppEngine())
				return new ObjectInputStreamInstantiator(type);
			if (PlatformDescription.SPECIFICATION_VERSION.equals("9"))
				return new SunReflectionFactorySerializationInstantiator(type);
			else
				return new ObjectStreamClassInstantiator(type);
		}
		if (PlatformDescription.JVM_NAME.startsWith("Dalvik"))
			if (PlatformDescription.isAndroidOpenJDK())
				return new ObjectStreamClassInstantiator(type);
			else
				return new AndroidSerializationInstantiator(type);
		if (PlatformDescription.JVM_NAME.startsWith("GNU libgcj"))
			return new GCJSerializationInstantiator(type);
		if (PlatformDescription.JVM_NAME.startsWith("PERC"))
			return new PercSerializationInstantiator(type);
		else
			return new ObjectStreamClassInstantiator(type);
	}
}
