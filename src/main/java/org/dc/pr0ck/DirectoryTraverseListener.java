package org.dc.pr0ck;

import java.io.File;

public interface DirectoryTraverseListener {
	void setPassword(String password);
	
	void onDirectoryStart(File file);

	void onDirectoryEnd(File file);

	void onFile(File file);
}
