// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NumberUtils.java

package org.springframework.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.*;
import java.util.*;

// Referenced classes of package org.springframework.util:
//			Assert, StringUtils

public abstract class NumberUtils
{

	private static final BigInteger LONG_MIN = BigInteger.valueOf(0x8000000000000000L);
	private static final BigInteger LONG_MAX = BigInteger.valueOf(0x7fffffffffffffffL);
	public static final Set STANDARD_NUMBER_TYPES;

	public NumberUtils()
	{
	}

	public static Number convertNumberToTargetClass(Number number, Class targetClass)
		throws IllegalArgumentException
	{
		Assert.notNull(number, "Number must not be null");
		Assert.notNull(targetClass, "Target class must not be null");
		if (targetClass.isInstance(number))
			return number;
		if (java/lang/Byte == targetClass)
		{
			long value = checkedLongValue(number, targetClass);
			if (value < -128L || value > 127L)
				raiseOverflowException(number, targetClass);
			return Byte.valueOf(number.byteValue());
		}
		if (java/lang/Short == targetClass)
		{
			long value = checkedLongValue(number, targetClass);
			if (value < -32768L || value > 32767L)
				raiseOverflowException(number, targetClass);
			return Short.valueOf(number.shortValue());
		}
		if (java/lang/Integer == targetClass)
		{
			long value = checkedLongValue(number, targetClass);
			if (value < 0xffffffff80000000L || value > 0x7fffffffL)
				raiseOverflowException(number, targetClass);
			return Integer.valueOf(number.intValue());
		}
		if (java/lang/Long == targetClass)
		{
			long value = checkedLongValue(number, targetClass);
			return Long.valueOf(value);
		}
		if (java/math/BigInteger == targetClass)
			if (number instanceof BigDecimal)
				return ((BigDecimal)number).toBigInteger();
			else
				return BigInteger.valueOf(number.longValue());
		if (java/lang/Float == targetClass)
			return Float.valueOf(number.floatValue());
		if (java/lang/Double == targetClass)
			return Double.valueOf(number.doubleValue());
		if (java/math/BigDecimal == targetClass)
			return new BigDecimal(number.toString());
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Could not convert number [").append(number).append("] of type [").append(number.getClass().getName()).append("] to unsupported target class [").append(targetClass.getName()).append("]").toString());
	}

	private static long checkedLongValue(Number number, Class targetClass)
	{
		BigInteger bigInt = null;
		if (number instanceof BigInteger)
			bigInt = (BigInteger)number;
		else
		if (number instanceof BigDecimal)
			bigInt = ((BigDecimal)number).toBigInteger();
		if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0))
			raiseOverflowException(number, targetClass);
		return number.longValue();
	}

	private static void raiseOverflowException(Number number, Class targetClass)
	{
		throw new IllegalArgumentException((new StringBuilder()).append("Could not convert number [").append(number).append("] of type [").append(number.getClass().getName()).append("] to target class [").append(targetClass.getName()).append("]: overflow").toString());
	}

	public static Number parseNumber(String text, Class targetClass)
	{
		Assert.notNull(text, "Text must not be null");
		Assert.notNull(targetClass, "Target class must not be null");
		String trimmed = StringUtils.trimAllWhitespace(text);
		if (java/lang/Byte == targetClass)
			return isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed);
		if (java/lang/Short == targetClass)
			return isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed);
		if (java/lang/Integer == targetClass)
			return isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed);
		if (java/lang/Long == targetClass)
			return isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed);
		if (java/math/BigInteger == targetClass)
			return isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed);
		if (java/lang/Float == targetClass)
			return Float.valueOf(trimmed);
		if (java/lang/Double == targetClass)
			return Double.valueOf(trimmed);
		if (java/math/BigDecimal == targetClass || java/lang/Number == targetClass)
			return new BigDecimal(trimmed);
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Cannot convert String [").append(text).append("] to target class [").append(targetClass.getName()).append("]").toString());
	}

	public static Number parseNumber(String text, Class targetClass, NumberFormat numberFormat)
	{
		DecimalFormat decimalFormat;
		Exception exception;
		if (numberFormat != null)
		{
			Assert.notNull(text, "Text must not be null");
			Assert.notNull(targetClass, "Target class must not be null");
			decimalFormat = null;
			boolean resetBigDecimal = false;
			if (numberFormat instanceof DecimalFormat)
			{
				decimalFormat = (DecimalFormat)numberFormat;
				if (java/math/BigDecimal == targetClass && !decimalFormat.isParseBigDecimal())
				{
					decimalFormat.setParseBigDecimal(true);
					resetBigDecimal = true;
				}
			}
			Number number1;
			try
			{
				Number number = numberFormat.parse(StringUtils.trimAllWhitespace(text));
				number1 = convertNumberToTargetClass(number, targetClass);
			}
			catch (ParseException ex)
			{
				throw new IllegalArgumentException((new StringBuilder()).append("Could not parse number: ").append(ex.getMessage()).toString());
			}
			finally
			{
				if (!resetBigDecimal) goto _L0; else goto _L0
			}
			if (resetBigDecimal)
				decimalFormat.setParseBigDecimal(false);
			return number1;
		} else
		{
			return parseNumber(text, targetClass);
		}
		decimalFormat.setParseBigDecimal(false);
		throw exception;
	}

	private static boolean isHexNumber(String value)
	{
		int index = value.startsWith("-") ? 1 : 0;
		return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index);
	}

	private static BigInteger decodeBigInteger(String value)
	{
		int radix = 10;
		int index = 0;
		boolean negative = false;
		if (value.startsWith("-"))
		{
			negative = true;
			index++;
		}
		if (value.startsWith("0x", index) || value.startsWith("0X", index))
		{
			index += 2;
			radix = 16;
		} else
		if (value.startsWith("#", index))
		{
			index++;
			radix = 16;
		} else
		if (value.startsWith("0", index) && value.length() > 1 + index)
		{
			index++;
			radix = 8;
		}
		BigInteger result = new BigInteger(value.substring(index), radix);
		return negative ? result.negate() : result;
	}

	static 
	{
		Set numberTypes = new HashSet(8);
		numberTypes.add(java/lang/Byte);
		numberTypes.add(java/lang/Short);
		numberTypes.add(java/lang/Integer);
		numberTypes.add(java/lang/Long);
		numberTypes.add(java/math/BigInteger);
		numberTypes.add(java/lang/Float);
		numberTypes.add(java/lang/Double);
		numberTypes.add(java/math/BigDecimal);
		STANDARD_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
	}
}
