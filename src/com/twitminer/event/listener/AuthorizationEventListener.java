package com.twitminer.event.listener;

import com.twitminer.event.AuthorizationEvent;

public interface AuthorizationEventListener {

	public void OnAuthorizeRequest(AuthorizationEvent authEvent);
	
}
