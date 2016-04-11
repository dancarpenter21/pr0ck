package org.dc.pr0ck;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class ProckFileInputStream extends FileInputStream {

	public static ProckFileInputStream open(ProckFile file) throws IOException {
		if (file.getBaseFile() != null) {
			return new ProckFileInputStream(file, file.getBaseFile());
		} else {
			return new ProckFileInputStream(file, file.getBaseFileDescriptor());
		}
	}

	private final ProckFile prockFile;
	
	private ProckFileInputStream(ProckFile prockFile, File file) throws IOException {
		super(file);
		this.prockFile = prockFile;
		super.getChannel().position(prockFile.getOffset());
	}
	
	private ProckFileInputStream(ProckFile prockFile, FileDescriptor file) throws IOException {
		super(file);
		this.prockFile = prockFile;
		super.getChannel().position(prockFile.getOffset());
	}
	
	@Override
	public int read() throws IOException {
		if (available() > 0) {
			return super.read();
		} else {
			return -1;
		}
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int available = available();
		if (available <= 0) {
			return -1;
		}

		if (len > available) {
			return super.read(b, off, available);
		} else {
			return super.read(b, off, len);
		}
	}

	@Override
	public int available() throws IOException {
		return (int) (prockFile.getLength() + prockFile.getOffset() - super.getChannel().position());
	}
	
}
