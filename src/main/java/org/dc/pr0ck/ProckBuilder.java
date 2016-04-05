package org.dc.pr0ck;

import java.io.File;
import java.io.IOException;

public interface ProckBuilder extends DirectoryTraverseListener {
	
	void build(File outputFile) throws IOException;

}
