// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Frame.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Label, Opcodes, MethodWriter, ClassWriter, 
//			Item, Type

class Frame
{

	static final int DIM = 0xf0000000;
	static final int ARRAY_OF = 0x10000000;
	static final int ELEMENT_OF = 0xf0000000;
	static final int KIND = 0xf000000;
	static final int TOP_IF_LONG_OR_DOUBLE = 0x800000;
	static final int VALUE = 0x7fffff;
	static final int BASE_KIND = 0xff00000;
	static final int BASE_VALUE = 0xfffff;
	static final int BASE = 0x1000000;
	static final int OBJECT = 0x1700000;
	static final int UNINITIALIZED = 0x1800000;
	private static final int LOCAL = 0x2000000;
	private static final int STACK = 0x3000000;
	static final int TOP = 0x1000000;
	static final int BOOLEAN = 0x1000009;
	static final int BYTE = 0x100000a;
	static final int CHAR = 0x100000b;
	static final int SHORT = 0x100000c;
	static final int INTEGER = 0x1000001;
	static final int FLOAT = 0x1000002;
	static final int DOUBLE = 0x1000003;
	static final int LONG = 0x1000004;
	static final int NULL = 0x1000005;
	static final int UNINITIALIZED_THIS = 0x1000006;
	static final int SIZE[];
	Label owner;
	int inputLocals[];
	int inputStack[];
	private int outputLocals[];
	private int outputStack[];
	int outputStackTop;
	private int initializationCount;
	private int initializations[];

	Frame()
	{
	}

	final void set(ClassWriter cw, int nLocal, Object local[], int nStack, Object stack[])
	{
		for (int i = convert(cw, nLocal, local, inputLocals); i < local.length;)
			inputLocals[i++] = 0x1000000;

		int nStackTop = 0;
		for (int j = 0; j < nStack; j++)
			if (stack[j] == Opcodes.LONG || stack[j] == Opcodes.DOUBLE)
				nStackTop++;

		inputStack = new int[nStack + nStackTop];
		convert(cw, nStack, stack, inputStack);
		outputStackTop = 0;
		initializationCount = 0;
	}

	private static int convert(ClassWriter cw, int nInput, Object input[], int output[])
	{
		int i = 0;
		for (int j = 0; j < nInput; j++)
		{
			if (input[j] instanceof Integer)
			{
				output[i++] = 0x1000000 | ((Integer)input[j]).intValue();
				if (input[j] == Opcodes.LONG || input[j] == Opcodes.DOUBLE)
					output[i++] = 0x1000000;
				continue;
			}
			if (input[j] instanceof String)
				output[i++] = type(cw, Type.getObjectType((String)input[j]).getDescriptor());
			else
				output[i++] = 0x1800000 | cw.addUninitializedType("", ((Label)input[j]).position);
		}

		return i;
	}

	final void set(Frame f)
	{
		inputLocals = f.inputLocals;
		inputStack = f.inputStack;
		outputLocals = f.outputLocals;
		outputStack = f.outputStack;
		outputStackTop = f.outputStackTop;
		initializationCount = f.initializationCount;
		initializations = f.initializations;
	}

	private int get(int local)
	{
		if (outputLocals == null || local >= outputLocals.length)
			return 0x2000000 | local;
		int type = outputLocals[local];
		if (type == 0)
			type = outputLocals[local] = 0x2000000 | local;
		return type;
	}

	private void set(int local, int type)
	{
		if (outputLocals == null)
			outputLocals = new int[10];
		int n = outputLocals.length;
		if (local >= n)
		{
			int t[] = new int[Math.max(local + 1, 2 * n)];
			System.arraycopy(outputLocals, 0, t, 0, n);
			outputLocals = t;
		}
		outputLocals[local] = type;
	}

	private void push(int type)
	{
		if (outputStack == null)
			outputStack = new int[10];
		int n = outputStack.length;
		if (outputStackTop >= n)
		{
			int t[] = new int[Math.max(outputStackTop + 1, 2 * n)];
			System.arraycopy(outputStack, 0, t, 0, n);
			outputStack = t;
		}
		outputStack[outputStackTop++] = type;
		int top = owner.inputStackTop + outputStackTop;
		if (top > owner.outputStackMax)
			owner.outputStackMax = top;
	}

