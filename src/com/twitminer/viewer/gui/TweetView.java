package com.twitminer.viewer.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

import java.awt.Font;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import javax.swing.border.EtchedBorder;

public class TweetView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2439977264324280578L;
	private JLabel userInfoLabel;
	private JLabel userIcon;
	private JLabel dateTimeLabel;
	private JLabel lblMood;
	private JLabel tweetTextLabel;
	
	/**
	 * Create the panel.
	 */
	public TweetView() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel userIconView = new JPanel();
		userIconView.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		userIconView.setPreferredSize(new Dimension(64, 64));
		userIconView.setMinimumSize(new Dimension(64, 64));
		add(userIconView, BorderLayout.WEST);
		userIconView.setLayout(new BoxLayout(userIconView, BoxLayout.X_AXIS));
		
		userIcon = new JLabel("");
		userIcon.setPreferredSize(new Dimension(64, 64));
		userIcon.setMaximumSize(new Dimension(64, 64));
		userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		userIconView.add(userIcon);
		
		JPanel tweetContent = new JPanel();
		add(tweetContent);
		tweetContent.setLayout(new BoxLayout(tweetContent, BoxLayout.Y_AXIS));
		
		userInfoLabel = new JLabel("UserID - User Name");
		userInfoLabel.setPreferredSize(new Dimension(126, 30));
		userInfoLabel.setMaximumSize(new Dimension(32767, 45));
		tweetContent.add(userInfoLabel);
		
		tweetTextLabel = new JLabel("Tweet content");
		tweetTextLabel.setHorizontalTextPosition(SwingConstants.LEADING);
		tweetTextLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tweetTextLabel.setMinimumSize(new Dimension(60, 16));
		tweetTextLabel.setPreferredSize(new Dimension(200, 16));
		tweetTextLabel.setVerticalAlignment(SwingConstants.TOP);
		tweetTextLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		tweetTextLabel.setMaximumSize(new Dimension(32767, 32767));
		tweetContent.add(tweetTextLabel);
		
		JPanel miscInfoPanel = new JPanel();
		miscInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		miscInfoPanel.setPreferredSize(new Dimension(10, 16));
		miscInfoPanel.setMaximumSize(new Dimension(8191, 16));
		tweetContent.add(miscInfoPanel);
		miscInfoPanel.setLayout(new BoxLayout(miscInfoPanel, BoxLayout.X_AXIS));
		
		dateTimeLabel = new JLabel("Date and time");
		miscInfoPanel.add(dateTimeLabel);
		dateTimeLabel.setPreferredSize(new Dimension(40, 30));
		dateTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dateTimeLabel.setToolTipText("Date and time metadata");
		dateTimeLabel.setMaximumSize(new Dimension(16383, 16));
		
		lblMood = new JLabel("Mood");
		lblMood.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMood.setPreferredSize(new Dimension(110, 16));
		lblMood.setMaximumSize(new Dimension(100, 16));
		miscInfoPanel.add(lblMood);

	}
	
	public void setUserDetails(User user) {
		if (user == null) {
			this.userInfoLabel.setText(encloseHTML(encloseBold("Invalid user detected.")));
			this.userIcon.setIcon(null);
			return;
		}
		
		this.userInfoLabel.setText(encloseHTML(encloseBold(user.getScreenName()) + " - " + user.getName()));
		this.userIcon.setIcon(new ImageIcon(user.getProfileImageURL()));
	}
	
	public void setTimeDetails(Date creationDate) {
		if (creationDate == null) {
			this.dateTimeLabel.setText("Date unavailable.");
			return;
		}
		
		this.dateTimeLabel.setText(new SimpleDateFormat("yyyy-mm-dd 'at' HH:mm:ss").format(creationDate));
	}
	
	public void setMoodDetails(int emotion) {
		switch(emotion) {
			case EmotionDAO.HAPPY:
				this.lblMood.setText("Happy");
				break;
			case EmotionDAO.SAD:
				this.lblMood.setText("Sad");
				break;
			case EmotionDAO.SURPRISE:
				this.lblMood.setText("Surprised");
				break;
			case EmotionDAO.DISGUST:
				this.lblMood.setText("Disgust");
				break;
			default:
				this.lblMood.setText("Unclassifiable");
		}
	}
	
	public void setTweetContent(String tweetContent) {
		this.tweetTextLabel.setText(encloseHTML(tweetContent));
	}
	
	public void setDetails(Tweet tweet, Twitter twitterInstance) {
		try {
			this.setUserDetails(twitterInstance.showUser(tweet.getUserId()));
			this.setMoodDetails(tweet.getEmotionId());
			this.setTimeDetails(tweet.getDateCreated().getTime());
			this.setTweetContent(tweet.getText());
		}
		catch (TwitterException tw) {
			tw.printStackTrace();
			this.setUserDetails(null);
		}
	}
	
	private String encloseBold (String content) {
		return this.encloseTags("b", content);
	}
	
	private String encloseHTML (String content) {
		return this.encloseTags("html", content);
	}
	
	private String encloseTags(String element, String content) {
		return "<" + element + ">" + content + "</" + element + ">";
	}

}
