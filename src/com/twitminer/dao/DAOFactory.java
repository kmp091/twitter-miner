package com.twitminer.dao;

import com.twitminer.dao.arraylist.ArrayListDAOFactory;
import com.twitminer.dao.mysql.MySQLDAOFactory;

public abstract class DAOFactory {

	public static final int MYSQL = 1;
	public static final int ARRAY_LIST = 2;
	
	public static DAOFactory getInstance(int dataSrc) {
		switch(dataSrc) {
			case MYSQL: return new MySQLDAOFactory();
			case ARRAY_LIST: return new ArrayListDAOFactory();
		}
		return null;
	}
	
	public abstract EmotionDAO getEmotionDAO();
	public abstract TweetDAO getTweetDAO();
	public abstract EmoticonDAO getEmoticonDAO();
	public abstract StopWordDAO getStopWordDAO();
	
}
