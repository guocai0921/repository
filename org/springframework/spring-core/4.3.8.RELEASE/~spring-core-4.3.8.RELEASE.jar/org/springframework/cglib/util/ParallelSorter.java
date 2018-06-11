// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParallelSorter.java

package org.springframework.cglib.util;

import java.util.Comparator;
import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.*;

// Referenced classes of package org.springframework.cglib.util:
//			SorterTemplate, ParallelSorterEmitter

public abstract class ParallelSorter extends SorterTemplate
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/util/ParallelSorter.getName());
		private Object arrays[];

		protected ClassLoader getDefaultClassLoader()
		{
			return null;
		}

		public void setArrays(Object arrays[])
		{
			this.arrays = arrays;
		}

		public ParallelSorter create()
		{
			return (ParallelSorter)super.create(ClassesKey.create(arrays));
		}

		public void generateClass(ClassVisitor v)
			throws Exception
		{
			if (arrays.length == 0)
				throw new IllegalArgumentException("No arrays specified to sort");
			for (int i = 0; i < arrays.length; i++)
				if (!arrays[i].getClass().isArray())
					throw new IllegalArgumentException((new StringBuilder()).append(arrays[i].getClass()).append(" is not an array").toString());

			new ParallelSorterEmitter(v, getClassName(), arrays);
		}

		protected Object firstInstance(Class type)
		{
			return ((ParallelSorter)ReflectUtils.newInstance(type)).newInstance(arrays);
		}

		protected Object nextInstance(Object instance)
		{
			return ((ParallelSorter)instance).newInstance(arrays);
		}


		public Generator()
		{
			super(SOURCE);
		}
	}

	static class ByteComparer
		implements Comparer
	{

		private byte a[];

		public int compare(int i, int j)
		{
			return a[i] - a[j];
		}

		public ByteComparer(byte a[])
		{
			this.a = a;
		}
	}

	static class ShortComparer
		implements Comparer
	{

		private short a[];

		public int compare(int i, int j)
		{
			return a[i] - a[j];
		}

		public ShortComparer(short a[])
		{
			this.a = a;
		}
	}

	static class DoubleComparer
		implements Comparer
	{

		private double a[];

		public int compare(int i, int j)
		{
			double vi = a[i];
			double vj = a[j];
			return vi != vj ? vi <= vj ? -1 : 1 : 0;
		}

		public DoubleComparer(double a[])
		{
			this.a = a;
		}
	}

	static class FloatComparer
		implements Comparer
	{

		private float a[];

		public int compare(int i, int j)
		{
			float vi = a[i];
			float vj = a[j];
			return vi != vj ? vi <= vj ? -1 : 1 : 0;
		}

		public FloatComparer(float a[])
		{
			this.a = a;
		}
	}

	static class LongComparer
		implements Comparer
	{

		private long a[];

		public int compare(int i, int j)
		{
			long vi = a[i];
			long vj = a[j];
			return vi != vj ? vi <= vj ? -1 : 1 : 0;
		}

		public LongComparer(long a[])
		{
			this.a = a;
		}
	}

	static class IntComparer
		implements Comparer
	{

		private int a[];

		public int compare(int i, int j)
		{
			return a[i] - a[j];
		}

		public IntComparer(int a[])
		{
			this.a = a;
		}
	}

	static class ObjectComparer
		implements Comparer
	{

		private Object a[];

		public int compare(int i, int j)
		{
			return ((Comparable)a[i]).compareTo(a[j]);
		}

		public ObjectComparer(Object a[])
		{
			this.a = a;
		}
	}

	static class ComparatorComparer
		implements Comparer
	{

		private Object a[];
		private Comparator cmp;

		public int compare(int i, int j)
		{
			return cmp.compare(a[i], a[j]);
		}

		public ComparatorComparer(Object a[], Comparator cmp)
		{
			this.a = a;
			this.cmp = cmp;
		}
	}

	static interface Comparer
	{

		public abstract int compare(int i, int j);
	}


	protected Object a[];
	private Comparer comparer;

	protected ParallelSorter()
	{
	}

	public abstract ParallelSorter newInstance(Object aobj[]);

	public static ParallelSorter create(Object arrays[])
	{
		Generator gen = new Generator();
		gen.setArrays(arrays);
		return gen.create();
	}

	private int len()
	{
		return ((Object[])(Object[])a[0]).length;
	}

	public void quickSort(int index)
	{
		quickSort(index, 0, len(), null);
	}

	public void quickSort(int index, int lo, int hi)
	{
		quickSort(index, lo, hi, null);
	}

	public void quickSort(int index, Comparator cmp)
	{
		quickSort(index, 0, len(), cmp);
	}

	public void quickSort(int index, int lo, int hi, Comparator cmp)
	{
		chooseComparer(index, cmp);
		super.quickSort(lo, hi - 1);
	}

	public void mergeSort(int index)
	{
		mergeSort(index, 0, len(), null);
	}

	public void mergeSort(int index, int lo, int hi)
	{
		mergeSort(index, lo, hi, null);
	}

	public void mergeSort(int index, Comparator cmp)
	{
		mergeSort(index, 0, len(), cmp);
	}

	public void mergeSort(int index, int lo, int hi, Comparator cmp)
	{
		chooseComparer(index, cmp);
		super.mergeSort(lo, hi - 1);
	}

	private void chooseComparer(int index, Comparator cmp)
	{
		Object array = a[index];
		Class type = array.getClass().getComponentType();
		if (type.equals(Integer.TYPE))
			comparer = new IntComparer((int[])(int[])array);
		else
		if (type.equals(Long.TYPE))
			comparer = new LongComparer((long[])(long[])array);
		else
		if (type.equals(Double.TYPE))
			comparer = new DoubleComparer((double[])(double[])array);
		else
		if (type.equals(Float.TYPE))
			comparer = new FloatComparer((float[])(float[])array);
		else
		if (type.equals(Short.TYPE))
			comparer = new ShortComparer((short[])(short[])array);
		else
		if (type.equals(Byte.TYPE))
			comparer = new ByteComparer((byte[])(byte[])array);
		else
		if (cmp != null)
			comparer = new ComparatorComparer((Object[])(Object[])array, cmp);
		else
			comparer = new ObjectComparer((Object[])(Object[])array);
	}

	protected int compare(int i, int j)
	{
		return comparer.compare(i, j);
	}
}
