package com.twitminer.event;

import java.util.EventObject;

import twitter4j.auth.RequestToken;

public class AuthorizationEvent extends EventObject {

	RequestToken reqToken;
	
	public AuthorizationEvent(Object src, RequestToken reqToken) {
		super(src);
		this.reqToken = reqToken;
	}

}
