// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ClassWriter.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			ClassVisitor, Opcodes, ClassReader, ByteVector, 
//			Item, MethodWriter, AnnotationWriter, FieldWriter, 
//			Type, Handle, Attribute, AnnotationVisitor, 
//			TypePath, FieldVisitor, MethodVisitor

public class ClassWriter extends ClassVisitor
{

	public static final int COMPUTE_MAXS = 1;
	public static final int COMPUTE_FRAMES = 2;
	static final int ACC_SYNTHETIC_ATTRIBUTE = 0x40000;
	static final int TO_ACC_SYNTHETIC = 64;
	static final int NOARG_INSN = 0;
	static final int SBYTE_INSN = 1;
	static final int SHORT_INSN = 2;
	static final int VAR_INSN = 3;
	static final int IMPLVAR_INSN = 4;
	static final int TYPE_INSN = 5;
	static final int FIELDORMETH_INSN = 6;
	static final int ITFMETH_INSN = 7;
	static final int INDYMETH_INSN = 8;
	static final int LABEL_INSN = 9;
	static final int LABELW_INSN = 10;
	static final int LDC_INSN = 11;
	static final int LDCW_INSN = 12;
	static final int IINC_INSN = 13;
	static final int TABL_INSN = 14;
	static final int LOOK_INSN = 15;
	static final int MANA_INSN = 16;
	static final int WIDE_INSN = 17;
	static final int ASM_LABEL_INSN = 18;
	static final int F_INSERT = 256;
	static final byte TYPE[];
	static final int CLASS = 7;
	static final int FIELD = 9;
	static final int METH = 10;
	static final int IMETH = 11;
	static final int STR = 8;
	static final int INT = 3;
	static final int FLOAT = 4;
	static final int LONG = 5;
	static final int DOUBLE = 6;
	static final int NAME_TYPE = 12;
	static final int UTF8 = 1;
	static final int MTYPE = 16;
	static final int HANDLE = 15;
	static final int INDY = 18;
	static final int HANDLE_BASE = 20;
	static final int TYPE_NORMAL = 30;
	static final int TYPE_UNINIT = 31;
	static final int TYPE_MERGED = 32;
	static final int BSM = 33;
	ClassReader cr;
	int version;
	int index;
	final ByteVector pool;
	Item items[];
	int threshold;
	final Item key;
	final Item key2;
	final Item key3;
	final Item key4;
	Item typeTable[];
	private short typeCount;
	private int access;
	private int name;
	String thisName;
	private int signature;
	private int superName;
	private int interfaceCount;
	private int interfaces[];
	private int sourceFile;
	private ByteVector sourceDebug;
	private int enclosingMethodOwner;
	private int enclosingMethod;
	private AnnotationWriter anns;
	private AnnotationWriter ianns;
	private AnnotationWriter tanns;
	private AnnotationWriter itanns;
	private Attribute attrs;
	private int innerClassesCount;
	private ByteVector innerClasses;
	int bootstrapMethodsCount;
	ByteVector bootstrapMethods;
	FieldWriter firstField;
	FieldWriter lastField;
	MethodWriter firstMethod;
	MethodWriter lastMethod;
	private int compute;
	boolean hasAsmInsns;

	public ClassWriter(int flags)
	{
		super(0x50000);
		index = 1;
		pool = new ByteVector();
		items = new Item[256];
		threshold = (int)(0.75D * (double)items.length);
		key = new Item();
		key2 = new Item();
		key3 = new Item();
		key4 = new Item();
		compute = (flags & 2) == 0 ? (flags & 1) == 0 ? 3 : 2 : 0;
	}

	public ClassWriter(ClassReader classReader, int flags)
	{
		this(flags);
		classReader.copyPool(this);
		cr = classReader;
	}

	public final void visit(int version, int access, String name, String signature, String superName, String interfaces[])
	{
		this.version = version;
		this.access = access;
		this.name = newClass(name);
		thisName = name;
		if (signature != null)
			this.signature = newUTF8(signature);
		this.superName = superName != null ? newClass(superName) : 0;
		if (interfaces != null && interfaces.length > 0)
		{
			interfaceCount = interfaces.length;
			this.interfaces = new int[interfaceCount];
			for (int i = 0; i < interfaceCount; i++)
				this.interfaces[i] = newClass(interfaces[i]);

		}
	}

	public final void visitSource(String file, String debug)
	{
		if (file != null)
			sourceFile = newUTF8(file);
		if (debug != null)
			sourceDebug = (new ByteVector()).encodeUTF8(debug, 0, 0x7fffffff);
	}

