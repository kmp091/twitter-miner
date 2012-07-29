package com.twitminer.dao.mysql;

import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.StopWordDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.dao.TweetDAO;

public class MySQLDAOFactory extends DAOFactory {
	
	@Override
	public EmotionDAO getEmotionDAO() {
		return new MySQLEmotionDAO();
	}

	@Override
	public TweetDAO getTweetDAO() {
		return new MySQLTweetDAO();
	}

	@Override
	public EmoticonDAO getEmoticonDAO() {
		return new MySQLEmoticonDAO();
	}

	@Override
	public StopWordDAO getStopWordDAO() {
		return new MySQLStopWordDAO();
	}

	@Override
	public TokenizedTweetDAO getTokenizedTweetDAO() {
		// TODO Auto-generated method stub
		return null;
	}

}
