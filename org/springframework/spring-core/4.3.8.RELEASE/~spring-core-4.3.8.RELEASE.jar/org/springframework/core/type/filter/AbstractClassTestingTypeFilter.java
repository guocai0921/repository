// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractClassTestingTypeFilter.java

package org.springframework.core.type.filter;

import java.io.IOException;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

// Referenced classes of package org.springframework.core.type.filter:
//			TypeFilter

public abstract class AbstractClassTestingTypeFilter
	implements TypeFilter
{

	public AbstractClassTestingTypeFilter()
	{
	}

	public final boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
		throws IOException
	{
		return match(metadataReader.getClassMetadata());
	}

	protected abstract boolean match(ClassMetadata classmetadata);
}
