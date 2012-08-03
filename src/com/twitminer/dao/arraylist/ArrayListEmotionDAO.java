package com.twitminer.dao.arraylist;

import java.util.ArrayList;

import com.twitminer.beans.Emotion;
import com.twitminer.dao.EmotionDAO;

public class ArrayListEmotionDAO extends EmotionDAO {

	ArrayList<Emotion> emotions;
	
	ArrayListEmotionDAO () {
		emotions = new ArrayList<Emotion>();
		Emotion tempEmo;
		tempEmo = new Emotion();
		tempEmo.setEmotionId(4);
		tempEmo.setEmotionName("Happy");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(1);
		tempEmo.setEmotionName("Sad");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(2);
		tempEmo.setEmotionName("Surprised");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(3);
		tempEmo.setEmotionName("Disgusted");
		emotions.add(tempEmo);
		
		emotions.trimToSize();
	}
	
	
	@Override
	public Emotion[] getEmotions() {
		// TODO Auto-generated method stub
		return emotions.toArray(new Emotion[emotions.size()]);
	}

	@Override
	public Emotion getEmotionById(int emoId) {
		for (Emotion emo : emotions) {
			if (emo.getEmotionId() == emoId) {
				return emo;
			}
		}
		return null;
	}

}
