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

public class ARFFSaver2 extends Saver {

	List<Emotion> emotionSequence;
	
	public ARFFSaver2() {
		super("Weka ARFF file", "arff");
		this.emotionSequence = new ArrayList<Emotion>();
	}
	
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
		
		for (Emotion emo : emotion.getEmotions()) {
			writer.append("@ATTRIBUTE ").append(emo.getEmotionName())
				.append("-class {").append(emo.getEmotionName()).append(",others")
				.append("}\n");
			
			emotionSequence.add(emo);
		}
		
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
			
			String emotionName;
			
			Iterator<Emotion> emoIterator = emotionSequence.iterator();
			
			while (emoIterator.hasNext()) {
				Emotion curEmotion = emoIterator.next();
				
				if (curEmotion.getEmotionId() == curTweet.getEmotionID()) {
					emotionName = curEmotion.getEmotionName();
				}
				else {
					emotionName = "others";
				}
				writer.append(emotionName);
				
				if (emoIterator.hasNext()) {
					writer.append(",");					
				}
			}
			
			if (tweetIterator.hasNext()) {
				writer.append('\n');
			}
		}
	}

}
