package com.twitminer.storage.conn;

import java.sql.Connection;
import com.twitminer.dao.DAOFactory;

public abstract class DBConnFactory {
	
	private static String username;
	private static String password;
	private static String datasource;
	private static String driverName;
	
	public static DBConnFactory getInstance(int dataSrc) {
		switch(dataSrc) {
		case DAOFactory.MYSQL:
			datasource = "jdbc/tweet_schema";
			return new MySQLConnImpl();
			
		}
		return null;
	}
	
	public abstract Connection getConnection();
	
	public static String getUsername() {
		return DBConnFactory.username;
	}
	
	public static void setUesrname(String uName) {
		DBConnFactory.username = uName;
	}
	
	public static String getPassword() {
		return DBConnFactory.password;
	}
	
	public static void setPassword(String newPass) {
		DBConnFactory.password = newPass;
	}
	
	public static String getDatasource() {
		return DBConnFactory.datasource;
	}
	
	public static void setDatasource(String dSrc) {
		DBConnFactory.datasource = dSrc;
	}
	
	public static String getDriverName() {
		return DBConnFactory.driverName;
	}
	
	public static void setDriverName(String driverName) {
		DBConnFactory.driverName = driverName;
	}
	
}
