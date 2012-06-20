package com.twitminer.dao;

import com.twitminer.beans.Emotion;

public abstract class EmotionDAO {

	public abstract Emotion[] getEmotions();
	public abstract Emotion getEmotionById(int emoId);
	
}
