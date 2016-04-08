package org.dc.pr0ck;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

public class Pr0ckTests {
	
	private static final String PASSWORD = "senator";
	private static final File SOURCE_DIRECTORY = new File("/media/martiuser/508cda5f-fadb-46e5-9b63-768ceb6d97a7/Personal/.funs/Organized/Wrestling/pr0ck/");
	private static final File OUTPUT_FILE = new File("/home/martiuser/output.pr0ck");
	
	@Test
	public void testMime() throws IOException, ProckPasswordException, ProckXmlException, ProckException {
		ProckReader reader = new DomProckReader(OUTPUT_FILE);
		reader.setPassword(PASSWORD);
		ProckDirectory rootDirectory = reader.load();
		printDirectory(rootDirectory);
		
		String prockPath1 = "Stills/dt693_1100/00001608.jpg";
		ProckFile file1 = rootDirectory.searchFile(prockPath1);
		ProckFileInputStream stream1 = new ProckFileInputStream(file1);
		try {
			
		} finally {
			stream1.close();
		}
	}
	
	@Test
	public void testDirectory() throws ProckException {
		ProckDirectory root = ProckDirectory.newRoot();
		ProckDirectory a = new ProckDirectory("a");
		ProckDirectory aa = new ProckDirectory("aa");
		ProckDirectory aaa = new ProckDirectory("aaa");
		ProckDirectory aaaa = new ProckDirectory("aaaa");
		ProckDirectory aaaaa = new ProckDirectory("aaaaa");
		root.add(a);
		root.add(aa);
		root.add(aaa);
		root.add(aaaa);
		root.add(aaaaa);

		ProckDirectory b = new ProckDirectory("b");
		ProckDirectory bb = new ProckDirectory("bb");
		ProckDirectory bbb = new ProckDirectory("bbb");
		ProckDirectory bbbb = new ProckDirectory("bbbb");
		ProckDirectory bbbbb = new ProckDirectory("bbbbb");
		a.add(b);
		aa.add(bb);
		aaa.add(bbb);
		aaaa.add(bbbb);
		aaaaa.add(bbbbb);
		
		ProckDirectory c = new ProckDirectory("c");
		ProckDirectory cc = new ProckDirectory("cc");
		ProckDirectory ccc = new ProckDirectory("ccc");
		ProckDirectory cccc = new ProckDirectory("cccc");
		ProckDirectory ccccc = new ProckDirectory("ccccc");
		b.add(c);
		bb.add(cc);
		bbb.add(ccc);
		bbbb.add(cccc);
		bbbbb.add(ccccc);
		
		c.newFile(new File("."), "d.file", 0, 1);
		cc.newFile(new File("."), "dd.file", 0, 1);
		ccc.newFile(new File("."), "ddd.file", 0, 1);
		cccc.newFile(new File("."), "dddd.file", 0, 1);
		ccccc.newFile(new File("."), "ddddd.file", 0, 1);
		
		assertNotNull(root.searchDirectory("a/b/c"));
		assertNotNull(root.searchDirectory("a/b/c/"));
		assertNotNull(aaaaa.searchDirectory("bbbbb"));
		assertNotNull(bbbb.searchDirectory("cccc"));
		assertNull(ccc.searchDirectory("ddd"));
		
		assertNotNull(ccc.searchFile("ddd.file"));
		assertNotNull(root.searchFile("a/b/c/d.file"));
		assertNotNull(root.searchFile("aa/bb/cc/dd.file"));
		assertNull(root.searchFile("aa/bb/cc/d.file"));
	}
	
	@Test
	public void testBuilder() throws IOException, ParserConfigurationException {
		if (OUTPUT_FILE.exists()) {
			assertTrue(OUTPUT_FILE.delete());
		}

		DirectoryTraverser directoryTraverser = new DirectoryTraverser();
		DomProckBuilder builder = new DomProckBuilder(SOURCE_DIRECTORY);
		directoryTraverser.traverse(SOURCE_DIRECTORY, builder);
		builder.setPassword(PASSWORD);
		builder.build(OUTPUT_FILE);
		
		assertTrue(OUTPUT_FILE.exists());
	}
	
	@Test
	public void testReader() throws IOException, ProckException {
		ProckReader reader = new DomProckReader(OUTPUT_FILE);
		reader.setPassword(PASSWORD);
		ProckDirectory rootDirectory = reader.load();
		printDirectory(rootDirectory);
		
		String prockPath1 = "Stills/jpegDT693/00001608.jpg";
		ProckFile file1 = rootDirectory.searchFile(prockPath1);
		ProckFileInputStream stream1 = new ProckFileInputStream(file1);
		try {
			byte[] buffer = new byte[(int) file1.getLength()];
			stream1.read(buffer);

			assertTrue(String.valueOf(stream1.available()), stream1.available() == 0);
			
			FileOutputStream testImage = new FileOutputStream("testImage1.jpg");
			try {
				testImage.write(buffer);
				testImage.flush();
				testImage.close();
			} finally {
				testImage.close();
			}
			
		} finally {
			stream1.close();
		}
		
		String prockPath2 = "Stills/jpegDT693/00001621.jpg";
		ProckFile file2 = rootDirectory.searchFile(prockPath2);
		ProckFileInputStream stream2 = new ProckFileInputStream(file2);
		try {
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			FileOutputStream testImage = new FileOutputStream("testImage2.jpg");
			try {
				while ((bytesRead = stream2.read(buffer)) > -1) {
					testImage.write(buffer, 0, bytesRead);
					testImage.flush();
				}

				assertTrue(String.valueOf(stream2.available()), stream2.available() == 0);
			} finally {
				testImage.close();
			}
			
		} finally {
			stream2.close();
		}

		String prockPath3 = "DT-693-01DL Topple the Topless - Tylene Buck v JC Marie.mp4";
		ProckFile file3 = rootDirectory.searchFile(prockPath3);
		ProckFileInputStream stream3 = new ProckFileInputStream(file3);
		try {
			byte[] buffer = new byte[8192];
			int bytesRead = -1;
			FileOutputStream testImage = new FileOutputStream("dt693");
			try {
				while ((bytesRead = stream3.read(buffer)) > -1) {
					testImage.write(buffer, 0, bytesRead);
					testImage.flush();
				}

				assertTrue(String.valueOf(stream3.available()), stream3.available() == 0);
			} finally {
				testImage.close();
			}
			
		} finally {
			stream3.close();
		}
	}
	
	private void printDirectory(ProckDirectory directory) {
		System.out.println(directory.getPath());
		for (ProckDirectory dir : directory.getDirectories()) {
			printDirectory(dir);
		}
		for (ProckFile file : directory.getFiles()) {
			System.out.println(file.getPath());
		}
	}
}
