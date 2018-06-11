// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassReader.java

package org.springframework.asm;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package org.springframework.asm:
//			Item, ClassWriter, ByteVector, Attribute, 
//			Context, Opcodes, MethodWriter, Label, 
//			Handle, TypePath, ClassVisitor, FieldVisitor, 
//			MethodVisitor, AnnotationVisitor, Type

public class ClassReader
{

	static final boolean SIGNATURES = true;
	static final boolean ANNOTATIONS = true;
	static final boolean FRAMES = true;
	static final boolean WRITER = true;
	static final boolean RESIZE = true;
	public static final int SKIP_CODE = 1;
	public static final int SKIP_DEBUG = 2;
	public static final int SKIP_FRAMES = 4;
	public static final int EXPAND_FRAMES = 8;
	static final int EXPAND_ASM_INSNS = 256;
	public final byte b[];
	private final int items[];
	private final String strings[];
	private final int maxStringLength;
	public final int header;

	public ClassReader(byte b[])
	{
		this(b, 0, b.length);
	}

	public ClassReader(byte b[], int off, int len)
	{
		this.b = b;
		items = new int[readUnsignedShort(off + 8)];
		int n = items.length;
		strings = new String[n];
		int max = 0;
		int index = off + 10;
		for (int i = 1; i < n; i++)
		{
			items[i] = index + 1;
			int size;
			switch (b[index])
			{
			case 3: // '\003'
			case 4: // '\004'
			case 9: // '\t'
			case 10: // '\n'
			case 11: // '\013'
			case 12: // '\f'
			case 18: // '\022'
				size = 5;
				break;

			case 5: // '\005'
			case 6: // '\006'
				size = 9;
				i++;
				break;

			case 1: // '\001'
				size = 3 + readUnsignedShort(index + 1);
				if (size > max)
					max = size;
				break;

			case 15: // '\017'
				size = 4;
				break;

			case 2: // '\002'
			case 7: // '\007'
			case 8: // '\b'
			case 13: // '\r'
			case 14: // '\016'
			case 16: // '\020'
			case 17: // '\021'
			default:
				size = 3;
				break;
			}
			index += size;
		}

		maxStringLength = max;
		header = index;
	}

	public int getAccess()
	{
		return readUnsignedShort(header);
	}

	public String getClassName()
	{
		return readClass(header + 2, new char[maxStringLength]);
	}

	public String getSuperName()
	{
		return readClass(header + 4, new char[maxStringLength]);
	}

	public String[] getInterfaces()
	{
		int index = header + 6;
		int n = readUnsignedShort(index);
		String interfaces[] = new String[n];
		if (n > 0)
		{
			char buf[] = new char[maxStringLength];
			for (int i = 0; i < n; i++)
			{
				index += 2;
				interfaces[i] = readClass(index, buf);
			}

		}
		return interfaces;
	}

