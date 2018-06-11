// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractEnvironment.java

package org.springframework.core.env;

import java.security.AccessControlException;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.SpringProperties;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core.env:
//			MutablePropertySources, PropertySourcesPropertyResolver, PropertySource, ConfigurableEnvironment, 
//			MissingRequiredPropertiesException, ConfigurablePropertyResolver, ReadOnlySystemAttributesMap

public abstract class AbstractEnvironment
	implements ConfigurableEnvironment
{

	public static final String IGNORE_GETENV_PROPERTY_NAME = "spring.getenv.ignore";
	public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
	public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
	protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
	protected final Log logger = LogFactory.getLog(getClass());
	private final Set activeProfiles = new LinkedHashSet();
	private final Set defaultProfiles = new LinkedHashSet(getReservedDefaultProfiles());
	private final MutablePropertySources propertySources;
	private final ConfigurablePropertyResolver propertyResolver;

	public AbstractEnvironment()
	{
		propertySources = new MutablePropertySources(logger);
		propertyResolver = new PropertySourcesPropertyResolver(propertySources);
		customizePropertySources(propertySources);
		if (logger.isDebugEnabled())
			logger.debug(String.format("Initialized %s with PropertySources %s", new Object[] {
				getClass().getSimpleName(), propertySources
			}));
	}

	protected void customizePropertySources(MutablePropertySources mutablepropertysources)
	{
	}

	protected Set getReservedDefaultProfiles()
	{
		return Collections.singleton("default");
	}

	public String[] getActiveProfiles()
	{
		return StringUtils.toStringArray(doGetActiveProfiles());
	}

	protected Set doGetActiveProfiles()
	{
		Set set = activeProfiles;
		JVM INSTR monitorenter ;
		if (activeProfiles.isEmpty())
		{
			String profiles = getProperty("spring.profiles.active");
			if (StringUtils.hasText(profiles))
				setActiveProfiles(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(profiles)));
		}
		return activeProfiles;
		Exception exception;
		exception;
		throw exception;
	}

	public transient void setActiveProfiles(String profiles[])
	{
		Assert.notNull(profiles, "Profile array must not be null");
		synchronized (activeProfiles)
		{
			activeProfiles.clear();
			String as[] = profiles;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String profile = as[j];
				validateProfile(profile);
				activeProfiles.add(profile);
			}

		}
	}

	public void addActiveProfile(String profile)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Activating profile '%s'", new Object[] {
				profile
			}));
		validateProfile(profile);
		doGetActiveProfiles();
		synchronized (activeProfiles)
		{
			activeProfiles.add(profile);
		}
	}

	public String[] getDefaultProfiles()
	{
		return StringUtils.toStringArray(doGetDefaultProfiles());
	}

	protected Set doGetDefaultProfiles()
	{
		Set set = defaultProfiles;
		JVM INSTR monitorenter ;
		if (defaultProfiles.equals(getReservedDefaultProfiles()))
		{
			String profiles = getProperty("spring.profiles.default");
			if (StringUtils.hasText(profiles))
				setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(profiles)));
		}
		return defaultProfiles;
		Exception exception;
		exception;
		throw exception;
	}

	public transient void setDefaultProfiles(String profiles[])
	{
		Assert.notNull(profiles, "Profile array must not be null");
		synchronized (defaultProfiles)
		{
			defaultProfiles.clear();
			String as[] = profiles;
			int i = as.length;
			for (int j = 0; j < i; j++)
			{
				String profile = as[j];
				validateProfile(profile);
				defaultProfiles.add(profile);
			}

		}
	}

	public transient boolean acceptsProfiles(String profiles[])
	{
		Assert.notEmpty(profiles, "Must specify at least one profile");
		String as[] = profiles;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String profile = as[j];
			if (StringUtils.hasLength(profile) && profile.charAt(0) == '!')
			{
				if (!isProfileActive(profile.substring(1)))
					return true;
				continue;
			}
			if (isProfileActive(profile))
				return true;
		}

		return false;
	}

	protected boolean isProfileActive(String profile)
	{
		validateProfile(profile);
		Set currentActiveProfiles = doGetActiveProfiles();
		return currentActiveProfiles.contains(profile) || currentActiveProfiles.isEmpty() && doGetDefaultProfiles().contains(profile);
	}

	protected void validateProfile(String profile)
	{
		if (!StringUtils.hasText(profile))
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid profile [").append(profile).append("]: must contain text").toString());
		if (profile.charAt(0) == '!')
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid profile [").append(profile).append("]: must not begin with ! operator").toString());
		else
			return;
	}

	public MutablePropertySources getPropertySources()
	{
		return propertySources;
	}

	public Map getSystemEnvironment()
	{
		if (suppressGetenvAccess())
			return Collections.emptyMap();
		return System.getenv();
		AccessControlException ex;
		ex;
		return new ReadOnlySystemAttributesMap() {

			final AbstractEnvironment this$0;

			protected String getSystemAttribute(String attributeName)
			{
				return System.getenv(attributeName);
				AccessControlException ex;
				ex;
				if (logger.isInfoEnabled())
					logger.info(String.format("Caught AccessControlException when accessing system environment variable [%s]; its value will be returned [null]. Reason: %s", new Object[] {
						attributeName, ex.getMessage()
					}));
				return null;
			}

			
			{
				this.this$0 = AbstractEnvironment.this;
				super();
			}
		};
	}

	protected boolean suppressGetenvAccess()
	{
		return SpringProperties.getFlag("spring.getenv.ignore");
	}

	public Map getSystemProperties()
	{
		return System.getProperties();
		AccessControlException ex;
		ex;
		return new ReadOnlySystemAttributesMap() {

			final AbstractEnvironment this$0;

			protected String getSystemAttribute(String attributeName)
			{
				return System.getProperty(attributeName);
				AccessControlException ex;
				ex;
				if (logger.isInfoEnabled())
					logger.info(String.format("Caught AccessControlException when accessing system property [%s]; its value will be returned [null]. Reason: %s", new Object[] {
						attributeName, ex.getMessage()
					}));
				return null;
			}

			
			{
				this.this$0 = AbstractEnvironment.this;
				super();
			}
		};
	}

	public void merge(ConfigurableEnvironment parent)
	{
		Iterator iterator = parent.getPropertySources().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			PropertySource ps = (PropertySource)iterator.next();
			if (!propertySources.contains(ps.getName()))
				propertySources.addLast(ps);
		} while (true);
		String parentActiveProfiles[] = parent.getActiveProfiles();
		if (!ObjectUtils.isEmpty(parentActiveProfiles))
			synchronized (activeProfiles)
			{
				String as[] = parentActiveProfiles;
				int i = as.length;
				for (int j = 0; j < i; j++)
				{
					String profile = as[j];
					activeProfiles.add(profile);
				}

			}
		String parentDefaultProfiles[] = parent.getDefaultProfiles();
		if (!ObjectUtils.isEmpty(parentDefaultProfiles))
			synchronized (defaultProfiles)
			{
				defaultProfiles.remove("default");
				String as1[] = parentDefaultProfiles;
				int k = as1.length;
				for (int l = 0; l < k; l++)
				{
					String profile = as1[l];
					defaultProfiles.add(profile);
				}

			}
	}

	public ConfigurableConversionService getConversionService()
	{
		return propertyResolver.getConversionService();
	}

	public void setConversionService(ConfigurableConversionService conversionService)
	{
		propertyResolver.setConversionService(conversionService);
	}

	public void setPlaceholderPrefix(String placeholderPrefix)
	{
		propertyResolver.setPlaceholderPrefix(placeholderPrefix);
	}

	public void setPlaceholderSuffix(String placeholderSuffix)
	{
		propertyResolver.setPlaceholderSuffix(placeholderSuffix);
	}

	public void setValueSeparator(String valueSeparator)
	{
		propertyResolver.setValueSeparator(valueSeparator);
	}

	public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders)
	{
		propertyResolver.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
	}

	public transient void setRequiredProperties(String requiredProperties[])
	{
		propertyResolver.setRequiredProperties(requiredProperties);
	}

	public void validateRequiredProperties()
		throws MissingRequiredPropertiesException
	{
		propertyResolver.validateRequiredProperties();
	}

	public boolean containsProperty(String key)
	{
		return propertyResolver.containsProperty(key);
	}

	public String getProperty(String key)
	{
		return propertyResolver.getProperty(key);
	}

	public String getProperty(String key, String defaultValue)
	{
		return propertyResolver.getProperty(key, defaultValue);
	}

	public Object getProperty(String key, Class targetType)
	{
		return propertyResolver.getProperty(key, targetType);
	}

	public Object getProperty(String key, Class targetType, Object defaultValue)
	{
		return propertyResolver.getProperty(key, targetType, defaultValue);
	}

	/**
	 * @deprecated Method getPropertyAsClass is deprecated
	 */

	public Class getPropertyAsClass(String key, Class targetType)
	{
		return propertyResolver.getPropertyAsClass(key, targetType);
	}

	public String getRequiredProperty(String key)
		throws IllegalStateException
	{
		return propertyResolver.getRequiredProperty(key);
	}

	public Object getRequiredProperty(String key, Class targetType)
		throws IllegalStateException
	{
		return propertyResolver.getRequiredProperty(key, targetType);
	}

	public String resolvePlaceholders(String text)
	{
		return propertyResolver.resolvePlaceholders(text);
	}

	public String resolveRequiredPlaceholders(String text)
		throws IllegalArgumentException
	{
		return propertyResolver.resolveRequiredPlaceholders(text);
	}

	public String toString()
	{
		return String.format("%s {activeProfiles=%s, defaultProfiles=%s, propertySources=%s}", new Object[] {
			getClass().getSimpleName(), activeProfiles, defaultProfiles, propertySources
		});
	}
}
