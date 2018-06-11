// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodWriter.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			MethodVisitor, ClassReader, Opcodes, ByteVector, 
//			Label, AnnotationWriter, CurrentFrame, Frame, 
//			Edge, ClassWriter, Handler, Item, 
//			Type, Attribute, TypePath, AnnotationVisitor, 
//			Handle

class MethodWriter extends MethodVisitor
{

	static final int ACC_CONSTRUCTOR = 0x80000;
	static final int SAME_FRAME = 0;
	static final int SAME_LOCALS_1_STACK_ITEM_FRAME = 64;
	static final int RESERVED = 128;
	static final int SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED = 247;
	static final int CHOP_FRAME = 248;
	static final int SAME_FRAME_EXTENDED = 251;
	static final int APPEND_FRAME = 252;
	static final int FULL_FRAME = 255;
	static final int FRAMES = 0;
	static final int INSERTED_FRAMES = 1;
	static final int MAXS = 2;
	static final int NOTHING = 3;
	final ClassWriter cw;
	private int access;
	private final int name;
	private final int desc;
	private final String descriptor;
	String signature;
	int classReaderOffset;
	int classReaderLength;
	int exceptionCount;
	int exceptions[];
	private ByteVector annd;
	private AnnotationWriter anns;
	private AnnotationWriter ianns;
	private AnnotationWriter tanns;
	private AnnotationWriter itanns;
	private AnnotationWriter panns[];
	private AnnotationWriter ipanns[];
	private int synthetics;
	private Attribute attrs;
	private ByteVector code;
	private int maxStack;
	private int maxLocals;
	private int currentLocals;
	private int frameCount;
	private ByteVector stackMap;
	private int previousFrameOffset;
	private int previousFrame[];
	private int frame[];
	private int handlerCount;
	private Handler firstHandler;
	private Handler lastHandler;
	private int methodParametersCount;
	private ByteVector methodParameters;
	private int localVarCount;
	private ByteVector localVar;
	private int localVarTypeCount;
	private ByteVector localVarType;
	private int lineNumberCount;
	private ByteVector lineNumber;
	private int lastCodeOffset;
	private AnnotationWriter ctanns;
	private AnnotationWriter ictanns;
	private Attribute cattrs;
	private int subroutines;
	private final int compute;
	private Label labels;
	private Label previousBlock;
	private Label currentBlock;
	private int stackSize;
	private int maxStackSize;

	MethodWriter(ClassWriter cw, int access, String name, String desc, String signature, String exceptions[], int compute)
	{
		super(0x50000);
		code = new ByteVector();
		if (cw.firstMethod == null)
			cw.firstMethod = this;
		else
			cw.lastMethod.mv = this;
		cw.lastMethod = this;
		this.cw = cw;
		this.access = access;
		if ("<init>".equals(name))
			this.access |= 0x80000;
		this.name = cw.newUTF8(name);
		this.desc = cw.newUTF8(desc);
		descriptor = desc;
		this.signature = signature;
		if (exceptions != null && exceptions.length > 0)
		{
			exceptionCount = exceptions.length;
			this.exceptions = new int[exceptionCount];
			for (int i = 0; i < exceptionCount; i++)
				this.exceptions[i] = cw.newClass(exceptions[i]);

		}
		this.compute = compute;
		if (compute != 3)
		{
			int size = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
			if ((access & 8) != 0)
				size--;
			maxLocals = size;
			currentLocals = size;
			labels = new Label();
			labels.status |= 8;
			visitLabel(labels);
		}
	}

	public void visitParameter(String name, int access)
	{
		if (methodParameters == null)
			methodParameters = new ByteVector();
		methodParametersCount++;
		methodParameters.putShort(name != null ? cw.newUTF8(name) : 0).putShort(access);
	}

