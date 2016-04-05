package org.dc.pr0ck;

import java.io.File;

public class ProckFile {
	
	private final File baseFile;
	private final String name;
	private final ProckDirectory parent;
	private final long length;
	private final long offset;
	
	ProckFile(File baseFile, String name, long length, long offset, ProckDirectory parent) throws ProckException {
		this.baseFile = baseFile;
		this.name = name;
		this.parent = parent;
		this.length = length;
		this.offset = offset;
	}

	public String getName() {
		return name;
	}
	
	public String getPath() {
		return parent.getPath() + name;
	}
	
	public long getLength() {
		return length;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public File getBaseFile() {
		return baseFile;
	}
}
