package com.twitminer.event.listener;

import com.twitminer.event.TaskFinishEvent;

public interface TaskFinishEventListener {

	public void onTaskFinished(TaskFinishEvent evt);
	
}
