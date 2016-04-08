package org.dc.pr0ck;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class ProckReader {
	
	private String password = null;
	protected final File prockFile;
	protected final FileDescriptor prockFileDescriptor;
	private long endOfHeader = -1;

	public ProckReader(FileDescriptor prockFileDescriptor) {
		this.prockFileDescriptor = prockFileDescriptor;
		prockFile = null;
	}
	
	public ProckReader(File prockFile) {
		this.prockFile = prockFile;
		prockFileDescriptor = null;
	}
	
	/**
	 * Loads a pr0ck file system's metadata into memory. This method must be
	 * called before any other methods may be called. This call may be expensive
	 * but only needs to be executed once.
	 * 
	 * @throws IOException generic file access failure
	 * @throws ProckPasswordException bad password supplied
	 * @throws ProckXmlException error parsing header
	 * @throws ProckException 
	 */
	public ProckDirectory load() throws IOException, ProckPasswordException, ProckXmlException, ProckException {
		FileInputStream stream = null;
		if (prockFile != null) {
			stream = new FileInputStream(prockFile);
		} else if (prockFileDescriptor != null) {
			stream = new FileInputStream(prockFileDescriptor);
		}

		try {
			int passwordByte = stream.read();
			if (passwordByte == 1) {
				String password = readString(stream);
				if (!password.equals(this.password)) {
					throw new ProckPasswordException("Passwords do not match");
				}
			}
			
			String xmlHeader = readString(stream);
			endOfHeader = stream.getChannel().position();
			return getRootDirectory(xmlHeader, endOfHeader);
		} finally {
			if (stream != null) stream.close();
		}
	}
	
	/**
	 * @param password password of the pr0ck file
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	protected abstract ProckDirectory getRootDirectory(String xml, long endOfHeader) throws ProckXmlException, ProckException;
	
	protected static final class ProckFileMeta {
		long streamPointer = -1;
		long length = -1;
	}
	
	private static int readInt(FileInputStream stream) throws IOException {
		byte[] lengthBytes = new byte[4];
		stream.read(lengthBytes);
		return ByteBuffer.wrap(lengthBytes).getInt();
	}
	
	private static String readString(FileInputStream stream) throws IOException {
		int stringLength = readInt(stream);
		byte[] stringBuffer = new byte[stringLength];
		stream.read(stringBuffer);
		return new String(stringBuffer);
	}
}
