// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StylerUtils.java

package org.springframework.core.style;


// Referenced classes of package org.springframework.core.style:
//			DefaultValueStyler, ValueStyler

public abstract class StylerUtils
{

	static final ValueStyler DEFAULT_VALUE_STYLER = new DefaultValueStyler();

	public StylerUtils()
	{
	}

	public static String style(Object value)
	{
		return DEFAULT_VALUE_STYLER.style(value);
	}

}
