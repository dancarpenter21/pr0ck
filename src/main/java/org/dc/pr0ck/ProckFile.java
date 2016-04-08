package org.dc.pr0ck;

import java.io.File;
import java.io.FileDescriptor;

public class ProckFile {
	
	private final File baseFile;
	private final FileDescriptor baseFileDescriptor;
	private final String name;
	private final ProckDirectory parent;
	private final long length;
	private final long offset;
	
	ProckFile(FileDescriptor baseFileDescriptor, String name, long length, long offset, ProckDirectory parent) throws ProckException {
		if (baseFileDescriptor == null) {
			throw new IllegalArgumentException("File descriptor cannot be null");
		}

		this.baseFileDescriptor = baseFileDescriptor;
		this.baseFile = null;
		this.name = name;
		this.parent = parent;
		this.length = length;
		this.offset = offset;
	}

	ProckFile(File baseFile, String name, long length, long offset, ProckDirectory parent) throws ProckException {
		if (baseFile== null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		this.baseFile = baseFile;
		this.baseFileDescriptor = null;
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
	
	public FileDescriptor getBaseFileDescriptor() {
		return baseFileDescriptor;
	}
	
	public File getBaseFile() {
		return baseFile;
	}
}
