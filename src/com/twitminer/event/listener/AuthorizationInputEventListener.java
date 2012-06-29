package com.twitminer.event.listener;

import com.twitminer.event.AuthorizationInputEvent;

public interface AuthorizationInputEventListener {

	public void onAuthorized(AuthorizationInputEvent evt);
	
}
