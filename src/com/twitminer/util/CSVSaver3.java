package com.twitminer.util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class CSVSaver3 extends Saver {

	List<Emotion> emotionSequence;
	
	public CSVSaver3() {
		super("Comma Separated Values", "csv");
		emotionSequence = new ArrayList<Emotion>();
	}
	
	@Override
	protected void writeHeader(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, EmotionDAO emotion) throws IOException {
		Emotion[] emotions = emotion.getEmotions();
		for (Emotion emo : emotions) {
			writer.append(emo.getEmotionName()).append("-class").append(',');
			emotionSequence.add(emo);
		}
		
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
			String emotionName;
			
			for (Emotion emo : emotionSequence) {
				if (emo.getEmotionId() == curTweet.getEmotionID()) {
					emotionName = emo.getEmotionName();
				}
				else {
					emotionName = "others";
				}
				writer.append(emotionName).append(",");
			}
						
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

}
