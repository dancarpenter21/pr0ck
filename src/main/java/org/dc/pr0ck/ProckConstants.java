package org.dc.pr0ck;

public final class ProckConstants {

	private  ProckConstants() { }
	
	/**
	 * Name of root element.
	 */
	public static final String ROOT_ELEMENT_NAME = "pr0ck";
	
	/**
	 * Denotes a file.
	 */
	public static final String FILE_ELEMENT_NAME = "f";
	
	/**
	 * Denotes a directory.
	 */
	public static final String DIRECTORY_ELEMENT_NAME = "d";

	/**
	 * Length of file in bytes.
	 */
	public static final String LENGTH_ATTRIBUTE_NAME = "l";
	
	/**
	 * The name of the file.
	 */
	public static final String NAME_ATTRIBUTE_NAME = "n";
	
	/**
	 * Byte offset from end of header where the end of the header is regarded as 0.
	 */
	public static final String OFFSET_ATTRIBUTE_NAME = "o";
	
	/**
	 * Prock path separator character.
	 */
	public static final String PATH_SEPARATOR = "/"; 	// java.io.File.pathSeparator;

}