	private void push(ClassWriter cw, String desc)
	{
		int type = type(cw, desc);
		if (type != 0)
		{
			push(type);
			if (type == 0x1000004 || type == 0x1000003)
				push(0x1000000);
		}
	}

	private static int type(ClassWriter cw, String desc)
	{
		int index = desc.charAt(0) != '(' ? 0 : desc.indexOf(')') + 1;
		switch (desc.charAt(index))
		{
		case 86: // 'V'
			return 0;

		case 66: // 'B'
		case 67: // 'C'
		case 73: // 'I'
		case 83: // 'S'
		case 90: // 'Z'
			return 0x1000001;

		case 70: // 'F'
			return 0x1000002;

		case 74: // 'J'
			return 0x1000004;

		case 68: // 'D'
			return 0x1000003;

		case 76: // 'L'
			String t = desc.substring(index + 1, desc.length() - 1);
			return 0x1700000 | cw.addType(t);

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
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		default:
			int dims;
			for (dims = index + 1; desc.charAt(dims) == '['; dims++);
			int data;
			switch (desc.charAt(dims))
			{
			case 90: // 'Z'
				data = 0x1000009;
				break;

			case 67: // 'C'
				data = 0x100000b;
				break;

			case 66: // 'B'
				data = 0x100000a;
				break;

			case 83: // 'S'
				data = 0x100000c;
				break;

			case 73: // 'I'
				data = 0x1000001;
				break;

			case 70: // 'F'
				data = 0x1000002;
				break;

			case 74: // 'J'
				data = 0x1000004;
				break;

			case 68: // 'D'
				data = 0x1000003;
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
				String t = desc.substring(dims + 1, desc.length() - 1);
				data = 0x1700000 | cw.addType(t);
				break;
			}
			return dims - index << 28 | data;
		}
	}

	private int pop()
	{
		if (outputStackTop > 0)
			return outputStack[--outputStackTop];
		else
			return 0x3000000 | ---owner.inputStackTop;
	}

	private void pop(int elements)
	{
		if (outputStackTop >= elements)
		{
			outputStackTop -= elements;
		} else
		{
			owner.inputStackTop -= elements - outputStackTop;
			outputStackTop = 0;
		}
	}

	private void pop(String desc)
	{
		char c = desc.charAt(0);
		if (c == '(')
			pop((Type.getArgumentsAndReturnSizes(desc) >> 2) - 1);
		else
		if (c == 'J' || c == 'D')
			pop(2);
		else
			pop(1);
	}

	private void init(int var)
	{
		if (initializations == null)
			initializations = new int[2];
		int n = initializations.length;
		if (initializationCount >= n)
		{
			int t[] = new int[Math.max(initializationCount + 1, 2 * n)];
			System.arraycopy(initializations, 0, t, 0, n);
			initializations = t;
		}
		initializations[initializationCount++] = var;
	}

	private int init(ClassWriter cw, int t)
	{
		int s;
		if (t == 0x1000006)
			s = 0x1700000 | cw.addType(cw.thisName);
		else
		if ((t & 0xfff00000) == 0x1800000)
		{
			String type = cw.typeTable[t & 0xfffff].strVal1;
			s = 0x1700000 | cw.addType(type);
		} else
		{
			return t;
		}
		for (int j = 0; j < initializationCount; j++)
		{
			int u = initializations[j];
			int dim = u & 0xf0000000;
			int kind = u & 0xf000000;
			if (kind == 0x2000000)
				u = dim + inputLocals[u & 0x7fffff];
			else
			if (kind == 0x3000000)
				u = dim + inputStack[inputStack.length - (u & 0x7fffff)];
			if (t == u)
				return s;
		}

		return t;
	}

