package com.twitminer.dao;

import java.util.List;

import com.twitminer.beans.StopWord;

public abstract class StopWordDAO {

	public StopWord[] getStopWordsArray() {
		List<StopWord> stopWords = getStopWordsList();
		return stopWords.toArray(new StopWord[stopWords.size()]);
	}
	
	public abstract List<StopWord> getStopWordsList();
	
	public abstract List<String> getStopWordStrings();
	
	public abstract StopWord getStopWordById(int stopWordId);
}
