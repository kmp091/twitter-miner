package com.twitminer.dao;

import com.twitminer.beans.Tweet;

public abstract class TweetDAO {

	public abstract Tweet getTweetByID(int tweetId);
	public abstract Tweet[] getTweetsByUserID(int userId);
	public abstract void insertTweet(Tweet newTweet);
	public abstract void deleteTweetByID(int tweetId);
	
}
