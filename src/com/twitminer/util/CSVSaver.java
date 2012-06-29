package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public class CSVSaver {

	public static void saveToCSV(List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(null);
		fileChooser.setFileFilter(new CSVSaver.ExtensionFileFilter("CSV file (.csv)", "CSV"));
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			FileWriter writer = new FileWriter(file);
			writer.append("tweet_id").append(",")
				.append("userId").append(",")
				.append("textMSG").append(",")
				.append("created_at").append(",")
				.append("emoname").append("\n");
			
			for (Tweet tweet : tweets) {
				writer.append(Long.toString(tweet.getTweetId())).append(",")
					.append(Long.toString(tweet.getUserId())).append(",")
					.append(tweet.getText()).append(",")
					.append(new java.sql.Timestamp(tweet.getDateCreated().getTimeInMillis()).toString()).append(",")
					.append(emotion.getEmotionById(tweet.getEmotionId()).getEmotionName()).append("\n");
			}
			
			writer.flush();
			writer.close();
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
