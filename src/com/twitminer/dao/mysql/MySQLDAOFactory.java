package com.twitminer.dao.mysql;

import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
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

}
