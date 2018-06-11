// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Handler.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Label

class Handler
{

	Label start;
	Label end;
	Label handler;
	String desc;
	int type;
	Handler next;

	Handler()
	{
	}

	static Handler remove(Handler h, Label start, Label end)
	{
		if (h == null)
			return null;
		h.next = remove(h.next, start, end);
		int hstart = h.start.position;
		int hend = h.end.position;
		int s = start.position;
		int e = end != null ? end.position : 0x7fffffff;
		if (s < hend && e > hstart)
			if (s <= hstart)
			{
				if (e >= hend)
					h = h.next;
				else
					h.start = end;
			} else
			if (e >= hend)
			{
				h.end = start;
			} else
			{
				Handler g = new Handler();
				g.start = end;
				g.end = h.end;
				g.handler = h.handler;
				g.desc = h.desc;
				g.type = h.type;
				g.next = h.next;
				h.end = start;
				h.next = g;
			}
		return h;
	}
}
