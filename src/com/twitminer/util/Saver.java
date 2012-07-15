package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.event.listener.SavedEventListener;

public abstract class Saver {

	private FileFilter filter;
	private String ext;
	
	private Set<String> allWords;
	
	private List<SavedEventListener> savedEventListeners;
	
	protected Saver(String description, String extension) {
		this();
		filter = new FileNameExtensionFilter(description, extension);
		this.ext = extension;
	}
	
	protected Saver() {
		this.allWords = new HashSet<String>();
		filter = new FileNameExtensionFilter("Comma separated values", "csv");
		this.ext = "csv";
		savedEventListeners = new ArrayList<SavedEventListener>();
	}
	
	private List<TokenizedTweet> preprocessTweets (List<Tweet> tweets) {
		List<TokenizedTweet> tokenizedTweets = new ArrayList<TokenizedTweet>();
		
		for (Tweet tweet : tweets) {
			TokenizedTweet tokenizedTweet = new TokenizedTweet(tweet);
			
			tokenizedTweets.add(tokenizedTweet);
			allWords.addAll(tokenizedTweet.getSetOfTokens());
		}
		
		return tokenizedTweets;
	}
	
	public void save(List<Tweet> tweets, EmotionDAO emotion) {
		JFileChooser fileChooser = new JFileChooser();
		if (filter != null) {
			fileChooser.setFileFilter(filter);			
		}

		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			this.fireOnSave();
			
			List<TokenizedTweet> processedTweets = preprocessTweets(tweets);
			
			try {
				File savedFile;
				
				if (!fileChooser.getSelectedFile().getName().endsWith("." + ext)) {
					savedFile = new File(fileChooser.getSelectedFile() + "." + ext);
				}
				else {
					savedFile = fileChooser.getSelectedFile();
				}
				
				FileWriter writer = new FileWriter(savedFile);
				saveOutput(writer, processedTweets, allWords, emotion);
				writer.close();
				this.fireOnSaveSuccess();
//				JOptionPane.showMessageDialog(null, "Save successful!");
			}
			catch (IOException io) {
//				JOptionPane.showMessageDialog(null, "<html>Save failed...<br><br>" + io.getMessage() + "</html>");
				this.fireOnSaveFailed(io);
			}
		}
		
	}
	
	protected void saveOutput(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException {
		writeHeader(writer, tweets, allWords, emotion);
		writePayload(writer, tweets, allWords, emotion);
	}
	
	protected abstract void writeHeader(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException;
	
	protected abstract void writePayload(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException;
	
	protected boolean isInAllWords(String token) {
		if (allWords.contains(token)) {
			return true;
		}
		
		return false;
	}
	
	public void addSavedEventListener(SavedEventListener listener) {
		this.savedEventListeners.add(listener);
	}
	
	public void removeSavedEventListener(SavedEventListener listener) {
		this.savedEventListeners.remove(listener);
	}
	
	private void fireOnSave() {
		for (SavedEventListener listener : this.savedEventListeners) {
			listener.onSave();
		}
	}
	
	private void fireOnSaveSuccess() {
		for (SavedEventListener listener : this.savedEventListeners) {
			listener.onSaveSuccess();
		}
	}
	
	private void fireOnSaveFailed(Exception ex) {
		for (SavedEventListener listener : this.savedEventListeners) {
			listener.onSaveFailed(ex);
		}
	}
	
}
