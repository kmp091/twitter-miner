package com.twitminer.login;

import twitter4j.auth.RequestToken;

public class AuthLogicFactory {

	public static final int GUI = 1;
	
	public AuthLogicFactory() {
		
	}

	public static AuthLogic getInstance(RequestToken token) {
		return getInstance(token, GUI);
	}
	
	public static AuthLogic getInstance(RequestToken token, int logicType) {
		switch(logicType) {
		case GUI:
			default:
				return new GUIAuthLogic(token);
		}
	}

}
