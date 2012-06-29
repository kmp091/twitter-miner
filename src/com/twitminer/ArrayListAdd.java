package com.twitminer;

import java.util.List;

import com.twitminer.beans.Emoticon;
import com.twitminer.beans.Emotion;
import com.twitminer.beans.StopWord;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.StopWordDAO;

public class ArrayListAdd {

	public static void main (String[] args) {
		DAOFactory dao = DAOFactory.getInstance(DAOFactory.MYSQL);
		StopWordDAO emoDao = dao.getStopWordDAO();
		List<StopWord> emos = emoDao.getStopWordsList();
		System.out.println("stopWords = new ArrayList<StopWord>();");
		System.out.println("StopWord tempStop;");
		for (StopWord emo : emos) {
			System.out.println("tempStop = new StopWord();");
			System.out.println("tempStop.setStopWordId(" + emo.getStopWordId() + ");");
			System.out.println("tempStop.setStopWord(\"" + emo.getStopWord() + "\");");
			System.out.println("stopWords.add(tempStop);");
		}
	}
	
}
