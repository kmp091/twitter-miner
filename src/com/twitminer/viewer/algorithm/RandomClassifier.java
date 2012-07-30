package com.twitminer.viewer.algorithm;

import java.util.Random;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class RandomClassifier implements Classifier {

	private Random rand;
	private EmotionDAO emotion;
	
	public RandomClassifier(EmotionDAO emotion) {
		this.emotion = emotion;
		this.rand = new Random();
	}

	@Override
	public Emotion classifyEmotion(TokenizedTweet tokenizedTweet) {
		return emotion.getEmotionById(rand.nextInt(4) + 1);
	}

}
