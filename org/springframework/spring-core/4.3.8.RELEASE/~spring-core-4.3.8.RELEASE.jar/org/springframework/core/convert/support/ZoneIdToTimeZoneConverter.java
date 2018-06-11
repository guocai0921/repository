// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ZoneIdToTimeZoneConverter.java

package org.springframework.core.convert.support;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.core.convert.converter.Converter;

final class ZoneIdToTimeZoneConverter
	implements Converter
{

	ZoneIdToTimeZoneConverter()
	{
	}

	public TimeZone convert(ZoneId source)
	{
		return TimeZone.getTimeZone(source);
	}

	public volatile Object convert(Object obj)
	{
		return convert((ZoneId)obj);
	}
}