	public final void visitOuterClass(String owner, String name, String desc)
	{
		enclosingMethodOwner = newClass(owner);
		if (name != null && desc != null)
			enclosingMethod = newNameType(name, desc);
	}

	public final AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		bv.putShort(newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, 2);
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

	public final AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		AnnotationWriter.putTarget(typeRef, typePath, bv);
		bv.putShort(newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(this, true, bv, bv, bv.length - 2);
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

	public final void visitAttribute(Attribute attr)
	{
		attr.next = attrs;
		attrs = attr;
	}

	public final void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if (innerClasses == null)
			innerClasses = new ByteVector();
		Item nameItem = newClassItem(name);
		if (nameItem.intVal == 0)
		{
			innerClassesCount++;
			innerClasses.putShort(nameItem.index);
			innerClasses.putShort(outerName != null ? newClass(outerName) : 0);
			innerClasses.putShort(innerName != null ? newUTF8(innerName) : 0);
			innerClasses.putShort(access);
			nameItem.intVal = innerClassesCount;
		}
	}

	public final FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		return new FieldWriter(this, access, name, desc, signature, value);
	}

	public final MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[])
	{
		return new MethodWriter(this, access, name, desc, signature, exceptions, compute);
	}

	public final void visitEnd()
	{
	}

	public byte[] toByteArray()
	{
		if (index > 65535)
			throw new RuntimeException("Class file too large!");
		int size = 24 + 2 * interfaceCount;
		int nbFields = 0;
		for (FieldWriter fb = firstField; fb != null; fb = (FieldWriter)fb.fv)
		{
			nbFields++;
			size += fb.getSize();
		}

		int nbMethods = 0;
		for (MethodWriter mb = firstMethod; mb != null; mb = (MethodWriter)mb.mv)
		{
			nbMethods++;
			size += mb.getSize();
		}

		int attributeCount = 0;
		if (bootstrapMethods != null)
		{
			attributeCount++;
			size += 8 + bootstrapMethods.length;
			newUTF8("BootstrapMethods");
		}
		if (signature != 0)
		{
			attributeCount++;
			size += 8;
			newUTF8("Signature");
		}
		if (sourceFile != 0)
		{
			attributeCount++;
			size += 8;
			newUTF8("SourceFile");
		}
		if (sourceDebug != null)
		{
			attributeCount++;
			size += sourceDebug.length + 6;
			newUTF8("SourceDebugExtension");
		}
		if (enclosingMethodOwner != 0)
		{
			attributeCount++;
			size += 10;
			newUTF8("EnclosingMethod");
		}
		if ((access & 0x20000) != 0)
		{
			attributeCount++;
			size += 6;
			newUTF8("Deprecated");
		}
		if ((access & 0x1000) != 0 && ((version & 0xffff) < 49 || (access & 0x40000) != 0))
		{
			attributeCount++;
			size += 6;
			newUTF8("Synthetic");
		}
		if (innerClasses != null)
		{
			attributeCount++;
			size += 8 + innerClasses.length;
			newUTF8("InnerClasses");
		}
		if (anns != null)
		{
			attributeCount++;
			size += 8 + anns.getSize();
			newUTF8("RuntimeVisibleAnnotations");
		}
		if (ianns != null)
		{
			attributeCount++;
			size += 8 + ianns.getSize();
			newUTF8("RuntimeInvisibleAnnotations");
		}
		if (tanns != null)
		{
			attributeCount++;
			size += 8 + tanns.getSize();
			newUTF8("RuntimeVisibleTypeAnnotations");
		}
		if (itanns != null)
		{
			attributeCount++;
			size += 8 + itanns.getSize();
			newUTF8("RuntimeInvisibleTypeAnnotations");
		}
		if (attrs != null)
		{
			attributeCount += attrs.getCount();
			size += attrs.getSize(this, null, 0, -1, -1);
		}
		size += pool.length;
		ByteVector out = new ByteVector(size);
		out.putInt(0xcafebabe).putInt(version);
		out.putShort(index).putByteArray(pool.data, 0, pool.length);
		int mask = 0x60000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(superName);
		out.putShort(interfaceCount);
		for (int i = 0; i < interfaceCount; i++)
			out.putShort(interfaces[i]);

		out.putShort(nbFields);
		for (FieldWriter fb = firstField; fb != null; fb = (FieldWriter)fb.fv)
			fb.put(out);

		out.putShort(nbMethods);
		for (MethodWriter mb = firstMethod; mb != null; mb = (MethodWriter)mb.mv)
			mb.put(out);

		out.putShort(attributeCount);
		if (bootstrapMethods != null)
		{
			out.putShort(newUTF8("BootstrapMethods"));
			out.putInt(bootstrapMethods.length + 2).putShort(bootstrapMethodsCount);
			out.putByteArray(bootstrapMethods.data, 0, bootstrapMethods.length);
		}
		if (signature != 0)
			out.putShort(newUTF8("Signature")).putInt(2).putShort(signature);
		if (sourceFile != 0)
			out.putShort(newUTF8("SourceFile")).putInt(2).putShort(sourceFile);
		if (sourceDebug != null)
		{
			int len = sourceDebug.length;
			out.putShort(newUTF8("SourceDebugExtension")).putInt(len);
			out.putByteArray(sourceDebug.data, 0, len);
		}
		if (enclosingMethodOwner != 0)
		{
			out.putShort(newUTF8("EnclosingMethod")).putInt(4);
			out.putShort(enclosingMethodOwner).putShort(enclosingMethod);
		}
		if ((access & 0x20000) != 0)
			out.putShort(newUTF8("Deprecated")).putInt(0);
		if ((access & 0x1000) != 0 && ((version & 0xffff) < 49 || (access & 0x40000) != 0))
			out.putShort(newUTF8("Synthetic")).putInt(0);
		if (innerClasses != null)
		{
			out.putShort(newUTF8("InnerClasses"));
			out.putInt(innerClasses.length + 2).putShort(innerClassesCount);
			out.putByteArray(innerClasses.data, 0, innerClasses.length);
		}
		if (anns != null)
		{
			out.putShort(newUTF8("RuntimeVisibleAnnotations"));
			anns.put(out);
		}
		if (ianns != null)
		{
			out.putShort(newUTF8("RuntimeInvisibleAnnotations"));
			ianns.put(out);
		}
		if (tanns != null)
		{
			out.putShort(newUTF8("RuntimeVisibleTypeAnnotations"));
			tanns.put(out);
		}
		if (itanns != null)
		{
			out.putShort(newUTF8("RuntimeInvisibleTypeAnnotations"));
			itanns.put(out);
		}
		if (attrs != null)
			attrs.put(this, null, 0, -1, -1, out);
		if (hasAsmInsns)
		{
			anns = null;
			ianns = null;
			attrs = null;
			innerClassesCount = 0;
			innerClasses = null;
			firstField = null;
			lastField = null;
			firstMethod = null;
			lastMethod = null;
			compute = 1;
			hasAsmInsns = false;
			(new ClassReader(out.data)).accept(this, 264);
			return toByteArray();
		} else
		{
			return out.data;
		}
	}

	Item newConstItem(Object cst)
	{
		if (cst instanceof Integer)
		{
			int val = ((Integer)cst).intValue();
			return newInteger(val);
		}
		if (cst instanceof Byte)
		{
			int val = ((Byte)cst).intValue();
			return newInteger(val);
		}
		if (cst instanceof Character)
		{
			int val = ((Character)cst).charValue();
			return newInteger(val);
		}
		if (cst instanceof Short)
		{
			int val = ((Short)cst).intValue();
			return newInteger(val);
		}
		if (cst instanceof Boolean)
		{
			int val = ((Boolean)cst).booleanValue() ? 1 : 0;
			return newInteger(val);
		}
		if (cst instanceof Float)
		{
			float val = ((Float)cst).floatValue();
			return newFloat(val);
		}
		if (cst instanceof Long)
		{
			long val = ((Long)cst).longValue();
			return newLong(val);
		}
		if (cst instanceof Double)
		{
			double val = ((Double)cst).doubleValue();
			return newDouble(val);
		}
		if (cst instanceof String)
			return newString((String)cst);
		if (cst instanceof Type)
		{
			Type t = (Type)cst;
			int s = t.getSort();
			if (s == 10)
				return newClassItem(t.getInternalName());
			if (s == 11)
				return newMethodTypeItem(t.getDescriptor());
			else
				return newClassItem(t.getDescriptor());
		}
		if (cst instanceof Handle)
		{
			Handle h = (Handle)cst;
			return newHandleItem(h.tag, h.owner, h.name, h.desc, h.itf);
		} else
		{
			throw new IllegalArgumentException((new StringBuilder()).append("value ").append(cst).toString());
		}
	}

	public int newConst(Object cst)
	{
		return newConstItem(cst).index;
	}

	public int newUTF8(String value)
	{
		key.set(1, value, null, null);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(1).putUTF8(value);
			result = new Item(index++, key);
			put(result);
		}
		return result.index;
	}

	Item newClassItem(String value)
	{
		key2.set(7, value, null, null);
		Item result = get(key2);
		if (result == null)
		{
			pool.put12(7, newUTF8(value));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	public int newClass(String value)
	{
		return newClassItem(value).index;
	}

	Item newMethodTypeItem(String methodDesc)
	{
		key2.set(16, methodDesc, null, null);
		Item result = get(key2);
		if (result == null)
		{
			pool.put12(16, newUTF8(methodDesc));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	public int newMethodType(String methodDesc)
	{
		return newMethodTypeItem(methodDesc).index;
	}

	Item newHandleItem(int tag, String owner, String name, String desc, boolean itf)
	{
		key4.set(20 + tag, owner, name, desc);
		Item result = get(key4);
		if (result == null)
		{
			if (tag <= 4)
				put112(15, tag, newField(owner, name, desc));
			else
				put112(15, tag, newMethod(owner, name, desc, itf));
			result = new Item(index++, key4);
			put(result);
		}
		return result;
	}

	/**
	 * @deprecated Method newHandle is deprecated
	 */

	public int newHandle(int tag, String owner, String name, String desc)
	{
		return newHandle(tag, owner, name, desc, tag == 9);
	}

	public int newHandle(int tag, String owner, String name, String desc, boolean itf)
	{
		return newHandleItem(tag, owner, name, desc, itf).index;
	}

	transient Item newInvokeDynamicItem(String name, String desc, Handle bsm, Object bsmArgs[])
	{
		ByteVector bootstrapMethods = this.bootstrapMethods;
		if (bootstrapMethods == null)
			bootstrapMethods = this.bootstrapMethods = new ByteVector();
		int position = bootstrapMethods.length;
		int hashCode = bsm.hashCode();
		bootstrapMethods.putShort(newHandle(bsm.tag, bsm.owner, bsm.name, bsm.desc, bsm.isInterface()));
		int argsLength = bsmArgs.length;
		bootstrapMethods.putShort(argsLength);
		for (int i = 0; i < argsLength; i++)
		{
			Object bsmArg = bsmArgs[i];
			hashCode ^= bsmArg.hashCode();
			bootstrapMethods.putShort(newConst(bsmArg));
		}

		byte data[] = bootstrapMethods.data;
		int length = 2 + argsLength << 1;
		hashCode &= 0x7fffffff;
		Item result = items[hashCode % items.length];
label0:
		do
		{
			if (result != null)
			{
				if (result.type != 33 || result.hashCode != hashCode)
				{
					result = result.next;
					continue;
				}
				int resultPosition = result.intVal;
				int p = 0;
				do
				{
					if (p >= length)
						break;
					if (data[position + p] != data[resultPosition + p])
					{
						result = result.next;
						continue label0;
					}
					p++;
				} while (true);
			}
			int bootstrapMethodIndex;
			if (result != null)
			{
				bootstrapMethodIndex = result.index;
				bootstrapMethods.length = position;
			} else
			{
				bootstrapMethodIndex = bootstrapMethodsCount++;
				result = new Item(bootstrapMethodIndex);
				result.set(position, hashCode);
				put(result);
			}
			key3.set(name, desc, bootstrapMethodIndex);
			result = get(key3);
			if (result == null)
			{
				put122(18, bootstrapMethodIndex, newNameType(name, desc));
				result = new Item(index++, key3);
				put(result);
			}
			return result;
		} while (true);
	}

	public transient int newInvokeDynamic(String name, String desc, Handle bsm, Object bsmArgs[])
	{
		return newInvokeDynamicItem(name, desc, bsm, bsmArgs).index;
	}

	Item newFieldItem(String owner, String name, String desc)
	{
		key3.set(9, owner, name, desc);
		Item result = get(key3);
		if (result == null)
		{
			put122(9, newClass(owner), newNameType(name, desc));
			result = new Item(index++, key3);
			put(result);
		}
		return result;
	}

	public int newField(String owner, String name, String desc)
	{
		return newFieldItem(owner, name, desc).index;
	}

	Item newMethodItem(String owner, String name, String desc, boolean itf)
	{
		int type = itf ? 11 : 10;
		key3.set(type, owner, name, desc);
		Item result = get(key3);
		if (result == null)
		{
			put122(type, newClass(owner), newNameType(name, desc));
			result = new Item(index++, key3);
			put(result);
		}
		return result;
	}

	public int newMethod(String owner, String name, String desc, boolean itf)
	{
		return newMethodItem(owner, name, desc, itf).index;
	}

	Item newInteger(int value)
	{
		key.set(value);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(3).putInt(value);
			result = new Item(index++, key);
			put(result);
		}
		return result;
	}

	Item newFloat(float value)
	{
		key.set(value);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(4).putInt(key.intVal);
			result = new Item(index++, key);
			put(result);
		}
		return result;
	}

	Item newLong(long value)
	{
		key.set(value);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(5).putLong(value);
			result = new Item(index, key);
			index += 2;
			put(result);
		}
		return result;
	}

	Item newDouble(double value)
	{
		key.set(value);
		Item result = get(key);
		if (result == null)
		{
			pool.putByte(6).putLong(key.longVal);
			result = new Item(index, key);
			index += 2;
			put(result);
		}
		return result;
	}

	private Item newString(String value)
	{
		key2.set(8, value, null, null);
		Item result = get(key2);
		if (result == null)
		{
			pool.put12(8, newUTF8(value));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	public int newNameType(String name, String desc)
	{
		return newNameTypeItem(name, desc).index;
	}

	Item newNameTypeItem(String name, String desc)
	{
		key2.set(12, name, desc, null);
		Item result = get(key2);
		if (result == null)
		{
			put122(12, newUTF8(name), newUTF8(desc));
			result = new Item(index++, key2);
			put(result);
		}
		return result;
	}

	int addType(String type)
	{
		key.set(30, type, null, null);
		Item result = get(key);
		if (result == null)
			result = addType(key);
		return result.index;
	}

	int addUninitializedType(String type, int offset)
	{
		key.type = 31;
		key.intVal = offset;
		key.strVal1 = type;
		key.hashCode = 0x7fffffff & 31 + type.hashCode() + offset;
		Item result = get(key);
		if (result == null)
			result = addType(key);
		return result.index;
	}

	private Item addType(Item item)
	{
		typeCount++;
		Item result = new Item(typeCount, key);
		put(result);
		if (typeTable == null)
			typeTable = new Item[16];
		if (typeCount == typeTable.length)
		{
			Item newTable[] = new Item[2 * typeTable.length];
			System.arraycopy(typeTable, 0, newTable, 0, typeTable.length);
			typeTable = newTable;
		}
		typeTable[typeCount] = result;
		return result;
	}

	int getMergedType(int type1, int type2)
	{
		key2.type = 32;
		key2.longVal = (long)type1 | (long)type2 << 32;
		key2.hashCode = 0x7fffffff & 32 + type1 + type2;
		Item result = get(key2);
		if (result == null)
		{
			String t = typeTable[type1].strVal1;
			String u = typeTable[type2].strVal1;
			key2.intVal = addType(getCommonSuperClass(t, u));
			result = new Item(0, key2);
			put(result);
		}
		return result.intVal;
	}

	protected String getCommonSuperClass(String type1, String type2)
	{
		ClassLoader classLoader = getClassLoader();
		Class c;
		Class d;
		try
		{
			c = Class.forName(type1.replace('/', '.'), false, classLoader);
			d = Class.forName(type2.replace('/', '.'), false, classLoader);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.toString());
		}
		if (c.isAssignableFrom(d))
			return type1;
		if (d.isAssignableFrom(c))
			return type2;
		if (c.isInterface() || d.isInterface())
			return "java/lang/Object";
		do
			c = c.getSuperclass();
		while (!c.isAssignableFrom(d));
		return c.getName().replace('.', '/');
	}

	protected ClassLoader getClassLoader()
	{
		ClassLoader classLoader = null;
		try
		{
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable throwable) { }
		return classLoader == null ? getClass().getClassLoader() : classLoader;
	}

	private Item get(Item key)
	{
		Item i;
		for (i = items[key.hashCode % items.length]; i != null && (i.type != key.type || !key.isEqualTo(i)); i = i.next);
		return i;
	}

	private void put(Item i)
	{
		if (this.index + typeCount > threshold)
		{
			int ll = items.length;
			int nl = ll * 2 + 1;
			Item newItems[] = new Item[nl];
			for (int l = ll - 1; l >= 0; l--)
			{
				Item k;
				for (Item j = items[l]; j != null; j = k)
				{
					int index = j.hashCode % newItems.length;
					k = j.next;
					j.next = newItems[index];
					newItems[index] = j;
				}

			}

			items = newItems;
			threshold = (int)((double)nl * 0.75D);
		}
		int index = i.hashCode % items.length;
		i.next = items[index];
		items[index] = i;
	}

	private void put122(int b, int s1, int s2)
	{
		pool.put12(b, s1).putShort(s2);
	}

	private void put112(int b1, int b2, int s)
	{
		pool.put11(b1, b2).putShort(s);
	}

	static 
	{
		byte b[] = new byte[220];
		String s = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKSSSSSSSSSSSSSSSSSS";
		for (int i = 0; i < b.length; i++)
			b[i] = (byte)(s.charAt(i) - 65);

		TYPE = b;
	}
}
