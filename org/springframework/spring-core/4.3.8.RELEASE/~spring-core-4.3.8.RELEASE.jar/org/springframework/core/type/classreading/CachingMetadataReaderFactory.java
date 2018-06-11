// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CachingMetadataReaderFactory.java

package org.springframework.core.type.classreading;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

// Referenced classes of package org.springframework.core.type.classreading:
//			SimpleMetadataReaderFactory, MetadataReader

public class CachingMetadataReaderFactory extends SimpleMetadataReaderFactory
{

	public static final int DEFAULT_CACHE_LIMIT = 256;
	private volatile int cacheLimit;
	private final Map metadataReaderCache;

	public CachingMetadataReaderFactory()
	{
		cacheLimit = 256;
		metadataReaderCache = new LinkedHashMap(256, 0.75F, true) {

			final CachingMetadataReaderFactory this$0;

			protected boolean removeEldestEntry(java.util.Map.Entry eldest)
			{
				return size() > getCacheLimit();
			}

			
			{
				this.this$0 = CachingMetadataReaderFactory.this;
				super(x0, x1, x2);
			}
		};
	}

	public CachingMetadataReaderFactory(ResourceLoader resourceLoader)
	{
		super(resourceLoader);
		cacheLimit = 256;
		metadataReaderCache = new 1(256, 0.75F, true);
	}

	public CachingMetadataReaderFactory(ClassLoader classLoader)
	{
		super(classLoader);
		cacheLimit = 256;
		metadataReaderCache = new 1(256, 0.75F, true);
	}

	public void setCacheLimit(int cacheLimit)
	{
		this.cacheLimit = cacheLimit;
	}

	public int getCacheLimit()
	{
		return cacheLimit;
	}

	public MetadataReader getMetadataReader(Resource resource)
		throws IOException
	{
		if (getCacheLimit() <= 0)
			return super.getMetadataReader(resource);
		Map map = metadataReaderCache;
		JVM INSTR monitorenter ;
		MetadataReader metadataReader;
		metadataReader = (MetadataReader)metadataReaderCache.get(resource);
		if (metadataReader == null)
		{
			metadataReader = super.getMetadataReader(resource);
			metadataReaderCache.put(resource, metadataReader);
		}
		return metadataReader;
		Exception exception;
		exception;
		throw exception;
	}

	public void clearCache()
	{
		synchronized (metadataReaderCache)
		{
			metadataReaderCache.clear();
		}
	}
}
