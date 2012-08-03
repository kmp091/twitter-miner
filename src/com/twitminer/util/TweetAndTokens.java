package com.twitminer.util;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.beans.Tweet;

public class TweetAndTokens {

	private Tweet tweet;
	private TokenizedTweet tokens;
	
	public TweetAndTokens(Tweet tweet, TokenizedTweet tokens) {
		this.tweet = tweet;
		this.tokens = tokens;
	}
	
	public Tweet getTweet() {
		return tweet;
	}
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}
	public TokenizedTweet getTokens() {
		return tokens;
	}
	public void setTokens(TokenizedTweet tokens) {
		this.tokens = tokens;
	}
	
}
