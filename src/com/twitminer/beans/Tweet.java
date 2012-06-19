package com.twitminer.beans;

import java.util.Calendar;

public class Tweet {

	private int tweetId;
	private int userId;
	private String text;
	private Calendar dateCreated;
	private int emotionId;
	
	public Tweet () {
		
	}
	
	public Tweet (int tweetId, int userId, String text, Calendar dateCreated) {
		this();
		this.tweetId = tweetId;
		this.userId = userId;
		this.text = text;
		this.dateCreated = dateCreated;
	}
	
	public Tweet (int tweetId, int userId, String text, Calendar dateCreated, int emotionId) {
		this(tweetId, userId, text, dateCreated);
		this.emotionId = emotionId;
	}
	
	public Tweet (int tweetId, int userId, String text, Calendar dateCreated, Emotion emotion) {
		this(tweetId, userId, text, dateCreated, emotion.getEmotionId());
	}
	
	public int getTweetId() {
		return tweetId;
	}
	
	public void setTweetId(int tweetId) {
		this.tweetId = tweetId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Calendar getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public int getEmotionId() {
		return emotionId;
	}
	
	public void setEmotionId(int emotionId) {
		this.emotionId = emotionId;
	}
	
}
