package org.dc.pr0ck;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public final class ProckWriter {

	private ProckWriter() { }
	
	public static void writeString(String string, OutputStream stream) throws IOException {
		writeInt(string.length(), stream);
		stream.write(string.getBytes());
	}
	
	public static void writeInt(int i, OutputStream stream) throws IOException {
		byte[] intBytes = ByteBuffer.allocate(4).putInt(i).array();
		stream.write(intBytes);
	}
}
