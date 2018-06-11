// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FieldWriter.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			FieldVisitor, ClassReader, Opcodes, ByteVector, 
//			AnnotationWriter, ClassWriter, Item, Attribute, 
//			AnnotationVisitor, TypePath

final class FieldWriter extends FieldVisitor
{

	private final ClassWriter cw;
	private final int access;
	private final int name;
	private final int desc;
	private int signature;
	private int value;
	private AnnotationWriter anns;
	private AnnotationWriter ianns;
	private AnnotationWriter tanns;
	private AnnotationWriter itanns;
	private Attribute attrs;

	FieldWriter(ClassWriter cw, int access, String name, String desc, String signature, Object value)
	{
		super(0x50000);
		if (cw.firstField == null)
			cw.firstField = this;
		else
			cw.lastField.fv = this;
		cw.lastField = this;
		this.cw = cw;
		this.access = access;
		this.name = cw.newUTF8(name);
		this.desc = cw.newUTF8(desc);
		if (signature != null)
			this.signature = cw.newUTF8(signature);
		if (value != null)
			this.value = cw.newConstItem(value).index;
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, 2);
		if (visible)
		{
			aw.next = anns;
			anns = aw;
		} else
		{
			aw.next = ianns;
			ianns = aw;
		}
		return aw;
	}

	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		AnnotationWriter.putTarget(typeRef, typePath, bv);
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
		if (visible)
		{
			aw.next = tanns;
			tanns = aw;
		} else
		{
			aw.next = itanns;
			itanns = aw;
		}
		return aw;
	}

	public void visitAttribute(Attribute attr)
	{
		attr.next = attrs;
		attrs = attr;
	}

	public void visitEnd()
	{
	}

	int getSize()
	{
		int size = 8;
		if (value != 0)
		{
			cw.newUTF8("ConstantValue");
			size += 8;
		}
		if ((access & 0x1000) != 0 && ((cw.version & 0xffff) < 49 || (access & 0x40000) != 0))
		{
			cw.newUTF8("Synthetic");
			size += 6;
		}
		if ((access & 0x20000) != 0)
		{
			cw.newUTF8("Deprecated");
			size += 6;
		}
		if (signature != 0)
		{
			cw.newUTF8("Signature");
			size += 8;
		}
		if (anns != null)
		{
			cw.newUTF8("RuntimeVisibleAnnotations");
			size += 8 + anns.getSize();
		}
		if (ianns != null)
		{
			cw.newUTF8("RuntimeInvisibleAnnotations");
			size += 8 + ianns.getSize();
		}
		if (tanns != null)
		{
			cw.newUTF8("RuntimeVisibleTypeAnnotations");
			size += 8 + tanns.getSize();
		}
		if (itanns != null)
		{
			cw.newUTF8("RuntimeInvisibleTypeAnnotations");
			size += 8 + itanns.getSize();
		}
		if (attrs != null)
			size += attrs.getSize(cw, null, 0, -1, -1);
		return size;
	}

	void put(ByteVector out)
	{
		int FACTOR = 64;
		int mask = 0x60000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(desc);
		int attributeCount = 0;
		if (value != 0)
			attributeCount++;
		if ((access & 0x1000) != 0 && ((cw.version & 0xffff) < 49 || (access & 0x40000) != 0))
			attributeCount++;
		if ((access & 0x20000) != 0)
			attributeCount++;
		if (signature != 0)
			attributeCount++;
		if (anns != null)
			attributeCount++;
		if (ianns != null)
			attributeCount++;
		if (tanns != null)
			attributeCount++;
		if (itanns != null)
			attributeCount++;
		if (attrs != null)
			attributeCount += attrs.getCount();
		out.putShort(attributeCount);
		if (value != 0)
		{
			out.putShort(cw.newUTF8("ConstantValue"));
			out.putInt(2).putShort(value);
		}
		if ((access & 0x1000) != 0 && ((cw.version & 0xffff) < 49 || (access & 0x40000) != 0))
			out.putShort(cw.newUTF8("Synthetic")).putInt(0);
		if ((access & 0x20000) != 0)
			out.putShort(cw.newUTF8("Deprecated")).putInt(0);
		if (signature != 0)
		{
			out.putShort(cw.newUTF8("Signature"));
			out.putInt(2).putShort(signature);
		}
		if (anns != null)
		{
			out.putShort(cw.newUTF8("RuntimeVisibleAnnotations"));
			anns.put(out);
		}
		if (ianns != null)
		{
			out.putShort(cw.newUTF8("RuntimeInvisibleAnnotations"));
			ianns.put(out);
		}
		if (tanns != null)
		{
			out.putShort(cw.newUTF8("RuntimeVisibleTypeAnnotations"));
			tanns.put(out);
		}
		if (itanns != null)
		{
			out.putShort(cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
			itanns.put(out);
		}
		if (attrs != null)
			attrs.put(cw, null, 0, -1, -1, out);
	}
}
