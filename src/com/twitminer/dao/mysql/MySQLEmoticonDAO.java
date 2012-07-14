package com.twitminer.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twitminer.beans.Emoticon;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.storage.conn.DBConnFactory;

public class MySQLEmoticonDAO extends EmoticonDAO {

	private final String tableName = "emoticon";
	
	@Override
	public List<String> getEmoticonStrings() {
		List<String> emoticons = new ArrayList<String>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT emoticon FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String emoticon = rs.getString("emoticon");
				emoticons.add(emoticon);
			}
			conn.close();
			return emoticons;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getEmoticonStringsByEmotion(int emotion) {
		
		List<String> emoticons = new ArrayList<String>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT emoticon FROM " + tableName + " WHERE emoId = ?");
			pstmt.setInt(1, emotion);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String emoticon = rs.getString("emoticon");
				emoticons.add(emoticon);
			}
			conn.close();
			
			return emoticons;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Emoticon> getEmoticons() {
		List<Emoticon> emoticons = new ArrayList<Emoticon>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int emoticonId = rs.getInt("emoticonId");
				int emoId = rs.getInt("emoId");
				String emoticon = rs.getString("emoticon");
				Emoticon tempEmoticon = new Emoticon();
				tempEmoticon.setEmoticon(emoticon);
				tempEmoticon.setEmoticonId(emoticonId);
				tempEmoticon.setEmotionId(emoId);
				
				emoticons.add(tempEmoticon);
			}
			conn.close();
			
			return emoticons;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Emoticon> getEmoticonsByEmotion(int emotion) {
		List<Emoticon> emoticons = new ArrayList<Emoticon>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE emoId = ?");
			pstmt.setInt(1, emotion);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int emoticonId = rs.getInt("emoticonId");
				int emoId = rs.getInt("emoId");
				String emoticon = rs.getString("emoticon");
				Emoticon tempEmoticon = new Emoticon();
				tempEmoticon.setEmoticon(emoticon);
				tempEmoticon.setEmoticonId(emoticonId);
				tempEmoticon.setEmotionId(emoId);
				
				emoticons.add(tempEmoticon);
			}
			conn.close();
			
			return emoticons;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public Emoticon getEmoticonByString(String emoticonString) {
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE emoticon = ?");
			pstmt.setString(1, emoticonString);
			
			ResultSet rs = pstmt.executeQuery();
			
			Emoticon emoticon = null;
			if (rs.next()) {
				emoticon = new Emoticon();
				emoticon.setEmoticon(rs.getString("emoticon"));
				emoticon.setEmoticonId(rs.getInt("emoticonId"));
				emoticon.setEmotionId(rs.getInt("emoId"));
			}
			conn.close();
			return emoticon;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
