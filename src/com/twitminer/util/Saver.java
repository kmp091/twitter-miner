package com.twitminer.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public abstract class Saver {

	public void save(List<Tweet> tweets, EmotionDAO emotion) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				saveOutput(fileChooser.getSelectedFile(), tweets, emotion);
				JOptionPane.showMessageDialog(null, "Save successful!");
			}
			catch (IOException io) {
				JOptionPane.showMessageDialog(null, "<html>Save failed...<br>" + io.getMessage() + "</html>");
			}
		}
		
	}
	
	protected abstract void saveOutput(File file, List<Tweet> tweets, EmotionDAO emotion) throws IOException;
	
}
