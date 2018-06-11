// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   KeyFactory.java

package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.*;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.internal.CustomizerRegistry;

// Referenced classes of package org.springframework.cglib.core:
//			KeyFactoryCustomizer, TypeUtils, Signature, Customizer, 
//			FieldTypeCustomizer, HashCodeCustomizer, AbstractClassGenerator, EmitUtils, 
//			ReflectUtils, ClassEmitter, CodeEmitter, Constants

public abstract class KeyFactory
{
	public static class Generator extends AbstractClassGenerator
	{

		private static final AbstractClassGenerator.Source SOURCE = new AbstractClassGenerator.Source(org/springframework/cglib/core/KeyFactory.getName());
		private static final Class KNOWN_CUSTOMIZER_TYPES[] = {
			org/springframework/cglib/core/Customizer, org/springframework/cglib/core/FieldTypeCustomizer
		};
		private Class keyInterface;
		private CustomizerRegistry customizers;
		private int constant;
		private int multiplier;

		protected ClassLoader getDefaultClassLoader()
		{
			return keyInterface.getClassLoader();
		}

		protected ProtectionDomain getProtectionDomain()
		{
			return ReflectUtils.getProtectionDomain(keyInterface);
		}

		/**
		 * @deprecated Method setCustomizer is deprecated
		 */

		public void setCustomizer(Customizer customizer)
		{
			customizers = CustomizerRegistry.singleton(customizer);
		}

		public void addCustomizer(KeyFactoryCustomizer customizer)
		{
			customizers.add(customizer);
		}

		public List getCustomizers(Class klass)
		{
			return customizers.get(klass);
		}

		public void setInterface(Class keyInterface)
		{
			this.keyInterface = keyInterface;
		}

		public KeyFactory create()
		{
			setNamePrefix(keyInterface.getName());
			return (KeyFactory)super.create(keyInterface.getName());
		}

		public void setHashConstant(int constant)
		{
			this.constant = constant;
		}

		public void setHashMultiplier(int multiplier)
		{
			this.multiplier = multiplier;
		}

		protected Object firstInstance(Class type)
		{
			return ReflectUtils.newInstance(type);
		}

		protected Object nextInstance(Object instance)
		{
			return instance;
		}

		public void generateClass(ClassVisitor v)
		{
			ClassEmitter ce = new ClassEmitter(v);
			Method newInstance = ReflectUtils.findNewInstance(keyInterface);
			if (!newInstance.getReturnType().equals(java/lang/Object))
				throw new IllegalArgumentException("newInstance method must return Object");
			Type parameterTypes[] = TypeUtils.getTypes(newInstance.getParameterTypes());
			ce.begin_class(46, 1, getClassName(), KeyFactory.KEY_FACTORY, new Type[] {
				Type.getType(keyInterface)
			}, "<generated>");
			EmitUtils.null_constructor(ce);
			EmitUtils.factory_method(ce, ReflectUtils.getSignature(newInstance));
			int seed = 0;
			CodeEmitter e = ce.begin_method(1, TypeUtils.parseConstructor(parameterTypes), null);
			e.load_this();
			e.super_invoke_constructor();
			e.load_this();
			List fieldTypeCustomizers = getCustomizers(org/springframework/cglib/core/FieldTypeCustomizer);
			for (int i = 0; i < parameterTypes.length; i++)
			{
				Type parameterType = parameterTypes[i];
				Type fieldType = parameterType;
				for (Iterator iterator = fieldTypeCustomizers.iterator(); iterator.hasNext();)
				{
					FieldTypeCustomizer customizer = (FieldTypeCustomizer)iterator.next();
					fieldType = customizer.getOutType(i, fieldType);
				}

				seed += fieldType.hashCode();
				ce.declare_field(18, getFieldName(i), fieldType, null);
				e.dup();
				e.load_arg(i);
				FieldTypeCustomizer customizer;
				for (Iterator iterator1 = fieldTypeCustomizers.iterator(); iterator1.hasNext(); customizer.customize(e, i, parameterType))
					customizer = (FieldTypeCustomizer)iterator1.next();

				e.putfield(getFieldName(i));
			}

			e.return_value();
			e.end_method();
			e = ce.begin_method(1, KeyFactory.HASH_CODE, null);
			int hc = constant == 0 ? KeyFactory.PRIMES[Math.abs(seed) % KeyFactory.PRIMES.length] : constant;
			int hm = multiplier == 0 ? KeyFactory.PRIMES[Math.abs(seed * 13) % KeyFactory.PRIMES.length] : multiplier;
			e.push(hc);
			for (int i = 0; i < parameterTypes.length; i++)
			{
				e.load_this();
				e.getfield(getFieldName(i));
				EmitUtils.hash_code(e, parameterTypes[i], hm, customizers);
			}

			e.return_value();
			e.end_method();
			e = ce.begin_method(1, KeyFactory.EQUALS, null);
			org.springframework.asm.Label fail = e.make_label();
			e.load_arg(0);
			e.instance_of_this();
			CodeEmitter  = e;
			e.if_jump(153, fail);
			for (int i = 0; i < parameterTypes.length; i++)
			{
				e.load_this();
				e.getfield(getFieldName(i));
				e.load_arg(0);
				e.checkcast_this();
				e.getfield(getFieldName(i));
				EmitUtils.not_equals(e, parameterTypes[i], fail, customizers);
			}

			e.push(1);
			e.return_value();
			e.mark(fail);
			e.push(0);
			e.return_value();
			e.end_method();
			e = ce.begin_method(1, KeyFactory.TO_STRING, null);
			e.new_instance(Constants.TYPE_STRING_BUFFER);
			e.dup();
			e.invoke_constructor(Constants.TYPE_STRING_BUFFER);
			for (int i = 0; i < parameterTypes.length; i++)
			{
				if (i > 0)
				{
					e.push(", ");
					e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.APPEND_STRING);
				}
				e.load_this();
				e.getfield(getFieldName(i));
				EmitUtils.append_string(e, parameterTypes[i], EmitUtils.DEFAULT_DELIMITERS, customizers);
			}

