package com.twitminer.dao.arraylist;

import java.util.ArrayList;
import java.util.List;

import com.twitminer.beans.Emotion;
import com.twitminer.dao.EmotionDAO;

public class ArrayListEmotionDAO extends EmotionDAO {

	List<Emotion> emotions;
	
	ArrayListEmotionDAO () {
		emotions = new ArrayList<Emotion>();
		Emotion tempEmo;
		tempEmo = new Emotion();
		tempEmo.setEmotionId(1);
		tempEmo.setEmotionName("Happy");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(2);
		tempEmo.setEmotionName("Sad");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(3);
		tempEmo.setEmotionName("Surprised");
		emotions.add(tempEmo);
		tempEmo = new Emotion();
		tempEmo.setEmotionId(4);
		tempEmo.setEmotionName("Disgusted");
		emotions.add(tempEmo);
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
