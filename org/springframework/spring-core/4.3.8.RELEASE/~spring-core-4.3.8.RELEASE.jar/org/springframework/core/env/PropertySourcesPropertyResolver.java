// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PropertySourcesPropertyResolver.java

package org.springframework.core.env;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.springframework.core.convert.ConversionException;
import org.springframework.util.ClassUtils;

// Referenced classes of package org.springframework.core.env:
//			AbstractPropertyResolver, PropertySource, PropertySources

public class PropertySourcesPropertyResolver extends AbstractPropertyResolver
{
	/**
	 * @deprecated Class ClassConversionException is deprecated
	 */

	private static class ClassConversionException extends ConversionException
	{

		public ClassConversionException(Class actual, Class expected)
		{
			super(String.format("Actual type %s is not assignable to expected type %s", new Object[] {
				actual.getName(), expected.getName()
			}));
		}

		public ClassConversionException(String actual, Class expected, Exception ex)
		{
			super(String.format("Could not find/load class %s during attempt to convert to %s", new Object[] {
				actual, expected.getName()
			}), ex);
		}
	}


	private final PropertySources propertySources;

	public PropertySourcesPropertyResolver(PropertySources propertySources)
	{
		this.propertySources = propertySources;
	}

	public boolean containsProperty(String key)
	{
label0:
		{
			if (propertySources == null)
				break label0;
			Iterator iterator = propertySources.iterator();
			PropertySource propertySource;
			do
			{
				if (!iterator.hasNext())
					break label0;
				propertySource = (PropertySource)iterator.next();
			} while (!propertySource.containsProperty(key));
			return true;
		}
		return false;
	}

	public String getProperty(String key)
	{
		return (String)getProperty(key, java/lang/String, true);
	}

	public Object getProperty(String key, Class targetValueType)
	{
		return getProperty(key, targetValueType, true);
	}

	protected String getPropertyAsRawString(String key)
	{
		return (String)getProperty(key, java/lang/String, false);
	}

	protected Object getProperty(String key, Class targetValueType, boolean resolveNestedPlaceholders)
	{
label0:
		{
			if (propertySources == null)
				break label0;
			Iterator iterator = propertySources.iterator();
			PropertySource propertySource;
			Object value;
			do
			{
				if (!iterator.hasNext())
					break label0;
				propertySource = (PropertySource)iterator.next();
				if (logger.isTraceEnabled())
					logger.trace(String.format("Searching for key '%s' in [%s]", new Object[] {
						key, propertySource.getName()
					}));
				value = propertySource.getProperty(key);
			} while (value == null);
			if (resolveNestedPlaceholders && (value instanceof String))
				value = resolveNestedPlaceholders((String)value);
			logKeyFound(key, propertySource, value);
			return convertValueIfNecessary(value, targetValueType);
		}
		if (logger.isDebugEnabled())
			logger.debug(String.format("Could not find key '%s' in any property source", new Object[] {
				key
			}));
		return null;
	}

	/**
	 * @deprecated Method getPropertyAsClass is deprecated
	 */

	public Class getPropertyAsClass(String key, Class targetValueType)
	{
label0:
		{
			if (propertySources == null)
				break label0;
			Iterator iterator = propertySources.iterator();
			PropertySource propertySource;
			Object value;
			do
			{
				if (!iterator.hasNext())
					break label0;
				propertySource = (PropertySource)iterator.next();
				if (logger.isTraceEnabled())
					logger.trace(String.format("Searching for key '%s' in [%s]", new Object[] {
						key, propertySource.getName()
					}));
				value = propertySource.getProperty(key);
			} while (value == null);
			logKeyFound(key, propertySource, value);
			Class clazz;
			if (value instanceof String)
				try
				{
					clazz = ClassUtils.forName((String)value, null);
				}
				catch (Exception ex)
				{
					throw new ClassConversionException((String)value, targetValueType, ex);
				}
			else
			if (value instanceof Class)
				clazz = (Class)value;
			else
				clazz = value.getClass();
			if (!targetValueType.isAssignableFrom(clazz))
			{
				throw new ClassConversionException(clazz, targetValueType);
			} else
			{
				Class targetClass = clazz;
				return targetClass;
			}
		}
		if (logger.isDebugEnabled())
			logger.debug(String.format("Could not find key '%s' in any property source", new Object[] {
				key
			}));
		return null;
	}

	protected void logKeyFound(String key, PropertySource propertySource, Object value)
	{
		if (logger.isDebugEnabled())
			logger.debug(String.format("Found key '%s' in [%s] with type [%s]", new Object[] {
				key, propertySource.getName(), value.getClass().getSimpleName()
			}));
	}
}
