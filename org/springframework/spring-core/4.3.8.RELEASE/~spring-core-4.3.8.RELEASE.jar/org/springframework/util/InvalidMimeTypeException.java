// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InvalidMimeTypeException.java

package org.springframework.util;


public class InvalidMimeTypeException extends IllegalArgumentException
{

	private final String mimeType;

	public InvalidMimeTypeException(String mimeType, String message)
	{
		super((new StringBuilder()).append("Invalid mime type \"").append(mimeType).append("\": ").append(message).toString());
		this.mimeType = mimeType;
	}

	public String getMimeType()
	{
		return mimeType;
	}
}
