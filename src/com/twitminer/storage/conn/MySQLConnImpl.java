package com.twitminer.storage.conn;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnImpl extends DBConnFactory {

	public MySQLConnImpl() {
		MySQLConnImpl.setDriverName("com.mysql.jdbc.Driver");
	}
	
	@Override
	public Connection getConnection() {
		try {
			Class.forName(MySQLConnImpl.getDriverName()).newInstance();
			
			return DriverManager.getConnection(getDatasource(), getUsername(), getPassword());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}