			e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.TO_STRING);
			e.return_value();
			e.end_method();
			ce.end_class();
		}

		private String getFieldName(int arg)
		{
			return (new StringBuilder()).append("FIELD_").append(arg).toString();
		}


		public Generator()
		{
			super(SOURCE);
			customizers = new CustomizerRegistry(KNOWN_CUSTOMIZER_TYPES);
		}
	}


	private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
	private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
	private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
	private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
	private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
	private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
	private static final Type KEY_FACTORY = TypeUtils.parseType("org.springframework.cglib.core.KeyFactory");
	private static final Signature GET_SORT = TypeUtils.parseSignature("int getSort()");
	private static final int PRIMES[] = {
		11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 
		5189, 7247, 10037, 13931, 19289, 26627, 36683, 50441, 0x10f1b, 0x174a9, 
		0x20039, 0x2bfd3, 0x3c6cd, 0x53059, 0x72077, 0x9c95b, 0xd6fdb, 0x127313, 0x1954c1, 0x22c6f9, 
		0x2fbeb3, 0x418c2b, 0x59fc4b, 0x7b8881, 0xa99717, 0xe8d0bf, 0x13f9c61, 0x1b6c2c5, 0x25a5403, 0x33adf2d, 
		0x46f1fa5, 0x6164929, 0x85b31e5, 0xb78a8d5, 0xfbf6821, 0x159e426f, 0x1dad5a43, 0x28bd8a15, 0x37ed85c9, 0x4cc6e229, 
		0x6965f003
	};
	public static final Customizer CLASS_BY_NAME = new Customizer() {

		public void customize(CodeEmitter e, Type type)
		{
			if (type.equals(Constants.TYPE_CLASS))
				e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
		}

	};
	public static final FieldTypeCustomizer STORE_CLASS_AS_STRING = new FieldTypeCustomizer() {

		public void customize(CodeEmitter e, int index, Type type)
		{
			if (type.equals(Constants.TYPE_CLASS))
				e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
		}

		public Type getOutType(int index, Type type)
		{
			if (type.equals(Constants.TYPE_CLASS))
				return Constants.TYPE_STRING;
			else
				return type;
		}

	};
	public static final HashCodeCustomizer HASH_ASM_TYPE = new HashCodeCustomizer() {

		public boolean customize(CodeEmitter e, Type type)
		{
			if (Constants.TYPE_TYPE.equals(type))
			{
				e.invoke_virtual(type, KeyFactory.GET_SORT);
				return true;
			} else
			{
				return false;
			}
		}

	};
	/**
	 * @deprecated Field OBJECT_BY_CLASS is deprecated
	 */
	public static final Customizer OBJECT_BY_CLASS = new Customizer() {

		public void customize(CodeEmitter e, Type type)
		{
			e.invoke_virtual(Constants.TYPE_OBJECT, KeyFactory.GET_CLASS);
		}

	};

	protected KeyFactory()
	{
	}

	public static KeyFactory create(Class keyInterface)
	{
		return create(keyInterface, null);
	}

	public static KeyFactory create(Class keyInterface, Customizer customizer)
	{
		return create(keyInterface.getClassLoader(), keyInterface, customizer);
	}

	public static KeyFactory create(Class keyInterface, KeyFactoryCustomizer first, List next)
	{
		return create(keyInterface.getClassLoader(), keyInterface, first, next);
	}

	public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer)
	{
		return create(loader, keyInterface, ((KeyFactoryCustomizer) (customizer)), Collections.emptyList());
	}

	public static KeyFactory create(ClassLoader loader, Class keyInterface, KeyFactoryCustomizer customizer, List next)
	{
		Generator gen = new Generator();
		gen.setInterface(keyInterface);
		if (customizer != null)
			gen.addCustomizer(customizer);
		if (next != null && !next.isEmpty())
		{
			KeyFactoryCustomizer keyFactoryCustomizer;
			for (Iterator iterator = next.iterator(); iterator.hasNext(); gen.addCustomizer(keyFactoryCustomizer))
				keyFactoryCustomizer = (KeyFactoryCustomizer)iterator.next();

		}
		gen.setClassLoader(loader);
		return gen.create();
	}










}
