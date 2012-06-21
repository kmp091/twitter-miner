package com.twitminer.dao;

import com.twitminer.beans.Emotion;

public abstract class EmotionDAO {

	public static int HAPPY = 1;
	public static int SAD = 2;
	public static int DISGUST = 3;
	public static int SURPRISE = 4;
	
	public abstract Emotion[] getEmotions();
	public abstract Emotion getEmotionById(int emoId);
	
}
