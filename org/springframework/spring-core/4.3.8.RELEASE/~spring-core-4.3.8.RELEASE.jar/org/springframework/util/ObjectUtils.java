// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ObjectUtils.java

package org.springframework.util;

import java.lang.reflect.Array;
import java.util.*;

public abstract class ObjectUtils
{

	private static final int INITIAL_HASH = 7;
	private static final int MULTIPLIER = 31;
	private static final String EMPTY_STRING = "";
	private static final String NULL_STRING = "null";
	private static final String ARRAY_START = "{";
	private static final String ARRAY_END = "}";
	private static final String EMPTY_ARRAY = "{}";
	private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

	public ObjectUtils()
	{
	}

	public static boolean isCheckedException(Throwable ex)
	{
		return !(ex instanceof RuntimeException) && !(ex instanceof Error);
	}

	public static transient boolean isCompatibleWithThrowsClause(Throwable ex, Class declaredExceptions[])
	{
		if (!isCheckedException(ex))
			return true;
		if (declaredExceptions != null)
		{
			Class aclass[] = declaredExceptions;
			int i = aclass.length;
			for (int j = 0; j < i; j++)
			{
				Class declaredException = aclass[j];
				if (declaredException.isInstance(ex))
					return true;
			}

		}
		return false;
	}

	public static boolean isArray(Object obj)
	{
		return obj != null && obj.getClass().isArray();
	}

	public static boolean isEmpty(Object array[])
	{
		return array == null || array.length == 0;
	}

	public static boolean isEmpty(Object obj)
	{
		if (obj == null)
			return true;
		if (obj.getClass().isArray())
			return Array.getLength(obj) == 0;
		if (obj instanceof CharSequence)
			return ((CharSequence)obj).length() == 0;
		if (obj instanceof Collection)
			return ((Collection)obj).isEmpty();
		if (obj instanceof Map)
			return ((Map)obj).isEmpty();
		else
			return false;
	}

	public static boolean containsElement(Object array[], Object element)
	{
		if (array == null)
			return false;
		Object aobj[] = array;
		int i = aobj.length;
		for (int j = 0; j < i; j++)
		{
			Object arrayEle = aobj[j];
			if (nullSafeEquals(arrayEle, element))
				return true;
		}

		return false;
	}

	public static boolean containsConstant(Enum enumValues[], String constant)
	{
		return containsConstant(enumValues, constant, false);
	}

	public static boolean containsConstant(Enum enumValues[], String constant, boolean caseSensitive)
	{
		Enum aenum[] = enumValues;
		int i = aenum.length;
		for (int j = 0; j < i; j++)
		{
			Enum candidate = aenum[j];
			if (caseSensitive ? candidate.toString().equals(constant) : candidate.toString().equalsIgnoreCase(constant))
				return true;
		}

		return false;
	}

	public static Enum caseInsensitiveValueOf(Enum enumValues[], String constant)
	{
		Enum aenum[] = enumValues;
		int i = aenum.length;
		for (int j = 0; j < i; j++)
		{
			Enum candidate = aenum[j];
			if (candidate.toString().equalsIgnoreCase(constant))
				return candidate;
		}

		throw new IllegalArgumentException(String.format("constant [%s] does not exist in enum type %s", new Object[] {
			constant, enumValues.getClass().getComponentType().getName()
		}));
	}

	public static Object[] addObjectToArray(Object array[], Object obj)
	{
		Class compType = java/lang/Object;
		if (array != null)
			compType = ((Object) (array)).getClass().getComponentType();
		else
		if (obj != null)
			compType = obj.getClass();
		int newArrLength = array == null ? 1 : array.length + 1;
		Object newArr[] = (Object[])(Object[])Array.newInstance(compType, newArrLength);
		if (array != null)
			System.arraycopy(((Object) (array)), 0, ((Object) (newArr)), 0, array.length);
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	public static Object[] toObjectArray(Object source)
	{
		if (source instanceof Object[])
			return (Object[])(Object[])source;
		if (source == null)
			return new Object[0];
		if (!source.getClass().isArray())
			throw new IllegalArgumentException((new StringBuilder()).append("Source is not an array: ").append(source).toString());
		int length = Array.getLength(source);
		if (length == 0)
			return new Object[0];
		Class wrapperType = Array.get(source, 0).getClass();
		Object newArray[] = (Object[])(Object[])Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++)
			newArray[i] = Array.get(source, i);

		return newArray;
	}

