package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.event.listener.SavedEventListener;

public abstract class Saver {

	private FileFilter filter;
	private String ext;
	
//	private Set<String> allWords;
	
	private List<SavedEventListener> savedEventListeners;
	
	protected Saver(String description, String extension) {
		this();
		filter = new FileNameExtensionFilter(description, extension);
		this.ext = extension;
	}
	
	protected Saver() {
//		this.allWords = new HashSet<String>();
		filter = new FileNameExtensionFilter("Comma separated values", "csv");
		this.ext = "csv";
		savedEventListeners = new ArrayList<SavedEventListener>();
	}
	
	private Set<String> getBagOfWords (List<TokenizedTweet> tweets) {
		Set<String> wordBag = new HashSet<String>();
		
		for (TokenizedTweet tweet : tweets) {
			wordBag.addAll(tweet.getSetOfTokens());
			System.out.println("Original tweet: " + tweet);
			System.out.println("Tokens: " + this.commafyCollection(tweet.getSetOfTokens()));
		}
		
		return wordBag;
	}
	
	public void saveMultiple(List<TokenizedTweet> tweets, EmotionDAO emotion) {
		JFileChooser fileChooser = new JFileChooser();
		if (filter != null) {
			fileChooser.setFileFilter(filter);
		}
		
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.fireOnSave();
			
			try {
				File happyFile;
				File sadFile;
				File surprisedFile;
				File disgustFile;
				
				if (!fileChooser.getSelectedFile().getName().endsWith("." + ext)) {
					File selectedFile = fileChooser.getSelectedFile();
					String fileName = selectedFile.getName();
					
					happyFile = new File(selectedFile.getParent(), fileName + "-happy" + "." + ext);
					sadFile = new File(selectedFile.getParent(), fileName + "-sad" + "." + ext);
					surprisedFile = new File(selectedFile.getParent(), fileName + "-surprised" + "." + ext);
					disgustFile = new File(selectedFile.getParent(), fileName + "-disgust" + "." + ext);
				}
				else {
					File selectedFile = fileChooser.getSelectedFile();
					String fileName = fileChooser.getSelectedFile().getName();
					int extensionIndex = fileName.lastIndexOf("." + ext);
					String newFileName = fileName.substring(0, extensionIndex);
					
					happyFile = new File(selectedFile.getParent(), newFileName + "-happy" + "." + ext);
					sadFile = new File(selectedFile.getParent(), newFileName + "-sad" + "." + ext);
					surprisedFile = new File(selectedFile.getParent(), newFileName + "-surprised" + "." + ext);
					disgustFile = new File(selectedFile.getParent(), newFileName + "-disgust" + "." + ext);
				}
				
				this.onConfirmSave(happyFile, tweets, emotion.getEmotionById(EmotionDAO.HAPPY));
				this.onConfirmSave(sadFile, tweets, emotion.getEmotionById(EmotionDAO.SAD));
				this.onConfirmSave(surprisedFile, tweets, emotion.getEmotionById(EmotionDAO.SURPRISE));
				this.onConfirmSave(disgustFile, tweets, emotion.getEmotionById(EmotionDAO.DISGUST));
				this.fireOnSaveSuccess();
			} catch (Exception io) {
				this.fireOnSaveFailed(io);
			}
		}
	}
	
	public void save(List<TokenizedTweet> tweets, EmotionDAO emotion) {
		JFileChooser fileChooser = new JFileChooser();
		if (filter != null) {
			fileChooser.setFileFilter(filter);			
		}

		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			this.fireOnSave();
			
			try {
				File savedFile;
				
				if (!fileChooser.getSelectedFile().getName().endsWith("." + ext)) {
					savedFile = new File(fileChooser.getSelectedFile() + "." + ext);
				}
				else {
					savedFile = fileChooser.getSelectedFile();
				}
				
				this.onConfirmSave(savedFile, tweets, emotion);
				this.fireOnSaveSuccess();
//				JOptionPane.showMessageDialog(null, "Save successful!");
			}
			catch (Exception io) {
//				JOptionPane.showMessageDialog(null, "<html>Save failed...<br><br>" + io.getMessage() + "</html>");
				this.fireOnSaveFailed(io);
			}
		}
		
	}
	
	protected void onConfirmSave(File savedFile, List<TokenizedTweet> tweets, EmotionDAO emotion) throws IOException {
		Set<String> wordBag = getBagOfWords(tweets);
		System.out.println("Bag of words: {" + commafyCollection(wordBag) + "}");
		
		FileWriter writer = new FileWriter(savedFile);
		saveOutput(writer, tweets, wordBag, emotion);
		System.out.println("Closing writer.");
		writer.close();
		System.out.println("Save success.");		
	}
	
	protected void onConfirmSave(File savedFile, List<TokenizedTweet> tweets, Emotion emotion) throws IOException {
		Set<String> wordBag = getBagOfWords(tweets);
		System.out.println("Bag of words: {" + commafyCollection(wordBag) + "}");
		
		FileWriter writer = new FileWriter(savedFile);
		saveSingleClassOutput(writer, tweets, wordBag, emotion);
		System.out.println("Closing writer.");
		writer.close();
		System.out.println("Save success.");		
	}
	
	protected String commafyCollection(Collection<String> set) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			
			if (it.hasNext()) {
				sb.append(',');
			}
		}
		return sb.toString();
	}
	
	protected void saveSingleClassOutput(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, Emotion emotion) throws IOException {
		System.out.println("Writing header...");
		this.writeHeader(writer, tweets, allWords, emotion);
		System.out.println("Writing payload...");
		this.writePayload(writer, tweets, allWords, emotion);
	}
	
	protected void saveOutput(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException {
		System.out.println("Writing header...");
		writeHeader(writer, tweets, allWords, emotion);
		System.out.println("Writing payload...");
		writePayload(writer, tweets, allWords, emotion);
	}
	
	protected abstract void writeHeader(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, Emotion emotion) throws IOException;
	
	protected abstract void writePayload(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, Emotion emotion) throws IOException;
	
	protected abstract void writeHeader(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException;
	
	protected abstract void writePayload(Writer writer, final List<TokenizedTweet> tweets, final Set<String> allWords, EmotionDAO emotion) throws IOException;
	
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
