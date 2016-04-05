package org.dc.pr0ck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomProckBuilder extends EventedProckBuilder {
	private final Document document;
	private final Element root;
	
	private final Deque<Element> directoryStack = new ArrayDeque<>();
	private final Queue<File> fileWriteQueue = new ArrayDeque<>();
	
	private long fileBytesWritten = 0;
	
	public DomProckBuilder(File sourceDirectory) throws ParserConfigurationException {
		super(sourceDirectory);

		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		root = document.createElement(ProckConstants.ROOT_ELEMENT_NAME);
		document.appendChild(root);
	}
	
	@Override
	public void onFile(File file) {
		Element fileElement = document.createElement(ProckConstants.FILE_ELEMENT_NAME);
		fileElement.setAttribute(ProckConstants.NAME_ATTRIBUTE_NAME, file.getName());
		if (directoryStack.size() > 0) {
			directoryStack.peek().appendChild(fileElement);
		} else {
			root.appendChild(fileElement);
		}
		
		if (fileWriteQueue.add(file)) {
			fileElement.setAttribute(ProckConstants.OFFSET_ATTRIBUTE_NAME, String.valueOf(fileBytesWritten));
			fileElement.setAttribute(ProckConstants.LENGTH_ATTRIBUTE_NAME, String.valueOf(file.length()));
			fileBytesWritten += file.length();
		} else {
			throw new RuntimeException("File Write Queue access exception");
		}
	}
	
	@Override
	public void onDirectoryStart(File file) {
		Element directoryElement = document.createElement(ProckConstants.DIRECTORY_ELEMENT_NAME);
		directoryElement.setAttribute(ProckConstants.NAME_ATTRIBUTE_NAME, file.getName());
		if (directoryStack.size() > 0) {
			directoryStack.peek().appendChild(directoryElement);
		}

		directoryStack.push(directoryElement);
	}
	
	@Override
	public void onDirectoryEnd(File file) {
		Element element = null;
		if (directoryStack.size() > 0) {
			element = directoryStack.pop();
		}
		
		if (directoryStack.size() == 0) {
			root.appendChild(element);
		}
	}
	
	@Override
	public void build(File outputFile) throws IOException {
		FileOutputStream stream = new FileOutputStream(outputFile);
		try {
			if (password == null) {
				stream.write(0);
			} else {
				stream.write(1);
				ProckWriter.writeString(password, stream);
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(writer));
			String header = writer.getBuffer().toString();
			ProckWriter.writeString(header, stream);
			
			stream.flush();
			
			File f = null;
			byte[] buffer = new byte[8192];
			while ((f = fileWriteQueue.poll()) != null) {
				FileInputStream fileInputStream = null;
				try {
					fileInputStream = new FileInputStream(f);
					int bytesRead = -1;
					while ((bytesRead = fileInputStream.read(buffer)) > -1) {
						stream.write(buffer, 0, bytesRead);
						stream.flush();
					}
				} catch (IOException e) {
					onError(e, "In individual file copy");
				} finally {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				}
			}
			
			stream.flush();
			
		} catch (TransformerException e) {
			onError(e, "XPath issue");
		} finally {
			stream.close();
		}
	}
	
	protected void onError(Exception e, String message) {
		e.printStackTrace();
	}
}
