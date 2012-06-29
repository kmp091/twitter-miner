package com.twitminer.dao;

import java.util.List;

import com.twitminer.beans.Tweet;

public abstract class TweetDAO {

	public abstract Tweet getTweetByID(long tweetId);
	public abstract Tweet[] getTweetsByUserID(long userId);
	public abstract void insertTweet(Tweet newTweet);
	public abstract void deleteTweetByID(long tweetId);
	public abstract List<Tweet> getTweets();
	public Tweet[] getTweetsArray() {
		List<Tweet> tweets = getTweets();
		return tweets.toArray(new Tweet[tweets.size()]);
	}
	
}
