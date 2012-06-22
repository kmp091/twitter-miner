package com.twitminer.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twitminer.beans.StopWord;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.StopWordDAO;
import com.twitminer.storage.conn.DBConnFactory;

public class MySQLStopWordDAO extends StopWordDAO {

	private final String tableName = "stop_word";
	
	@Override
	public List<StopWord> getStopWordsList() {
		List<StopWord> stopWords = new ArrayList<StopWord>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int stopWordId = rs.getInt("stop_word_id");
				String stopWord = rs.getString("stop_word");
				
				StopWord stopWordInstance = new StopWord();
				stopWordInstance.setStopWordId(stopWordId);
				stopWordInstance.setStopWord(stopWord);
				stopWords.add(stopWordInstance);
			}
			conn.close();
			return stopWords;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getStopWordStrings() {
		List<String> stopWords = new ArrayList<String>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT stop_word FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String stopWord = rs.getString("stop_word");
				
				stopWords.add(stopWord);
			}
			conn.close();
			return stopWords;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public StopWord getStopWordById(int stopWordId) {
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE stop_word_id = ?");
			pstmt.setInt(1, stopWordId);
			
			ResultSet rs = pstmt.executeQuery();
			
			StopWord stopWordInstance = new StopWord();
			
			if (rs.next()) {
				int stopWordInstanceId = rs.getInt("stop_word_id");
				String stopWord = rs.getString("stop_word");
				
				stopWordInstance.setStopWordId(stopWordInstanceId);
				stopWordInstance.setStopWord(stopWord);
			}
			conn.close();
			return stopWordInstance;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