	public static boolean nullSafeEquals(Object o1, Object o2)
	{
		if (o1 == o2)
			return true;
		if (o1 == null || o2 == null)
			return false;
		if (o1.equals(o2))
			return true;
		if (o1.getClass().isArray() && o2.getClass().isArray())
			return arrayEquals(o1, o2);
		else
			return false;
	}

	private static boolean arrayEquals(Object o1, Object o2)
	{
		if ((o1 instanceof Object[]) && (o2 instanceof Object[]))
			return Arrays.equals((Object[])(Object[])o1, (Object[])(Object[])o2);
		if ((o1 instanceof boolean[]) && (o2 instanceof boolean[]))
			return Arrays.equals((boolean[])(boolean[])o1, (boolean[])(boolean[])o2);
		if ((o1 instanceof byte[]) && (o2 instanceof byte[]))
			return Arrays.equals((byte[])(byte[])o1, (byte[])(byte[])o2);
		if ((o1 instanceof char[]) && (o2 instanceof char[]))
			return Arrays.equals((char[])(char[])o1, (char[])(char[])o2);
		if ((o1 instanceof double[]) && (o2 instanceof double[]))
			return Arrays.equals((double[])(double[])o1, (double[])(double[])o2);
		if ((o1 instanceof float[]) && (o2 instanceof float[]))
			return Arrays.equals((float[])(float[])o1, (float[])(float[])o2);
		if ((o1 instanceof int[]) && (o2 instanceof int[]))
			return Arrays.equals((int[])(int[])o1, (int[])(int[])o2);
		if ((o1 instanceof long[]) && (o2 instanceof long[]))
			return Arrays.equals((long[])(long[])o1, (long[])(long[])o2);
		if ((o1 instanceof short[]) && (o2 instanceof short[]))
			return Arrays.equals((short[])(short[])o1, (short[])(short[])o2);
		else
			return false;
	}

	public static int nullSafeHashCode(Object obj)
	{
		if (obj == null)
			return 0;
		if (obj.getClass().isArray())
		{
			if (obj instanceof Object[])
				return nullSafeHashCode((Object[])(Object[])obj);
			if (obj instanceof boolean[])
				return nullSafeHashCode((boolean[])(boolean[])obj);
			if (obj instanceof byte[])
				return nullSafeHashCode((byte[])(byte[])obj);
			if (obj instanceof char[])
				return nullSafeHashCode((char[])(char[])obj);
			if (obj instanceof double[])
				return nullSafeHashCode((double[])(double[])obj);
			if (obj instanceof float[])
				return nullSafeHashCode((float[])(float[])obj);
			if (obj instanceof int[])
				return nullSafeHashCode((int[])(int[])obj);
			if (obj instanceof long[])
				return nullSafeHashCode((long[])(long[])obj);
			if (obj instanceof short[])
				return nullSafeHashCode((short[])(short[])obj);
		}
		return obj.hashCode();
	}

	public static int nullSafeHashCode(Object array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		Object aobj[] = array;
		int i = aobj.length;
		for (int j = 0; j < i; j++)
		{
			Object element = aobj[j];
			hash = 31 * hash + nullSafeHashCode(element);
		}

		return hash;
	}

	public static int nullSafeHashCode(boolean array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		boolean aflag[] = array;
		int i = aflag.length;
		for (int j = 0; j < i; j++)
		{
			boolean element = aflag[j];
			hash = 31 * hash + hashCode(element);
		}

		return hash;
	}

	public static int nullSafeHashCode(byte array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		byte abyte0[] = array;
		int i = abyte0.length;
		for (int j = 0; j < i; j++)
		{
			byte element = abyte0[j];
			hash = 31 * hash + element;
		}

		return hash;
	}

