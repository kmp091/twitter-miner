package com.twitminer.viewer.algorithm;

public class ClassifierFactory {

	public static final int SMO = 1;
	
	public static Classifier getInstance() {
		return getInstance(SMO);
	}
	
	public static Classifier getInstance(int algoType) {
		switch(algoType) {
		case SMO:
			default:
				return new SMO();
		}
	}

}
