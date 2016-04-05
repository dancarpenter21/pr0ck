package org.dc.pr0ck;

import java.io.File;

/**
 * Not really using events yet, this is more of a placeholder if events
 * are needed.
 * 
 * @author Dc
 */
public abstract class EventedProckBuilder implements ProckBuilder {

	protected final File sourceDirectory;
	
	protected String password = null;
	
	public EventedProckBuilder(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
}
