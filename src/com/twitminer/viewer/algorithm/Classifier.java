package com.twitminer.viewer.algorithm;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;

public interface Classifier {

	public Emotion classifyEmotion(TokenizedTweet tokenizedTweet);
	
}
