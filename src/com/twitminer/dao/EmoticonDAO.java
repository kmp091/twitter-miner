package com.twitminer.dao;

import java.util.List;

import com.twitminer.beans.Emoticon;
import com.twitminer.beans.Emotion;

public abstract class EmoticonDAO {

	public abstract List<String> getEmoticonStrings();
	
	public abstract List<String> getEmoticonStringsByEmotion(int emotion);
	
	public List<String> getEmoticonStringsByEmotion(Emotion emotion) {
		return getEmoticonStringsByEmotion(emotion.getEmotionId());
	}
	
	public abstract List<Emoticon> getEmoticons();
	
	public abstract List<Emoticon> getEmoticonsByEmotion(int emotion);
	
	public List<Emoticon> getEmoticonsByEmotion(Emotion emotion) {
		return getEmoticonsByEmotion(emotion.getEmotionId());
	}
	
	public abstract Emoticon getEmoticonByString(String emoticonString);
	
}
