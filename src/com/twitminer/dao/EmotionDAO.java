package com.twitminer.dao;

import com.twitminer.beans.Emotion;

public abstract class EmotionDAO {

	public static final int HAPPY = 1;
	public static final int SAD = 2;
	public static final int SURPRISE = 3;
	public static final int DISGUST = 4;
	
	public abstract Emotion[] getEmotions();
	public abstract Emotion getEmotionById(int emoId);
	
	public String toString() {
		Emotion[] emotions = this.getEmotions();
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < emotions.length; i++) {
			Emotion emo = emotions[i];
			
			builder.append(emo.getEmotionName());
			
			if (i < emotions.length - 1) {
				builder.append(",");
			}
		}
		
		return builder.toString();
	}
	
}
