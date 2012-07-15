package com.twitminer.event;

import java.util.EventObject;

public class TaskFinishEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8617348995423939010L;

	public TaskFinishEvent(Object src) {
		super(src);
	}

}