	final void initInputFrame(ClassWriter cw, int access, Type args[], int maxLocals)
	{
		inputLocals = new int[maxLocals];
		inputStack = new int[0];
		int i = 0;
		if ((access & 8) == 0)
			if ((access & 0x80000) == 0)
				inputLocals[i++] = 0x1700000 | cw.addType(cw.thisName);
			else
				inputLocals[i++] = 0x1000006;
		for (int j = 0; j < args.length; j++)
		{
			int t = type(cw, args[j].getDescriptor());
			inputLocals[i++] = t;
			if (t == 0x1000004 || t == 0x1000003)
				inputLocals[i++] = 0x1000000;
		}

		while (i < maxLocals) 
			inputLocals[i++] = 0x1000000;
	}

	void execute(int opcode, int arg, ClassWriter cw, Item item)
	{
		switch (opcode)
		{
		case 0: // '\0'
		case 116: // 't'
		case 117: // 'u'
		case 118: // 'v'
		case 119: // 'w'
		case 145: 
		case 146: 
		case 147: 
		case 167: 
		case 177: 
			break;

		case 1: // '\001'
		{
			push(0x1000005);
			break;
		}

		case 2: // '\002'
		case 3: // '\003'
		case 4: // '\004'
		case 5: // '\005'
		case 6: // '\006'
		case 7: // '\007'
		case 8: // '\b'
		case 16: // '\020'
		case 17: // '\021'
		case 21: // '\025'
		{
			push(0x1000001);
			break;
		}

		case 9: // '\t'
		case 10: // '\n'
		case 22: // '\026'
		{
			push(0x1000004);
			push(0x1000000);
			break;
		}

		case 11: // '\013'
		case 12: // '\f'
		case 13: // '\r'
		case 23: // '\027'
		{
			push(0x1000002);
			break;
		}

		case 14: // '\016'
		case 15: // '\017'
		case 24: // '\030'
		{
			push(0x1000003);
			push(0x1000000);
			break;
		}

		case 18: // '\022'
		{
			switch (item.type)
			{
			case 3: // '\003'
				push(0x1000001);
				break;

			case 5: // '\005'
				push(0x1000004);
				push(0x1000000);
				break;

			case 4: // '\004'
				push(0x1000002);
				break;

			case 6: // '\006'
				push(0x1000003);
				push(0x1000000);
				break;

			case 7: // '\007'
				push(0x1700000 | cw.addType("java/lang/Class"));
				break;

			case 8: // '\b'
				push(0x1700000 | cw.addType("java/lang/String"));
				break;

			case 16: // '\020'
				push(0x1700000 | cw.addType("java/lang/invoke/MethodType"));
				break;

			case 9: // '\t'
			case 10: // '\n'
			case 11: // '\013'
			case 12: // '\f'
			case 13: // '\r'
			case 14: // '\016'
			case 15: // '\017'
			default:
				push(0x1700000 | cw.addType("java/lang/invoke/MethodHandle"));
				break;
			}
			break;
		}

		case 25: // '\031'
		{
			push(get(arg));
			break;
		}

		case 46: // '.'
		case 51: // '3'
		case 52: // '4'
		case 53: // '5'
		{
			pop(2);
			push(0x1000001);
			break;
		}

		case 47: // '/'
		case 143: 
		{
			pop(2);
			push(0x1000004);
			push(0x1000000);
			break;
		}

		case 48: // '0'
		{
			pop(2);
			push(0x1000002);
			break;
		}

		case 49: // '1'
		case 138: 
		{
			pop(2);
			push(0x1000003);
			push(0x1000000);
			break;
		}

		case 50: // '2'
		{
			pop(1);
			int t1 = pop();
			push(0xf0000000 + t1);
			break;
		}

		case 54: // '6'
		case 56: // '8'
		case 58: // ':'
		{
			int t1 = pop();
			set(arg, t1);
			if (arg <= 0)
				break;
			int t2 = get(arg - 1);
			if (t2 == 0x1000004 || t2 == 0x1000003)
			{
				set(arg - 1, 0x1000000);
				break;
			}
			if ((t2 & 0xf000000) != 0x1000000)
				set(arg - 1, t2 | 0x800000);
			break;
		}

		case 55: // '7'
		case 57: // '9'
		{
			pop(1);
			int t1 = pop();
			set(arg, t1);
			set(arg + 1, 0x1000000);
			if (arg <= 0)
				break;
			int t2 = get(arg - 1);
			if (t2 == 0x1000004 || t2 == 0x1000003)
			{
				set(arg - 1, 0x1000000);
				break;
			}
			if ((t2 & 0xf000000) != 0x1000000)
				set(arg - 1, t2 | 0x800000);
			break;
		}

		case 79: // 'O'
		case 81: // 'Q'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		{
			pop(3);
			break;
		}

		case 80: // 'P'
		case 82: // 'R'
		{
			pop(4);
			break;
		}

		case 87: // 'W'
		case 153: 
		case 154: 
		case 155: 
		case 156: 
		case 157: 
		case 158: 
		case 170: 
		case 171: 
		case 172: 
		case 174: 
		case 176: 
		case 191: 
		case 194: 
		case 195: 
		case 198: 
		case 199: 
		{
			pop(1);
			break;
		}

		case 88: // 'X'
		case 159: 
		case 160: 
		case 161: 
		case 162: 
		case 163: 
		case 164: 
		case 165: 
		case 166: 
		case 173: 
		case 175: 
		{
			pop(2);
			break;
		}

		case 89: // 'Y'
		{
			int t1 = pop();
			push(t1);
			push(t1);
			break;
		}

		case 90: // 'Z'
		{
			int t1 = pop();
			int t2 = pop();
			push(t1);
			push(t2);
			push(t1);
			break;
		}

		case 91: // '['
		{
			int t1 = pop();
			int t2 = pop();
			int t3 = pop();
			push(t1);
			push(t3);
			push(t2);
			push(t1);
			break;
		}

		case 92: // '\\'
		{
			int t1 = pop();
			int t2 = pop();
			push(t2);
			push(t1);
			push(t2);
			push(t1);
			break;
		}

		case 93: // ']'
		{
			int t1 = pop();
			int t2 = pop();
			int t3 = pop();
			push(t2);
			push(t1);
			push(t3);
			push(t2);
			push(t1);
			break;
		}

		case 94: // '^'
		{
			int t1 = pop();
			int t2 = pop();
			int t3 = pop();
			int t4 = pop();
			push(t2);
			push(t1);
			push(t4);
			push(t3);
			push(t2);
			push(t1);
			break;
		}

		case 95: // '_'
		{
			int t1 = pop();
			int t2 = pop();
			push(t1);
			push(t2);
			break;
		}

		case 96: // '`'
		case 100: // 'd'
		case 104: // 'h'
		case 108: // 'l'
		case 112: // 'p'
		case 120: // 'x'
		case 122: // 'z'
		case 124: // '|'
		case 126: // '~'
		case 128: 
		case 130: 
		case 136: 
		case 142: 
		case 149: 
		case 150: 
		{
			pop(2);
			push(0x1000001);
			break;
		}

		case 97: // 'a'
		case 101: // 'e'
		case 105: // 'i'
		case 109: // 'm'
		case 113: // 'q'
		case 127: // '\177'
		case 129: 
		case 131: 
		{
			pop(4);
			push(0x1000004);
			push(0x1000000);
			break;
		}

		case 98: // 'b'
		case 102: // 'f'
		case 106: // 'j'
		case 110: // 'n'
		case 114: // 'r'
		case 137: 
		case 144: 
		{
			pop(2);
			push(0x1000002);
			break;
		}

		case 99: // 'c'
		case 103: // 'g'
		case 107: // 'k'
		case 111: // 'o'
		case 115: // 's'
		{
			pop(4);
			push(0x1000003);
			push(0x1000000);
			break;
		}

		case 121: // 'y'
		case 123: // '{'
		case 125: // '}'
		{
			pop(3);
			push(0x1000004);
			push(0x1000000);
			break;
		}

		case 132: 
		{
			set(arg, 0x1000001);
			break;
		}

		case 133: 
		case 140: 
		{
			pop(1);
			push(0x1000004);
			push(0x1000000);
			break;
		}

		case 134: 
		{
			pop(1);
			push(0x1000002);
			break;
		}

		case 135: 
		case 141: 
		{
			pop(1);
			push(0x1000003);
			push(0x1000000);
			break;
		}

		case 139: 
		case 190: 
		case 193: 
		{
			pop(1);
			push(0x1000001);
			break;
		}

		case 148: 
		case 151: 
		case 152: 
		{
			pop(4);
			push(0x1000001);
			break;
		}

		case 168: 
		case 169: 
		{
			throw new RuntimeException("JSR/RET are not supported with computeFrames option");
		}

		case 178: 
		{
			push(cw, item.strVal3);
			break;
		}

		case 179: 
		{
			pop(item.strVal3);
			break;
		}

		case 180: 
		{
			pop(1);
			push(cw, item.strVal3);
			break;
		}

		case 181: 
		{
			pop(item.strVal3);
			pop();
			break;
		}

		case 182: 
		case 183: 
		case 184: 
		case 185: 
		{
			pop(item.strVal3);
			if (opcode != 184)
			{
				int t1 = pop();
				if (opcode == 183 && item.strVal2.charAt(0) == '<')
					init(t1);
			}
			push(cw, item.strVal3);
			break;
		}

		case 186: 
		{
			pop(item.strVal2);
			push(cw, item.strVal2);
			break;
		}

		case 187: 
		{
			push(0x1800000 | cw.addUninitializedType(item.strVal1, arg));
			break;
		}

		case 188: 
		{
			pop();
			switch (arg)
			{
			case 4: // '\004'
				push(0x11000009);
				break;

			case 5: // '\005'
				push(0x1100000b);
				break;

			case 8: // '\b'
				push(0x1100000a);
				break;

			case 9: // '\t'
				push(0x1100000c);
				break;

			case 10: // '\n'
				push(0x11000001);
				break;

			case 6: // '\006'
				push(0x11000002);
				break;

			case 7: // '\007'
				push(0x11000003);
				break;

			default:
				push(0x11000004);
				break;
			}
			break;
		}

		case 189: 
		{
			String s = item.strVal1;
			pop();
			if (s.charAt(0) == '[')
				push(cw, (new StringBuilder()).append('[').append(s).toString());
			else
				push(0x11700000 | cw.addType(s));
			break;
		}

		case 192: 
		{
			String s = item.strVal1;
			pop();
			if (s.charAt(0) == '[')
				push(cw, s);
			else
				push(0x1700000 | cw.addType(s));
			break;
		}

		case 19: // '\023'
		case 20: // '\024'
		case 26: // '\032'
		case 27: // '\033'
		case 28: // '\034'
		case 29: // '\035'
		case 30: // '\036'
		case 31: // '\037'
		case 32: // ' '
		case 33: // '!'
		case 34: // '"'
		case 35: // '#'
		case 36: // '$'
		case 37: // '%'
		case 38: // '&'
		case 39: // '\''
		case 40: // '('
		case 41: // ')'
		case 42: // '*'
		case 43: // '+'
		case 44: // ','
		case 45: // '-'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 68: // 'D'
		case 69: // 'E'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 196: 
		case 197: 
		default:
		{
			pop(arg);
			push(cw, item.strVal1);
			break;
		}
		}
	}

	final boolean merge(ClassWriter cw, Frame frame, int edge)
	{
		boolean changed = false;
		int nLocal = inputLocals.length;
		int nStack = inputStack.length;
		if (frame.inputLocals == null)
		{
			frame.inputLocals = new int[nLocal];
			changed = true;
		}
		for (int i = 0; i < nLocal; i++)
		{
			int t;
			if (outputLocals != null && i < outputLocals.length)
			{
				int s = outputLocals[i];
				if (s == 0)
				{
					t = inputLocals[i];
				} else
				{
					int dim = s & 0xf0000000;
					int kind = s & 0xf000000;
					if (kind == 0x1000000)
					{
						t = s;
					} else
					{
						if (kind == 0x2000000)
							t = dim + inputLocals[s & 0x7fffff];
						else
							t = dim + inputStack[nStack - (s & 0x7fffff)];
						if ((s & 0x800000) != 0 && (t == 0x1000004 || t == 0x1000003))
							t = 0x1000000;
					}
				}
			} else
			{
				t = inputLocals[i];
			}
			if (initializations != null)
				t = init(cw, t);
			changed |= merge(cw, t, frame.inputLocals, i);
		}

		if (edge > 0)
		{
			for (int i = 0; i < nLocal; i++)
			{
				int t = inputLocals[i];
				changed |= merge(cw, t, frame.inputLocals, i);
			}

			if (frame.inputStack == null)
			{
				frame.inputStack = new int[1];
				changed = true;
			}
			changed |= merge(cw, edge, frame.inputStack, 0);
			return changed;
		}
		int nInputStack = inputStack.length + owner.inputStackTop;
		if (frame.inputStack == null)
		{
			frame.inputStack = new int[nInputStack + outputStackTop];
			changed = true;
		}
		for (int i = 0; i < nInputStack; i++)
		{
			int t = inputStack[i];
			if (initializations != null)
				t = init(cw, t);
			changed |= merge(cw, t, frame.inputStack, i);
		}

		for (int i = 0; i < outputStackTop; i++)
		{
			int s = outputStack[i];
			int dim = s & 0xf0000000;
			int kind = s & 0xf000000;
			int t;
			if (kind == 0x1000000)
			{
				t = s;
			} else
			{
				if (kind == 0x2000000)
					t = dim + inputLocals[s & 0x7fffff];
				else
					t = dim + inputStack[nStack - (s & 0x7fffff)];
				if ((s & 0x800000) != 0 && (t == 0x1000004 || t == 0x1000003))
					t = 0x1000000;
			}
			if (initializations != null)
				t = init(cw, t);
			changed |= merge(cw, t, frame.inputStack, nInputStack + i);
		}

		return changed;
	}

	private static boolean merge(ClassWriter cw, int t, int types[], int index)
	{
		int u = types[index];
		if (u == t)
			return false;
		if ((t & 0xfffffff) == 0x1000005)
		{
			if (u == 0x1000005)
				return false;
			t = 0x1000005;
		}
		if (u == 0)
		{
			types[index] = t;
			return true;
		}
		int v;
		if ((u & 0xff00000) == 0x1700000 || (u & 0xf0000000) != 0)
		{
			if (t == 0x1000005)
				return false;
			if ((t & 0xfff00000) == (u & 0xfff00000))
			{
				if ((u & 0xff00000) == 0x1700000)
				{
					v = t & 0xf0000000 | 0x1700000 | cw.getMergedType(t & 0xfffff, u & 0xfffff);
				} else
				{
					int vdim = 0xf0000000 + (u & 0xf0000000);
					v = vdim | 0x1700000 | cw.addType("java/lang/Object");
				}
			} else
			if ((t & 0xff00000) == 0x1700000 || (t & 0xf0000000) != 0)
			{
				int tdim = ((t & 0xf0000000) != 0 && (t & 0xff00000) != 0x1700000 ? 0xf0000000 : 0) + (t & 0xf0000000);
				int udim = ((u & 0xf0000000) != 0 && (u & 0xff00000) != 0x1700000 ? 0xf0000000 : 0) + (u & 0xf0000000);
				v = Math.min(tdim, udim) | 0x1700000 | cw.addType("java/lang/Object");
			} else
			{
				v = 0x1000000;
			}
		} else
		if (u == 0x1000005)
			v = (t & 0xff00000) != 0x1700000 && (t & 0xf0000000) == 0 ? 0x1000000 : t;
		else
			v = 0x1000000;
		if (u != v)
		{
			types[index] = v;
			return true;
		} else
		{
			return false;
		}
	}

	static 
	{
		int b[] = new int[202];
		String s = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
		for (int i = 0; i < b.length; i++)
			b[i] = s.charAt(i) - 69;

		SIZE = b;
	}
}
