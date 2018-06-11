// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConverterNotFoundException.java

package org.springframework.core.convert;


// Referenced classes of package org.springframework.core.convert:
//			ConversionException, TypeDescriptor

public class ConverterNotFoundException extends ConversionException
{

	private final TypeDescriptor sourceType;
	private final TypeDescriptor targetType;

	public ConverterNotFoundException(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		super((new StringBuilder()).append("No converter found capable of converting from type [").append(sourceType).append("] to type [").append(targetType).append("]").toString());
		this.sourceType = sourceType;
		this.targetType = targetType;
	}

	public TypeDescriptor getSourceType()
	{
		return sourceType;
	}

	public TypeDescriptor getTargetType()
	{
		return targetType;
	}
}
