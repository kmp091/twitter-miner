package com.twitminer.login;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import twitter4j.auth.RequestToken;

public abstract class AuthLogic {

	private String authCode;
	
	public String getAuthenticationToken() {
		return this.authCode;
	}
	
	protected URL getAuthenticationURL(RequestToken reqToken) throws MalformedURLException {
		return new URL(reqToken.getAuthorizationURL());
	}
	
	protected URI getAuthenticationURI(RequestToken reqToken) throws MalformedURLException, URISyntaxException {
		return getAuthenticationURL(reqToken).toURI();
	}
	
	protected void setAuthenticationCode(String code) {
		this.authCode = code;
	}
	
}
