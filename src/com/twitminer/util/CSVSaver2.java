package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public class CSVSaver2 extends Saver {

	private HashMap<Long, String[]> tokensPerTweet;
	private int maxNumOfTokens;
	
	private void preProcessTweets(List<Tweet> tweets) {
		tokensPerTweet = new HashMap<Long, String[]>();
		maxNumOfTokens = 0;
		
		for (Tweet tweet : tweets) {
			String[] tokens = tweet.getText().split(" ");
			tokensPerTweet.put(tweet.getTweetId(), tokens);
			if (tokens.length > maxNumOfTokens) {
				maxNumOfTokens = tokens.length;
			}
		}
	}
	
	@Override
	protected void saveOutput(File file, List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		preProcessTweets(tweets);
		
		FileWriter writer = new FileWriter(file);

		writer.append("emotion").append(",");
		
		for (int i = 1; i <= maxNumOfTokens; i++) {
			
			writer.append("token").append(Integer.toString(i));
			
			if (i != maxNumOfTokens) {
				writer.append(",");					
			}
			else {
				writer.append("\n");
			}
		}
		
		for (Tweet tweet : tweets) {
			writer.append(emotion.getEmotionById(tweet.getEmotionId()).getEmotionName()).append(",");
			
			String[] retrievedTweetTokens = tokensPerTweet.get(tweet.getTweetId());
			int tweetLength = retrievedTweetTokens.length;
			
			for (int curSize = 0; curSize < maxNumOfTokens; curSize++) {
				if (curSize < tweetLength) {
					writer.append(retrievedTweetTokens[curSize]);
				}
				
				if (curSize < maxNumOfTokens - 1) {
					writer.append(",");
				}
			}
			
			writer.append("\n");
		}
		
		writer.flush();
		writer.close();
	}

}
