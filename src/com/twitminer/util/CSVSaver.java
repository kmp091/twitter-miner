package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public class CSVSaver {
	
	private static HashMap<Long, String[]> tokensPerTweet;
	private static int maxNumOfTokens;
	
	private static void preProcessTweets(List<Tweet> tweets) {
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

	public static void saveToCSV(List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(null);
		fileChooser.setFileFilter(new CSVSaver.ExtensionFileFilter("CSV file (.csv)", "CSV"));
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			preProcessTweets(tweets);
			
			File file = fileChooser.getSelectedFile();
			
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
			JOptionPane.showMessageDialog(null, "Finished saving!");
		}
	}	
	
	
	
	public static class ExtensionFileFilter extends FileFilter {
		  String description;

		  String extensions[];

		  public ExtensionFileFilter(String description, String extension) {
		    this(description, new String[] { extension });
		  }

		  public ExtensionFileFilter(String description, String extensions[]) {
		    if (description == null) {
		      this.description = extensions[0];
		    } else {
		      this.description = description;
		    }
		    this.extensions = (String[]) extensions.clone();
		    toLower(this.extensions);
		  }

		  private void toLower(String array[]) {
		    for (int i = 0, n = array.length; i < n; i++) {
		      array[i] = array[i].toLowerCase();
		    }
		  }

		  public String getDescription() {
		    return description;
		  }

		  public boolean accept(File file) {
		    if (file.isDirectory()) {
		      return true;
		    } else {
		      String path = file.getAbsolutePath().toLowerCase();
		      for (int i = 0, n = extensions.length; i < n; i++) {
		        String extension = extensions[i];
		        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
		          return true;
		        }
		      }
		    }
		    return false;
		  }
		}
}
