package org.dc.pr0ck;

import java.io.File;
import java.io.IOException;

public class DirectoryTraverser {
	
	public void traverse(File file, DirectoryTraverseListener listener) throws IOException {
		File[] fileList = file.listFiles();
		for (File f : fileList) {
			doTraverse(f, listener);
		}
	}
	
	private void doTraverse(File file, DirectoryTraverseListener listener) {
		if (file.isDirectory()) {
			listener.onDirectoryStart(file);

			File[] files = file.listFiles();
			for (File f : files) {
				doTraverse(f, listener);
			}
			
			listener.onDirectoryEnd(file);
		} else {
			listener.onFile(file);
		}
		
	}
}
