package com.twitminer.beans;

import java.util.Calendar;

public class Tweet {

	private long tweetId;
	private long userId;
	private String text;
	private Calendar dateCreated;
	private int emotionId;
	
	public Tweet () {
		
	}
	
	public Tweet (long tweetId, long userId, String text, Calendar dateCreated) {
		this();
		this.tweetId = tweetId;
		this.userId = userId;
		this.text = text;
		this.dateCreated = dateCreated;
	}
	
	public Tweet (long tweetId, long userId, String text, Calendar dateCreated, int emotionId) {
		this(tweetId, userId, text, dateCreated);
		this.emotionId = emotionId;
	}
	
	public Tweet (long tweetId, long userId, String text, Calendar dateCreated, Emotion emotion) {
		this(tweetId, userId, text, dateCreated, emotion.getEmotionId());
	}
	
	public long getTweetId() {
		return tweetId;
	}
	
	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
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
