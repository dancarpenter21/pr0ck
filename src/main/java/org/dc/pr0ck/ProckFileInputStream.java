package org.dc.pr0ck;

import java.io.FileInputStream;
import java.io.IOException;

public class ProckFileInputStream extends FileInputStream {

	private final ProckFile file;
	
	private int bytesRead = 0;
	
	public ProckFileInputStream(ProckFile file) throws IOException {
		super(file.getBaseFile());
		this.file = file;
		super.getChannel().position(file.getOffset());
	}
	
	@Override
	public int read() throws IOException {
		if (available() > 0) {
			int byteValue = super.read();
			if (byteValue > -1) {
				bytesRead++;
			}
			
			return byteValue;
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
	public long skip(long n) throws IOException {
		return super.skip(n);
	}


	@Override
	public int available() throws IOException {
		return (int) (file.getLength() + file.getOffset() - super.getChannel().position());
	}

	@Override
	public synchronized void mark(int readlimit) {
		// TODO Auto-generated method stub
		super.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		// TODO Auto-generated method stub
		super.reset();
	}

	@Override
	public boolean markSupported() {
		return true;
	}
	
	
}
