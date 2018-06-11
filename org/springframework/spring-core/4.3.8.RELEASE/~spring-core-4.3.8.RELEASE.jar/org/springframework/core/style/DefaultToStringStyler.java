// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DefaultToStringStyler.java

package org.springframework.core.style;

import org.springframework.util.*;

// Referenced classes of package org.springframework.core.style:
//			ToStringStyler, ValueStyler

public class DefaultToStringStyler
	implements ToStringStyler
{

	private final ValueStyler valueStyler;

	public DefaultToStringStyler(ValueStyler valueStyler)
	{
		Assert.notNull(valueStyler, "ValueStyler must not be null");
		this.valueStyler = valueStyler;
	}

	protected final ValueStyler getValueStyler()
	{
		return valueStyler;
	}

	public void styleStart(StringBuilder buffer, Object obj)
	{
		if (!obj.getClass().isArray())
		{
			buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
			styleIdentityHashCode(buffer, obj);
		} else
		{
			buffer.append('[');
			styleIdentityHashCode(buffer, obj);
			buffer.append(' ');
			styleValue(buffer, obj);
		}
	}

	private void styleIdentityHashCode(StringBuilder buffer, Object obj)
	{
		buffer.append('@');
		buffer.append(ObjectUtils.getIdentityHexString(obj));
	}

	public void styleEnd(StringBuilder buffer, Object o)
	{
		buffer.append(']');
	}

	public void styleField(StringBuilder buffer, String fieldName, Object value)
	{
		styleFieldStart(buffer, fieldName);
		styleValue(buffer, value);
		styleFieldEnd(buffer, fieldName);
	}

	protected void styleFieldStart(StringBuilder buffer, String fieldName)
	{
		buffer.append(' ').append(fieldName).append(" = ");
	}

	protected void styleFieldEnd(StringBuilder stringbuilder, String s)
	{
	}

	public void styleValue(StringBuilder buffer, Object value)
	{
		buffer.append(valueStyler.style(value));
	}

	public void styleFieldSeparator(StringBuilder buffer)
	{
		buffer.append(',');
	}
}
