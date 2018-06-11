// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ToStringCreator.java

package org.springframework.core.style;

import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.style:
//			ToStringStyler, DefaultToStringStyler, ValueStyler, StylerUtils

public class ToStringCreator
{

	private static final ToStringStyler DEFAULT_TO_STRING_STYLER;
	private final StringBuilder buffer;
	private final ToStringStyler styler;
	private final Object object;
	private boolean styledFirstField;

	public ToStringCreator(Object obj)
	{
		this(obj, (ToStringStyler)null);
	}

	public ToStringCreator(Object obj, ValueStyler styler)
	{
		this(obj, ((ToStringStyler) (new DefaultToStringStyler(styler == null ? StylerUtils.DEFAULT_VALUE_STYLER : styler))));
	}

	public ToStringCreator(Object obj, ToStringStyler styler)
	{
		buffer = new StringBuilder(256);
		Assert.notNull(obj, "The object to be styled must not be null");
		object = obj;
		this.styler = styler == null ? DEFAULT_TO_STRING_STYLER : styler;
		this.styler.styleStart(buffer, object);
	}

	public ToStringCreator append(String fieldName, byte value)
	{
		return append(fieldName, Byte.valueOf(value));
	}

	public ToStringCreator append(String fieldName, short value)
	{
		return append(fieldName, Short.valueOf(value));
	}

	public ToStringCreator append(String fieldName, int value)
	{
		return append(fieldName, Integer.valueOf(value));
	}

	public ToStringCreator append(String fieldName, long value)
	{
		return append(fieldName, Long.valueOf(value));
	}

	public ToStringCreator append(String fieldName, float value)
	{
		return append(fieldName, Float.valueOf(value));
	}

	public ToStringCreator append(String fieldName, double value)
	{
		return append(fieldName, Double.valueOf(value));
	}

	public ToStringCreator append(String fieldName, boolean value)
	{
		return append(fieldName, Boolean.valueOf(value));
	}

	public ToStringCreator append(String fieldName, Object value)
	{
		printFieldSeparatorIfNecessary();
		styler.styleField(buffer, fieldName, value);
		return this;
	}

	private void printFieldSeparatorIfNecessary()
	{
		if (styledFirstField)
			styler.styleFieldSeparator(buffer);
		else
			styledFirstField = true;
	}

	public ToStringCreator append(Object value)
	{
		styler.styleValue(buffer, value);
		return this;
	}

	public String toString()
	{
		styler.styleEnd(buffer, object);
		return buffer.toString();
	}

	static 
	{
		DEFAULT_TO_STRING_STYLER = new DefaultToStringStyler(StylerUtils.DEFAULT_VALUE_STYLER);
	}
}
