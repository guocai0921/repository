// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RegexPatternTypeFilter.java

package org.springframework.core.type.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core.type.filter:
//			AbstractClassTestingTypeFilter

public class RegexPatternTypeFilter extends AbstractClassTestingTypeFilter
{

	private final Pattern pattern;

	public RegexPatternTypeFilter(Pattern pattern)
	{
		Assert.notNull(pattern, "Pattern must not be null");
		this.pattern = pattern;
	}

	protected boolean match(ClassMetadata metadata)
	{
		return pattern.matcher(metadata.getClassName()).matches();
	}
}
