package com.twitminer.dao;

import java.util.List;

import com.twitminer.beans.TokenizedTweet;

public abstract class TokenizedTweetDAO {

	public abstract void insertTweet(TokenizedTweet newTweet);
	public abstract List<TokenizedTweet> getTweets();
	public TokenizedTweet[] getTweetsArray() {
		List<TokenizedTweet> tweets = getTweets();
		return tweets.toArray(new TokenizedTweet[tweets.size()]);
	}
	
}
