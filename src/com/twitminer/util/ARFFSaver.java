package com.twitminer.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.twitminer.dao.EmotionDAO;

public class ARFFSaver extends Saver {

	public ARFFSaver() {
		super("Weka ARFF file", "arff");
	}
	
/*	@Override
	protected void saveOutput(Writer writer, List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		writer.append("@RELATION tweet\n");
		writer.append("@ATTRIBUTE tweetMessage STRING\n");
		writer.append("@ATTRIBUTE class {" + emotion.toString() + "}\n");
		
		writer.append("@DATA\n");
		for (int i = 0; i < tweets.size(); i++) {
			Tweet tweet = tweets.get(i);
			
			writer.append('\'').append(tweet.getText()).append('\'').append(",");
			writer.append(emotion.getEmotionById(tweet.getEmotionId()).getEmotionName());
			
			if (i < tweets.size() - 1) {
				writer.append('\n');				
			}
		}
	}*/

	@Override
	protected void writeHeader(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, EmotionDAO emotion) throws IOException {
		writer.append("@RELATION tweet\n");
		
		Iterator<String> wordIterator = allWords.iterator();
		while (wordIterator.hasNext()) {
			String word = wordIterator.next();
			
			writer.append("@ATTRIBUTE ").append(word).append(" ")
				.append("NUMERIC").append("\n");
		}
		
		writer.append("@ATTRIBUTE class {" + emotion.toString() + "}\n");
	}

	@Override
	protected void writePayload(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, EmotionDAO emotion) throws IOException {
		writer.append("@DATA\n");
		
		Iterator<TokenizedTweet> tweetIterator = tweets.iterator();
		while (tweetIterator.hasNext()) {
			TokenizedTweet curTweet = tweetIterator.next();
			
			Iterator<String> allWordIterator = allWords.iterator();
			while (allWordIterator.hasNext()) {
				String masterWord = allWordIterator.next();
				
				if (curTweet.containsWord(masterWord)) {
					writer.append('1');
				}
				else {
					writer.append('0');
				}
				
				writer.append(',');
			}
			
			String emotionName = emotion.getEmotionById(curTweet.getEmotionID()).getEmotionName();
			writer.append(emotionName);
			
			if (tweetIterator.hasNext()) {
				writer.append('\n');
			}
		}
	}

}
