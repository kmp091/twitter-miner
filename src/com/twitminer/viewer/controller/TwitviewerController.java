package com.twitminer.viewer.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.TweetDAO;
import com.twitminer.login.Authentication;
import com.twitminer.util.TweetCleaner;
import com.twitminer.viewer.gui.TwitViewerFrame;
import com.twitminer.viewer.gui.images.ImageUtil;
import com.twitminer.viewer.prefs.TwitViewerSettings;
import com.twitminer.viewer.stream.ViewStreamer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitviewerController {

	private Twitter twitterInstance;
	private TwitViewerFrame view;
	private ViewStreamer stream;
	private TweetDAO tweetDAO;
	private TweetCleaner tweetCleaner;
	
	private Tweet currentTweet;
	
	private ToolbarAction startStreamAction;
	private ToolbarAction stopStreamAction;
	
	public TwitviewerController() {
		twitterInstance = new TwitterFactory().getInstance();
		view = new TwitViewerFrame();
		tweetDAO = DAOFactory.getInstance(DAOFactory.ARRAY_LIST).getTweetDAO();
		currentTweet = null;
		tweetCleaner = new TweetCleaner();
		try {
			twitterInstance = Authentication.setUpTwitterInstance(twitterInstance);
			stream = new ViewStreamer(Authentication.CONSUMER_KEY, Authentication.CONSUMER_SECRET, twitterInstance.getOAuthAccessToken(), tweetDAO, tweetCleaner);
		} catch (TwitterException e1) {
			JOptionPane.showMessageDialog(view, "<html>In order to use this application, you have to be connected to the Internet." +
					"<br>Please re-run the application.</html>");
			e1.printStackTrace();
			System.exit(1);
		}	
		initialize();
	}
	
	private void initialize() {
		
		final ChangeListener dynamicDisplay = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				boolean autoscroll = TwitViewerSettings.getAutoScrollSetting();
				
				if (!autoscroll && tweetDAO.getTweets().size() > 1 && !view.getNextButton().isEnabled()) {
					view.getNextButton().setEnabled(true);
				}
				
				if (currentTweet == null || autoscroll) {
					Tweet receivedStatus = (Tweet)e.getSource();
					setCurrentTweet(receivedStatus);
				}
			}
			
		};

		stream.addChangeListener(dynamicDisplay);
		
		view.getPreviousButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Tweet> allTweets = tweetDAO.getTweets();
				
				int curTweetIndex = allTweets.indexOf(currentTweet);
				if (curTweetIndex != -1) {
					if (curTweetIndex == 0) {
						view.getPreviousButton().setEnabled(false);
						//do nothing
					}
					else {
						setCurrentTweet(allTweets.get(curTweetIndex - 1));						
					}
				}
				else {
					System.err.println("ERROR OCCURRED");
				}
			}
			
		});
		
		view.getNextButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Tweet> allTweets = tweetDAO.getTweets();
				
				int curTweetIndex = allTweets.indexOf(currentTweet);
				if (curTweetIndex != -1) {
					if (curTweetIndex == allTweets.size() - 1) {
						view.getNextButton().setEnabled(false);
						// do nothing
					}
					else {
						setCurrentTweet(allTweets.get(curTweetIndex + 1));						
					}
				}
				else {
					System.err.println("ERROR OCCURRED");
		        }
		}
		
		});
		
		//initialize actions
		startStreamAction = new ToolbarAction("Start", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "play-button.png"), "Starts the stream of tweets")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stream.filter("Harry Potter");
				this.setEnabled(false);
				stopStreamAction.setEnabled(true);
			}
			
		};
		
		stopStreamAction = new ToolbarAction("Stop", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "stop-button.png"), "Stops the stream of tweets")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				stream.shutdown();
				this.setEnabled(false);
				startStreamAction.setEnabled(true);
			}
			
		};		
		
		stopStreamAction.setEnabled(false);
		
		ToolbarAction autoUpdateAction = new ToolbarAction("Turn On Autoscroll", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "autoscroll-on.png")), "TURN ON AUTOSCROLL: The newest tweet is automatically displayed when this is turned on (this does not affect how the chart updates).") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (TwitViewerSettings.getAutoScrollSetting()) {
					setActionProperties(false);
				}
				else {
					setActionProperties(true);
				}
			}
			
			private void setActionProperties(boolean autoScrollSetting) {
				TwitViewerSettings.setAutoScrollSetting(autoScrollSetting);
				if (autoScrollSetting) {
					this.setIcon(new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "autoscroll-off.png")));
					this.setName("Turn Off Autoscroll");
					this.setTooltip("TURN OFF AUTOSCROLL: The newest tweet is not automatically displayed, and the previous tweets can be browsed by navigating the buttons.");