	public AnnotationVisitor visitAnnotationDefault()
	{
		annd = new ByteVector();
		return new AnnotationWriter(cw, false, annd, null, 0);
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

	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		if ("Ljava/lang/Synthetic;".equals(desc))
		{
			synthetics = Math.max(synthetics, parameter + 1);
			return new AnnotationWriter(cw, false, bv, null, 0);
		}
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, 2);
		if (visible)
		{
			if (panns == null)
				panns = new AnnotationWriter[Type.getArgumentTypes(descriptor).length];
			aw.next = panns[parameter];
			panns[parameter] = aw;
		} else
		{
			if (ipanns == null)
				ipanns = new AnnotationWriter[Type.getArgumentTypes(descriptor).length];
			aw.next = ipanns[parameter];
			ipanns[parameter] = aw;
		}
		return aw;
	}

	public void visitAttribute(Attribute attr)
	{
		if (attr.isCodeAttribute())
		{
			attr.next = cattrs;
			cattrs = attr;
		} else
		{
			attr.next = attrs;
			attrs = attr;
		}
	}

	public void visitCode()
	{
	}

	public void visitFrame(int type, int nLocal, Object local[], int nStack, Object stack[])
	{
		if (compute == 0)
			return;
		if (compute == 1)
		{
			if (currentBlock.frame == null)
			{
				currentBlock.frame = new CurrentFrame();
				currentBlock.frame.owner = currentBlock;
				currentBlock.frame.initInputFrame(cw, access, Type.getArgumentTypes(descriptor), nLocal);
				visitImplicitFirstFrame();
			} else
			{
				if (type == -1)
					currentBlock.frame.set(cw, nLocal, local, nStack, stack);
				visitFrame(currentBlock.frame);
			}
		} else
		if (type == -1)
		{
			if (previousFrame == null)
				visitImplicitFirstFrame();
			currentLocals = nLocal;
			int frameIndex = startFrame(code.length, nLocal, nStack);
			for (int i = 0; i < nLocal; i++)
			{
				if (local[i] instanceof String)
				{
					frame[frameIndex++] = 0x1700000 | cw.addType((String)local[i]);
					continue;
				}
				if (local[i] instanceof Integer)
					frame[frameIndex++] = ((Integer)local[i]).intValue();
				else
					frame[frameIndex++] = 0x1800000 | cw.addUninitializedType("", ((Label)local[i]).position);
			}

			for (int i = 0; i < nStack; i++)
			{
				if (stack[i] instanceof String)
				{
					frame[frameIndex++] = 0x1700000 | cw.addType((String)stack[i]);
					continue;
				}
				if (stack[i] instanceof Integer)
					frame[frameIndex++] = ((Integer)stack[i]).intValue();
				else
					frame[frameIndex++] = 0x1800000 | cw.addUninitializedType("", ((Label)stack[i]).position);
			}

			endFrame();
		} else
		{
			int delta;
			if (stackMap == null)
			{
				stackMap = new ByteVector();
				delta = code.length;
			} else
			{
				delta = code.length - previousFrameOffset - 1;
				if (delta < 0)
					if (type == 3)
						return;
					else
						throw new IllegalStateException();
			}
			switch (type)
			{
			default:
				break;

			case 0: // '\0'
				currentLocals = nLocal;
				stackMap.putByte(255).putShort(delta).putShort(nLocal);
				for (int i = 0; i < nLocal; i++)
					writeFrameType(local[i]);

				stackMap.putShort(nStack);
				for (int i = 0; i < nStack; i++)
					writeFrameType(stack[i]);

				break;

			case 1: // '\001'
				currentLocals += nLocal;
				stackMap.putByte(251 + nLocal).putShort(delta);
				for (int i = 0; i < nLocal; i++)
					writeFrameType(local[i]);

				break;

			case 2: // '\002'
				currentLocals -= nLocal;
				stackMap.putByte(251 - nLocal).putShort(delta);
				break;

			case 3: // '\003'
				if (delta < 64)
					stackMap.putByte(delta);
				else
					stackMap.putByte(251).putShort(delta);
				break;

			case 4: // '\004'
				if (delta < 64)
					stackMap.putByte(64 + delta);
				else
					stackMap.putByte(247).putShort(delta);
				writeFrameType(stack[0]);
				break;
			}
			previousFrameOffset = code.length;
			frameCount++;
		}
		maxStack = Math.max(maxStack, nStack);
		maxLocals = Math.max(maxLocals, currentLocals);
	}

	public void visitInsn(int opcode)
	{
		lastCodeOffset = code.length;
		code.putByte(opcode);
		if (currentBlock != null)
		{
			if (compute == 0 || compute == 1)
			{
				currentBlock.frame.execute(opcode, 0, null, null);
			} else
			{
				int size = stackSize + Frame.SIZE[opcode];
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
			if (opcode >= 172 && opcode <= 177 || opcode == 191)
				noSuccessor();
		}
	}

	public void visitIntInsn(int opcode, int operand)
	{
		lastCodeOffset = code.length;
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
				currentBlock.frame.execute(opcode, operand, null, null);
			else
			if (opcode != 188)
			{
				int size = stackSize + 1;
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		if (opcode == 17)
			code.put12(opcode, operand);
		else
			code.put11(opcode, operand);
	}

	public void visitVarInsn(int opcode, int var)
	{
		lastCodeOffset = code.length;
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
				currentBlock.frame.execute(opcode, var, null, null);
			else
			if (opcode == 169)
			{
				currentBlock.status |= 0x100;
				currentBlock.inputStackTop = stackSize;
				noSuccessor();
			} else
			{
				int size = stackSize + Frame.SIZE[opcode];
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		if (compute != 3)
		{
			int n;
			if (opcode == 22 || opcode == 24 || opcode == 55 || opcode == 57)
				n = var + 2;
			else
				n = var + 1;
			if (n > maxLocals)
				maxLocals = n;
		}
		if (var < 4 && opcode != 169)
		{
			int opt;
			if (opcode < 54)
				opt = 26 + (opcode - 21 << 2) + var;
			else
				opt = 59 + (opcode - 54 << 2) + var;
			code.putByte(opt);
		} else
		if (var >= 256)
			code.putByte(196).put12(opcode, var);
		else
			code.put11(opcode, var);
		if (opcode >= 54 && compute == 0 && handlerCount > 0)
			visitLabel(new Label());
	}

	public void visitTypeInsn(int opcode, String type)
	{
		lastCodeOffset = code.length;
		Item i = cw.newClassItem(type);
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
				currentBlock.frame.execute(opcode, code.length, cw, i);
			else
			if (opcode == 187)
			{
				int size = stackSize + 1;
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		code.put12(opcode, i.index);
	}

	public void visitFieldInsn(int opcode, String owner, String name, String desc)
	{
		lastCodeOffset = code.length;
		Item i = cw.newFieldItem(owner, name, desc);
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
			{
				currentBlock.frame.execute(opcode, 0, cw, i);
			} else
			{
				char c = desc.charAt(0);
				int size;
				switch (opcode)
				{
				case 178: 
					size = stackSize + (c != 'D' && c != 'J' ? 1 : 2);
					break;

				case 179: 
					size = stackSize + (c != 'D' && c != 'J' ? -1 : -2);
					break;

				case 180: 
					size = stackSize + (c != 'D' && c != 'J' ? 0 : 1);
					break;

				default:
					size = stackSize + (c != 'D' && c != 'J' ? -2 : -3);
					break;
				}
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		code.put12(opcode, i.index);
	}

	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf)
	{
		lastCodeOffset = code.length;
		Item i = cw.newMethodItem(owner, name, desc, itf);
		int argSize = i.intVal;
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
			{
				currentBlock.frame.execute(opcode, 0, cw, i);
			} else
			{
				if (argSize == 0)
				{
					argSize = Type.getArgumentsAndReturnSizes(desc);
					i.intVal = argSize;
				}
				int size;
				if (opcode == 184)
					size = (stackSize - (argSize >> 2)) + (argSize & 3) + 1;
				else
					size = (stackSize - (argSize >> 2)) + (argSize & 3);
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		if (opcode == 185)
		{
			if (argSize == 0)
			{
				argSize = Type.getArgumentsAndReturnSizes(desc);
				i.intVal = argSize;
			}
			code.put12(185, i.index).put11(argSize >> 2, 0);
		} else
		{
			code.put12(opcode, i.index);
		}
	}

	public transient void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object bsmArgs[])
	{
		lastCodeOffset = code.length;
		Item i = cw.newInvokeDynamicItem(name, desc, bsm, bsmArgs);
		int argSize = i.intVal;
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
			{
				currentBlock.frame.execute(186, 0, cw, i);
			} else
			{
				if (argSize == 0)
				{
					argSize = Type.getArgumentsAndReturnSizes(desc);
					i.intVal = argSize;
				}
				int size = (stackSize - (argSize >> 2)) + (argSize & 3) + 1;
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		code.put12(186, i.index);
		code.putShort(0);
	}

	public void visitJumpInsn(int opcode, Label label)
	{
		boolean isWide = opcode >= 200;
		opcode = isWide ? opcode - 33 : opcode;
		lastCodeOffset = code.length;
		Label nextInsn = null;
		if (currentBlock != null)
			if (compute == 0)
			{
				currentBlock.frame.execute(opcode, 0, null, null);
				label.getFirst().status |= 0x10;
				addSuccessor(0, label);
				if (opcode != 167)
					nextInsn = new Label();
			} else
			if (compute == 1)
				currentBlock.frame.execute(opcode, 0, null, null);
			else
			if (opcode == 168)
			{
				if ((label.status & 0x200) == 0)
				{
					label.status |= 0x200;
					subroutines++;
				}
				currentBlock.status |= 0x80;
				addSuccessor(stackSize + 1, label);
				nextInsn = new Label();
			} else
			{
				stackSize += Frame.SIZE[opcode];
				addSuccessor(stackSize, label);
			}
		if ((label.status & 2) != 0 && label.position - code.length < -32768)
		{
			if (opcode == 167)
				code.putByte(200);
			else
			if (opcode == 168)
			{
				code.putByte(201);
			} else
			{
				if (nextInsn != null)
					nextInsn.status |= 0x10;
				code.putByte(opcode > 166 ? opcode ^ 1 : (opcode + 1 ^ 1) - 1);
				code.putShort(8);
				code.putByte(200);
			}
			label.put(this, code, code.length - 1, true);
		} else
		if (isWide)
		{
			code.putByte(opcode + 33);
			label.put(this, code, code.length - 1, true);
		} else
		{
			code.putByte(opcode);
			label.put(this, code, code.length - 1, false);
		}
		if (currentBlock != null)
		{
			if (nextInsn != null)
				visitLabel(nextInsn);
			if (opcode == 167)
				noSuccessor();
		}
	}

	public void visitLabel(Label label)
	{
		cw.hasAsmInsns |= label.resolve(this, code.length, code.data);
		if ((label.status & 1) != 0)
			return;
		if (compute == 0)
		{
			if (currentBlock != null)
			{
				if (label.position == currentBlock.position)
				{
					currentBlock.status |= label.status & 0x10;
					label.frame = currentBlock.frame;
					return;
				}
				addSuccessor(0, label);
			}
			currentBlock = label;
			if (label.frame == null)
			{
				label.frame = new Frame();
				label.frame.owner = label;
			}
			if (previousBlock != null)
			{
				if (label.position == previousBlock.position)
				{
					previousBlock.status |= label.status & 0x10;
					label.frame = previousBlock.frame;
					currentBlock = previousBlock;
					return;
				}
				previousBlock.successor = label;
			}
			previousBlock = label;
		} else
		if (compute == 1)
		{
			if (currentBlock == null)
				currentBlock = label;
			else
				currentBlock.frame.owner = label;
		} else
		if (compute == 2)
		{
			if (currentBlock != null)
			{
				currentBlock.outputStackMax = maxStackSize;
				addSuccessor(stackSize, label);
			}
			currentBlock = label;
			stackSize = 0;
			maxStackSize = 0;
			if (previousBlock != null)
				previousBlock.successor = label;
			previousBlock = label;
		}
	}

	public void visitLdcInsn(Object cst)
	{
		lastCodeOffset = code.length;
		Item i = cw.newConstItem(cst);
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
			{
				currentBlock.frame.execute(18, 0, cw, i);
			} else
			{
				int size;
				if (i.type == 5 || i.type == 6)
					size = stackSize + 2;
				else
					size = stackSize + 1;
				if (size > maxStackSize)
					maxStackSize = size;
				stackSize = size;
			}
		int index = i.index;
		if (i.type == 5 || i.type == 6)
			code.put12(20, index);
		else
		if (index >= 256)
			code.put12(19, index);
		else
			code.put11(18, index);
	}

	public void visitIincInsn(int var, int increment)
	{
		lastCodeOffset = code.length;
		if (currentBlock != null && (compute == 0 || compute == 1))
			currentBlock.frame.execute(132, var, null, null);
		if (compute != 3)
		{
			int n = var + 1;
			if (n > maxLocals)
				maxLocals = n;
		}
		if (var > 255 || increment > 127 || increment < -128)
			code.putByte(196).put12(132, var).putShort(increment);
		else
			code.putByte(132).put11(var, increment);
	}

	public transient void visitTableSwitchInsn(int min, int max, Label dflt, Label labels[])
	{
		lastCodeOffset = code.length;
		int source = code.length;
		code.putByte(170);
		code.putByteArray(null, 0, (4 - code.length % 4) % 4);
		dflt.put(this, code, source, true);
		code.putInt(min).putInt(max);
		for (int i = 0; i < labels.length; i++)
			labels[i].put(this, code, source, true);

		visitSwitchInsn(dflt, labels);
	}

	public void visitLookupSwitchInsn(Label dflt, int keys[], Label labels[])
	{
		lastCodeOffset = code.length;
		int source = code.length;
		code.putByte(171);
		code.putByteArray(null, 0, (4 - code.length % 4) % 4);
		dflt.put(this, code, source, true);
		code.putInt(labels.length);
		for (int i = 0; i < labels.length; i++)
		{
			code.putInt(keys[i]);
			labels[i].put(this, code, source, true);
		}

		visitSwitchInsn(dflt, labels);
	}

	private void visitSwitchInsn(Label dflt, Label labels[])
	{
		if (currentBlock != null)
		{
			if (compute == 0)
			{
				currentBlock.frame.execute(171, 0, null, null);
				addSuccessor(0, dflt);
				dflt.getFirst().status |= 0x10;
				for (int i = 0; i < labels.length; i++)
				{
					addSuccessor(0, labels[i]);
					labels[i].getFirst().status |= 0x10;
				}

			} else
			{
				stackSize--;
				addSuccessor(stackSize, dflt);
				for (int i = 0; i < labels.length; i++)
					addSuccessor(stackSize, labels[i]);

			}
			noSuccessor();
		}
	}

	public void visitMultiANewArrayInsn(String desc, int dims)
	{
		lastCodeOffset = code.length;
		Item i = cw.newClassItem(desc);
		if (currentBlock != null)
			if (compute == 0 || compute == 1)
				currentBlock.frame.execute(197, dims, cw, i);
			else
				stackSize += 1 - dims;
		code.put12(197, i.index).putByte(dims);
	}

	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		typeRef = typeRef & 0xff0000ff | lastCodeOffset << 8;
		AnnotationWriter.putTarget(typeRef, typePath, bv);
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
		if (visible)
		{
			aw.next = ctanns;
			ctanns = aw;
		} else
		{
			aw.next = ictanns;
			ictanns = aw;
		}
		return aw;
	}

	public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
	{
		handlerCount++;
		Handler h = new Handler();
		h.start = start;
		h.end = end;
		h.handler = handler;
		h.desc = type;
		h.type = type == null ? 0 : cw.newClass(type);
		if (lastHandler == null)
			firstHandler = h;
		else
			lastHandler.next = h;
		lastHandler = h;
	}

	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		AnnotationWriter.putTarget(typeRef, typePath, bv);
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
		if (visible)
		{
			aw.next = ctanns;
			ctanns = aw;
		} else
		{
			aw.next = ictanns;
			ictanns = aw;
		}
		return aw;
	}

	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		if (signature != null)
		{
			if (localVarType == null)
				localVarType = new ByteVector();
			localVarTypeCount++;
			localVarType.putShort(start.position).putShort(end.position - start.position).putShort(cw.newUTF8(name)).putShort(cw.newUTF8(signature)).putShort(index);
		}
		if (localVar == null)
			localVar = new ByteVector();
		localVarCount++;
		localVar.putShort(start.position).putShort(end.position - start.position).putShort(cw.newUTF8(name)).putShort(cw.newUTF8(desc)).putShort(index);
		if (compute != 3)
		{
			char c = desc.charAt(0);
			int n = index + (c != 'J' && c != 'D' ? 1 : 2);
			if (n > maxLocals)
				maxLocals = n;
		}
	}

	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label start[], Label end[], int index[], String desc, boolean visible)
	{
		ByteVector bv = new ByteVector();
		bv.putByte(typeRef >>> 24).putShort(start.length);
		for (int i = 0; i < start.length; i++)
			bv.putShort(start[i].position).putShort(end[i].position - start[i].position).putShort(index[i]);

		if (typePath == null)
		{
			bv.putByte(0);
		} else
		{
			int length = typePath.b[typePath.offset] * 2 + 1;
			bv.putByteArray(typePath.b, typePath.offset, length);
		}
		bv.putShort(cw.newUTF8(desc)).putShort(0);
		AnnotationWriter aw = new AnnotationWriter(cw, true, bv, bv, bv.length - 2);
		if (visible)
		{
			aw.next = ctanns;
			ctanns = aw;
		} else
		{
			aw.next = ictanns;
			ictanns = aw;
		}
		return aw;
	}

	public void visitLineNumber(int line, Label start)
	{
		if (lineNumber == null)
			lineNumber = new ByteVector();
		lineNumberCount++;
		lineNumber.putShort(start.position);
		lineNumber.putShort(line);
	}

	public void visitMaxs(int maxStack, int maxLocals)
	{
		if (compute == 0)
		{
			Handler handler;
			for (handler = firstHandler; handler != null; handler = handler.next)
			{
				Label l = handler.start.getFirst();
				Label h = handler.handler.getFirst();
				Label e = handler.end.getFirst();
				String t = handler.desc != null ? handler.desc : "java/lang/Throwable";
				int kind = 0x1700000 | cw.addType(t);
				h.status |= 0x10;
				for (; l != e; l = l.successor)
				{
					Edge b = new Edge();
					b.info = kind;
					b.successor = h;
					b.next = l.successors;
					l.successors = b;
				}

			}

			Frame f = labels.frame;
			f.initInputFrame(cw, access, Type.getArgumentTypes(descriptor), this.maxLocals);
			visitFrame(f);
			int max = 0;
			for (Label changed = labels; changed != null;)
			{
				Label l = changed;
				changed = changed.next;
				l.next = null;
				f = l.frame;
				if ((l.status & 0x10) != 0)
					l.status |= 0x20;
				l.status |= 0x40;
				int blockMax = f.inputStack.length + l.outputStackMax;
				if (blockMax > max)
					max = blockMax;
				Edge e = l.successors;
				while (e != null) 
				{
					Label n = e.successor.getFirst();
					boolean change = f.merge(cw, n.frame, e.info);
					if (change && n.next == null)
					{
						n.next = changed;
						changed = n;
					}
					e = e.next;
				}
			}

			for (Label l = labels; l != null; l = l.successor)
			{
				f = l.frame;
				if ((l.status & 0x20) != 0)
					visitFrame(f);
				if ((l.status & 0x40) != 0)
					continue;
				Label k = l.successor;
				int start = l.position;
				int end = (k != null ? k.position : code.length) - 1;
				if (end < start)
					continue;
				max = Math.max(max, 1);
				for (int i = start; i < end; i++)
					code.data[i] = 0;

				code.data[end] = -65;
				int frameIndex = startFrame(start, 0, 1);
				frame[frameIndex] = 0x1700000 | cw.addType("java/lang/Throwable");
				endFrame();
				firstHandler = Handler.remove(firstHandler, l, k);
			}

			handler = firstHandler;
			handlerCount = 0;
			for (; handler != null; handler = handler.next)
				handlerCount++;

			this.maxStack = max;
		} else
		if (compute == 2)
		{
			for (Handler handler = firstHandler; handler != null; handler = handler.next)
			{
				Label l = handler.start;
				Label h = handler.handler;
				for (Label e = handler.end; l != e; l = l.successor)
				{
					Edge b = new Edge();
					b.info = 0x7fffffff;
					b.successor = h;
					if ((l.status & 0x80) == 0)
					{
						b.next = l.successors;
						l.successors = b;
					} else
					{
						b.next = l.successors.next.next;
						l.successors.next.next = b;
					}
				}

			}

			if (subroutines > 0)
			{
				int id = 0;
				labels.visitSubroutine(null, 1L, subroutines);
				for (Label l = labels; l != null; l = l.successor)
				{
					if ((l.status & 0x80) == 0)
						continue;
					Label subroutine = l.successors.next.successor;
					if ((subroutine.status & 0x400) == 0)
					{
						id++;
						subroutine.visitSubroutine(null, (long)id / 32L << 32 | 1L << id % 32, subroutines);
					}
				}

				for (Label l = labels; l != null; l = l.successor)
				{
					if ((l.status & 0x80) == 0)
						continue;
					for (Label L = labels; L != null; L = L.successor)
						L.status &= 0xfffff7ff;

					Label subroutine = l.successors.next.successor;
					subroutine.visitSubroutine(l, 0L, subroutines);
				}

			}
			int max = 0;
			for (Label stack = labels; stack != null;)
			{
				Label l = stack;
				stack = stack.next;
				int start = l.inputStackTop;
				int blockMax = start + l.outputStackMax;
				if (blockMax > max)
					max = blockMax;
				Edge b = l.successors;
				if ((l.status & 0x80) != 0)
					b = b.next;
				while (b != null) 
				{
					l = b.successor;
					if ((l.status & 8) == 0)
					{
						l.inputStackTop = b.info != 0x7fffffff ? start + b.info : 1;
						l.status |= 8;
						l.next = stack;
						stack = l;
					}
					b = b.next;
				}
			}

			this.maxStack = Math.max(maxStack, max);
		} else
		{
			this.maxStack = maxStack;
			this.maxLocals = maxLocals;
		}
	}

	public void visitEnd()
	{
	}

	private void addSuccessor(int info, Label successor)
	{
		Edge b = new Edge();
		b.info = info;
		b.successor = successor;
		b.next = currentBlock.successors;
		currentBlock.successors = b;
	}

	private void noSuccessor()
	{
		if (compute == 0)
		{
			Label l = new Label();
			l.frame = new Frame();
			l.frame.owner = l;
			l.resolve(this, code.length, code.data);
			previousBlock.successor = l;
			previousBlock = l;
		} else
		{
			currentBlock.outputStackMax = maxStackSize;
		}
		if (compute != 1)
			currentBlock = null;
	}

	private void visitFrame(Frame f)
	{
		int nTop = 0;
		int nLocal = 0;
		int nStack = 0;
		int locals[] = f.inputLocals;
		int stacks[] = f.inputStack;
		int i;
		for (i = 0; i < locals.length; i++)
		{
			int t = locals[i];
			if (t == 0x1000000)
			{
				nTop++;
			} else
			{
				nLocal += nTop + 1;
				nTop = 0;
			}
			if (t == 0x1000004 || t == 0x1000003)
				i++;
		}

		for (i = 0; i < stacks.length; i++)
		{
			int t = stacks[i];
			nStack++;
			if (t == 0x1000004 || t == 0x1000003)
				i++;
		}

		int frameIndex = startFrame(f.owner.position, nLocal, nStack);
		i = 0;
		for (; nLocal > 0; nLocal--)
		{
			int t = locals[i];
			frame[frameIndex++] = t;
			if (t == 0x1000004 || t == 0x1000003)
				i++;
			i++;
		}

		for (i = 0; i < stacks.length; i++)
		{
			int t = stacks[i];
			frame[frameIndex++] = t;
			if (t == 0x1000004 || t == 0x1000003)
				i++;
		}

		endFrame();
	}

	private void visitImplicitFirstFrame()
	{
		int frameIndex = startFrame(0, descriptor.length() + 1, 0);
		if ((access & 8) == 0)
			if ((access & 0x80000) == 0)
				frame[frameIndex++] = 0x1700000 | cw.addType(cw.thisName);
			else
				frame[frameIndex++] = 6;
		int i = 1;
		do
		{
			int j = i;
			switch (descriptor.charAt(i++))
			{
			case 66: // 'B'
			case 67: // 'C'
			case 73: // 'I'
			case 83: // 'S'
			case 90: // 'Z'
				frame[frameIndex++] = 1;
				break;

			case 70: // 'F'
				frame[frameIndex++] = 2;
				break;

			case 74: // 'J'
				frame[frameIndex++] = 4;
				break;

			case 68: // 'D'
				frame[frameIndex++] = 3;
				break;

			case 91: // '['
				for (; descriptor.charAt(i) == '['; i++);
				if (descriptor.charAt(i) == 'L')
					for (i++; descriptor.charAt(i) != ';'; i++);
				frame[frameIndex++] = 0x1700000 | cw.addType(descriptor.substring(j, ++i));
				break;

			case 76: // 'L'
				for (; descriptor.charAt(i) != ';'; i++);
				frame[frameIndex++] = 0x1700000 | cw.addType(descriptor.substring(j + 1, i++));
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
				frame[1] = frameIndex - 3;
				endFrame();
				return;
			}
		} while (true);
	}

	private int startFrame(int offset, int nLocal, int nStack)
	{
		int n = 3 + nLocal + nStack;
		if (frame == null || frame.length < n)
			frame = new int[n];
		frame[0] = offset;
		frame[1] = nLocal;
		frame[2] = nStack;
		return 3;
	}

	private void endFrame()
	{
		if (previousFrame != null)
		{
			if (stackMap == null)
				stackMap = new ByteVector();
			writeFrame();
			frameCount++;
		}
		previousFrame = frame;
		frame = null;
	}

	private void writeFrame()
	{
		int clocalsSize = frame[1];
		int cstackSize = frame[2];
		if ((cw.version & 0xffff) < 50)
		{
			stackMap.putShort(frame[0]).putShort(clocalsSize);
			writeFrameTypes(3, 3 + clocalsSize);
			stackMap.putShort(cstackSize);
			writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
			return;
		}
		int localsSize = previousFrame[1];
		int type = 255;
		int k = 0;
		int delta;
		if (frameCount == 0)
			delta = frame[0];
		else
			delta = frame[0] - previousFrame[0] - 1;
		if (cstackSize == 0)
		{
			k = clocalsSize - localsSize;
			switch (k)
			{
			case -3: 
			case -2: 
			case -1: 
				type = 248;
				localsSize = clocalsSize;
				break;

			case 0: // '\0'
				type = delta >= 64 ? 251 : 0;
				break;

			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
				type = 252;
				break;
			}
		} else
		if (clocalsSize == localsSize && cstackSize == 1)
			type = delta >= 63 ? 247 : 64;
		if (type != 255)
		{
			int l = 3;
			int j = 0;
			do
			{
				if (j >= localsSize)
					break;
				if (frame[l] != previousFrame[l])
				{
					type = 255;
					break;
				}
				l++;
				j++;
			} while (true);
		}
		switch (type)
		{
		case 0: // '\0'
			stackMap.putByte(delta);
			break;

		case 64: // '@'
			stackMap.putByte(64 + delta);
			writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
			break;

		case 247: 
			stackMap.putByte(247).putShort(delta);
			writeFrameTypes(3 + clocalsSize, 4 + clocalsSize);
			break;

		case 251: 
			stackMap.putByte(251).putShort(delta);
			break;

		case 248: 
			stackMap.putByte(251 + k).putShort(delta);
			break;

		case 252: 
			stackMap.putByte(251 + k).putShort(delta);
			writeFrameTypes(3 + localsSize, 3 + clocalsSize);
			break;

		default:
			stackMap.putByte(255).putShort(delta).putShort(clocalsSize);
			writeFrameTypes(3, 3 + clocalsSize);
			stackMap.putShort(cstackSize);
			writeFrameTypes(3 + clocalsSize, 3 + clocalsSize + cstackSize);
			break;
		}
	}

	private void writeFrameTypes(int start, int end)
	{
		for (int i = start; i < end; i++)
		{
			int t = frame[i];
			int d = t & 0xf0000000;
			if (d == 0)
			{
				int v = t & 0xfffff;
				switch (t & 0xff00000)
				{
				case 24117248: 
					stackMap.putByte(7).putShort(cw.newClass(cw.typeTable[v].strVal1));
					break;

				case 25165824: 
					stackMap.putByte(8).putShort(cw.typeTable[v].intVal);
					break;

				default:
					stackMap.putByte(v);
					break;
				}
				continue;
			}
			StringBuilder sb = new StringBuilder();
			for (d >>= 28; d-- > 0;)
				sb.append('[');

			if ((t & 0xff00000) == 0x1700000)
			{
				sb.append('L');
				sb.append(cw.typeTable[t & 0xfffff].strVal1);
				sb.append(';');
			} else
			{
				switch (t & 0xf)
				{
				case 1: // '\001'
					sb.append('I');
					break;

				case 2: // '\002'
					sb.append('F');
					break;

				case 3: // '\003'
					sb.append('D');
					break;

				case 9: // '\t'
					sb.append('Z');
					break;

				case 10: // '\n'
					sb.append('B');
					break;

				case 11: // '\013'
					sb.append('C');
					break;

				case 12: // '\f'
					sb.append('S');
					break;

				case 4: // '\004'
				case 5: // '\005'
				case 6: // '\006'
				case 7: // '\007'
				case 8: // '\b'
				default:
					sb.append('J');
					break;
				}
			}
			stackMap.putByte(7).putShort(cw.newClass(sb.toString()));
		}

	}

	private void writeFrameType(Object type)
	{
		if (type instanceof String)
			stackMap.putByte(7).putShort(cw.newClass((String)type));
		else
		if (type instanceof Integer)
			stackMap.putByte(((Integer)type).intValue());
		else
			stackMap.putByte(8).putShort(((Label)type).position);
	}

	final int getSize()
	{
		if (classReaderOffset != 0)
			return 6 + classReaderLength;
		int size = 8;
		if (code.length > 0)
		{
			if (code.length > 65535)
				throw new RuntimeException("Method code too large!");
			cw.newUTF8("Code");
			size += 18 + code.length + 8 * handlerCount;
			if (localVar != null)
			{
				cw.newUTF8("LocalVariableTable");
				size += 8 + localVar.length;
			}
			if (localVarType != null)
			{
				cw.newUTF8("LocalVariableTypeTable");
				size += 8 + localVarType.length;
			}
			if (lineNumber != null)
			{
				cw.newUTF8("LineNumberTable");
				size += 8 + lineNumber.length;
			}
			if (stackMap != null)
			{
				boolean zip = (cw.version & 0xffff) >= 50;
				cw.newUTF8(zip ? "StackMapTable" : "StackMap");
				size += 8 + stackMap.length;
			}
			if (ctanns != null)
			{
				cw.newUTF8("RuntimeVisibleTypeAnnotations");
				size += 8 + ctanns.getSize();
			}
			if (ictanns != null)
			{
				cw.newUTF8("RuntimeInvisibleTypeAnnotations");
				size += 8 + ictanns.getSize();
			}
			if (cattrs != null)
				size += cattrs.getSize(cw, code.data, code.length, maxStack, maxLocals);
		}
		if (exceptionCount > 0)
		{
			cw.newUTF8("Exceptions");
			size += 8 + 2 * exceptionCount;
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
		if (signature != null)
		{
			cw.newUTF8("Signature");
			cw.newUTF8(signature);
			size += 8;
		}
		if (methodParameters != null)
		{
			cw.newUTF8("MethodParameters");
			size += 7 + methodParameters.length;
		}
		if (annd != null)
		{
			cw.newUTF8("AnnotationDefault");
			size += 6 + annd.length;
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
		if (panns != null)
		{
			cw.newUTF8("RuntimeVisibleParameterAnnotations");
			size += 7 + 2 * (panns.length - synthetics);
			for (int i = panns.length - 1; i >= synthetics; i--)
				size += panns[i] != null ? panns[i].getSize() : 0;

		}
		if (ipanns != null)
		{
			cw.newUTF8("RuntimeInvisibleParameterAnnotations");
			size += 7 + 2 * (ipanns.length - synthetics);
			for (int i = ipanns.length - 1; i >= synthetics; i--)
				size += ipanns[i] != null ? ipanns[i].getSize() : 0;

		}
		if (attrs != null)
			size += attrs.getSize(cw, null, 0, -1, -1);
		return size;
	}

	final void put(ByteVector out)
	{
		int FACTOR = 64;
		int mask = 0xe0000 | (access & 0x40000) / 64;
		out.putShort(access & ~mask).putShort(name).putShort(desc);
		if (classReaderOffset != 0)
		{
			out.putByteArray(cw.cr.b, classReaderOffset, classReaderLength);
			return;
		}
		int attributeCount = 0;
		if (code.length > 0)
			attributeCount++;
		if (exceptionCount > 0)
			attributeCount++;
		if ((access & 0x1000) != 0 && ((cw.version & 0xffff) < 49 || (access & 0x40000) != 0))
			attributeCount++;
		if ((access & 0x20000) != 0)
			attributeCount++;
		if (signature != null)
			attributeCount++;
		if (methodParameters != null)
			attributeCount++;
		if (annd != null)
			attributeCount++;
		if (anns != null)
			attributeCount++;
		if (ianns != null)
			attributeCount++;
		if (tanns != null)
			attributeCount++;
		if (itanns != null)
			attributeCount++;
		if (panns != null)
			attributeCount++;
		if (ipanns != null)
			attributeCount++;
		if (attrs != null)
			attributeCount += attrs.getCount();
		out.putShort(attributeCount);
		if (code.length > 0)
		{
			int size = 12 + code.length + 8 * handlerCount;
			if (localVar != null)
				size += 8 + localVar.length;
			if (localVarType != null)
				size += 8 + localVarType.length;
			if (lineNumber != null)
				size += 8 + lineNumber.length;
			if (stackMap != null)
				size += 8 + stackMap.length;
			if (ctanns != null)
				size += 8 + ctanns.getSize();
			if (ictanns != null)
				size += 8 + ictanns.getSize();
			if (cattrs != null)
				size += cattrs.getSize(cw, code.data, code.length, maxStack, maxLocals);
			out.putShort(cw.newUTF8("Code")).putInt(size);
			out.putShort(maxStack).putShort(maxLocals);
			out.putInt(code.length).putByteArray(code.data, 0, code.length);
			out.putShort(handlerCount);
			if (handlerCount > 0)
			{
				for (Handler h = firstHandler; h != null; h = h.next)
					out.putShort(h.start.position).putShort(h.end.position).putShort(h.handler.position).putShort(h.type);

			}
			attributeCount = 0;
			if (localVar != null)
				attributeCount++;
			if (localVarType != null)
				attributeCount++;
			if (lineNumber != null)
				attributeCount++;
			if (stackMap != null)
				attributeCount++;
			if (ctanns != null)
				attributeCount++;
			if (ictanns != null)
				attributeCount++;
			if (cattrs != null)
				attributeCount += cattrs.getCount();
			out.putShort(attributeCount);
			if (localVar != null)
			{
				out.putShort(cw.newUTF8("LocalVariableTable"));
				out.putInt(localVar.length + 2).putShort(localVarCount);
				out.putByteArray(localVar.data, 0, localVar.length);
			}
			if (localVarType != null)
			{
				out.putShort(cw.newUTF8("LocalVariableTypeTable"));
				out.putInt(localVarType.length + 2).putShort(localVarTypeCount);
				out.putByteArray(localVarType.data, 0, localVarType.length);
			}
			if (lineNumber != null)
			{
				out.putShort(cw.newUTF8("LineNumberTable"));
				out.putInt(lineNumber.length + 2).putShort(lineNumberCount);
				out.putByteArray(lineNumber.data, 0, lineNumber.length);
			}
			if (stackMap != null)
			{
				boolean zip = (cw.version & 0xffff) >= 50;
				out.putShort(cw.newUTF8(zip ? "StackMapTable" : "StackMap"));
				out.putInt(stackMap.length + 2).putShort(frameCount);
				out.putByteArray(stackMap.data, 0, stackMap.length);
			}
			if (ctanns != null)
			{
				out.putShort(cw.newUTF8("RuntimeVisibleTypeAnnotations"));
				ctanns.put(out);
			}
			if (ictanns != null)
			{
				out.putShort(cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
				ictanns.put(out);
			}
			if (cattrs != null)
				cattrs.put(cw, code.data, code.length, maxLocals, maxStack, out);
		}
		if (exceptionCount > 0)
		{
			out.putShort(cw.newUTF8("Exceptions")).putInt(2 * exceptionCount + 2);
			out.putShort(exceptionCount);
			for (int i = 0; i < exceptionCount; i++)
				out.putShort(exceptions[i]);

		}
		if ((access & 0x1000) != 0 && ((cw.version & 0xffff) < 49 || (access & 0x40000) != 0))
			out.putShort(cw.newUTF8("Synthetic")).putInt(0);
		if ((access & 0x20000) != 0)
			out.putShort(cw.newUTF8("Deprecated")).putInt(0);
		if (signature != null)
			out.putShort(cw.newUTF8("Signature")).putInt(2).putShort(cw.newUTF8(signature));
		if (methodParameters != null)
		{
			out.putShort(cw.newUTF8("MethodParameters"));
			out.putInt(methodParameters.length + 1).putByte(methodParametersCount);
			out.putByteArray(methodParameters.data, 0, methodParameters.length);
		}
		if (annd != null)
		{
			out.putShort(cw.newUTF8("AnnotationDefault"));
			out.putInt(annd.length);
			out.putByteArray(annd.data, 0, annd.length);
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
		if (panns != null)
		{
			out.putShort(cw.newUTF8("RuntimeVisibleParameterAnnotations"));
			AnnotationWriter.put(panns, synthetics, out);
		}
		if (ipanns != null)
		{
			out.putShort(cw.newUTF8("RuntimeInvisibleParameterAnnotations"));
			AnnotationWriter.put(ipanns, synthetics, out);
		}
		if (attrs != null)
			attrs.put(cw, null, 0, -1, -1, out);
	}
}
