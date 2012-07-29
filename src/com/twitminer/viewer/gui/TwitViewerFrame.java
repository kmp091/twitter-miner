package com.twitminer.viewer.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.Box;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import twitter4j.Twitter;

import com.twitminer.beans.Tweet;
import java.awt.Cursor;

public class TwitViewerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8350003044720788785L;
	private JPanel contentPane;
	private TweetViewer tweetViewer;
	private MoodView moodPanel;
	private JPanel topView;
	private JPanel bottomView;
	private JPanel body;
	private Component panelGap;
	private ChartPanel ganttPanel;
	private JFreeChart sentimentGraph;
	private Component rigidArea;
	private JToolBar viewOptions;
	
	/**
	 * Create the frame.
	 */
	public TwitViewerFrame() {
		setTitle("Twitter Sentiment Analysis - 'Harry Potter'");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 524, 557);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 2));
		setContentPane(contentPane);
		
		viewOptions = new JToolBar();
		viewOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		viewOptions.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewOptions.setAlignmentY(Component.CENTER_ALIGNMENT);
		viewOptions.setName("");
		viewOptions.setPreferredSize(new Dimension(128, 128));
		viewOptions.setMaximumSize(new Dimension(200, 250));
		viewOptions.setMinimumSize(new Dimension(128, 64));
		viewOptions.setFloatable(false);
		contentPane.add(viewOptions, BorderLayout.NORTH);
		
		body = new JPanel();
		contentPane.add(body, BorderLayout.CENTER);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		
		topView = new JPanel();
		topView.setMaximumSize(new Dimension(32767, 190));
		topView.setPreferredSize(new Dimension(10, 190));
		body.add(topView);
		topView.setMinimumSize(new Dimension(10, 140));
		topView.setLayout(new BoxLayout(topView, BoxLayout.X_AXIS));
		
		tweetViewer = new TweetViewer();
		tweetViewer.setMaximumSize(new Dimension(2147483647, 175));
		tweetViewer.getNextButton().setEnabled(false);
		tweetViewer.getPreviousButton().setEnabled(false);
		tweetViewer.setPreferredSize(new Dimension(260, 150));
		tweetViewer.setMinimumSize(new Dimension(260, 150));
		topView.add(tweetViewer);
		
		panelGap = Box.createRigidArea(new Dimension(20, 20));
		body.add(panelGap);
		
		bottomView = new JPanel();
		body.add(bottomView);
		bottomView.setLayout(new BoxLayout(bottomView, BoxLayout.X_AXIS));
		
		//initialize gantt chart
		this.sentimentGraph = ChartFactory.createTimeSeriesChart("Predominant Sentiment Over Time", "Time", "Sentiment", null, true, false, false);
		
		ganttPanel = new ChartPanel(sentimentGraph);
		bottomView.add(ganttPanel);
		
		
		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		bottomView.add(rigidArea);
		
		moodPanel = new MoodView();
		bottomView.add(moodPanel);
		moodPanel.setPreferredSize(new Dimension(122, 150));
		moodPanel.setMaximumSize(new Dimension(32771, 150));
	}
	
	public void setCurrentMood(int mood) {
		moodPanel.setMoodDisplay(mood);
	}
	
	public void setCurrentTweet(Tweet tweet, Twitter twitter) {
		tweetViewer.setDetails(tweet, twitter);
	}
	
	public JButton getPreviousButton() {
		return tweetViewer.getPreviousButton();
	}
	
	public JButton getNextButton() {
		return tweetViewer.getNextButton();
	}
	
	public TweetViewer getTweetViewer() {
		return tweetViewer;
	}
	
	public JFreeChart getChart() {
		return this.sentimentGraph;
	}
	
	public void setChart(JFreeChart newChart) {
		this.sentimentGraph = newChart;
	}
	
	public JToolBar getToolbar() {
		return this.viewOptions;
	}

	public MoodView getMoodPanel() {
		return moodPanel;
	}
	public JPanel getTopView() {
		return topView;
	}
	public JPanel getBottomView() {
		return bottomView;
	}
}
