// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AutoPopulatingList.java

package org.springframework.util;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			Assert

public class AutoPopulatingList
	implements List, Serializable
{
	private static class ReflectiveElementFactory
		implements ElementFactory, Serializable
	{

		private final Class elementClass;

		public Object createElement(int index)
		{
			return elementClass.newInstance();
			InstantiationException ex;
			ex;
			throw new ElementInstantiationException((new StringBuilder()).append("Unable to instantiate element class: ").append(elementClass.getName()).toString(), ex);
			ex;
			throw new ElementInstantiationException((new StringBuilder()).append("Could not access element constructor: ").append(elementClass.getName()).toString(), ex);
		}

		public ReflectiveElementFactory(Class elementClass)
		{
			Assert.notNull(elementClass, "Element class must not be null");
			Assert.isTrue(!elementClass.isInterface(), "Element class must not be an interface type");
			Assert.isTrue(!Modifier.isAbstract(elementClass.getModifiers()), "Element class cannot be an abstract class");
			this.elementClass = elementClass;
		}
	}

	public static class ElementInstantiationException extends RuntimeException
	{

		public ElementInstantiationException(String msg)
		{
			super(msg);
		}

		public ElementInstantiationException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}

	public static interface ElementFactory
	{

		public abstract Object createElement(int i)
			throws ElementInstantiationException;
	}


	private final List backingList;
	private final ElementFactory elementFactory;

	public AutoPopulatingList(Class elementClass)
	{
		this(((List) (new ArrayList())), elementClass);
	}

	public AutoPopulatingList(List backingList, Class elementClass)
	{
		this(backingList, ((ElementFactory) (new ReflectiveElementFactory(elementClass))));
	}

	public AutoPopulatingList(ElementFactory elementFactory)
	{
		this(((List) (new ArrayList())), elementFactory);
	}

	public AutoPopulatingList(List backingList, ElementFactory elementFactory)
	{
		Assert.notNull(backingList, "Backing List must not be null");
		Assert.notNull(elementFactory, "Element factory must not be null");
		this.backingList = backingList;
		this.elementFactory = elementFactory;
	}

	public void add(int index, Object element)
	{
		backingList.add(index, element);
	}

	public boolean add(Object o)
	{
		return backingList.add(o);
	}

	public boolean addAll(Collection c)
	{
		return backingList.addAll(c);
	}

	public boolean addAll(int index, Collection c)
	{
		return backingList.addAll(index, c);
	}

	public void clear()
	{
		backingList.clear();
	}

	public boolean contains(Object o)
	{
		return backingList.contains(o);
	}

	public boolean containsAll(Collection c)
	{
		return backingList.containsAll(c);
	}

	public Object get(int index)
	{
		int backingListSize = backingList.size();
		Object element = null;
		if (index < backingListSize)
		{
			element = backingList.get(index);
			if (element == null)
			{
				element = elementFactory.createElement(index);
				backingList.set(index, element);
			}
		} else
		{
			for (int x = backingListSize; x < index; x++)
				backingList.add(null);

			element = elementFactory.createElement(index);
			backingList.add(element);
		}
		return element;
	}

	public int indexOf(Object o)
	{
		return backingList.indexOf(o);
	}

	public boolean isEmpty()
	{
		return backingList.isEmpty();
	}

	public Iterator iterator()
	{
		return backingList.iterator();
	}

	public int lastIndexOf(Object o)
	{
		return backingList.lastIndexOf(o);
	}

	public ListIterator listIterator()
	{
		return backingList.listIterator();
	}

	public ListIterator listIterator(int index)
	{
		return backingList.listIterator(index);
	}

	public Object remove(int index)
	{
		return backingList.remove(index);
	}

	public boolean remove(Object o)
	{
		return backingList.remove(o);
	}

	public boolean removeAll(Collection c)
	{
		return backingList.removeAll(c);
	}

	public boolean retainAll(Collection c)
	{
		return backingList.retainAll(c);
	}

	public Object set(int index, Object element)
	{
		return backingList.set(index, element);
	}

	public int size()
	{
		return backingList.size();
	}

	public List subList(int fromIndex, int toIndex)
	{
		return backingList.subList(fromIndex, toIndex);
	}

	public Object[] toArray()
	{
		return backingList.toArray();
	}

	public Object[] toArray(Object a[])
	{
		return backingList.toArray(a);
	}

	public boolean equals(Object other)
	{
		return backingList.equals(other);
	}

	public int hashCode()
	{
		return backingList.hashCode();
	}
}
