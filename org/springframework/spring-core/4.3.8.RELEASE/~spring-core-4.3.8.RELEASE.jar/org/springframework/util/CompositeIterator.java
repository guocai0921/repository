// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CompositeIterator.java

package org.springframework.util;

import java.util.*;

// Referenced classes of package org.springframework.util:
//			Assert

public class CompositeIterator
	implements Iterator
{

	private final Set iterators = new LinkedHashSet();
	private boolean inUse;

	public CompositeIterator()
	{
		inUse = false;
	}

	public void add(Iterator iterator)
	{
		Assert.state(!inUse, "You can no longer add iterators to a composite iterator that's already in use");
		if (iterators.contains(iterator))
		{
			throw new IllegalArgumentException("You cannot add the same iterator twice");
		} else
		{
			iterators.add(iterator);
			return;
		}
	}

	public boolean hasNext()
	{
		inUse = true;
		for (Iterator iterator1 = iterators.iterator(); iterator1.hasNext();)
		{
			Iterator iterator = (Iterator)iterator1.next();
			if (iterator.hasNext())
				return true;
		}

		return false;
	}

	public Object next()
	{
		inUse = true;
		for (Iterator iterator1 = iterators.iterator(); iterator1.hasNext();)
		{
			Iterator iterator = (Iterator)iterator1.next();
			if (iterator.hasNext())
				return iterator.next();
		}

		throw new NoSuchElementException("All iterators exhausted");
	}

	public void remove()
	{
		throw new UnsupportedOperationException("CompositeIterator does not support remove()");
	}
}
