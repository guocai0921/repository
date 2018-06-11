// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XmlValidationModeDetector.java

package org.springframework.util.xml;

import java.io.*;
import org.springframework.util.StringUtils;

public class XmlValidationModeDetector
{

	public static final int VALIDATION_NONE = 0;
	public static final int VALIDATION_AUTO = 1;
	public static final int VALIDATION_DTD = 2;
	public static final int VALIDATION_XSD = 3;
	private static final String DOCTYPE = "DOCTYPE";
	private static final String START_COMMENT = "<!--";
	private static final String END_COMMENT = "-->";
	private boolean inComment;

	public XmlValidationModeDetector()
	{
	}

	public int detectValidationMode(InputStream inputStream)
		throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		byte byte0;
		boolean isDtdValidated = false;
		String content;
label0:
		do
		{
			do
			{
				if ((content = reader.readLine()) == null)
					break label0;
				content = consumeCommentTokens(content);
			} while (inComment || !StringUtils.hasText(content));
			if (!hasDoctype(content))
				continue;
			isDtdValidated = true;
			break;
		} while (!hasOpeningTag(content));
		byte0 = ((byte)(isDtdValidated ? 2 : 3));
		reader.close();
		return byte0;
		CharConversionException ex;
		ex;
		int i = 1;
		reader.close();
		return i;
		Exception exception;
		exception;
		reader.close();
		throw exception;
	}

	private boolean hasDoctype(String content)
	{
		return content.contains("DOCTYPE");
	}

	private boolean hasOpeningTag(String content)
	{
		if (inComment)
		{
			return false;
		} else
		{
			int openTagIndex = content.indexOf('<');
			return openTagIndex > -1 && content.length() > openTagIndex + 1 && Character.isLetter(content.charAt(openTagIndex + 1));
		}
	}

	private String consumeCommentTokens(String line)
	{
		if (!line.contains("<!--") && !line.contains("-->"))
			return line;
		while ((line = consume(line)) != null) 
			if (!inComment && !line.trim().startsWith("<!--"))
				return line;
		return line;
	}

	private String consume(String line)
	{
		int index = inComment ? endComment(line) : startComment(line);
		return index != -1 ? line.substring(index) : null;
	}

	private int startComment(String line)
	{
		return commentToken(line, "<!--", true);
	}

	private int endComment(String line)
	{
		return commentToken(line, "-->", false);
	}

	private int commentToken(String line, String token, boolean inCommentIfPresent)
	{
		int index = line.indexOf(token);
		if (index > -1)
			inComment = inCommentIfPresent;
		return index != -1 ? index + token.length() : index;
	}
}
