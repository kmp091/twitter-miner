package com.twitminer.dao.arraylist;

import java.util.ArrayList;
import java.util.List;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.TokenizedTweetDAO;

public class ArrayListTokenizedTweetDAO extends TokenizedTweetDAO {

	ArrayList<TokenizedTweet> tokenizedTweets;
	
	public ArrayListTokenizedTweetDAO () {
		this.tokenizedTweets = new ArrayList<TokenizedTweet>();
	}
	
	@Override
	public void insertTweet(TokenizedTweet newTweet) {
		tokenizedTweets.add(newTweet);
	}

	@Override
	public List<TokenizedTweet> getTweets() {
		return this.tokenizedTweets;
	}

}
