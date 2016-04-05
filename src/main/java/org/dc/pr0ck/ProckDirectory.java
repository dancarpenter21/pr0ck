package org.dc.pr0ck;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProckDirectory {
	
	public static ProckDirectory newRoot() {
		return new ProckDirectory();
	}
	
	private final String name;
	
	private ProckDirectory parent = null;
	private List<ProckDirectory> directories = new ArrayList<>();
	private List<ProckFile> files = new ArrayList<>();
	
	private ProckDirectory() {
		this.name = null;
	}
	
	public ProckDirectory(String name) {
		if (Util.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Directory name cannot be null");
		}

		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		if (name == null) {
			return "";
		}

		if (parent != null) {
			return parent.getPath() + name + ProckConstants.PATH_SEPARATOR;
		} else {
			return name + ProckConstants.PATH_SEPARATOR;
		}
	}
	
	/**
	 * 
	 * @param subDirectory directory to attach
	 */
	public synchronized void add(ProckDirectory subDirectory) {
		for (ProckDirectory prockDirectory : directories) {
			if (prockDirectory.name.equals(subDirectory.name)) {
				return;
			}
		}
		
		if (subDirectory.parent == null) {
			subDirectory.parent = this;
			directories.add(subDirectory);
		}
	}
	
	/**
	 * @param filename file to attach
	 * @throws ProckException 
	 */
	public synchronized void add(ProckFile file) throws ProckException {
		for (ProckFile f : files) {
			if (file.getName().equals(f.getName())) {
				throw new ProckException("File " + f.getName() + " already exists in " + name);
			}
		}
		
		files.add(file);
	}
	
	public ProckFile searchFile(String path) {
		if (Util.isNullOrEmpty(path)) {
			throw new IllegalArgumentException("Cannot search file null path");
		}

		int index = path.indexOf(ProckConstants.PATH_SEPARATOR);
		if (index == -1) {
			return locateFile(path);
		}
		
		// traverse directories in path
		int lastIndex = path.lastIndexOf(ProckConstants.PATH_SEPARATOR);
		String directoryPath = path.substring(0, lastIndex);
		ProckDirectory directory = searchDirectory(directoryPath);
		if (directory == null) {
			return null;
		}
		
		return directory.locateFile(path.substring(lastIndex+1));
	}
	
	private ProckFile locateFile(String name) {
		for (ProckFile file : files) {
			if (file.getName().equals(name)) {
				return file;
			}
		}
		
		return null;
	}
	
	public ProckDirectory searchDirectory(String path) {
		if (path.endsWith(ProckConstants.PATH_SEPARATOR)) {
			path = path.substring(0, path.length()-1);
		}
		if (path.startsWith(ProckConstants.PATH_SEPARATOR)) {
			path = path.substring(1);
		}

		if (Util.isNullOrEmpty(path)) {
			return this;
		}

		int index = path.indexOf(ProckConstants.PATH_SEPARATOR);
		if (index == -1) {
			return locateDirectory(path);
		} else {
			String dirName = path.substring(0, index);
			ProckDirectory dir = locateDirectory(dirName);
			return dir.searchDirectory(path.substring(index));
		}
	}
	
	private ProckDirectory locateDirectory(String name) {
		for (ProckDirectory dir : directories) {
			if (dir.getName().equals(name)) {
				return dir;
			}
		}
		
		return null;
	}
	
	public void newFile(File prockFile, String name, long length, long offset) throws ProckException {
		add(new ProckFile(prockFile, name, length, offset, this));
	}
	
	/**
	 * @return This directory's parent or <code>null</code> if this
	 * directory is a root.
	 */
	public ProckDirectory getParent() {
		return parent;
	}

	/**
	 * @return Sub-directories of this directory.
	 */
	public ProckDirectory[] getDirectories() {
		return directories.toArray(new ProckDirectory[directories.size()]);
	}
	
	/**
	 * @return Sub-files of this directory.
	 */
	public ProckFile[] getFiles() {
		return files.toArray(new ProckFile[files.size()]);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
