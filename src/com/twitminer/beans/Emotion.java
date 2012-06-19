package com.twitminer.beans;

public class Emotion {

	private int emotionId;
	private String emotionName;

	public Emotion () {
		
	}
	
	public Emotion(int emotionId, String emotionName) {
		this();
		this.emotionId = emotionId;
		this.emotionName = emotionName;
	}
	
	public int getEmotionId() {
		return emotionId;
	}
	
	public void setEmotionId(int emotionId) {
		this.emotionId = emotionId;
	}
	
	public String getEmotionName() {
		return emotionName;
	}
	
	public void setEmotionName(String emotionName) {
		this.emotionName = emotionName;
	}
	
}
