// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassEmitter.java

package org.springframework.cglib.core;

import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.*;
import org.springframework.cglib.transform.ClassTransformer;

// Referenced classes of package org.springframework.cglib.core:
//			ClassInfo, TypeUtils, Signature, CodeEmitter, 
//			Constants

public class ClassEmitter extends ClassTransformer
{
	static class FieldInfo
	{

		int access;
		String name;
		Type type;
		Object value;

		public boolean equals(Object o)
		{
			if (o == null)
				return false;
			if (!(o instanceof FieldInfo))
				return false;
			FieldInfo other = (FieldInfo)o;
			if (access != other.access || !name.equals(other.name) || !type.equals(other.type))
				return false;
			if ((value == null) ^ (other.value == null))
				return false;
			return value == null || value.equals(other.value);
		}

		public int hashCode()
		{
			return access ^ name.hashCode() ^ type.hashCode() ^ (value != null ? value.hashCode() : 0);
		}

		public FieldInfo(int access, String name, Type type, Object value)
		{
			this.access = access;
			this.name = name;
			this.type = type;
			this.value = value;
		}
	}


	private ClassInfo classInfo;
	private Map fieldInfo;
	private static int hookCounter;
	private MethodVisitor rawStaticInit;
	private CodeEmitter staticInit;
	private CodeEmitter staticHook;
	private Signature staticHookSig;

	public ClassEmitter(ClassVisitor cv)
	{
		setTarget(cv);
	}

	public ClassEmitter()
	{
		super(0x50000);
	}

	public void setTarget(ClassVisitor cv)
	{
		this.cv = cv;
		fieldInfo = new HashMap();
		staticInit = staticHook = null;
		staticHookSig = null;
	}

	private static synchronized int getNextHook()
	{
		return ++hookCounter;
	}

	public ClassInfo getClassInfo()
	{
		return classInfo;
	}

	public void begin_class(int version, final int access, String className, final Type superType, final Type interfaces[], String source)
	{
		final Type classType = Type.getType((new StringBuilder()).append("L").append(className.replace('.', '/')).append(";").toString());
		classInfo = new ClassInfo() {

			final Type val$classType;
			final Type val$superType;
			final Type val$interfaces[];
			final int val$access;
			final ClassEmitter this$0;

			public Type getType()
			{
				return classType;
			}

			public Type getSuperType()
			{
				return superType == null ? Constants.TYPE_OBJECT : superType;
			}

			public Type[] getInterfaces()
			{
				return interfaces;
			}

			public int getModifiers()
			{
				return access;
			}

			
			{
				this.this$0 = ClassEmitter.this;
				classType = type;
				superType = type1;
				interfaces = atype;
				access = i;
				super();
			}
		};
		cv.visit(version, access, classInfo.getType().getInternalName(), null, classInfo.getSuperType().getInternalName(), TypeUtils.toInternalNames(interfaces));
		if (source != null)
			cv.visitSource(source, null);
		init();
	}

	public CodeEmitter getStaticHook()
	{
		if (TypeUtils.isInterface(getAccess()))
			throw new IllegalStateException("static hook is invalid for this class");
		if (staticHook == null)
		{
			staticHookSig = new Signature((new StringBuilder()).append("CGLIB$STATICHOOK").append(getNextHook()).toString(), "()V");
			staticHook = begin_method(8, staticHookSig, null);
			if (staticInit != null)
				staticInit.invoke_static_this(staticHookSig);
		}
		return staticHook;
	}

	protected void init()
	{
	}

	public int getAccess()
	{
		return classInfo.getModifiers();
	}

	public Type getClassType()
	{
		return classInfo.getType();
	}

	public Type getSuperType()
	{
		return classInfo.getSuperType();
	}

	public void end_class()
	{
		if (staticHook != null && staticInit == null)
			begin_static();
		if (staticInit != null)
		{
			staticHook.return_value();
			staticHook.end_method();
			rawStaticInit.visitInsn(177);
			rawStaticInit.visitMaxs(0, 0);
			staticInit = staticHook = null;
			staticHookSig = null;
		}
		cv.visitEnd();
	}

	public CodeEmitter begin_method(int access, Signature sig, Type exceptions[])
	{
		if (classInfo == null)
			throw new IllegalStateException((new StringBuilder()).append("classInfo is null! ").append(this).toString());
		MethodVisitor v = cv.visitMethod(access, sig.getName(), sig.getDescriptor(), null, TypeUtils.toInternalNames(exceptions));
		if (sig.equals(Constants.SIG_STATIC) && !TypeUtils.isInterface(getAccess()))
		{
			rawStaticInit = v;
			MethodVisitor wrapped = new MethodVisitor(0x50000, v) {

				final ClassEmitter this$0;

				public void visitMaxs(int i, int j)
				{
				}

				public void visitInsn(int insn)
				{
					if (insn != 177)
						super.visitInsn(insn);
				}

			
			{
				this.this$0 = ClassEmitter.this;
				super(x0, x1);
			}
			};
			staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
			if (staticHook == null)
				getStaticHook();
			else
				staticInit.invoke_static_this(staticHookSig);
			return staticInit;
		}
		if (sig.equals(staticHookSig))
			return new CodeEmitter(this, v, access, sig, exceptions) {

				final ClassEmitter this$0;

				public boolean isStaticHook()
				{
					return true;
				}

			
			{
				this.this$0 = ClassEmitter.this;
				super(ce, mv, access, sig, exceptionTypes);
			}
			};
		else
			return new CodeEmitter(this, v, access, sig, exceptions);
	}

	public CodeEmitter begin_static()
	{
		return begin_method(8, Constants.SIG_STATIC, null);
	}

	public void declare_field(int access, String name, Type type, Object value)
	{
		FieldInfo existing = (FieldInfo)fieldInfo.get(name);
		FieldInfo info = new FieldInfo(access, name, type, value);
		if (existing != null)
		{
			if (!info.equals(existing))
				throw new IllegalArgumentException((new StringBuilder()).append("Field \"").append(name).append("\" has been declared differently").toString());
		} else
		{
			fieldInfo.put(name, info);
			cv.visitField(access, name, type.getDescriptor(), null, value);
		}
	}

	boolean isFieldDeclared(String name)
	{
		return fieldInfo.get(name) != null;
	}

	FieldInfo getFieldInfo(String name)
	{
		FieldInfo field = (FieldInfo)fieldInfo.get(name);
		if (field == null)
			throw new IllegalArgumentException((new StringBuilder()).append("Field ").append(name).append(" is not declared in ").append(getClassType().getClassName()).toString());
		else
			return field;
	}

	public void visit(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		begin_class(version, access, name.replace('/', '.'), TypeUtils.fromInternalName(superName), TypeUtils.fromInternalNames(interfaces), null);
	}

	public void visitEnd()
	{
		end_class();
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		declare_field(access, name, Type.getType(desc), value);
		return null;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return begin_method(access, new Signature(name, desc), TypeUtils.fromInternalNames(exceptions));
	}
}
