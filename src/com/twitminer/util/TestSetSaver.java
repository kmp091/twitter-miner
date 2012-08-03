package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public abstract class TestSetSaver extends Saver {

	private Set<String> bagOfWords;
	
	protected TestSetSaver(Reader wordBagSource) {
		this("Comma separated values", "csv", wordBagSource);
	}
	
	protected TestSetSaver(String desc, String ext, Reader wordBagSource) {
		super(desc, ext);
		Scanner scanner = new Scanner(wordBagSource);
		this.bagOfWords = getBagOfWordsFromSrcFile(scanner);
		scanner.close();
		/*try {
			wordBagSource.close();
		} catch (IOException e) {
			System.err.println("You're doing it wrong!");
			e.printStackTrace();
		}*/
	}
	
	protected abstract Set<String> getBagOfWordsFromSrcFile(Scanner scanner);
	
	protected Set<String> getBagOfWords() {
		return this.bagOfWords;
	}

	@Override
	protected void onConfirmSave(File savedFile, List<TokenizedTweet> tweets,
			EmotionDAO emotion) throws IOException {
		Set<String> wordBag = getBagOfWords();
		
		FileWriter writer = new FileWriter(savedFile);
		saveOutput(writer, tweets, wordBag, emotion);
		System.out.println("Closing writer.");
		writer.close();
		System.out.println("Save success.");
	}
	
	protected void onConfirmSave(File saveFile, List<TokenizedTweet> tweets, Emotion emotion) throws IOException {
		Set<String> wordBag = getBagOfWords();
		System.out.println("Bag of words: {" + commafyCollection(wordBag) + "}");
		
		FileWriter writer = new FileWriter(saveFile);
		saveSingleClassOutput(writer, tweets, wordBag, emotion);
		System.out.println("Closing writer.");
		writer.close();
		System.out.println("Save success.");
	}
	
}
