package com.twitminer.viewer.algorithm;

import com.twitminer.dao.EmotionDAO;

public class ClassifierFactory {

	public static final int SMO = 1;
	public static final int RANDOM = 2;
	
	public static Classifier getInstance(EmotionDAO emotion) {
		return getInstance(RANDOM, emotion);
	}
	
	public static Classifier getInstance(int algoType, EmotionDAO emotion) {
		switch(algoType) {
		case SMO:
			return new SMO(emotion);
		case RANDOM:
			default:
				return new RandomClassifier(emotion);
		}
	}

}
