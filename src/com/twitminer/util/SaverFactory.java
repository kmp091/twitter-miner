package com.twitminer.util;

import java.io.Reader;

public class SaverFactory {

	public static final int TRAIN_CSV = 1;
	public static final int TRAIN_ARFF = 2;
	public static final int TEST_CSV = 3;
	public static final int TEST_ARFF = 4;
	
	public static Saver getTrainingSaverInstance(int type) {
		switch (type) {
			case TRAIN_CSV:
				return new CSVSaver3();
			case TRAIN_ARFF:
				return new ARFFSaver2();
			default:
				return new CSVSaver3();
		}
	}
	
	public static TestSetSaver getTestSaverInstance(int type, Reader reader) {
		switch (type) {
		case TEST_CSV:
			return new CSVTestSetSaver(reader);
		case TEST_ARFF:
			return new ARFFTestSetSaver(reader);
		default:
			return new CSVTestSetSaver(reader);
		}
	}
	
}
