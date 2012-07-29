package com.twitminer.viewer.gui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Date;

import javax.swing.Box;

import com.twitminer.beans.Tweet;
import com.twitminer.viewer.gui.images.ImageUtil;

import twitter4j.Twitter;
import twitter4j.User;

public class TweetViewer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7238532607163476143L;
	private TweetView tweetContent;
	private JButton nextButton;
	private JButton previousButton;
	
	/**
	 * Create the panel.
	 */
	public TweetViewer() {
		setPreferredSize(new Dimension(413, 172));
		setMinimumSize(new Dimension(250, 200));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel tweetPanel = new JPanel();
		tweetPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tweetPanel.setLayout(new BorderLayout(0, 0));
		
		tweetContent = new TweetView();
		tweetContent.setAlignmentX(Component.LEFT_ALIGNMENT);
		tweetPanel.add(tweetContent, BorderLayout.CENTER);
		
		add(tweetPanel);
		
		Component leftGap = Box.createRigidArea(new Dimension(20, 20));
		leftGap.setPreferredSize(new Dimension(5, 20));
		tweetPanel.add(leftGap, BorderLayout.WEST);
		
		Component rightGap = Box.createRigidArea(new Dimension(20, 20));
		rightGap.setPreferredSize(new Dimension(5, 20));
		tweetPanel.add(rightGap, BorderLayout.EAST);
		
		Component navGap = Box.createRigidArea(new Dimension(20, 20));
		navGap.setPreferredSize(new Dimension(10, 20));
		add(navGap);
		
		JPanel navPanel = new JPanel();
		add(navPanel);
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		
		previousButton = new JButton("Previous");
		previousButton.setPreferredSize(new Dimension(70, 80));
		previousButton.setMinimumSize(new Dimension(70, 80));
		previousButton.setMaximumSize(new Dimension(70, 80));
		previousButton.setToolTipText("Previous Tweet");
		previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
		previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		previousButton.setIcon(new ImageIcon(TweetViewer.class.getResource(ImageUtil.ROOT + "up-small.png")));
		navPanel.add(previousButton);
		
		nextButton = new JButton("Next");
		nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		nextButton.setVerticalTextPosition(SwingConstants.TOP);
		nextButton.setIcon(new ImageIcon(TweetViewer.class.getResource(ImageUtil.ROOT + "down-small.png")));
		nextButton.setPreferredSize(new Dimension(70, 80));
		nextButton.setMinimumSize(new Dimension(70, 80));
		nextButton.setMaximumSize(new Dimension(70, 80));
		navPanel.add(nextButton);

	}
	
	public void setTweetContent(String tweetContent) {
		this.tweetContent.setTweetContent(tweetContent);
	}
	
	public void setDetails(Tweet tweet, Twitter twitterInstance) {
		this.tweetContent.setDetails(tweet, twitterInstance);
	}
	
	public void setMoodDetails(int emotion) {
		this.tweetContent.setMoodDetails(emotion);
	}
	
	public void setTimeDetails(Date creationDate) {
		this.tweetContent.setTimeDetails(creationDate);
	}
	
	public void setUserDetails(User user) {
		this.tweetContent.setUserDetails(user);
	}
	
	public JButton getNextButton() {
		return this.nextButton;
	}
	
	public JButton getPreviousButton() {
		return this.previousButton;
	}

}
