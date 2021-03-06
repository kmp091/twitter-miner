package com.twitminer.viewer.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.dao.TweetDAO;
import com.twitminer.login.Authentication;
import com.twitminer.util.TweetAndTokens;
import com.twitminer.util.TweetCleaner;
import com.twitminer.viewer.algorithm.ClassifierFactory;
import com.twitminer.viewer.gui.TwitViewerFrame;
import com.twitminer.viewer.gui.images.ImageUtil;
import com.twitminer.viewer.mood.MoodManager;
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
	private TokenizedTweetDAO tokenizedTweetDAO;
	private EmotionDAO emotionDAO;
	private TweetCleaner tweetCleaner;
	private MoodManager moodMgr;
	private ScheduledThreadPoolExecutor liveMoodUpdater;
	
	private Tweet currentTweet;
	
	private ToolbarAction startStreamAction;
	private ToolbarAction stopStreamAction;
	
	private AlgorithmSelectController algoSelector;
	
	private JButton selectAlgoButton;
	private JButton hideChartButton;
	private JButton autoUpdateButton;
	private JButton stopButton;
	private JButton startButton;
	
	private Runnable updateAction = new Runnable() {

		@Override
		public void run() {
			int emotionId = moodMgr.getPredominantMoodId();
			view.setCurrentMood(emotionId);
			
			//add to chart
			view.addEmotionToChart(Calendar.getInstance(), emotionId);
		}
		
	};
	
	public TwitviewerController() {
		DAOFactory daoFactory = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		tweetDAO = daoFactory.getTweetDAO();
		emotionDAO = daoFactory.getEmotionDAO();
		tokenizedTweetDAO = daoFactory.getTokenizedTweetDAO();
		twitterInstance = new TwitterFactory().getInstance();
		view = new TwitViewerFrame(emotionDAO);
		currentTweet = null;
		moodMgr = new MoodManager(emotionDAO);
		tweetCleaner = new TweetCleaner();
		try {
			twitterInstance = Authentication.setUpTwitterInstance(twitterInstance);
			stream = new ViewStreamer(Authentication.CONSUMER_KEY, Authentication.CONSUMER_SECRET, twitterInstance.getOAuthAccessToken(), tweetDAO, emotionDAO, tweetCleaner, ClassifierFactory.SMO);
		} catch (TwitterException e1) {
			JOptionPane.showMessageDialog(view, "<html>In order to use this application, you have to be connected to the Internet." +
					"<br>Please re-run the application.</html>");
			e1.printStackTrace();
			System.exit(1);
		}	

		this.algoSelector = new AlgorithmSelectController();
		
		initialize();
		
		this.liveMoodUpdater = new ScheduledThreadPoolExecutor(2);
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
					TweetAndTokens received = (TweetAndTokens)e.getSource();
					Tweet receivedStatus = received.getTweet();
					TokenizedTweet receivedTokens = received.getTokens();
					setCurrentTweet(receivedStatus, receivedTokens);
					moodMgr.addTweet(receivedStatus);
//					view.addEmotionToChart(receivedStatus.getDateCreated(), /*receivedStatus.getEmotionId()*/ new Random().nextInt(3) + 1);
				}
			}
			
		};

		stream.addChangeListener(dynamicDisplay);
		
		view.getPreviousButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Tweet> allTweets = tweetDAO.getTweets();
				List<TokenizedTweet> allTokenizedTweets = tokenizedTweetDAO.getTweets();
				
				int curTweetIndex = allTweets.indexOf(currentTweet);
				if (curTweetIndex != -1) {
					if (curTweetIndex == 0) {
						view.getPreviousButton().setEnabled(false);
						//do nothing
					}
					else {
						setCurrentTweet(allTweets.get(curTweetIndex - 1), allTokenizedTweets.get(curTweetIndex - 1));						
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
				setupMoodUpdater();	
				view.startChart();
				selectAlgoButton.setEnabled(false);
			}
			
		};
		
		stopStreamAction = new ToolbarAction("Stop", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "stop-button.png"), "Stops the stream of tweets")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				this.setEnabled(false);
				
				Thread shutDown = new Thread(new Runnable() {				
					@Override
					public void run() {
						stream.shutdown();
						//shutDownMoodUpdater();
						startStreamAction.setEnabled(true);
						view.stopChart();
						selectAlgoButton.setEnabled(true);
					}
				});
				
				shutDown.start();
			}
			
		};		
		
		stopStreamAction.setEnabled(false);
		
		ToolbarAction autoUpdateAction = new ToolbarAction("Autoscroll Setting",
				new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + (TwitViewerSettings.getAutoScrollSetting() ? "autoscroll-off.png" : "autoscroll-on.png")))) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void initialize() {
				setActionProperties(TwitViewerSettings.getAutoScrollSetting());
			}
			
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
					if (!tweetDAO.getTweets().isEmpty()) {
						view.getNextButton().setEnabled(true);
						view.getPreviousButton().setEnabled(true);
					}
				}

			}
			
		};
		
		ToolbarAction hideChartAction = new ToolbarAction(
				TwitViewerSettings.getChartVisibilitySetting() ? "Hide Chart" : "Show Chart", 
				new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "show-graph.png"))) {

			protected void initialize() {
				setActionProperties(TwitViewerSettings.getChartVisibilitySetting());
			}
			
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
					this.setTooltip("HIDE CHART: The sentiment over time chart is hidden.");
					//view.getTopView().remove(view.getMoodPanel());
					//view.getBottomView().add(view.getMoodPanel());
					view.getBottomView().setVisible(true);
					//view.setSize(new Dimension(view.getSize().width, view.getSize().height + view.getBottomView().getSize().height));
				}
				else {
					this.setName("Show Chart");
					this.setTooltip("SHOW CHART: The sentiment over time chart is shown.");
					view.getBottomView().setVisible(false);
					//view.getTopView().add(view.getMoodPanel());
					//xview.setSize(new Dimension(view.getPreferredSize().width, view.getPreferredSize().height - view.getBottomView().getSize().height));
				}
			}
			
		};
		
		ToolbarAction selectAlgorithmAction = new ToolbarAction(
				"Select Algorithm", new ImageIcon(TwitviewerController.class.getResource(ImageUtil.ROOT + "algorithm.png"))) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent arg0) {
						algoSelector.setAlgoGUI();
						updateAlgorithmButtonLabel(algoSelector.getCurrentAlgo());
					}
			
		};
		
		//initialize toolbar with actions
		 startButton = view.getToolbar().add(startStreamAction);
		 stopButton = view.getToolbar().add(stopStreamAction);
		 autoUpdateButton = view.getToolbar().add(autoUpdateAction);
		 hideChartButton = view.getToolbar().add(hideChartAction);
		 selectAlgoButton = view.getToolbar().add(selectAlgorithmAction);
		
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
		selectAlgoButton.setPreferredSize(new Dimension(64, 150));
		
		startButton.setHorizontalAlignment(JButton.CENTER);
		stopButton.setHorizontalAlignment(JButton.CENTER);
		autoUpdateButton.setHorizontalAlignment(JButton.CENTER);
		hideChartButton.setHorizontalAlignment(JButton.CENTER);
		selectAlgoButton.setHorizontalAlignment(JButton.CENTER);
		
		startButton.setVerticalAlignment(JButton.BOTTOM);
		stopButton.setVerticalAlignment(JButton.BOTTOM);
		autoUpdateButton.setVerticalAlignment(JButton.BOTTOM);
		hideChartButton.setVerticalAlignment(JButton.BOTTOM);
		selectAlgoButton.setVerticalAlignment(JButton.BOTTOM);
		
		selectAlgoButton.setHorizontalTextPosition(JButton.RIGHT);
		selectAlgoButton.setVerticalTextPosition(JButton.CENTER);
		
		updateAlgorithmButtonLabel(this.algoSelector.getCurrentAlgo());
	}
	
	private void updateAlgorithmButtonLabel(int algo) {
		switch (algo) {
		case ClassifierFactory.SMO:
				this.selectAlgoButton.setText("SMO");
			break;
		case ClassifierFactory.NAIVE_BAYES:
				this.selectAlgoButton.setText("Naive Bayes");
			break;
		case ClassifierFactory.J48:
				this.selectAlgoButton.setText("J48");
			break;
		}
		
		try {
			stream.setClassifier(algoSelector.getCurrentAlgo());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setupMoodUpdater() {
		this.liveMoodUpdater.scheduleWithFixedDelay(updateAction, 2 * 1000, moodMgr.getWindowSizeInMillis(), TimeUnit.MILLISECONDS);
	}
	
	@SuppressWarnings("unused")
	private void shutDownMoodUpdater() {
		this.liveMoodUpdater.shutdownNow();
	}
	
	private void setCurrentTweet(Tweet curTweet) {
		this.currentTweet = curTweet;
		try {
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
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "<html>Twitter isn't letting people in at this time.<br/>" +
					"Please run the application again at a later time.</html>");
			ex.printStackTrace();
		}
	}
	
	private void setCurrentTweet(Tweet curTweet, TokenizedTweet tokens) {
		view.setCurrentTweet(curTweet, twitterInstance, new ArrayList<String>(tokens.getSetOfTokens()));
	}
	
	public static void run() {
		try {
			TwitviewerController twitviewer = new TwitviewerController();
			twitviewer.view.setVisible(true);
		}
		catch (OutOfMemoryError err) {
			JOptionPane.showMessageDialog(null, "<html>Please allocate the Java Virtual Machine with more memory.<br/>" +
					"Please try running it from the command line using java -jar -Xmx900M TwitMonitor.jar<br/>" +
					"The application will now terminate.</html>");
		}
	}
	
}