	public static int nullSafeHashCode(char array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		char ac[] = array;
		int i = ac.length;
		for (int j = 0; j < i; j++)
		{
			char element = ac[j];
			hash = 31 * hash + element;
		}

		return hash;
	}

	public static int nullSafeHashCode(double array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		double ad[] = array;
		int i = ad.length;
		for (int j = 0; j < i; j++)
		{
			double element = ad[j];
			hash = 31 * hash + hashCode(element);
		}

		return hash;
	}

	public static int nullSafeHashCode(float array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		float af[] = array;
		int i = af.length;
		for (int j = 0; j < i; j++)
		{
			float element = af[j];
			hash = 31 * hash + hashCode(element);
		}

		return hash;
	}

	public static int nullSafeHashCode(int array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		int ai[] = array;
		int i = ai.length;
		for (int j = 0; j < i; j++)
		{
			int element = ai[j];
			hash = 31 * hash + element;
		}

		return hash;
	}

	public static int nullSafeHashCode(long array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		long al[] = array;
		int i = al.length;
		for (int j = 0; j < i; j++)
		{
			long element = al[j];
			hash = 31 * hash + hashCode(element);
		}

		return hash;
	}

	public static int nullSafeHashCode(short array[])
	{
		if (array == null)
			return 0;
		int hash = 7;
		short aword0[] = array;
		int i = aword0.length;
		for (int j = 0; j < i; j++)
		{
			short element = aword0[j];
			hash = 31 * hash + element;
		}

		return hash;
	}

	public static int hashCode(boolean bool)
	{
		return bool ? 1231 : '\u04D5';
	}

	public static int hashCode(double dbl)
	{
		return hashCode(Double.doubleToLongBits(dbl));
	}

	public static int hashCode(float flt)
	{
		return Float.floatToIntBits(flt);
	}

	public static int hashCode(long lng)
	{
		return (int)(lng ^ lng >>> 32);
	}

	public static String identityToString(Object obj)
	{
		if (obj == null)
			return "";
		else
			return (new StringBuilder()).append(obj.getClass().getName()).append("@").append(getIdentityHexString(obj)).toString();
	}

	public static String getIdentityHexString(Object obj)
	{
		return Integer.toHexString(System.identityHashCode(obj));
	}

	public static String getDisplayString(Object obj)
	{
		if (obj == null)
			return "";
		else
			return nullSafeToString(obj);
	}

	public static String nullSafeClassName(Object obj)
	{
		return obj == null ? "null" : obj.getClass().getName();
	}

	public static String nullSafeToString(Object obj)
	{
		if (obj == null)
			return "null";
		if (obj instanceof String)
			return (String)obj;
		if (obj instanceof Object[])
			return nullSafeToString((Object[])(Object[])obj);
		if (obj instanceof boolean[])
			return nullSafeToString((boolean[])(boolean[])obj);
		if (obj instanceof byte[])
			return nullSafeToString((byte[])(byte[])obj);
		if (obj instanceof char[])
			return nullSafeToString((char[])(char[])obj);
		if (obj instanceof double[])
			return nullSafeToString((double[])(double[])obj);
		if (obj instanceof float[])
			return nullSafeToString((float[])(float[])obj);
		if (obj instanceof int[])
			return nullSafeToString((int[])(int[])obj);
		if (obj instanceof long[])
			return nullSafeToString((long[])(long[])obj);
		if (obj instanceof short[])
		{
			return nullSafeToString((short[])(short[])obj);
		} else
		{
			String str = obj.toString();
			return str == null ? "" : str;
		}
	}

	public static String nullSafeToString(Object array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(String.valueOf(array[i]));
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(boolean array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(byte array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(char array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append("'").append(array[i]).append("'");
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(double array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(float array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(int array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(long array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}

	public static String nullSafeToString(short array[])
	{
		if (array == null)
			return "null";
		int length = array.length;
		if (length == 0)
			return "{}";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			if (i == 0)
				sb.append("{");
			else
				sb.append(", ");
			sb.append(array[i]);
		}

		sb.append("}");
		return sb.toString();
	}
}
