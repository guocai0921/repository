// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ControlFlowFactory.java

package org.springframework.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.util.Assert;

// Referenced classes of package org.springframework.core:
//			ControlFlow

/**
 * @deprecated Class ControlFlowFactory is deprecated
 */

public abstract class ControlFlowFactory
{
	static class Jdk14ControlFlow
		implements ControlFlow
	{

		private StackTraceElement stack[];

		public boolean under(Class clazz)
		{
			Assert.notNull(clazz, "Class must not be null");
			String className = clazz.getName();
			StackTraceElement astacktraceelement[] = stack;
			int i = astacktraceelement.length;
			for (int j = 0; j < i; j++)
			{
				StackTraceElement element = astacktraceelement[j];
				if (element.getClassName().equals(className))
					return true;
			}

			return false;
		}

		public boolean under(Class clazz, String methodName)
		{
			Assert.notNull(clazz, "Class must not be null");
			Assert.notNull(methodName, "Method name must not be null");
			String className = clazz.getName();
			StackTraceElement astacktraceelement[] = stack;
			int i = astacktraceelement.length;
			for (int j = 0; j < i; j++)
			{
				StackTraceElement element = astacktraceelement[j];
				if (element.getClassName().equals(className) && element.getMethodName().equals(methodName))
					return true;
			}

			return false;
		}

		public boolean underToken(String token)
		{
			if (token == null)
			{
				return false;
			} else
			{
				StringWriter sw = new StringWriter();
				(new Throwable()).printStackTrace(new PrintWriter(sw));
				String stackTrace = sw.toString();
				return stackTrace.contains(token);
			}
		}

		public String toString()
		{
			StringBuilder sb = new StringBuilder("Jdk14ControlFlow: ");
			for (int i = 0; i < stack.length; i++)
			{
				if (i > 0)
					sb.append("\n\t@");
				sb.append(stack[i]);
			}

			return sb.toString();
		}

		public Jdk14ControlFlow()
		{
			stack = (new Throwable()).getStackTrace();
		}
	}


	public ControlFlowFactory()
	{
	}

	public static ControlFlow createControlFlow()
	{
		return new Jdk14ControlFlow();
	}
}
