// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TransformerUtils.java

package org.springframework.util.xml;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import org.springframework.util.Assert;

public abstract class TransformerUtils
{

	public static final int DEFAULT_INDENT_AMOUNT = 2;

	public TransformerUtils()
	{
	}

	public static void enableIndenting(Transformer transformer)
	{
		enableIndenting(transformer, 2);
	}

	public static void enableIndenting(Transformer transformer, int indentAmount)
	{
		Assert.notNull(transformer, "Transformer must not be null");
		Assert.isTrue(indentAmount > -1, (new StringBuilder()).append("The indent amount cannot be less than zero : got ").append(indentAmount).toString());
		transformer.setOutputProperty("indent", "yes");
		try
		{
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
		}
		catch (IllegalArgumentException illegalargumentexception) { }
	}

	public static void disableIndenting(Transformer transformer)
	{
		Assert.notNull(transformer, "Transformer must not be null");
		transformer.setOutputProperty("indent", "no");
	}
}
