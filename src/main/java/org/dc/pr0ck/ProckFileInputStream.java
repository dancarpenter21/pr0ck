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

	private final Object markLock = new Object();
	private final ProckFile prockFile;
	
	private int bytesRead = 0;
	private int readLimit = -1;
	private long mark = 0;
	
	private ProckFileInputStream(ProckFile prockFile, File file) throws IOException {
		super(file);
		this.prockFile = prockFile;
		super.getChannel().position(prockFile.getOffset());
		mark = super.getChannel().position();
	}
	
	private ProckFileInputStream(ProckFile prockFile, FileDescriptor file) throws IOException {
		super(file);
		this.prockFile = prockFile;
		super.getChannel().position(prockFile.getOffset());
		mark = super.getChannel().position();
	}
	
	@Override
	public int read() throws IOException {
		if (available() > 0) {
			int byteValue = super.read();
			if (byteValue > -1) {
				synchronized (markLock) {
					if (readLimit > -1) {
						bytesRead++;
						checkReadLimit();
					}
				}
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
			return markRead(b, off, available);
		} else {
			return markRead(b, off, len);
		}
	}
	
	private int markRead(byte[] b, int off, int len) throws IOException {
		int read = super.read(b, off, len);
		synchronized (markLock) {
			if (readLimit > -1) {
				bytesRead += read;
				checkReadLimit();
			}
		}
		
		return read;
	}

	@Override
	public long skip(long n) throws IOException {
		long skipped = super.skip(n);
		synchronized (markLock) {
			if (readLimit > -1) {
				bytesRead += skipped;
				checkReadLimit();
			}
		}

		return skipped;
	}


	@Override
	public int available() throws IOException {
		return (int) (prockFile.getLength() + prockFile.getOffset() - super.getChannel().position());
	}

	@Override
	public synchronized void mark(int readlimit) {
		synchronized (markLock) {
			try {
				mark = super.getChannel().position();
				this.readLimit = readlimit;
				bytesRead = 0;
			} catch (IOException e) {
				mark = 0;
			}
		}
	}

	@Override
	public synchronized void reset() throws IOException {
		synchronized (markLock) {
			if (readLimit == -1) {
				throw new IOException("Illegal state.");
			}

			super.getChannel().position(mark);
			bytesRead = 0;
			readLimit = -1;
		}
	}

	@Override
	public boolean markSupported() {
		return true;
	}
	
	private void checkReadLimit() {
		if (bytesRead > readLimit) {
			bytesRead = 0;
			readLimit = -1;
		}
	}
	
}
