package com.twitminer.viewer.algorithm;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class SMO implements Classifier {

	private EmotionDAO emotionDAO;
	
	public SMO(EmotionDAO emotion) {
		this.emotionDAO = emotion;
	}

	@Override
	public Emotion classifyEmotion(TokenizedTweet tokenizedTweet) {
		return null;
	}

}
