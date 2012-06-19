package com.twitminer.dao;

import com.twitminer.beans.Tweet;

public abstract class TweetDAO {

	public abstract Tweet getTweetByID(int tweetId);
	public abstract Tweet getTweetByUserID(int userId);
	public abstract Tweet insertTweet(Tweet newTweet);
	public abstract Tweet deleteTweetByID(int tweetId);
	
}
