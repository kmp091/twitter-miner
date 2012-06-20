package com.twitminer.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twitminer.beans.Emotion;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.storage.conn.DBConnFactory;

public class MySQLEmotionDAO extends EmotionDAO {

	private final String tableName = "emotion";
	
	@Override
	public Emotion[] getEmotions() {
		List<Emotion> emotions = new ArrayList<Emotion>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int emoId = rs.getInt("emoId");
				String emoName = rs.getString("emoname");
				emotions.add(new Emotion(emoId, emoName));
			}
			conn.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		
		return emotions.toArray(new Emotion[emotions.size()]);
	}

	@Override
	public Emotion getEmotionById(int emoId) {
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE emoId = ?");
			pstmt.setInt(1, emoId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int emotionId = rs.getInt("emoId");
				String emotionName = rs.getString("emoname");
				Emotion emo = new Emotion(emotionId, emotionName);
				conn.close();
				return emo;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}

}
