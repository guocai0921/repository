// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CollectionUtils.java

package org.springframework.cglib.core;

import java.util.*;

// Referenced classes of package org.springframework.cglib.core:
//			Transformer, Predicate

public class CollectionUtils
{

	private CollectionUtils()
	{
	}

	public static Map bucket(Collection c, Transformer t)
	{
		Map buckets = new HashMap();
		Object value;
		List bucket;
		for (Iterator it = c.iterator(); it.hasNext(); bucket.add(value))
		{
			value = it.next();
			Object key = t.transform(value);
			bucket = (List)buckets.get(key);
			if (bucket == null)
				buckets.put(key, bucket = new LinkedList());
		}

		return buckets;
	}

	public static void reverse(Map source, Map target)
	{
		Object key;
		for (Iterator it = source.keySet().iterator(); it.hasNext(); target.put(source.get(key), key))
			key = it.next();

	}

	public static Collection filter(Collection c, Predicate p)
	{
		Iterator it = c.iterator();
		do
		{
			if (!it.hasNext())
				break;
			if (!p.evaluate(it.next()))
				it.remove();
		} while (true);
		return c;
	}

	public static List transform(Collection c, Transformer t)
	{
		List result = new ArrayList(c.size());
		for (Iterator it = c.iterator(); it.hasNext(); result.add(t.transform(it.next())));
		return result;
	}

	public static Map getIndexMap(List list)
	{
		Map indexes = new HashMap();
		int index = 0;
		for (Iterator it = list.iterator(); it.hasNext(); indexes.put(it.next(), new Integer(index++)));
		return indexes;
	}
}
