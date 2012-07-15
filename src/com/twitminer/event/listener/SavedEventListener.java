package com.twitminer.event.listener;

public interface SavedEventListener {

	public void onSave();
	public void onSaveSuccess();
	public void onSaveFailed(Exception e);
	
}
