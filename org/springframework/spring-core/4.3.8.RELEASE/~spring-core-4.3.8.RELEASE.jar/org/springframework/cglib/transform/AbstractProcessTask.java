// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractProcessTask.java

package org.springframework.cglib.transform;

import java.io.File;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.FileSet;

public abstract class AbstractProcessTask extends Task
{

	private Vector filesets;

	public AbstractProcessTask()
	{
		filesets = new Vector();
	}

	public void addFileset(FileSet set)
	{
		filesets.addElement(set);
	}

	protected Collection getFiles()
	{
		Map fileMap = new HashMap();
		org.apache.tools.ant.Project p = getProject();
		for (int i = 0; i < filesets.size(); i++)
		{
			FileSet fs = (FileSet)filesets.elementAt(i);
			DirectoryScanner ds = fs.getDirectoryScanner(p);
			String srcFiles[] = ds.getIncludedFiles();
			File dir = fs.getDir(p);
			for (int j = 0; j < srcFiles.length; j++)
			{
				File src = new File(dir, srcFiles[j]);
				fileMap.put(src.getAbsolutePath(), src);
			}

		}

		return fileMap.values();
	}

	public void execute()
		throws BuildException
	{
		beforeExecute();
		for (Iterator it = getFiles().iterator(); it.hasNext();)
			try
			{
				processFile((File)it.next());
			}
			catch (Exception e)
			{
				throw new BuildException(e);
			}

	}

	protected void beforeExecute()
		throws BuildException
	{
	}

	protected abstract void processFile(File file)
		throws Exception;
}
