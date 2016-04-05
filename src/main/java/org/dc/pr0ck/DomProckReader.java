package org.dc.pr0ck;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomProckReader extends ProckReader {
	
	private Document document = null;

	public DomProckReader(File prockFile) throws FileNotFoundException {
		super(prockFile);
	}

	@Override
	protected ProckDirectory getRootDirectory(String xml, long endOfHeader) throws ProckXmlException, ProckException {
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new ProckXmlException(e);
		}
		
		Element prock = (Element) document.getElementsByTagName(ProckConstants.ROOT_ELEMENT_NAME).item(0);
		
		ProckDirectory rootDirectory = ProckDirectory.newRoot();
		traverse(prock, rootDirectory, endOfHeader);
		
		return rootDirectory;
	}

	private void traverse(Element element, ProckDirectory currentDirectory, long endOfHeader) throws ProckException {
		NodeList nodeList = element.getChildNodes();
		for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				if (e.getTagName().equals(ProckConstants.DIRECTORY_ELEMENT_NAME)) {
					ProckDirectory subDirectory = new ProckDirectory(e.getAttribute(ProckConstants.NAME_ATTRIBUTE_NAME));
					currentDirectory.add(subDirectory);
					traverse(e, subDirectory, endOfHeader);
				} else if (e.getTagName().equals(ProckConstants.FILE_ELEMENT_NAME)) {
					String name = e.getAttribute(ProckConstants.NAME_ATTRIBUTE_NAME);
					long length = Long.parseLong(e.getAttribute(ProckConstants.LENGTH_ATTRIBUTE_NAME));
					long offset = Long.parseLong(e.getAttribute(ProckConstants.OFFSET_ATTRIBUTE_NAME));
					currentDirectory.newFile(prockFile, name, length, offset + endOfHeader);
				}
			}
		}
	}
}
