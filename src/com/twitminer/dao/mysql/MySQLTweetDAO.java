package com.twitminer.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.TweetDAO;
import com.twitminer.storage.conn.DBConnFactory;

public class MySQLTweetDAO extends TweetDAO {

	private final String tableName = "tweet";
	
	@Override
	public Tweet getTweetByID(long tweetId) {
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE tweet_id = ?");
			pstmt.setLong(1, tweetId);
			
			ResultSet rs = pstmt.executeQuery();
			
			Tweet newTweet = null;
			
			if (rs.next()) {
				newTweet = retrieveObject(rs);
			}
			conn.close();
			return newTweet;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public Tweet[] getTweetsByUserID(long userId) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE userId = ?");
			pstmt.setLong(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				tweets.add(retrieveObject(rs));
			}
			
			conn.close();
			
			return tweets.toArray(new Tweet[tweets.size()]);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public void insertTweet(Tweet newTweet) {
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + tableName + " (tweet_id, userId, textMSG, created_AT, emoId) VALUES (?,?,?,?,?)");
			pstmt.setLong(1, newTweet.getTweetId());
			pstmt.setLong(2, newTweet.getUserId());
			pstmt.setString(3, newTweet.getText());
			pstmt.setTimestamp(4, new Timestamp(newTweet.getDateCreated().getTimeInMillis()));
			pstmt.setInt(5, newTweet.getEmotionId());
			
			pstmt.execute();
			
			conn.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void deleteTweetByID(long tweetId) {
		try {
			Connection con = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + tableName + " WHERE tweet_id = ?");
			pstmt.setLong(1, tweetId);
			
			pstmt.execute();
			
			con.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private Tweet retrieveObject(ResultSet rs) throws SQLException {
		Tweet newTweet = null;
		
		int id = rs.getInt("tweet_id");
		int userId = rs.getInt("userId");
		String text = rs.getString("textMSG");
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(rs.getTimestamp("created_AT").getTime());
		int emoId = rs.getInt("emoId");
		
		newTweet = new Tweet(id, userId, text, date, emoId);
		return newTweet;
	}

	@Override
	public List<Tweet> getTweets() {
		try {
			Connection conn = DBConnFactory.getInstance(DAOFactory.MYSQL).getConnection();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Tweet> newTweets = new ArrayList<Tweet>();
			
			if (rs.next()) {
				newTweets.add(retrieveObject(rs));
			}
			conn.close();
			return newTweets;
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
