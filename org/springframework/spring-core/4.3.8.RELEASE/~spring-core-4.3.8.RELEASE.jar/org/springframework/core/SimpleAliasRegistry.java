// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimpleAliasRegistry.java

package org.springframework.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.util.*;

// Referenced classes of package org.springframework.core:
//			AliasRegistry

public class SimpleAliasRegistry
	implements AliasRegistry
{

	private final Map aliasMap = new ConcurrentHashMap(16);

	public SimpleAliasRegistry()
	{
	}

	public void registerAlias(String name, String alias)
	{
		Assert.hasText(name, "'name' must not be empty");
		Assert.hasText(alias, "'alias' must not be empty");
		if (alias.equals(name))
		{
			aliasMap.remove(alias);
		} else
		{
			String registeredName = (String)aliasMap.get(alias);
			if (registeredName != null)
			{
				if (registeredName.equals(name))
					return;
				if (!allowAliasOverriding())
					throw new IllegalStateException((new StringBuilder()).append("Cannot register alias '").append(alias).append("' for name '").append(name).append("': It is already registered for name '").append(registeredName).append("'.").toString());
			}
			checkForAliasCircle(name, alias);
			aliasMap.put(alias, name);
		}
	}

	protected boolean allowAliasOverriding()
	{
		return true;
	}

	public boolean hasAlias(String name, String alias)
	{
		for (Iterator iterator = aliasMap.entrySet().iterator(); iterator.hasNext();)
		{
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			String registeredName = (String)entry.getValue();
			if (registeredName.equals(name))
			{
				String registeredAlias = (String)entry.getKey();
				return registeredAlias.equals(alias) || hasAlias(registeredAlias, alias);
			}
		}

		return false;
	}

	public void removeAlias(String alias)
	{
		String name = (String)aliasMap.remove(alias);
		if (name == null)
			throw new IllegalStateException((new StringBuilder()).append("No alias '").append(alias).append("' registered").toString());
		else
			return;
	}

	public boolean isAlias(String name)
	{
		return aliasMap.containsKey(name);
	}

	public String[] getAliases(String name)
	{
		List result = new ArrayList();
		synchronized (aliasMap)
		{
			retrieveAliases(name, result);
		}
		return StringUtils.toStringArray(result);
	}

	private void retrieveAliases(String name, List result)
	{
		Iterator iterator = aliasMap.entrySet().iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			String registeredName = (String)entry.getValue();
			if (registeredName.equals(name))
			{
				String alias = (String)entry.getKey();
				result.add(alias);
				retrieveAliases(alias, result);
			}
		} while (true);
	}

	public void resolveAliases(StringValueResolver valueResolver)
	{
		Assert.notNull(valueResolver, "StringValueResolver must not be null");
		synchronized (aliasMap)
		{
			Map aliasCopy = new HashMap(aliasMap);
			Iterator iterator = aliasCopy.keySet().iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				String alias = (String)iterator.next();
				String registeredName = (String)aliasCopy.get(alias);
				String resolvedAlias = valueResolver.resolveStringValue(alias);
				String resolvedName = valueResolver.resolveStringValue(registeredName);
				if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName))
				{
					aliasMap.remove(alias);
					continue;
				}
				if (!resolvedAlias.equals(alias))
				{
					String existingName = (String)aliasMap.get(resolvedAlias);
					if (existingName != null)
					{
						if (existingName.equals(resolvedName))
							aliasMap.remove(alias);
						else
							throw new IllegalStateException((new StringBuilder()).append("Cannot register resolved alias '").append(resolvedAlias).append("' (original: '").append(alias).append("') for name '").append(resolvedName).append("': It is already registered for name '").append(registeredName).append("'.").toString());
						break;
					}
					checkForAliasCircle(resolvedName, resolvedAlias);
					aliasMap.remove(alias);
					aliasMap.put(resolvedAlias, resolvedName);
				} else
				if (!registeredName.equals(resolvedName))
					aliasMap.put(alias, resolvedName);
			} while (true);
		}
	}

	protected void checkForAliasCircle(String name, String alias)
	{
		if (hasAlias(alias, name))
			throw new IllegalStateException((new StringBuilder()).append("Cannot register alias '").append(alias).append("' for name '").append(name).append("': Circular reference - '").append(name).append("' is a direct or indirect alias for '").append(alias).append("' already").toString());
		else
			return;
	}

	public String canonicalName(String name)
	{
		String canonicalName = name;
		String resolvedName;
		do
		{
			resolvedName = (String)aliasMap.get(canonicalName);
			if (resolvedName != null)
				canonicalName = resolvedName;
		} while (resolvedName != null);
		return canonicalName;
	}
}
