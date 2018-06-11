// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConfigurablePropertyResolver.java

package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;

// Referenced classes of package org.springframework.core.env:
//			PropertyResolver, MissingRequiredPropertiesException

public interface ConfigurablePropertyResolver
	extends PropertyResolver
{

	public abstract ConfigurableConversionService getConversionService();

	public abstract void setConversionService(ConfigurableConversionService configurableconversionservice);

	public abstract void setPlaceholderPrefix(String s);

	public abstract void setPlaceholderSuffix(String s);

	public abstract void setValueSeparator(String s);

	public abstract void setIgnoreUnresolvableNestedPlaceholders(boolean flag);

	public transient abstract void setRequiredProperties(String as[]);

	public abstract void validateRequiredProperties()
		throws MissingRequiredPropertiesException;
}
