// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   GenericConverter.java

package org.springframework.core.convert.converter;

import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

public interface GenericConverter
{
	public static final class ConvertiblePair
	{

		private final Class sourceType;
		private final Class targetType;

		public Class getSourceType()
		{
			return sourceType;
		}

		public Class getTargetType()
		{
			return targetType;
		}

		public boolean equals(Object other)
		{
			if (this == other)
				return true;
			if (other == null || other.getClass() != org/springframework/core/convert/converter/GenericConverter$ConvertiblePair)
			{
				return false;
			} else
			{
				ConvertiblePair otherPair = (ConvertiblePair)other;
				return sourceType == otherPair.sourceType && targetType == otherPair.targetType;
			}
		}

		public int hashCode()
		{
			return sourceType.hashCode() * 31 + targetType.hashCode();
		}

		public String toString()
		{
			return (new StringBuilder()).append(sourceType.getName()).append(" -> ").append(targetType.getName()).toString();
		}

		public ConvertiblePair(Class sourceType, Class targetType)
		{
			Assert.notNull(sourceType, "Source type must not be null");
			Assert.notNull(targetType, "Target type must not be null");
			this.sourceType = sourceType;
			this.targetType = targetType;
		}
	}


	public abstract Set getConvertibleTypes();

	public abstract Object convert(Object obj, TypeDescriptor typedescriptor, TypeDescriptor typedescriptor1);
}
