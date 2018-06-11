// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractTypeHierarchyTraversingFilter.java

package org.springframework.core.type.filter;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

// Referenced classes of package org.springframework.core.type.filter:
//			TypeFilter

public abstract class AbstractTypeHierarchyTraversingFilter
	implements TypeFilter
{

	protected final Log logger = LogFactory.getLog(getClass());
	private final boolean considerInherited;
	private final boolean considerInterfaces;

	protected AbstractTypeHierarchyTraversingFilter(boolean considerInherited, boolean considerInterfaces)
	{
		this.considerInherited = considerInherited;
		this.considerInterfaces = considerInterfaces;
	}

	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
		throws IOException
	{
		ClassMetadata metadata;
		if (matchSelf(metadataReader))
			return true;
		metadata = metadataReader.getClassMetadata();
		if (matchClassName(metadata.getClassName()))
			return true;
		if (!considerInherited || !metadata.hasSuperClass())
			break MISSING_BLOCK_LABEL_148;
		Boolean superClassMatch = matchSuperClass(metadata.getSuperClassName());
		if (superClassMatch != null)
		{
			if (superClassMatch.booleanValue())
				return true;
			break MISSING_BLOCK_LABEL_148;
		}
		if (match(metadata.getSuperClassName(), metadataReaderFactory))
			return true;
		break MISSING_BLOCK_LABEL_148;
		IOException ex;
		ex;
		logger.debug((new StringBuilder()).append("Could not read super class [").append(metadata.getSuperClassName()).append("] of type-filtered class [").append(metadata.getClassName()).append("]").toString());
		String as[];
		int i;
		int j;
		if (!considerInterfaces)
			break MISSING_BLOCK_LABEL_279;
		as = metadata.getInterfaceNames();
		i = as.length;
		j = 0;
_L3:
		if (j >= i) goto _L2; else goto _L1
_L1:
		String ifc;
		ifc = as[j];
		Boolean interfaceMatch = matchInterface(ifc);
		if (interfaceMatch != null)
		{
			if (interfaceMatch.booleanValue())
				return true;
			continue; /* Loop/switch isn't completed */
		}
		if (match(ifc, metadataReaderFactory))
			return true;
		continue; /* Loop/switch isn't completed */
		IOException ex;
		ex;
		logger.debug((new StringBuilder()).append("Could not read interface [").append(ifc).append("] for type-filtered class [").append(metadata.getClassName()).append("]").toString());
		j++;
		  goto _L3
_L2:
		return false;
	}

	private boolean match(String className, MetadataReaderFactory metadataReaderFactory)
		throws IOException
	{
		return match(metadataReaderFactory.getMetadataReader(className), metadataReaderFactory);
	}

	protected boolean matchSelf(MetadataReader metadataReader)
	{
		return false;
	}

	protected boolean matchClassName(String className)
	{
		return false;
	}

	protected Boolean matchSuperClass(String superClassName)
	{
		return null;
	}

	protected Boolean matchInterface(String interfaceName)
	{
		return null;
	}
}
