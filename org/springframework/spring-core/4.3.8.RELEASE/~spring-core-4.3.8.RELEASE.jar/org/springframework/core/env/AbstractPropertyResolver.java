// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractPropertyResolver.java

package org.springframework.core.env;

import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.env:
//			MissingRequiredPropertiesException, ConfigurablePropertyResolver

public abstract class AbstractPropertyResolver
	implements ConfigurablePropertyResolver
{

	protected final Log logger = LogFactory.getLog(getClass());
	private volatile ConfigurableConversionService conversionService;
	private PropertyPlaceholderHelper nonStrictHelper;
	private PropertyPlaceholderHelper strictHelper;
	private boolean ignoreUnresolvableNestedPlaceholders;
	private String placeholderPrefix;
	private String placeholderSuffix;
	private String valueSeparator;
	private final Set requiredProperties = new LinkedHashSet();

	public AbstractPropertyResolver()
	{
		ignoreUnresolvableNestedPlaceholders = false;
		placeholderPrefix = "${";
		placeholderSuffix = "}";
		valueSeparator = ":";
	}

	public ConfigurableConversionService getConversionService()
	{
		if (conversionService == null)
			synchronized (this)
			{
				if (conversionService == null)
					conversionService = new DefaultConversionService();
			}
		return conversionService;
	}

	public void setConversionService(ConfigurableConversionService conversionService)
	{
		Assert.notNull(conversionService, "ConversionService must not be null");
		this.conversionService = conversionService;
	}

	public void setPlaceholderPrefix(String placeholderPrefix)
	{
		Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
		this.placeholderPrefix = placeholderPrefix;
	}

	public void setPlaceholderSuffix(String placeholderSuffix)
	{
		Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
		this.placeholderSuffix = placeholderSuffix;
	}

	public void setValueSeparator(String valueSeparator)
	{
		this.valueSeparator = valueSeparator;
	}

	public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders)
	{
		this.ignoreUnresolvableNestedPlaceholders = ignoreUnresolvableNestedPlaceholders;
	}

	public transient void setRequiredProperties(String requiredProperties[])
	{
		if (requiredProperties != null)
		{
			String as[] = requiredProperties;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String key = as[j];
				this.requiredProperties.add(key);
			}

		}
	}

	public void validateRequiredProperties()
	{
		MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
		Iterator iterator = requiredProperties.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String key = (String)iterator.next();
			if (getProperty(key) == null)
				ex.addMissingRequiredProperty(key);
		} while (true);
		if (!ex.getMissingRequiredProperties().isEmpty())
			throw ex;
		else
			return;
	}

	public boolean containsProperty(String key)
	{
		return getProperty(key) != null;
	}

	public String getProperty(String key)
	{
		return (String)getProperty(key, java/lang/String);
	}

	public String getProperty(String key, String defaultValue)
	{
		String value = getProperty(key);
		return value == null ? defaultValue : value;
	}

	public Object getProperty(String key, Class targetType, Object defaultValue)
	{
		Object value = getProperty(key, targetType);
		return value == null ? defaultValue : value;
	}

	/**
	 * @deprecated Method getPropertyAsClass is deprecated
	 */

	public Class getPropertyAsClass(String key, Class targetValueType)
	{
		throw new UnsupportedOperationException();
	}

	public String getRequiredProperty(String key)
		throws IllegalStateException
	{
		String value = getProperty(key);
		if (value == null)
			throw new IllegalStateException(String.format("required key [%s] not found", new Object[] {
				key
			}));
		else
			return value;
	}

	public Object getRequiredProperty(String key, Class valueType)
		throws IllegalStateException
	{
		Object value = getProperty(key, valueType);
		if (value == null)
			throw new IllegalStateException(String.format("required key [%s] not found", new Object[] {
				key
			}));
		else
			return value;
	}

	public String resolvePlaceholders(String text)
	{
		if (nonStrictHelper == null)
			nonStrictHelper = createPlaceholderHelper(true);
		return doResolvePlaceholders(text, nonStrictHelper);
	}

	public String resolveRequiredPlaceholders(String text)
		throws IllegalArgumentException
	{
		if (strictHelper == null)
			strictHelper = createPlaceholderHelper(false);
		return doResolvePlaceholders(text, strictHelper);
	}

	protected String resolveNestedPlaceholders(String value)
	{
		return ignoreUnresolvableNestedPlaceholders ? resolvePlaceholders(value) : resolveRequiredPlaceholders(value);
	}

	private PropertyPlaceholderHelper createPlaceholderHelper(boolean ignoreUnresolvablePlaceholders)
	{
		return new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
	}

	private String doResolvePlaceholders(String text, PropertyPlaceholderHelper helper)
	{
		return helper.replacePlaceholders(text, new org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver() {

			final AbstractPropertyResolver this$0;

			public String resolvePlaceholder(String placeholderName)
			{
				return getPropertyAsRawString(placeholderName);
			}

			
			{
				this.this$0 = AbstractPropertyResolver.this;
				super();
			}
		});
	}

	protected Object convertValueIfNecessary(Object value, Class targetType)
	{
		if (targetType == null)
			return value;
		ConversionService conversionServiceToUse = conversionService;
		if (conversionServiceToUse == null)
		{
			if (ClassUtils.isAssignableValue(targetType, value))
				return value;
			conversionServiceToUse = DefaultConversionService.getSharedInstance();
		}
		return conversionServiceToUse.convert(value, targetType);
	}

	protected abstract String getPropertyAsRawString(String s);
}
