package com.twitminer.util;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class TweetCleaner {

	List<String> stopWords;
	
	
	public TweetCleaner() {
		//load List<String> of stop words
		
		//regex of urls
		
		//regex of mentions (@username)
	}
	
	public String tokenizeAndCleanTweet(String tweetText) {
		String lowerCasedTweet = tweetText.toLowerCase();
		lowerCasedTweet.split(" ");
		
		return null;
	}
	
}
