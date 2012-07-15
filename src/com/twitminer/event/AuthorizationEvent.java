package com.twitminer.event;

import java.util.EventObject;

import twitter4j.auth.RequestToken;

public class AuthorizationEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7871041025672004461L;
	RequestToken reqToken;
	
	public AuthorizationEvent(Object src, RequestToken reqToken) {
		super(src);
		this.reqToken = reqToken;
	}

}
