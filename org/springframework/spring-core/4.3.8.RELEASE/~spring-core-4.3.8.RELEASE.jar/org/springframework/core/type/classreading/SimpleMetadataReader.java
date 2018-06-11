// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleMetadataReader.java

package org.springframework.core.type.classreading;

import java.io.*;
import org.springframework.asm.ClassReader;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;

// Referenced classes of package org.springframework.core.type.classreading:
//			AnnotationMetadataReadingVisitor, MetadataReader

final class SimpleMetadataReader
	implements MetadataReader
{

	private final Resource resource;
	private final ClassMetadata classMetadata;
	private final AnnotationMetadata annotationMetadata;

	SimpleMetadataReader(Resource resource, ClassLoader classLoader)
		throws IOException
	{
		ClassReader classReader;
		Exception exception;
		InputStream is = new BufferedInputStream(resource.getInputStream());
		try
		{
			classReader = new ClassReader(is);
		}
		catch (IllegalArgumentException ex)
		{
			throw new NestedIOException((new StringBuilder()).append("ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: ").append(resource).toString(), ex);
		}
		finally
		{
			is.close();
		}
		is.close();
		break MISSING_BLOCK_LABEL_75;
		throw exception;
		AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
		classReader.accept(visitor, 2);
		annotationMetadata = visitor;
		classMetadata = visitor;
		this.resource = resource;
		return;
	}

	public Resource getResource()
	{
		return resource;
	}

	public ClassMetadata getClassMetadata()
	{
		return classMetadata;
	}

	public AnnotationMetadata getAnnotationMetadata()
	{
		return annotationMetadata;
	}
}
