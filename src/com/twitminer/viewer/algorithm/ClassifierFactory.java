package com.twitminer.viewer.algorithm;

import java.net.URISyntaxException;

import com.twitminer.dao.EmotionDAO;

public class ClassifierFactory {

	public static final int SMO = 1;
	public static final int RANDOM = 2;
	public static final int J48 = 3;
	public static final int NAIVE_BAYES = 4;
	
	public static Classifier getInstance(EmotionDAO emotion) throws URISyntaxException, Exception {
		return getInstance(SMO, emotion);
	}
	
	public static Classifier getInstance(int algoType, EmotionDAO emotion) throws URISyntaxException, Exception {
		switch(algoType) {
		case SMO:
			return new SMO(emotion);
		case J48:
			return new J48(emotion);
		case NAIVE_BAYES:
			return new NaiveBayes(emotion);
		case RANDOM:
			default:
				return new RandomClassifier(emotion);
		}
	}

}
