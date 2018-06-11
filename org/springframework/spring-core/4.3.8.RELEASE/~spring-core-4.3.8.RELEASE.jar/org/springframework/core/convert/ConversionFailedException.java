// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConversionFailedException.java

package org.springframework.core.convert;

import org.springframework.util.ObjectUtils;

// Referenced classes of package org.springframework.core.convert:
//			ConversionException, TypeDescriptor

public class ConversionFailedException extends ConversionException
{

	private final TypeDescriptor sourceType;
	private final TypeDescriptor targetType;
	private final Object value;

	public ConversionFailedException(TypeDescriptor sourceType, TypeDescriptor targetType, Object value, Throwable cause)
	{
		super((new StringBuilder()).append("Failed to convert from type [").append(sourceType).append("] to type [").append(targetType).append("] for value '").append(ObjectUtils.nullSafeToString(value)).append("'").toString(), cause);
		this.sourceType = sourceType;
		this.targetType = targetType;
		this.value = value;
	}

	public TypeDescriptor getSourceType()
	{
		return sourceType;
	}

	public TypeDescriptor getTargetType()
	{
		return targetType;
	}

	public Object getValue()
	{
		return value;
	}
}
