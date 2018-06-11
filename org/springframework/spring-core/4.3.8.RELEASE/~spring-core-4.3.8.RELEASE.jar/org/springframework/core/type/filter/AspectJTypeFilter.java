// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AspectJTypeFilter.java

package org.springframework.core.type.filter;

import java.io.IOException;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.patterns.*;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

// Referenced classes of package org.springframework.core.type.filter:
//			TypeFilter

public class AspectJTypeFilter
	implements TypeFilter
{

	private final World world;
	private final TypePattern typePattern;

	public AspectJTypeFilter(String typePatternExpression, ClassLoader classLoader)
	{
		world = new BcelWorld(classLoader, IMessageHandler.THROW, null);
		world.setBehaveInJava5Way(true);
		PatternParser patternParser = new PatternParser(typePatternExpression);
		TypePattern typePattern = patternParser.parseTypePattern();
		typePattern.resolve(world);
		org.aspectj.weaver.patterns.IScope scope = new SimpleScope(world, new FormalBinding[0]);
		this.typePattern = typePattern.resolveBindings(scope, Bindings.NONE, false, false);
	}

	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
		throws IOException
	{
		String className = metadataReader.getClassMetadata().getClassName();
		org.aspectj.weaver.ResolvedType resolvedType = world.resolve(className);
		return typePattern.matchesStatically(resolvedType);
	}
}
