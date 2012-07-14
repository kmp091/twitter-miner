package com.twitminer.util;

public class SaverFactory {

	public static final int CSV = 1;
	public static final int ARFF = 2;
	
	public static Saver getInstance(int type) {
		switch (type) {
			case CSV:
				return new CSVSaver2();
			case ARFF:
				return new ARFFSaver();
			default:
				return new CSVSaver2();
		}
	}
	
}
