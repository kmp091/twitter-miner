package com.twitminer.dao.arraylist;

import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.StopWordDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.dao.TweetDAO;

public class ArrayListDAOFactory extends DAOFactory {

	@Override
	public EmotionDAO getEmotionDAO() {
		return new ArrayListEmotionDAO();
	}

	@Override
	public TweetDAO getTweetDAO() {
		return new ArrayListTweetDAO();
	}

	@Override
	public EmoticonDAO getEmoticonDAO() {
		return new ArrayListEmoticonDAO();
	}

	@Override
	public StopWordDAO getStopWordDAO() {
		return new ArrayListStopWordDAO();
	}

	@Override
	public TokenizedTweetDAO getTokenizedTweetDAO() {
		return new ArrayListTokenizedTweetDAO();
	}

}
