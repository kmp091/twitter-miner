package com.twitminer.dao.arraylist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.TweetDAO;

public class ArrayListTweetDAO extends TweetDAO {

	ArrayList<Tweet> tweets;
	
	public ArrayListTweetDAO() {
		tweets = new ArrayList<Tweet>();
	}
	
	@Override
	public Tweet getTweetByID(long tweetId) {
		Tweet toReturn = null;
		
		for (Tweet tweet : tweets) {
			if (tweet.getTweetId() == tweetId) {
				toReturn = tweet;
				break;
			}
		}
		return toReturn;
	}

	@Override
	public Tweet[] getTweetsByUserID(long userId) {
		ArrayList<Tweet> tweetsUnderUser = new ArrayList<Tweet>();
		for (Tweet tweet : tweets) { 
			if (tweet.getUserId() == userId) {
				tweetsUnderUser.add(tweet);
			}
		}
		return tweetsUnderUser.toArray(new Tweet[tweetsUnderUser.size()]);
	}

	@Override
	public void insertTweet(Tweet newTweet) {
		tweets.add(newTweet);
	}

	@Override
	public void deleteTweetByID(long tweetId) {
		Iterator<Tweet> it = tweets.iterator();
		while (it.hasNext()) {
			Tweet curTweet = it.next();
			if (curTweet.getTweetId() == tweetId) {
				it.remove();
				break;
			}
		}
		
	}

	@Override
	public List<Tweet> getTweets() {
		return tweets;
	}

}
