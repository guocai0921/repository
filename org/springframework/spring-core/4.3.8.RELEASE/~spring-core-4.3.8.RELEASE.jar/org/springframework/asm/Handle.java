// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Handle.java

package org.springframework.asm;


// Referenced classes of package org.springframework.asm:
//			Opcodes

public final class Handle
{

	final int tag;
	final String owner;
	final String name;
	final String desc;
	final boolean itf;

	/**
	 * @deprecated Method Handle is deprecated
	 */

	public Handle(int tag, String owner, String name, String desc)
	{
		this(tag, owner, name, desc, tag == 9);
	}

	public Handle(int tag, String owner, String name, String desc, boolean itf)
	{
		this.tag = tag;
		this.owner = owner;
		this.name = name;
		this.desc = desc;
		this.itf = itf;
	}

	public int getTag()
	{
		return tag;
	}

	public String getOwner()
	{
		return owner;
	}

	public String getName()
	{
		return name;
	}

	public String getDesc()
	{
		return desc;
	}

	public boolean isInterface()
	{
		return itf;
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof Handle))
		{
			return false;
		} else
		{
			Handle h = (Handle)obj;
			return tag == h.tag && itf == h.itf && owner.equals(h.owner) && name.equals(h.name) && desc.equals(h.desc);
		}
	}

	public int hashCode()
	{
		return tag + (itf ? 64 : 0) + owner.hashCode() * name.hashCode() * desc.hashCode();
	}

	public String toString()
	{
		return (new StringBuilder()).append(owner).append('.').append(name).append(desc).append(" (").append(tag).append(itf ? " itf" : "").append(')').toString();
	}
}