	void copyPool(ClassWriter classWriter)
	{
		char buf[] = new char[maxStringLength];
		int ll = items.length;
		Item items2[] = new Item[ll];
		for (int i = 1; i < ll; i++)
		{
			int index = items[i];
			int tag = b[index - 1];
			Item item = new Item(i);
			switch (tag)
			{
			case 9: // '\t'
			case 10: // '\n'
			case 11: // '\013'
			{
				int nameType = items[readUnsignedShort(index + 2)];
				item.set(tag, readClass(index, buf), readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
				break;
			}

			case 3: // '\003'
			{
				item.set(readInt(index));
				break;
			}

			case 4: // '\004'
			{
				item.set(Float.intBitsToFloat(readInt(index)));
				break;
			}

			case 12: // '\f'
			{
				item.set(tag, readUTF8(index, buf), readUTF8(index + 2, buf), null);
				break;
			}

			case 5: // '\005'
			{
				item.set(readLong(index));
				i++;
				break;
			}

			case 6: // '\006'
			{
				item.set(Double.longBitsToDouble(readLong(index)));
				i++;
				break;
			}

			case 1: // '\001'
			{
				String s = strings[i];
				if (s == null)
				{
					index = items[i];
					s = strings[i] = readUTF(index + 2, readUnsignedShort(index), buf);
				}
				item.set(tag, s, null, null);
				break;
			}

			case 15: // '\017'
			{
				int fieldOrMethodRef = items[readUnsignedShort(index + 1)];
				int nameType = items[readUnsignedShort(fieldOrMethodRef + 2)];
				item.set(20 + readByte(index), readClass(fieldOrMethodRef, buf), readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
				break;
			}

			case 18: // '\022'
			{
				if (classWriter.bootstrapMethods == null)
					copyBootstrapMethods(classWriter, items2, buf);
				int nameType = items[readUnsignedShort(index + 2)];
				item.set(readUTF8(nameType, buf), readUTF8(nameType + 2, buf), readUnsignedShort(index));
				break;
			}

			case 2: // '\002'
			case 7: // '\007'
			case 8: // '\b'
			case 13: // '\r'
			case 14: // '\016'
			case 16: // '\020'
			case 17: // '\021'
			default:
			{
				item.set(tag, readUTF8(index, buf), null, null);
				break;
			}
			}
			int index2 = item.hashCode % items2.length;
			item.next = items2[index2];
			items2[index2] = item;
		}

		int off = items[1] - 1;
		classWriter.pool.putByteArray(b, off, header - off);
		classWriter.items = items2;
		classWriter.threshold = (int)(0.75D * (double)ll);
		classWriter.index = ll;
	}

	private void copyBootstrapMethods(ClassWriter classWriter, Item items[], char c[])
	{
		int u = getAttributes();
		boolean found = false;
		int i = readUnsignedShort(u);
		do
		{
			if (i <= 0)
				break;
			String attrName = readUTF8(u + 2, c);
			if ("BootstrapMethods".equals(attrName))
			{
				found = true;
				break;
			}
			u += 6 + readInt(u + 4);
			i--;
		} while (true);
		if (!found)
			return;
		int boostrapMethodCount = readUnsignedShort(u + 8);
		int j = 0;
		int v = u + 10;
		for (; j < boostrapMethodCount; j++)
		{
			int position = v - u - 10;
			int hashCode = readConst(readUnsignedShort(v), c).hashCode();
			for (int k = readUnsignedShort(v + 2); k > 0; k--)
			{
				hashCode ^= readConst(readUnsignedShort(v + 4), c).hashCode();
				v += 2;
			}

			v += 4;
			Item item = new Item(j);
			item.set(position, hashCode & 0x7fffffff);
			int index = item.hashCode % items.length;
			item.next = items[index];
			items[index] = item;
		}

		int attrSize = readInt(u + 4);
		ByteVector bootstrapMethods = new ByteVector(attrSize + 62);
		bootstrapMethods.putByteArray(b, u + 10, attrSize - 2);
		classWriter.bootstrapMethodsCount = boostrapMethodCount;
		classWriter.bootstrapMethods = bootstrapMethods;
	}

	public ClassReader(InputStream is)
		throws IOException
	{
		this(readClass(is, false));
	}

	public ClassReader(String name)
		throws IOException
	{
		this(readClass(ClassLoader.getSystemResourceAsStream((new StringBuilder()).append(name.replace('.', '/')).append(".class").toString()), true));
	}

	private static byte[] readClass(InputStream is, boolean close)
		throws IOException
	{
		if (is == null)
			throw new IOException("Class not found");
		byte b[];
		int len;
		b = new byte[is.available()];
		len = 0;
_L4:
		int n = is.read(b, len, b.length - len);
		if (n != -1) goto _L2; else goto _L1
_L1:
		byte abyte0[];
		if (len < b.length)
		{
			byte c[] = new byte[len];
			System.arraycopy(b, 0, c, 0, len);
			b = c;
		}
		abyte0 = b;
		if (close)
			is.close();
		return abyte0;
_L2:
		len += n;
		if (len != b.length) goto _L4; else goto _L3
_L3:
		int last;
		byte abyte1[];
		last = is.read();
		if (last >= 0)
			break MISSING_BLOCK_LABEL_114;
		abyte1 = b;
		if (close)
			is.close();
		return abyte1;
		byte c[] = new byte[b.length + 1000];
		System.arraycopy(b, 0, c, 0, len);
		c[len++] = (byte)last;
		b = c;
		  goto _L4
		Exception exception;
		exception;
		if (close)
			is.close();
		throw exception;
	}

	public void accept(ClassVisitor classVisitor, int flags)
	{
		accept(classVisitor, new Attribute[0], flags);
	}

	public void accept(ClassVisitor classVisitor, Attribute attrs[], int flags)
	{
		int u = header;
		char c[] = new char[maxStringLength];
		Context context = new Context();
		context.attrs = attrs;
		context.flags = flags;
		context.buffer = c;
		int access = readUnsignedShort(u);
		String name = readClass(u + 2, c);
		String superClass = readClass(u + 4, c);
		String interfaces[] = new String[readUnsignedShort(u + 6)];
		u += 8;
		for (int i = 0; i < interfaces.length; i++)
		{
			interfaces[i] = readClass(u, c);
			u += 2;
		}

		String signature = null;
		String sourceFile = null;
		String sourceDebug = null;
		String enclosingOwner = null;
		String enclosingName = null;
		String enclosingDesc = null;
		int anns = 0;
		int ianns = 0;
		int tanns = 0;
		int itanns = 0;
		int innerClasses = 0;
		Attribute attributes = null;
		u = getAttributes();
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			String attrName = readUTF8(u + 2, c);
			if ("SourceFile".equals(attrName))
				sourceFile = readUTF8(u + 8, c);
			else
			if ("InnerClasses".equals(attrName))
				innerClasses = u + 8;
			else
			if ("EnclosingMethod".equals(attrName))
			{
				enclosingOwner = readClass(u + 8, c);
				int item = readUnsignedShort(u + 10);
				if (item != 0)
				{
					enclosingName = readUTF8(items[item], c);
					enclosingDesc = readUTF8(items[item] + 2, c);
				}
			} else
			if ("Signature".equals(attrName))
				signature = readUTF8(u + 8, c);
			else
			if ("RuntimeVisibleAnnotations".equals(attrName))
				anns = u + 8;
			else
			if ("RuntimeVisibleTypeAnnotations".equals(attrName))
				tanns = u + 8;
			else
			if ("Deprecated".equals(attrName))
				access |= 0x20000;
			else
			if ("Synthetic".equals(attrName))
				access |= 0x41000;
			else
			if ("SourceDebugExtension".equals(attrName))
			{
				int len = readInt(u + 4);
				sourceDebug = readUTF(u + 8, len, new char[len]);
			} else
			if ("RuntimeInvisibleAnnotations".equals(attrName))
				ianns = u + 8;
			else
			if ("RuntimeInvisibleTypeAnnotations".equals(attrName))
				itanns = u + 8;
			else
			if ("BootstrapMethods".equals(attrName))
			{
				int bootstrapMethods[] = new int[readUnsignedShort(u + 8)];
				int j = 0;
				int v = u + 10;
				for (; j < bootstrapMethods.length; j++)
				{
					bootstrapMethods[j] = v;
					v += 2 + readUnsignedShort(v + 2) << 1;
				}

				context.bootstrapMethods = bootstrapMethods;
			} else
			{
				Attribute attr = readAttribute(attrs, attrName, u + 8, readInt(u + 4), c, -1, null);
				if (attr != null)
				{
					attr.next = attributes;
					attributes = attr;
				}
			}
			u += 6 + readInt(u + 4);
		}

		classVisitor.visit(readInt(items[1] - 7), access, name, signature, superClass, interfaces);
		if ((flags & 2) == 0 && (sourceFile != null || sourceDebug != null))
			classVisitor.visitSource(sourceFile, sourceDebug);
		if (enclosingOwner != null)
			classVisitor.visitOuterClass(enclosingOwner, enclosingName, enclosingDesc);
		if (anns != 0)
		{
			int i = readUnsignedShort(anns);
			int v = anns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, classVisitor.visitAnnotation(readUTF8(v, c), true));

		}
		if (ianns != 0)
		{
			int i = readUnsignedShort(ianns);
			int v = ianns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, classVisitor.visitAnnotation(readUTF8(v, c), false));

		}
		if (tanns != 0)
		{
			int i = readUnsignedShort(tanns);
			int v = tanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), true));
			}

		}
		if (itanns != 0)
		{
			int i = readUnsignedShort(itanns);
			int v = itanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), false));
			}

		}
		Attribute attr;
		for (; attributes != null; attributes = attr)
		{
			attr = attributes.next;
			attributes.next = null;
			classVisitor.visitAttribute(attributes);
		}

		if (innerClasses != 0)
		{
			int v = innerClasses + 2;
			for (int i = readUnsignedShort(innerClasses); i > 0; i--)
			{
				classVisitor.visitInnerClass(readClass(v, c), readClass(v + 2, c), readUTF8(v + 4, c), readUnsignedShort(v + 6));
				v += 8;
			}

		}
		u = header + 10 + 2 * interfaces.length;
		for (int i = readUnsignedShort(u - 2); i > 0; i--)
			u = readField(classVisitor, context, u);

		u += 2;
		for (int i = readUnsignedShort(u - 2); i > 0; i--)
			u = readMethod(classVisitor, context, u);

		classVisitor.visitEnd();
	}

	private int readField(ClassVisitor classVisitor, Context context, int u)
	{
		char c[] = context.buffer;
		int access = readUnsignedShort(u);
		String name = readUTF8(u + 2, c);
		String desc = readUTF8(u + 4, c);
		u += 6;
		String signature = null;
		int anns = 0;
		int ianns = 0;
		int tanns = 0;
		int itanns = 0;
		Object value = null;
		Attribute attributes = null;
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			String attrName = readUTF8(u + 2, c);
			if ("ConstantValue".equals(attrName))
			{
				int item = readUnsignedShort(u + 8);
				value = item != 0 ? readConst(item, c) : null;
			} else
			if ("Signature".equals(attrName))
				signature = readUTF8(u + 8, c);
			else
			if ("Deprecated".equals(attrName))
				access |= 0x20000;
			else
			if ("Synthetic".equals(attrName))
				access |= 0x41000;
			else
			if ("RuntimeVisibleAnnotations".equals(attrName))
				anns = u + 8;
			else
			if ("RuntimeVisibleTypeAnnotations".equals(attrName))
				tanns = u + 8;
			else
			if ("RuntimeInvisibleAnnotations".equals(attrName))
				ianns = u + 8;
			else
			if ("RuntimeInvisibleTypeAnnotations".equals(attrName))
			{
				itanns = u + 8;
			} else
			{
				Attribute attr = readAttribute(context.attrs, attrName, u + 8, readInt(u + 4), c, -1, null);
				if (attr != null)
				{
					attr.next = attributes;
					attributes = attr;
				}
			}
			u += 6 + readInt(u + 4);
		}

		u += 2;
		FieldVisitor fv = classVisitor.visitField(access, name, desc, signature, value);
		if (fv == null)
			return u;
		if (anns != 0)
		{
			int i = readUnsignedShort(anns);
			int v = anns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, fv.visitAnnotation(readUTF8(v, c), true));

		}
		if (ianns != 0)
		{
			int i = readUnsignedShort(ianns);
			int v = ianns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, fv.visitAnnotation(readUTF8(v, c), false));

		}
		if (tanns != 0)
		{
			int i = readUnsignedShort(tanns);
			int v = tanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, fv.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), true));
			}

		}
		if (itanns != 0)
		{
			int i = readUnsignedShort(itanns);
			int v = itanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, fv.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), false));
			}

		}
		Attribute attr;
		for (; attributes != null; attributes = attr)
		{
			attr = attributes.next;
			attributes.next = null;
			fv.visitAttribute(attributes);
		}

		fv.visitEnd();
		return u;
	}

	private int readMethod(ClassVisitor classVisitor, Context context, int u)
	{
		char c[] = context.buffer;
		context.access = readUnsignedShort(u);
		context.name = readUTF8(u + 2, c);
		context.desc = readUTF8(u + 4, c);
		u += 6;
		int code = 0;
		int exception = 0;
		String exceptions[] = null;
		String signature = null;
		int methodParameters = 0;
		int anns = 0;
		int ianns = 0;
		int tanns = 0;
		int itanns = 0;
		int dann = 0;
		int mpanns = 0;
		int impanns = 0;
		int firstAttribute = u;
		Attribute attributes = null;
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			String attrName = readUTF8(u + 2, c);
			if ("Code".equals(attrName))
			{
				if ((context.flags & 1) == 0)
					code = u + 8;
			} else
			if ("Exceptions".equals(attrName))
			{
				exceptions = new String[readUnsignedShort(u + 8)];
				exception = u + 10;
				for (int j = 0; j < exceptions.length; j++)
				{
					exceptions[j] = readClass(exception, c);
					exception += 2;
				}

			} else
			if ("Signature".equals(attrName))
				signature = readUTF8(u + 8, c);
			else
			if ("Deprecated".equals(attrName))
				context.access |= 0x20000;
			else
			if ("RuntimeVisibleAnnotations".equals(attrName))
				anns = u + 8;
			else
			if ("RuntimeVisibleTypeAnnotations".equals(attrName))
				tanns = u + 8;
			else
			if ("AnnotationDefault".equals(attrName))
				dann = u + 8;
			else
			if ("Synthetic".equals(attrName))
				context.access |= 0x41000;
			else
			if ("RuntimeInvisibleAnnotations".equals(attrName))
				ianns = u + 8;
			else
			if ("RuntimeInvisibleTypeAnnotations".equals(attrName))
				itanns = u + 8;
			else
			if ("RuntimeVisibleParameterAnnotations".equals(attrName))
				mpanns = u + 8;
			else
			if ("RuntimeInvisibleParameterAnnotations".equals(attrName))
				impanns = u + 8;
			else
			if ("MethodParameters".equals(attrName))
			{
				methodParameters = u + 8;
			} else
			{
				Attribute attr = readAttribute(context.attrs, attrName, u + 8, readInt(u + 4), c, -1, null);
				if (attr != null)
				{
					attr.next = attributes;
					attributes = attr;
				}
			}
			u += 6 + readInt(u + 4);
		}

		u += 2;
		MethodVisitor mv = classVisitor.visitMethod(context.access, context.name, context.desc, signature, exceptions);
		if (mv == null)
			return u;
		if (mv instanceof MethodWriter)
		{
			MethodWriter mw = (MethodWriter)mv;
			if (mw.cw.cr == this && (signature == null ? mw.signature == null : signature.equals(mw.signature)))
			{
				boolean sameExceptions = false;
				if (exceptions == null)
					sameExceptions = mw.exceptionCount == 0;
				else
				if (exceptions.length == mw.exceptionCount)
				{
					sameExceptions = true;
					int j = exceptions.length - 1;
					do
					{
						if (j < 0)
							break;
						exception -= 2;
						if (mw.exceptions[j] != readUnsignedShort(exception))
						{
							sameExceptions = false;
							break;
						}
						j--;
					} while (true);
				}
				if (sameExceptions)
				{
					mw.classReaderOffset = firstAttribute;
					mw.classReaderLength = u - firstAttribute;
					return u;
				}
			}
		}
		if (methodParameters != 0)
		{
			int i = b[methodParameters] & 0xff;
			for (int v = methodParameters + 1; i > 0; v += 4)
			{
				mv.visitParameter(readUTF8(v, c), readUnsignedShort(v + 2));
				i--;
			}

		}
		if (dann != 0)
		{
			AnnotationVisitor dv = mv.visitAnnotationDefault();
			readAnnotationValue(dann, c, null, dv);
			if (dv != null)
				dv.visitEnd();
		}
		if (anns != 0)
		{
			int i = readUnsignedShort(anns);
			int v = anns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, mv.visitAnnotation(readUTF8(v, c), true));

		}
		if (ianns != 0)
		{
			int i = readUnsignedShort(ianns);
			int v = ianns + 2;
			for (; i > 0; i--)
				v = readAnnotationValues(v + 2, c, true, mv.visitAnnotation(readUTF8(v, c), false));

		}
		if (tanns != 0)
		{
			int i = readUnsignedShort(tanns);
			int v = tanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, mv.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), true));
			}

		}
		if (itanns != 0)
		{
			int i = readUnsignedShort(itanns);
			int v = itanns + 2;
			for (; i > 0; i--)
			{
				v = readAnnotationTarget(context, v);
				v = readAnnotationValues(v + 2, c, true, mv.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(v, c), false));
			}

		}
		if (mpanns != 0)
			readParameterAnnotations(mv, context, mpanns, true);
		if (impanns != 0)
			readParameterAnnotations(mv, context, impanns, false);
		Attribute attr;
		for (; attributes != null; attributes = attr)
		{
			attr = attributes.next;
			attributes.next = null;
			mv.visitAttribute(attributes);
		}

		if (code != 0)
		{
			mv.visitCode();
			readCode(mv, context, code);
		}
		mv.visitEnd();
		return u;
	}

	private void readCode(MethodVisitor mv, Context context, int u)
	{
		byte b[] = this.b;
		char c[] = context.buffer;
		int maxStack = readUnsignedShort(u);
		int maxLocals = readUnsignedShort(u + 2);
		int codeLength = readInt(u + 4);
		int codeStart = u += 8;
		int codeEnd = u + codeLength;
		Label labels[] = context.labels = new Label[codeLength + 2];
		readLabel(codeLength + 1, labels);
		do
		{
			if (u >= codeEnd)
				break;
			int offset = u - codeStart;
			int opcode = b[u] & 0xff;
			switch (ClassWriter.TYPE[opcode])
			{
			case 0: // '\0'
			case 4: // '\004'
				u++;
				break;

			case 9: // '\t'
				readLabel(offset + readShort(u + 1), labels);
				u += 3;
				break;

			case 18: // '\022'
				readLabel(offset + readUnsignedShort(u + 1), labels);
				u += 3;
				break;

			case 10: // '\n'
				readLabel(offset + readInt(u + 1), labels);
				u += 5;
				break;

			case 17: // '\021'
				opcode = b[u + 1] & 0xff;
				if (opcode == 132)
					u += 6;
				else
					u += 4;
				break;

			case 14: // '\016'
				u = (u + 4) - (offset & 3);
				readLabel(offset + readInt(u), labels);
				for (int i = (readInt(u + 8) - readInt(u + 4)) + 1; i > 0; i--)
				{
					readLabel(offset + readInt(u + 12), labels);
					u += 4;
				}

				u += 12;
				break;

			case 15: // '\017'
				u = (u + 4) - (offset & 3);
				readLabel(offset + readInt(u), labels);
				for (int i = readInt(u + 4); i > 0; i--)
				{
					readLabel(offset + readInt(u + 12), labels);
					u += 8;
				}

				u += 8;
				break;

			case 1: // '\001'
			case 3: // '\003'
			case 11: // '\013'
				u += 2;
				break;

			case 2: // '\002'
			case 5: // '\005'
			case 6: // '\006'
			case 12: // '\f'
			case 13: // '\r'
				u += 3;
				break;

			case 7: // '\007'
			case 8: // '\b'
				u += 5;
				break;

			case 16: // '\020'
			default:
				u += 4;
				break;
			}
		} while (true);
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			Label start = readLabel(readUnsignedShort(u + 2), labels);
			Label end = readLabel(readUnsignedShort(u + 4), labels);
			Label handler = readLabel(readUnsignedShort(u + 6), labels);
			String type = readUTF8(items[readUnsignedShort(u + 8)], c);
			mv.visitTryCatchBlock(start, end, handler, type);
			u += 8;
		}

		u += 2;
		int tanns[] = null;
		int itanns[] = null;
		int tann = 0;
		int itann = 0;
		int ntoff = -1;
		int nitoff = -1;
		int varTable = 0;
		int varTypeTable = 0;
		boolean zip = true;
		boolean unzip = (context.flags & 8) != 0;
		int stackMap = 0;
		int stackMapSize = 0;
		int frameCount = 0;
		Context frame = null;
		Attribute attributes = null;
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			String attrName = readUTF8(u + 2, c);
			if ("LocalVariableTable".equals(attrName))
			{
				if ((context.flags & 2) == 0)
				{
					varTable = u + 8;
					int j = readUnsignedShort(u + 8);
					int v = u;
					for (; j > 0; j--)
					{
						int label = readUnsignedShort(v + 10);
						if (labels[label] == null)
							readLabel(label, labels).status |= 1;
						label += readUnsignedShort(v + 12);
						if (labels[label] == null)
							readLabel(label, labels).status |= 1;
						v += 10;
					}

				}
			} else
			if ("LocalVariableTypeTable".equals(attrName))
				varTypeTable = u + 8;
			else
			if ("LineNumberTable".equals(attrName))
			{
				if ((context.flags & 2) == 0)
				{
					int j = readUnsignedShort(u + 8);
					int v = u;
					for (; j > 0; j--)
					{
						int label = readUnsignedShort(v + 10);
						if (labels[label] == null)
							readLabel(label, labels).status |= 1;
						Label l;
						for (l = labels[label]; l.line > 0; l = l.next)
							if (l.next == null)
								l.next = new Label();

						l.line = readUnsignedShort(v + 12);
						v += 4;
					}

				}
			} else
			if ("RuntimeVisibleTypeAnnotations".equals(attrName))
			{
				tanns = readTypeAnnotations(mv, context, u + 8, true);
				ntoff = tanns.length != 0 && readByte(tanns[0]) >= 67 ? readUnsignedShort(tanns[0] + 1) : -1;
			} else
			if ("RuntimeInvisibleTypeAnnotations".equals(attrName))
			{
				itanns = readTypeAnnotations(mv, context, u + 8, false);
				nitoff = itanns.length != 0 && readByte(itanns[0]) >= 67 ? readUnsignedShort(itanns[0] + 1) : -1;
			} else
			if ("StackMapTable".equals(attrName))
			{
				if ((context.flags & 4) == 0)
				{
					stackMap = u + 10;
					stackMapSize = readInt(u + 4);
					frameCount = readUnsignedShort(u + 8);
				}
			} else
			if ("StackMap".equals(attrName))
			{
				if ((context.flags & 4) == 0)
				{
					zip = false;
					stackMap = u + 10;
					stackMapSize = readInt(u + 4);
					frameCount = readUnsignedShort(u + 8);
				}
			} else
			{
				for (int j = 0; j < context.attrs.length; j++)
				{
					if (!context.attrs[j].type.equals(attrName))
						continue;
					Attribute attr = context.attrs[j].read(this, u + 8, readInt(u + 4), c, codeStart - 8, labels);
					if (attr != null)
					{
						attr.next = attributes;
						attributes = attr;
					}
				}

			}
			u += 6 + readInt(u + 4);
		}

		u += 2;
		if (stackMap != 0)
		{
			frame = context;
			frame.offset = -1;
			frame.mode = 0;
			frame.localCount = 0;
			frame.localDiff = 0;
			frame.stackCount = 0;
			frame.local = new Object[maxLocals];
			frame.stack = new Object[maxStack];
			if (unzip)
				getImplicitFrame(context);
			for (int i = stackMap; i < (stackMap + stackMapSize) - 2; i++)
			{
				if (b[i] != 8)
					continue;
				int v = readUnsignedShort(i + 1);
				if (v >= 0 && v < codeLength && (b[codeStart + v] & 0xff) == 187)
					readLabel(v, labels);
			}

		}
		if ((context.flags & 0x100) != 0)
			mv.visitFrame(-1, maxLocals, null, 0, null);
		int opcodeDelta = (context.flags & 0x100) != 0 ? 0 : -33;
		for (u = codeStart; u < codeEnd;)
		{
			int offset = u - codeStart;
			Label l = labels[offset];
			if (l != null)
			{
				Label next = l.next;
				l.next = null;
				mv.visitLabel(l);
				if ((context.flags & 2) == 0 && l.line > 0)
				{
					mv.visitLineNumber(l.line, l);
					for (; next != null; next = next.next)
						mv.visitLineNumber(next.line, l);

				}
			}
			while (frame != null && (frame.offset == offset || frame.offset == -1)) 
			{
				if (frame.offset != -1)
					if (!zip || unzip)
						mv.visitFrame(-1, frame.localCount, frame.local, frame.stackCount, frame.stack);
					else
						mv.visitFrame(frame.mode, frame.localDiff, frame.local, frame.stackCount, frame.stack);
				if (frameCount > 0)
				{
					stackMap = readFrame(stackMap, zip, unzip, frame);
					frameCount--;
				} else
				{
					frame = null;
				}
			}
			int opcode = b[u] & 0xff;
			switch (ClassWriter.TYPE[opcode])
			{
			case 0: // '\0'
			{
				mv.visitInsn(opcode);
				u++;
				break;
			}

			case 4: // '\004'
			{
				if (opcode > 54)
				{
					opcode -= 59;
					mv.visitVarInsn(54 + (opcode >> 2), opcode & 3);
				} else
				{
					opcode -= 26;
					mv.visitVarInsn(21 + (opcode >> 2), opcode & 3);
				}
				u++;
				break;
			}

			case 9: // '\t'
			{
				mv.visitJumpInsn(opcode, labels[offset + readShort(u + 1)]);
				u += 3;
				break;
			}

			case 10: // '\n'
			{
				mv.visitJumpInsn(opcode + opcodeDelta, labels[offset + readInt(u + 1)]);
				u += 5;
				break;
			}

			case 18: // '\022'
			{
				opcode = opcode >= 218 ? opcode - 20 : opcode - 49;
				Label target = labels[offset + readUnsignedShort(u + 1)];
				if (opcode == 167 || opcode == 168)
				{
					mv.visitJumpInsn(opcode + 33, target);
				} else
				{
					opcode = opcode > 166 ? opcode ^ 1 : (opcode + 1 ^ 1) - 1;
					Label endif = new Label();
					mv.visitJumpInsn(opcode, endif);
					mv.visitJumpInsn(200, target);
					mv.visitLabel(endif);
					if (stackMap != 0 && (frame == null || frame.offset != offset + 3))
						mv.visitFrame(256, 0, null, 0, null);
				}
				u += 3;
				break;
			}

			case 17: // '\021'
			{
				opcode = b[u + 1] & 0xff;
				if (opcode == 132)
				{
					mv.visitIincInsn(readUnsignedShort(u + 2), readShort(u + 4));
					u += 6;
				} else
				{
					mv.visitVarInsn(opcode, readUnsignedShort(u + 2));
					u += 4;
				}
				break;
			}

			case 14: // '\016'
			{
				u = (u + 4) - (offset & 3);
				int label = offset + readInt(u);
				int min = readInt(u + 4);
				int max = readInt(u + 8);
				Label table[] = new Label[(max - min) + 1];
				u += 12;
				for (int i = 0; i < table.length; i++)
				{
					table[i] = labels[offset + readInt(u)];
					u += 4;
				}

				mv.visitTableSwitchInsn(min, max, labels[label], table);
				break;
			}

			case 15: // '\017'
			{
				u = (u + 4) - (offset & 3);
				int label = offset + readInt(u);
				int len = readInt(u + 4);
				int keys[] = new int[len];
				Label values[] = new Label[len];
				u += 8;
				for (int i = 0; i < len; i++)
				{
					keys[i] = readInt(u);
					values[i] = labels[offset + readInt(u + 4)];
					u += 8;
				}

				mv.visitLookupSwitchInsn(labels[label], keys, values);
				break;
			}

			case 3: // '\003'
			{
				mv.visitVarInsn(opcode, b[u + 1] & 0xff);
				u += 2;
				break;
			}

			case 1: // '\001'
			{
				mv.visitIntInsn(opcode, b[u + 1]);
				u += 2;
				break;
			}

			case 2: // '\002'
			{
				mv.visitIntInsn(opcode, readShort(u + 1));
				u += 3;
				break;
			}

			case 11: // '\013'
			{
				mv.visitLdcInsn(readConst(b[u + 1] & 0xff, c));
				u += 2;
				break;
			}

			case 12: // '\f'
			{
				mv.visitLdcInsn(readConst(readUnsignedShort(u + 1), c));
				u += 3;
				break;
			}

			case 6: // '\006'
			case 7: // '\007'
			{
				int cpIndex = items[readUnsignedShort(u + 1)];
				boolean itf = b[cpIndex - 1] == 11;
				String iowner = readClass(cpIndex, c);
				cpIndex = items[readUnsignedShort(cpIndex + 2)];
				String iname = readUTF8(cpIndex, c);
				String idesc = readUTF8(cpIndex + 2, c);
				if (opcode < 182)
					mv.visitFieldInsn(opcode, iowner, iname, idesc);
				else
					mv.visitMethodInsn(opcode, iowner, iname, idesc, itf);
				if (opcode == 185)
					u += 5;
				else
					u += 3;
				break;
			}

			case 8: // '\b'
			{
				int cpIndex = items[readUnsignedShort(u + 1)];
				int bsmIndex = context.bootstrapMethods[readUnsignedShort(cpIndex)];
				Handle bsm = (Handle)readConst(readUnsignedShort(bsmIndex), c);
				int bsmArgCount = readUnsignedShort(bsmIndex + 2);
				Object bsmArgs[] = new Object[bsmArgCount];
				bsmIndex += 4;
				for (int i = 0; i < bsmArgCount; i++)
				{
					bsmArgs[i] = readConst(readUnsignedShort(bsmIndex), c);
					bsmIndex += 2;
				}

				cpIndex = items[readUnsignedShort(cpIndex + 2)];
				String iname = readUTF8(cpIndex, c);
				String idesc = readUTF8(cpIndex + 2, c);
				mv.visitInvokeDynamicInsn(iname, idesc, bsm, bsmArgs);
				u += 5;
				break;
			}

			case 5: // '\005'
			{
				mv.visitTypeInsn(opcode, readClass(u + 1, c));
				u += 3;
				break;
			}

			case 13: // '\r'
			{
				mv.visitIincInsn(b[u + 1] & 0xff, b[u + 2]);
				u += 3;
				break;
			}

			case 16: // '\020'
			default:
			{
				mv.visitMultiANewArrayInsn(readClass(u + 1, c), b[u + 3] & 0xff);
				u += 4;
				break;
			}
			}
			for (; tanns != null && tann < tanns.length && ntoff <= offset; ntoff = ++tann < tanns.length && readByte(tanns[tann]) >= 67 ? readUnsignedShort(tanns[tann] + 1) : -1)
				if (ntoff == offset)
				{
					int v = readAnnotationTarget(context, tanns[tann]);
					readAnnotationValues(v + 2, c, true, mv.visitInsnAnnotation(context.typeRef, context.typePath, readUTF8(v, c), true));
				}

			while (itanns != null && itann < itanns.length && nitoff <= offset) 
			{
				if (nitoff == offset)
				{
					int v = readAnnotationTarget(context, itanns[itann]);
					readAnnotationValues(v + 2, c, true, mv.visitInsnAnnotation(context.typeRef, context.typePath, readUTF8(v, c), false));
				}
				nitoff = ++itann < itanns.length && readByte(itanns[itann]) >= 67 ? readUnsignedShort(itanns[itann] + 1) : -1;
			}
		}

		if (labels[codeLength] != null)
			mv.visitLabel(labels[codeLength]);
		if ((context.flags & 2) == 0 && varTable != 0)
		{
			int typeTable[] = null;
			if (varTypeTable != 0)
			{
				u = varTypeTable + 2;
				typeTable = new int[readUnsignedShort(varTypeTable) * 3];
				for (int i = typeTable.length; i > 0;)
				{
					typeTable[--i] = u + 6;
					typeTable[--i] = readUnsignedShort(u + 8);
					typeTable[--i] = readUnsignedShort(u);
					u += 10;
				}

			}
			u = varTable + 2;
			for (int i = readUnsignedShort(varTable); i > 0; i--)
			{
				int start = readUnsignedShort(u);
				int length = readUnsignedShort(u + 2);
				int index = readUnsignedShort(u + 8);
				String vsignature = null;
				if (typeTable != null)
				{
					int j = 0;
					do
					{
						if (j >= typeTable.length)
							break;
						if (typeTable[j] == start && typeTable[j + 1] == index)
						{
							vsignature = readUTF8(typeTable[j + 2], c);
							break;
						}
						j += 3;
					} while (true);
				}
				mv.visitLocalVariable(readUTF8(u + 4, c), readUTF8(u + 6, c), vsignature, labels[start], labels[start + length], index);
				u += 10;
			}

		}
		if (tanns != null)
		{
			for (int i = 0; i < tanns.length; i++)
				if (readByte(tanns[i]) >> 1 == 32)
				{
					int v = readAnnotationTarget(context, tanns[i]);
					v = readAnnotationValues(v + 2, c, true, mv.visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, readUTF8(v, c), true));
				}

		}
		if (itanns != null)
		{
			for (int i = 0; i < itanns.length; i++)
				if (readByte(itanns[i]) >> 1 == 32)
				{
					int v = readAnnotationTarget(context, itanns[i]);
					v = readAnnotationValues(v + 2, c, true, mv.visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, readUTF8(v, c), false));
				}

		}
		Attribute attr;
		for (; attributes != null; attributes = attr)
		{
			attr = attributes.next;
			attributes.next = null;
			mv.visitAttribute(attributes);
		}

		mv.visitMaxs(maxStack, maxLocals);
	}

	private int[] readTypeAnnotations(MethodVisitor mv, Context context, int u, boolean visible)
	{
		char c[] = context.buffer;
		int offsets[] = new int[readUnsignedShort(u)];
		u += 2;
		for (int i = 0; i < offsets.length; i++)
		{
			offsets[i] = u;
			int target = readInt(u);
			switch (target >>> 24)
			{
			case 0: // '\0'
			case 1: // '\001'
			case 22: // '\026'
				u += 2;
				break;

			case 19: // '\023'
			case 20: // '\024'
			case 21: // '\025'
				u++;
				break;

			case 64: // '@'
			case 65: // 'A'
				for (int j = readUnsignedShort(u + 1); j > 0; j--)
				{
					int start = readUnsignedShort(u + 3);
					int length = readUnsignedShort(u + 5);
					readLabel(start, context.labels);
					readLabel(start + length, context.labels);
					u += 6;
				}

				u += 3;
				break;

			case 71: // 'G'
			case 72: // 'H'
			case 73: // 'I'
			case 74: // 'J'
			case 75: // 'K'
				u += 4;
				break;

			default:
				u += 3;
				break;
			}
			int pathLength = readByte(u);
			if (target >>> 24 == 66)
			{
				TypePath path = pathLength != 0 ? new TypePath(b, u) : null;
				u += 1 + 2 * pathLength;
				u = readAnnotationValues(u + 2, c, true, mv.visitTryCatchAnnotation(target, path, readUTF8(u, c), visible));
			} else
			{
				u = readAnnotationValues(u + 3 + 2 * pathLength, c, true, null);
			}
		}

		return offsets;
	}

	private int readAnnotationTarget(Context context, int u)
	{
		int target = readInt(u);
		switch (target >>> 24)
		{
		case 0: // '\0'
		case 1: // '\001'
		case 22: // '\026'
			target &= 0xffff0000;
			u += 2;
			break;

		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
			target &= 0xff000000;
			u++;
			break;

		case 64: // '@'
		case 65: // 'A'
			target &= 0xff000000;
			int n = readUnsignedShort(u + 1);
			context.start = new Label[n];
			context.end = new Label[n];
			context.index = new int[n];
			u += 3;
			for (int i = 0; i < n; i++)
			{
				int start = readUnsignedShort(u);
				int length = readUnsignedShort(u + 2);
				context.start[i] = readLabel(start, context.labels);
				context.end[i] = readLabel(start + length, context.labels);
				context.index[i] = readUnsignedShort(u + 4);
				u += 6;
			}

			break;

		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
			target &= 0xff0000ff;
			u += 4;
			break;

		default:
			target &= target >>> 24 >= 67 ? 0xff000000 : -256;
			u += 3;
			break;
		}
		int pathLength = readByte(u);
		context.typeRef = target;
		context.typePath = pathLength != 0 ? new TypePath(b, u) : null;
		return u + 1 + 2 * pathLength;
	}

	private void readParameterAnnotations(MethodVisitor mv, Context context, int v, boolean visible)
	{
		int n = b[v++] & 0xff;
		int synthetics = Type.getArgumentTypes(context.desc).length - n;
		int i;
		for (i = 0; i < synthetics; i++)
		{
			AnnotationVisitor av = mv.visitParameterAnnotation(i, "Ljava/lang/Synthetic;", false);
			if (av != null)
				av.visitEnd();
		}

		char c[] = context.buffer;
		for (; i < n + synthetics; i++)
		{
			int j = readUnsignedShort(v);
			v += 2;
			for (; j > 0; j--)
			{
				AnnotationVisitor av = mv.visitParameterAnnotation(i, readUTF8(v, c), visible);
				v = readAnnotationValues(v + 2, c, true, av);
			}

		}

	}

	private int readAnnotationValues(int v, char buf[], boolean named, AnnotationVisitor av)
	{
		int i = readUnsignedShort(v);
		v += 2;
		if (named)
			for (; i > 0; i--)
				v = readAnnotationValue(v + 2, buf, readUTF8(v, buf), av);

		else
			for (; i > 0; i--)
				v = readAnnotationValue(v, buf, null, av);

		if (av != null)
			av.visitEnd();
		return v;
	}

	private int readAnnotationValue(int v, char buf[], String name, AnnotationVisitor av)
	{
		if (av == null)
		{
			switch (b[v] & 0xff)
			{
			case 101: // 'e'
				return v + 5;

			case 64: // '@'
				return readAnnotationValues(v + 3, buf, true, null);

			case 91: // '['
				return readAnnotationValues(v + 1, buf, false, null);
			}
			return v + 3;
		}
label0:
		switch (b[v++] & 0xff)
		{
		case 65: // 'A'
		case 69: // 'E'
		case 71: // 'G'
		case 72: // 'H'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		case 97: // 'a'
		case 98: // 'b'
		case 100: // 'd'
		case 102: // 'f'
		case 103: // 'g'
		case 104: // 'h'
		case 105: // 'i'
		case 106: // 'j'
		case 107: // 'k'
		case 108: // 'l'
		case 109: // 'm'
		case 110: // 'n'
		case 111: // 'o'
		case 112: // 'p'
		case 113: // 'q'
		case 114: // 'r'
		default:
			break;

		case 68: // 'D'
		case 70: // 'F'
		case 73: // 'I'
		case 74: // 'J'
			av.visit(name, readConst(readUnsignedShort(v), buf));
			v += 2;
			break;

		case 66: // 'B'
			av.visit(name, Byte.valueOf((byte)readInt(items[readUnsignedShort(v)])));
			v += 2;
			break;

		case 90: // 'Z'
			av.visit(name, readInt(items[readUnsignedShort(v)]) != 0 ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)));
			v += 2;
			break;

		case 83: // 'S'
			av.visit(name, Short.valueOf((short)readInt(items[readUnsignedShort(v)])));
			v += 2;
			break;

		case 67: // 'C'
			av.visit(name, Character.valueOf((char)readInt(items[readUnsignedShort(v)])));
			v += 2;
			break;

		case 115: // 's'
			av.visit(name, readUTF8(v, buf));
			v += 2;
			break;

		case 101: // 'e'
			av.visitEnum(name, readUTF8(v, buf), readUTF8(v + 2, buf));
			v += 4;
			break;

		case 99: // 'c'
			av.visit(name, Type.getType(readUTF8(v, buf)));
			v += 2;
			break;

		case 64: // '@'
			v = readAnnotationValues(v + 2, buf, true, av.visitAnnotation(name, readUTF8(v, buf)));
			break;

		case 91: // '['
			int size = readUnsignedShort(v);
			v += 2;
			if (size == 0)
				return readAnnotationValues(v - 2, buf, false, av.visitArray(name));
			switch (b[v++] & 0xff)
			{
			case 66: // 'B'
				byte bv[] = new byte[size];
				for (int i = 0; i < size; i++)
				{
					bv[i] = (byte)readInt(items[readUnsignedShort(v)]);
					v += 3;
				}

				av.visit(name, bv);
				v--;
				break label0;

			case 90: // 'Z'
				boolean zv[] = new boolean[size];
				for (int i = 0; i < size; i++)
				{
					zv[i] = readInt(items[readUnsignedShort(v)]) != 0;
					v += 3;
				}

				av.visit(name, zv);
				v--;
				break label0;

			case 83: // 'S'
				short sv[] = new short[size];
				for (int i = 0; i < size; i++)
				{
					sv[i] = (short)readInt(items[readUnsignedShort(v)]);
					v += 3;
				}

				av.visit(name, sv);
				v--;
				break label0;

			case 67: // 'C'
				char cv[] = new char[size];
				for (int i = 0; i < size; i++)
				{
					cv[i] = (char)readInt(items[readUnsignedShort(v)]);
					v += 3;
				}

				av.visit(name, cv);
				v--;
				break label0;

			case 73: // 'I'
				int iv[] = new int[size];
				for (int i = 0; i < size; i++)
				{
					iv[i] = readInt(items[readUnsignedShort(v)]);
					v += 3;
				}

				av.visit(name, iv);
				v--;
				break label0;

			case 74: // 'J'
				long lv[] = new long[size];
				for (int i = 0; i < size; i++)
				{
					lv[i] = readLong(items[readUnsignedShort(v)]);
					v += 3;
				}

				av.visit(name, lv);
				v--;
				break label0;

			case 70: // 'F'
				float fv[] = new float[size];
				for (int i = 0; i < size; i++)
				{
					fv[i] = Float.intBitsToFloat(readInt(items[readUnsignedShort(v)]));
					v += 3;
				}

				av.visit(name, fv);
				v--;
				break label0;

			case 68: // 'D'
				double dv[] = new double[size];
				for (int i = 0; i < size; i++)
				{
					dv[i] = Double.longBitsToDouble(readLong(items[readUnsignedShort(v)]));
					v += 3;
				}

				av.visit(name, dv);
				v--;
				break;

			case 69: // 'E'
			case 71: // 'G'
			case 72: // 'H'
			case 75: // 'K'
			case 76: // 'L'
			case 77: // 'M'
			case 78: // 'N'
			case 79: // 'O'
			case 80: // 'P'
			case 81: // 'Q'
			case 82: // 'R'
			case 84: // 'T'
			case 85: // 'U'
			case 86: // 'V'
			case 87: // 'W'
			case 88: // 'X'
			case 89: // 'Y'
			default:
				v = readAnnotationValues(v - 3, buf, false, av.visitArray(name));
				break;
			}
			break;
		}
		return v;
	}

	private void getImplicitFrame(Context frame)
	{
		String desc = frame.desc;
		Object locals[] = frame.local;
		int local = 0;
		if ((frame.access & 8) == 0)
			if ("<init>".equals(frame.name))
				locals[local++] = Opcodes.UNINITIALIZED_THIS;
			else
				locals[local++] = readClass(header + 2, frame.buffer);
		int i = 1;
		do
		{
			int j = i;
			switch (desc.charAt(i++))
			{
			case 66: // 'B'
			case 67: // 'C'
			case 73: // 'I'
			case 83: // 'S'
			case 90: // 'Z'
				locals[local++] = Opcodes.INTEGER;
				break;

			case 70: // 'F'
				locals[local++] = Opcodes.FLOAT;
				break;

			case 74: // 'J'
				locals[local++] = Opcodes.LONG;
				break;

			case 68: // 'D'
				locals[local++] = Opcodes.DOUBLE;
				break;

			case 91: // '['
				for (; desc.charAt(i) == '['; i++);
				if (desc.charAt(i) == 'L')
					for (i++; desc.charAt(i) != ';'; i++);
				locals[local++] = desc.substring(j, ++i);
				break;

			case 76: // 'L'
				for (; desc.charAt(i) != ';'; i++);
				locals[local++] = desc.substring(j + 1, i++);
				break;

			case 69: // 'E'
			case 71: // 'G'
			case 72: // 'H'
			case 75: // 'K'
			case 77: // 'M'
			case 78: // 'N'
			case 79: // 'O'
			case 80: // 'P'
			case 81: // 'Q'
			case 82: // 'R'
			case 84: // 'T'
			case 85: // 'U'
			case 86: // 'V'
			case 87: // 'W'
			case 88: // 'X'
			case 89: // 'Y'
			default:
				frame.localCount = local;
				return;
			}
		} while (true);
	}

	private int readFrame(int stackMap, boolean zip, boolean unzip, Context frame)
	{
		char c[] = frame.buffer;
		Label labels[] = frame.labels;
		int tag;
		if (zip)
		{
			tag = b[stackMap++] & 0xff;
		} else
		{
			tag = 255;
			frame.offset = -1;
		}
		frame.localDiff = 0;
		int delta;
		if (tag < 64)
		{
			delta = tag;
			frame.mode = 3;
			frame.stackCount = 0;
		} else
		if (tag < 128)
		{
			delta = tag - 64;
			stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
			frame.mode = 4;
			frame.stackCount = 1;
		} else
		{
			delta = readUnsignedShort(stackMap);
			stackMap += 2;
			if (tag == 247)
			{
				stackMap = readFrameType(frame.stack, 0, stackMap, c, labels);
				frame.mode = 4;
				frame.stackCount = 1;
			} else
			if (tag >= 248 && tag < 251)
			{
				frame.mode = 2;
				frame.localDiff = 251 - tag;
				frame.localCount -= frame.localDiff;
				frame.stackCount = 0;
			} else
			if (tag == 251)
			{
				frame.mode = 3;
				frame.stackCount = 0;
			} else
			if (tag < 255)
			{
				int local = unzip ? frame.localCount : 0;
				for (int i = tag - 251; i > 0; i--)
					stackMap = readFrameType(frame.local, local++, stackMap, c, labels);

				frame.mode = 1;
				frame.localDiff = tag - 251;
				frame.localCount += frame.localDiff;
				frame.stackCount = 0;
			} else
			{
				frame.mode = 0;
				int n = readUnsignedShort(stackMap);
				stackMap += 2;
				frame.localDiff = n;
				frame.localCount = n;
				int local = 0;
				for (; n > 0; n--)
					stackMap = readFrameType(frame.local, local++, stackMap, c, labels);

				n = readUnsignedShort(stackMap);
				stackMap += 2;
				frame.stackCount = n;
				int stack = 0;
				for (; n > 0; n--)
					stackMap = readFrameType(frame.stack, stack++, stackMap, c, labels);

			}
		}
		frame.offset += delta + 1;
		readLabel(frame.offset, labels);
		return stackMap;
	}

	private int readFrameType(Object frame[], int index, int v, char buf[], Label labels[])
	{
		int type = b[v++] & 0xff;
		switch (type)
		{
		case 0: // '\0'
			frame[index] = Opcodes.TOP;
			break;

		case 1: // '\001'
			frame[index] = Opcodes.INTEGER;
			break;

		case 2: // '\002'
			frame[index] = Opcodes.FLOAT;
			break;

		case 3: // '\003'
			frame[index] = Opcodes.DOUBLE;
			break;

		case 4: // '\004'
			frame[index] = Opcodes.LONG;
			break;

		case 5: // '\005'
			frame[index] = Opcodes.NULL;
			break;

		case 6: // '\006'
			frame[index] = Opcodes.UNINITIALIZED_THIS;
			break;

		case 7: // '\007'
			frame[index] = readClass(v, buf);
			v += 2;
			break;

		default:
			frame[index] = readLabel(readUnsignedShort(v), labels);
			v += 2;
			break;
		}
		return v;
	}

	protected Label readLabel(int offset, Label labels[])
	{
		if (offset >= labels.length)
			return new Label();
		if (labels[offset] == null)
			labels[offset] = new Label();
		return labels[offset];
	}

	private int getAttributes()
	{
		int u = header + 8 + readUnsignedShort(header + 6) * 2;
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			for (int j = readUnsignedShort(u + 8); j > 0; j--)
				u += 6 + readInt(u + 12);

			u += 8;
		}

		u += 2;
		for (int i = readUnsignedShort(u); i > 0; i--)
		{
			for (int j = readUnsignedShort(u + 8); j > 0; j--)
				u += 6 + readInt(u + 12);

			u += 8;
		}

		return u + 2;
	}

	private Attribute readAttribute(Attribute attrs[], String type, int off, int len, char buf[], int codeOff, Label labels[])
	{
		for (int i = 0; i < attrs.length; i++)
			if (attrs[i].type.equals(type))
				return attrs[i].read(this, off, len, buf, codeOff, labels);

		return (new Attribute(type)).read(this, off, len, null, -1, null);
	}

	public int getItemCount()
	{
		return items.length;
	}

	public int getItem(int item)
	{
		return items[item];
	}

	public int getMaxStringLength()
	{
		return maxStringLength;
	}

	public int readByte(int index)
	{
		return b[index] & 0xff;
	}

	public int readUnsignedShort(int index)
	{
		byte b[] = this.b;
		return (b[index] & 0xff) << 8 | b[index + 1] & 0xff;
	}

	public short readShort(int index)
	{
		byte b[] = this.b;
		return (short)((b[index] & 0xff) << 8 | b[index + 1] & 0xff);
	}

	public int readInt(int index)
	{
		byte b[] = this.b;
		return (b[index] & 0xff) << 24 | (b[index + 1] & 0xff) << 16 | (b[index + 2] & 0xff) << 8 | b[index + 3] & 0xff;
	}

	public long readLong(int index)
	{
		long l1 = readInt(index);
		long l0 = (long)readInt(index + 4) & 0xffffffffL;
		return l1 << 32 | l0;
	}

	public String readUTF8(int index, char buf[])
	{
		int item = readUnsignedShort(index);
		if (index == 0 || item == 0)
			return null;
		String s = strings[item];
		if (s != null)
		{
			return s;
		} else
		{
			index = items[item];
			return strings[item] = readUTF(index + 2, readUnsignedShort(index), buf);
		}
	}

	private String readUTF(int index, int utfLen, char buf[])
	{
		int endIndex = index + utfLen;
		byte b[] = this.b;
		int strLen = 0;
		int st = 0;
		char cc = '\0';
		do
			if (index < endIndex)
			{
				int c = b[index++];
				switch (st)
				{
				case 0: // '\0'
					c &= 0xff;
					if (c < 128)
						buf[strLen++] = (char)c;
					else
					if (c < 224 && c > 191)
					{
						cc = (char)(c & 0x1f);
						st = 1;
					} else
					{
						cc = (char)(c & 0xf);
						st = 2;
					}
					break;

				case 1: // '\001'
					buf[strLen++] = (char)(cc << 6 | c & 0x3f);
					st = 0;
					break;

				case 2: // '\002'
					cc = (char)(cc << 6 | c & 0x3f);
					st = 1;
					break;
				}
			} else
			{
				return new String(buf, 0, strLen);
			}
		while (true);
	}

	public String readClass(int index, char buf[])
	{
		String name = readUTF8(items[readUnsignedShort(index)], buf);
		return name == null ? null : name.intern();
	}

	public Object readConst(int item, char buf[])
	{
		int index = this.items[item];
		switch (b[index - 1])
		{
		case 3: // '\003'
			return Integer.valueOf(readInt(index));

		case 4: // '\004'
			return Float.valueOf(Float.intBitsToFloat(readInt(index)));

		case 5: // '\005'
			return Long.valueOf(readLong(index));

		case 6: // '\006'
			return Double.valueOf(Double.longBitsToDouble(readLong(index)));

		case 7: // '\007'
			return Type.getObjectType(readUTF8(index, buf));

		case 8: // '\b'
			return readUTF8(index, buf);

		case 16: // '\020'
			return Type.getMethodType(readUTF8(index, buf));

		case 9: // '\t'
		case 10: // '\n'
		case 11: // '\013'
		case 12: // '\f'
		case 13: // '\r'
		case 14: // '\016'
		case 15: // '\017'
		default:
			int tag = readByte(index);
			int items[] = this.items;
			int cpIndex = items[readUnsignedShort(index + 1)];
			boolean itf = b[cpIndex - 1] == 11;
			String owner = readClass(cpIndex, buf);
			cpIndex = items[readUnsignedShort(cpIndex + 2)];
			String name = readUTF8(cpIndex, buf);
			String desc = readUTF8(cpIndex + 2, buf);
			return new Handle(tag, owner, name, desc, itf);
		}
	}
}
