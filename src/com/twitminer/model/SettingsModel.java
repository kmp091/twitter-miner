package com.twitminer.model;

public class SettingsModel {

	private int numOfTweets;
	private String tweetTopic;
	
	public SettingsModel(int numOfTweets, String tweetTopic) {
		this.numOfTweets = numOfTweets;
		this.tweetTopic = tweetTopic;
	}

	public int getNumOfTweets() {
		return numOfTweets;
	}

	public void setNumOfTweets(int numOfTweets) {
		this.numOfTweets = numOfTweets;
	}

	public String getTweetTopic() {
		return tweetTopic;
	}

	public void setTweetTopic(String tweetTopic) {
		this.tweetTopic = tweetTopic;
	}
	
}