//					stream.addChangeListener(dynamicDisplay);
					view.getNextButton().setEnabled(false);
					view.getPreviousButton().setEnabled(false);
				}
				else {
					this.setIcon(new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "autoscroll-on.png")));					
					this.setName("Turn On Autoscroll");
					this.setTooltip("TURN ON AUTOSCROLL: The newest tweet is automatically displayed when this is turned on (this does not affect how the chart updates).");
//					stream.removeChangeListener(dynamicDisplay);
					view.getNextButton().setEnabled(true);
					view.getPreviousButton().setEnabled(true);
				}

			}
			
		};
		
		ToolbarAction hideChartAction = new ToolbarAction("Hide Chart", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "show-graph.png")), "The sentiment over time chart is shown when this is turned on and hidden when turned off.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (TwitViewerSettings.getChartVisibilitySetting()) {
					setActionProperties(false);
				}
				else {
					setActionProperties(true);
				}
			}
			
			private void setActionProperties(boolean chartVisibleSetting) {
				TwitViewerSettings.setChartVisibilitySetting(chartVisibleSetting);
				if (chartVisibleSetting) {
					this.setName("Hide Chart");
					view.getBottomView().setVisible(false);
					view.getTopView().add(view.getMoodPanel());
				}
				else {
					this.setName("Show Chart");
					view.getTopView().remove(view.getMoodPanel());
					view.getBottomView().add(view.getMoodPanel());
					view.getBottomView().setVisible(true);
				}
			}
			
		};
		
		//initialize toolbar with actions
		JButton startButton = view.getToolbar().add(startStreamAction);
		JButton stopButton = view.getToolbar().add(stopStreamAction);
		JButton autoUpdateButton = view.getToolbar().add(autoUpdateAction);
		JButton hideChartButton = view.getToolbar().add(hideChartAction);
		
		/*startButton.setText(startStreamAction.getName());
		startButton.setToolTipText(startStreamAction.getTooltip());
		
		stopButton.setText(stopStreamAction.getName());
		stopButton.setToolTipText(stopStreamAction.getTooltip());
		
		autoUpdateButton.setText(autoUpdateAction.getName());
		autoUpdateButton.setToolTipText(autoUpdateAction.getTooltip());
		
		hideChartButton.setText(hideChartAction.getName());
		hideChartButton.setToolTipText(hideChartAction.getTooltip());*/
		
		startButton.setPreferredSize(new Dimension(64, 150));
		stopButton.setPreferredSize(new Dimension(64, 150));
		autoUpdateButton.setPreferredSize(new Dimension(64, 150));
		hideChartButton.setPreferredSize(new Dimension(64, 150));
		
		startButton.setHorizontalAlignment(JButton.CENTER);
		stopButton.setHorizontalAlignment(JButton.CENTER);
		autoUpdateButton.setHorizontalAlignment(JButton.CENTER);
		hideChartButton.setHorizontalAlignment(JButton.CENTER);
		
		startButton.setVerticalAlignment(JButton.BOTTOM);
		stopButton.setVerticalAlignment(JButton.BOTTOM);
		autoUpdateButton.setVerticalAlignment(JButton.BOTTOM);
		hideChartButton.setVerticalAlignment(JButton.BOTTOM);
	}
	
	private void setCurrentTweet(Tweet curTweet) {
		this.currentTweet = curTweet;
		view.setCurrentTweet(curTweet, twitterInstance);
		
		if (tweetDAO.getTweets().indexOf(curTweet) == 0) {
			view.getPreviousButton().setEnabled(false);
		}
		else if (tweetDAO.getTweets().indexOf(curTweet) == tweetDAO.getTweets().size() - 1) {
			view.getNextButton().setEnabled(false);
		}
		else {
			view.getPreviousButton().setEnabled(true);
			view.getNextButton().setEnabled(true);
		}
	}
	
	public static void run() {
		TwitviewerController twitviewer = new TwitviewerController();
		twitviewer.view.setVisible(true);
	}
	
	public static void main(String[] args) {
		TwitviewerController.run();
	}

}
