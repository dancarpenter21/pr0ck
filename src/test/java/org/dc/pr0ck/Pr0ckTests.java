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
	private static final File SOURCE_DIRECTORY = new File("./src/test/resources/pr0ck/");
	private static final File TEST_FILE = new File("./src/test/resources/test.pr0ck");
	private static final File OUTPUT_TEST_DIR = new File("./src/test/resources/testOutputs/");
	
	@Test
	public void testMime() throws IOException, ProckPasswordException, ProckXmlException, ProckException {
		ProckReader reader = new DomProckReader(TEST_FILE);
		reader.setPassword(PASSWORD);
		ProckDirectory rootDirectory = reader.load();
		printDirectory(rootDirectory);
		
		String prockPath1 = "Dir/bird_gif.gif";
		ProckFile file1 = rootDirectory.searchFile(prockPath1);
		ProckFileInputStream stream1 = ProckFileInputStream.open(file1);
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
		if (TEST_FILE.exists()) {
			assertTrue(TEST_FILE.delete());
		}

		DirectoryTraverser directoryTraverser = new DirectoryTraverser();
		DomProckBuilder builder = new DomProckBuilder(SOURCE_DIRECTORY);
		directoryTraverser.traverse(SOURCE_DIRECTORY, builder);
		builder.setPassword(PASSWORD);
		builder.build(TEST_FILE);
		
		assertTrue(TEST_FILE.exists());
	}
	
	@Test
	public void testReader() throws IOException, ProckException {
		if (!OUTPUT_TEST_DIR.exists()) {
			OUTPUT_TEST_DIR.mkdirs();
		}
		ProckReader reader = new DomProckReader(TEST_FILE);
		reader.setPassword(PASSWORD);
		ProckDirectory rootDirectory = reader.load();
		printDirectory(rootDirectory);
		
		String prockPath1 = "Dir/bird_gif.gif";
		ProckFile file1 = rootDirectory.searchFile(prockPath1);
		ProckFileInputStream stream1 = ProckFileInputStream.open(file1);
		try {
			byte[] buffer = new byte[(int) file1.getLength()];
			stream1.read(buffer);

			assertTrue(String.valueOf(stream1.available()), stream1.available() == 0);
			
			FileOutputStream testImage = new FileOutputStream(new File(OUTPUT_TEST_DIR, "testOutput1.gif"));
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
		
		String prockPath2 = "test_mp4.mp4";
		ProckFile file2 = rootDirectory.searchFile(prockPath2);
		ProckFileInputStream stream2 = ProckFileInputStream.open(file2);
		try {
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			FileOutputStream testImage = new FileOutputStream(new File(OUTPUT_TEST_DIR, "testOutput2.mp4"));
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

		String prockPath3 = "test_webm.webm";
		ProckFile file3 = rootDirectory.searchFile(prockPath3);
		ProckFileInputStream stream3 = ProckFileInputStream.open(file3);
		try {
			byte[] buffer = new byte[8192];
			int bytesRead = -1;
			FileOutputStream testImage = new FileOutputStream(new File(OUTPUT_TEST_DIR, "testOutput3.webm"));
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
