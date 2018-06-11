// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertySourceFactory.java

package org.springframework.core.io.support;

import java.io.IOException;
import org.springframework.core.env.PropertySource;

// Referenced classes of package org.springframework.core.io.support:
//			EncodedResource

public interface PropertySourceFactory
{

	public abstract PropertySource createPropertySource(String s, EncodedResource encodedresource)
		throws IOException;
}
