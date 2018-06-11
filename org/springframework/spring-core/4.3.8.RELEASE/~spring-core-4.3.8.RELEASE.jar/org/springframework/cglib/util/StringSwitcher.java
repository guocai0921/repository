// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   StringSwitcher.java

package org.springframework.cglib.util;

import java.util.Arrays;
import java.util.List;
import org.springframework.asm.*;
import org.springframework.cglib.core.*;

public abstract class StringSwitcher
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final org.springframework.cglib.core.AbstractClassGenerator.Source SOURCE = new org.springframework.cglib.core.AbstractClassGenerator.Source(org/springframework/cglib/util/StringSwitcher.getName());
		private String strings[];
		private int ints[];
		private boolean fixedInput;

		public void setStrings(String strings[])
		{
			this.strings = strings;
		}

		public void setInts(int ints[])
		{
			this.ints = ints;
		}

		public void setFixedInput(boolean fixedInput)
		{
			this.fixedInput = fixedInput;
		}

		protected ClassLoader getDefaultClassLoader()
		{
			return getClass().getClassLoader();
		}

		public StringSwitcher create()
		{
			setNamePrefix(org/springframework/cglib/util/StringSwitcher.getName());
			Object key = StringSwitcher.KEY_FACTORY.newInstance(strings, ints, fixedInput);
			return (StringSwitcher)super.create(key);
		}

		public void generateClass(ClassVisitor v)
			throws Exception
		{
			ClassEmitter ce = new ClassEmitter(v);
			ce.begin_class(46, 1, getClassName(), StringSwitcher.STRING_SWITCHER, null, "<generated>");
			EmitUtils.null_constructor(ce);
			final CodeEmitter e = ce.begin_method(1, StringSwitcher.INT_VALUE, null);
			e.load_arg(0);
			final List stringList = Arrays.asList(strings);
			int style = fixedInput ? 2 : 1;
			EmitUtils.string_switch(e, strings, style, new ObjectSwitchCallback() {

				final CodeEmitter val$e;
				final List val$stringList;
				final Generator this$0;

				public void processCase(Object key, Label end)
				{
					e.push(ints[stringList.indexOf(key)]);
					e.return_value();
				}

				public void processDefault()
				{
					e.push(-1);
					e.return_value();
				}

				
				{
					this.this$0 = Generator.this;
					e = codeemitter;
					stringList = list;
					super();
				}
			});
			e.end_method();
			ce.end_class();
		}

		protected Object firstInstance(Class type)
		{
			return (StringSwitcher)ReflectUtils.newInstance(type);
		}

		protected Object nextInstance(Object instance)
		{
			return instance;
		}



		public Generator()
		{
			super(SOURCE);
		}
	}

	static interface StringSwitcherKey
	{

		public abstract Object newInstance(String as[], int ai[], boolean flag);
	}


	private static final Type STRING_SWITCHER = TypeUtils.parseType("org.springframework.cglib.util.StringSwitcher");
	private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue(String)");
	private static final StringSwitcherKey KEY_FACTORY = (StringSwitcherKey)KeyFactory.create(org/springframework/cglib/util/StringSwitcher$StringSwitcherKey);

	public static StringSwitcher create(String strings[], int ints[], boolean fixedInput)
	{
		Generator gen = new Generator();
		gen.setStrings(strings);
		gen.setInts(ints);
		gen.setFixedInput(fixedInput);
		return gen.create();
	}

	protected StringSwitcher()
	{
	}

	public abstract int intValue(String s);




}
