// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StdInstantiatorStrategy.java

package org.springframework.objenesis.strategy;

import java.io.Serializable;
import org.springframework.objenesis.instantiator.ObjectInstantiator;
import org.springframework.objenesis.instantiator.android.*;
import org.springframework.objenesis.instantiator.basic.AccessibleInstantiator;
import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.springframework.objenesis.instantiator.gcj.GCJInstantiator;
import org.springframework.objenesis.instantiator.perc.PercInstantiator;
import org.springframework.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;
import org.springframework.objenesis.instantiator.sun.UnsafeFactoryInstantiator;

// Referenced classes of package org.springframework.objenesis.strategy:
//			BaseInstantiatorStrategy, PlatformDescription

public class StdInstantiatorStrategy extends BaseInstantiatorStrategy
{

	public StdInstantiatorStrategy()
	{
	}

	public ObjectInstantiator newInstantiatorOf(Class type)
	{
		if (PlatformDescription.isThisJVM("Java HotSpot") || PlatformDescription.isThisJVM("OpenJDK"))
			if (PlatformDescription.isGoogleAppEngine())
			{
				if (java/io/Serializable.isAssignableFrom(type))
					return new ObjectInputStreamInstantiator(type);
				else
					return new AccessibleInstantiator(type);
			} else
			{
				return new SunReflectionFactoryInstantiator(type);
			}
		if (PlatformDescription.isThisJVM("Dalvik"))
		{
			if (PlatformDescription.isAndroidOpenJDK())
				return new UnsafeFactoryInstantiator(type);
			if (PlatformDescription.ANDROID_VERSION <= 10)
				return new Android10Instantiator(type);
			if (PlatformDescription.ANDROID_VERSION <= 17)
				return new Android17Instantiator(type);
			else
				return new Android18Instantiator(type);
		}
		if (PlatformDescription.isThisJVM("BEA"))
			return new SunReflectionFactoryInstantiator(type);
		if (PlatformDescription.isThisJVM("GNU libgcj"))
			return new GCJInstantiator(type);
		if (PlatformDescription.isThisJVM("PERC"))
			return new PercInstantiator(type);
		else
			return new UnsafeFactoryInstantiator(type);
	}
}
