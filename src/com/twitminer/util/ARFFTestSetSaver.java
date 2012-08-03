package com.twitminer.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class ARFFTestSetSaver extends TestSetSaver {

	List<Emotion> emotionSequence;
	
	public ARFFTestSetSaver(Reader wordBagSource) {
		super("Weka ARFF file", "arff", wordBagSource);
		this.emotionSequence = new ArrayList<Emotion> ();
	}

	@Override
	protected Set<String> getBagOfWordsFromSrcFile(Scanner scanner) {
		Set<String> wordBag = new HashSet<String>();
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("@RELATION")) {
				continue;
			}
			else if (line.contains("@ATTRIBUTE") && line.contains("NUMERIC")) {
				System.out.println("Current attribute line: " + line);
				String word = line.split(" ")[1];
				System.out.println("Word to be added: " + word);
				if (word.contains("\n")) {
					wordBag.add(word.replace("\\s", ""));
				}
				else {
					wordBag.add(word);
				}
			}
			else {
				//no more NUMERIC attributes (which are usually the tokens we want
				break;
			}
		}
		return wordBag;
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
				.append("-class {").append(emo.getEmotionName().toLowerCase()).append(",others")
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
					emotionName = curEmotion.getEmotionName().toLowerCase();
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

	@Override
	protected void writeHeader(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, Emotion emo) throws IOException {
		writer.append("@RELATION tweet\n");
		
		Iterator<String> wordIterator = allWords.iterator();
		while (wordIterator.hasNext()) {
			String word = wordIterator.next();
			
			writer.append("@ATTRIBUTE ").append(word).append(" ")
				.append("NUMERIC").append("\n");
		}
		
		writer.append("@ATTRIBUTE ").append(emo.getEmotionName().toLowerCase())
			.append("-class {").append(emo.getEmotionName()).append(",others")
			.append("}\n");
		emotionSequence.add(emo);
		
	}

	@Override
	protected void writePayload(Writer writer, List<TokenizedTweet> tweets,
			Set<String> allWords, Emotion curEmotion) throws IOException {
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
				
			if (curEmotion.getEmotionId() == curTweet.getEmotionID()) {
				emotionName = curEmotion.getEmotionName();
			}
			else {
				emotionName = "others";
			}
			writer.append(emotionName);
			
			
			if (tweetIterator.hasNext()) {
				writer.append('\n');
			}
		}
	}

}
