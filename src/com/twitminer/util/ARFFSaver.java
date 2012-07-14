package com.twitminer.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public class ARFFSaver extends Saver {

	@Override
	protected void saveOutput(File file, List<Tweet> tweets, EmotionDAO emotion) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.append("@RELATION tweet\n");
		writer.append("@ATTRIBUTE tweetMessage STRING\n");
		writer.append("@ATTRIBUTE class {" + emotion.toString() + "}\n");
		
		writer.append("@DATA\n");
		for (int i = 0; i < tweets.size(); i++) {
			Tweet tweet = tweets.get(i);
			
			writer.append('\'').append(tweet.getText()).append('\'').append(",");
			writer.append(emotion.getEmotionById(tweet.getEmotionId()).getEmotionName());
			
			if (i < tweets.size() - 1) {
				writer.append('\n');				
			}
		}
		writer.flush();
		writer.close();
	}

}
