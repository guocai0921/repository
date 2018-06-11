// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassMetadataReadingVisitor.java

package org.springframework.core.type.classreading;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.asm.*;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.ClassUtils;

class ClassMetadataReadingVisitor extends ClassVisitor
	implements ClassMetadata
{
	private static class EmptyFieldVisitor extends FieldVisitor
	{

		public EmptyFieldVisitor()
		{
			super(0x50000);
		}
	}

	private static class EmptyMethodVisitor extends MethodVisitor
	{

		public EmptyMethodVisitor()
		{
			super(0x50000);
		}
	}

	private static class EmptyAnnotationVisitor extends AnnotationVisitor
	{

		public AnnotationVisitor visitAnnotation(String name, String desc)
		{
			return this;
		}

		public AnnotationVisitor visitArray(String name)
		{
			return this;
		}

		public EmptyAnnotationVisitor()
		{
			super(0x50000);
		}
	}


	private String className;
	private boolean isInterface;
	private boolean isAnnotation;
	private boolean isAbstract;
	private boolean isFinal;
	private String enclosingClassName;
	private boolean independentInnerClass;
	private String superClassName;
	private String interfaces[];
	private Set memberClassNames;

	public ClassMetadataReadingVisitor()
	{
		super(0x50000);
		memberClassNames = new LinkedHashSet();
	}

	public void visit(int version, int access, String name, String signature, String supername, String interfaces[])
	{
		className = ClassUtils.convertResourcePathToClassName(name);
		isInterface = (access & 0x200) != 0;
		isAnnotation = (access & 0x2000) != 0;
		isAbstract = (access & 0x400) != 0;
		isFinal = (access & 0x10) != 0;
		if (supername != null && !isInterface)
			superClassName = ClassUtils.convertResourcePathToClassName(supername);
		this.interfaces = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++)
			this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);

	}

	public void visitOuterClass(String owner, String name, String desc)
	{
		enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
	}

	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if (outerName != null)
		{
			String fqName = ClassUtils.convertResourcePathToClassName(name);
			String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
			if (className.equals(fqName))
			{
				enclosingClassName = fqOuterName;
				independentInnerClass = (access & 8) != 0;
			} else
			if (className.equals(fqOuterName))
				memberClassNames.add(fqName);
		}
	}

	public void visitSource(String s, String s1)
	{
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		return new EmptyAnnotationVisitor();
	}

	public void visitAttribute(Attribute attribute)
	{
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		return new EmptyFieldVisitor();
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return new EmptyMethodVisitor();
	}

	public void visitEnd()
	{
	}

	public String getClassName()
	{
		return className;
	}

	public boolean isInterface()
	{
		return isInterface;
	}

	public boolean isAnnotation()
	{
		return isAnnotation;
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public boolean isConcrete()
	{
		return !isInterface && !isAbstract;
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	public boolean isIndependent()
	{
		return enclosingClassName == null || independentInnerClass;
	}

	public boolean hasEnclosingClass()
	{
		return enclosingClassName != null;
	}

	public String getEnclosingClassName()
	{
		return enclosingClassName;
	}

	public boolean hasSuperClass()
	{
		return superClassName != null;
	}

	public String getSuperClassName()
	{
		return superClassName;
	}

	public String[] getInterfaceNames()
	{
		return interfaces;
	}

	public String[] getMemberClassNames()
	{
		return (String[])memberClassNames.toArray(new String[memberClassNames.size()]);
	}
}
