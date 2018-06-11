// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ByteBufferConverter.java

package org.springframework.core.convert.support;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

final class ByteBufferConverter
	implements ConditionalGenericConverter
{

	private static final TypeDescriptor BYTE_BUFFER_TYPE = TypeDescriptor.valueOf(java/nio/ByteBuffer);
	private static final TypeDescriptor BYTE_ARRAY_TYPE = TypeDescriptor.valueOf([B);
	private static final Set CONVERTIBLE_PAIRS;
	private final ConversionService conversionService;

	public ByteBufferConverter(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}

	public Set getConvertibleTypes()
	{
		return CONVERTIBLE_PAIRS;
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
		if (sourceType.isAssignableTo(BYTE_BUFFER_TYPE))
			return byteBufferTarget || matchesFromByteBuffer(targetType);
		else
			return byteBufferTarget && matchesToByteBuffer(sourceType);
	}

	private boolean matchesFromByteBuffer(TypeDescriptor targetType)
	{
		return targetType.isAssignableTo(BYTE_ARRAY_TYPE) || conversionService.canConvert(BYTE_ARRAY_TYPE, targetType);
	}

	private boolean matchesToByteBuffer(TypeDescriptor sourceType)
	{
		return sourceType.isAssignableTo(BYTE_ARRAY_TYPE) || conversionService.canConvert(sourceType, BYTE_ARRAY_TYPE);
	}

	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
	{
		boolean byteBufferTarget = targetType.isAssignableTo(BYTE_BUFFER_TYPE);
		if (source instanceof ByteBuffer)
		{
			ByteBuffer buffer = (ByteBuffer)source;
			return byteBufferTarget ? buffer.duplicate() : convertFromByteBuffer(buffer, targetType);
		}
		if (byteBufferTarget)
			return convertToByteBuffer(source, sourceType);
		else
			throw new IllegalStateException("Unexpected source/target types");
	}

	private Object convertFromByteBuffer(ByteBuffer source, TypeDescriptor targetType)
	{
		byte bytes[] = new byte[source.remaining()];
		source.get(bytes);
		if (targetType.isAssignableTo(BYTE_ARRAY_TYPE))
			return bytes;
		else
			return conversionService.convert(bytes, BYTE_ARRAY_TYPE, targetType);
	}

	private Object convertToByteBuffer(Object source, TypeDescriptor sourceType)
	{
		byte bytes[] = (byte[])(byte[])((source instanceof byte[]) ? source : conversionService.convert(source, sourceType, BYTE_ARRAY_TYPE));
		ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
		byteBuffer.put(bytes);
		return byteBuffer.rewind();
	}

	static 
	{
		Set convertiblePairs = new HashSet(4);
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/nio/ByteBuffer, [B));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair([B, java/nio/ByteBuffer));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/nio/ByteBuffer, java/lang/Object));
		convertiblePairs.add(new org.springframework.core.convert.converter.GenericConverter.ConvertiblePair(java/lang/Object, java/nio/ByteBuffer));
		CONVERTIBLE_PAIRS = Collections.unmodifiableSet(convertiblePairs);
	}
}
