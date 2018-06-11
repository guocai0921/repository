// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MetadataReaderFactory.java

package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

// Referenced classes of package org.springframework.core.type.classreading:
//			MetadataReader

public interface MetadataReaderFactory
{

	public abstract MetadataReader getMetadataReader(String s)
		throws IOException;

	public abstract MetadataReader getMetadataReader(Resource resource)
		throws IOException;
}
