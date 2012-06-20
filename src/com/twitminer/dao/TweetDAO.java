package com.twitminer.dao;

import com.twitminer.beans.Tweet;

public abstract class TweetDAO {

	public abstract Tweet getTweetByID(long tweetId);
	public abstract Tweet[] getTweetsByUserID(long userId);
	public abstract void insertTweet(Tweet newTweet);
	public abstract void deleteTweetByID(long tweetId);
	
}
