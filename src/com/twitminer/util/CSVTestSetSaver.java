package com.twitminer.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class CSVTestSetSaver extends TestSetSaver {

	List<Emotion> emotionSequence;
	
	public CSVTestSetSaver(Reader wordBagSource) {
		super("Comma separated values", "csv", wordBagSource);
		emotionSequence = new ArrayList<Emotion>();
	}

	@Override
	protected Set<String> getBagOfWordsFromSrcFile(Scanner scanner) {
		String headerLine = null;
		Set<String> wordBag = new LinkedHashSet<String>();
		
		while (scanner.hasNext()) {
			String line = scanner.next();
			if (!line.isEmpty()) {
				headerLine = line;
				break;
			}
		}
		
		if (headerLine == null) {
			System.err.println("Invalid/blank CSV file!");
		}
		else {
			String[] commaDelimitedValues = headerLine.split(",");
			for (String header : commaDelimitedValues) {
				if (header.contains("-class")) {
					continue;
				}
				else {
					if (header.contains("\n")) {
						wordBag.add(header.replace("\\s", ""));
					}
					else {
						wordBag.add(header);						
					}
				}
			}
		}
		
		
		//Set<String> wordBag = new SortedSet<String>(wordList);
		
		
		return wordBag;
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
					emotionName = emo.getEmotionName().toLowerCase();
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
