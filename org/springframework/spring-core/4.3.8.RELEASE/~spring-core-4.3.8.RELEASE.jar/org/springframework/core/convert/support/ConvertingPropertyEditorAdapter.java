// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConvertingPropertyEditorAdapter.java

package org.springframework.core.convert.support;

import java.beans.PropertyEditorSupport;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

public class ConvertingPropertyEditorAdapter extends PropertyEditorSupport
{

	private final ConversionService conversionService;
	private final TypeDescriptor targetDescriptor;
	private final boolean canConvertToString;

	public ConvertingPropertyEditorAdapter(ConversionService conversionService, TypeDescriptor targetDescriptor)
	{
		Assert.notNull(conversionService, "ConversionService must not be null");
		Assert.notNull(targetDescriptor, "TypeDescriptor must not be null");
		this.conversionService = conversionService;
		this.targetDescriptor = targetDescriptor;
		canConvertToString = conversionService.canConvert(this.targetDescriptor, TypeDescriptor.valueOf(java/lang/String));
	}

	public void setAsText(String text)
		throws IllegalArgumentException
	{
		setValue(conversionService.convert(text, TypeDescriptor.valueOf(java/lang/String), targetDescriptor));
	}

	public String getAsText()
	{
		if (canConvertToString)
			return (String)conversionService.convert(getValue(), targetDescriptor, TypeDescriptor.valueOf(java/lang/String));
		else
			return null;
	}
}
