package com.twitminer.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class CSVSaver2 extends Saver {

//	private HashMap<Long, String[]> tokensPerTweet;
//	private int maxNumOfTokens;
	
	public CSVSaver2() {
		super("Comma Separated Values", "csv");
	}

	@Override
	protected void writeHeader(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, EmotionDAO emotion) throws IOException {
		writer.append("emotion-class").append(",");
		
		Iterator<String> allWordIterator = allWords.iterator();
		while (allWordIterator.hasNext()) {
			writer.append(allWordIterator.next());
			
			if (allWordIterator.hasNext()) {
				writer.append(",");
			}
		}
		
		writer.append("\n");
		
	}

	@Override
	protected void writePayload(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, EmotionDAO emotion) throws IOException {
		Iterator<TokenizedTweet> tweetIterator = tweets.iterator();
		while (tweetIterator.hasNext()) {
			TokenizedTweet curTweet = tweetIterator.next();
			
			String emotionName = emotion.getEmotionById(curTweet.getEmotionID()).getEmotionName();
			writer.append(emotionName).append(",");
			
			Iterator<String> allWordIterator = allWords.iterator();
			while (allWordIterator.hasNext()) {
				String masterWord = allWordIterator.next();
				
				if (curTweet.containsWord(masterWord)) {
					writer.append('1');
				}
				else {
					writer.append('0');
				}
				
				if (allWordIterator.hasNext()) {
					writer.append(',');
				}
			}
			
			if (tweetIterator.hasNext()) {
				writer.append('\n');
			}
		}		
	}
	
	/*private void preProcessTweets(List<Tweet> tweets) {
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
	protected void saveOutput(Writer writer, List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		preProcessTweets(tweets);
		
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
	}*/


}
