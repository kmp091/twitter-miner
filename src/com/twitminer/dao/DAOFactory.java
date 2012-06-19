package com.twitminer.dao;

import com.twitminer.dao.mysql.MySQLDAOFactory;

public abstract class DAOFactory {

	public static final int MYSQL = 1;
	
	public static DAOFactory getInstance(int dataSrc) {
		switch(dataSrc) {
			case MYSQL: return new MySQLDAOFactory();
		}
		return null;
	}
	
	public abstract EmotionDAO getEmotionDAO();
	public abstract TweetDAO getTweetDAO();
	
}
