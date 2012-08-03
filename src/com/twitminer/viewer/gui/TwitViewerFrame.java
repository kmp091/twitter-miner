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
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import twitter4j.Twitter;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

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
	private ChartPanel chartPanel;
	private JFreeChart sentimentGraph;
	private JToolBar viewOptions;
//	private DefaultKeyedValues2DDataset emotionSeries;
	private TimeSeriesCollection emotionSeriesDataset;
	private TimeSeries timeSeries;
	
	private EmotionDAO emotions;
	
	private Calendar chartStartDate = null;
	private JLabel legendLabel;
	
	/**
	 * Create the frame.
	 */
	public TwitViewerFrame(EmotionDAO emotionStorage) {
		this.emotions = emotionStorage;
		
		setTitle("Twitter Sentiment Analysis - 'Harry Potter'");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 687, 719);
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
		viewOptions.setMaximumSize(new Dimension(200, 175));
		viewOptions.setMinimumSize(new Dimension(128, 64));
		viewOptions.setFloatable(false);
		contentPane.add(viewOptions, BorderLayout.NORTH);
		
		body = new JPanel();
		contentPane.add(body, BorderLayout.CENTER);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		
		topView = new JPanel();
		topView.setPreferredSize(new Dimension(10, 170));
		body.add(topView);
		topView.setMinimumSize(new Dimension(10, 140));
		topView.setLayout(new BoxLayout(topView, BoxLayout.X_AXIS));
		
		tweetViewer = new TweetViewer();
		tweetViewer.setMaximumSize(new Dimension(2147483647, 250));
		tweetViewer.getNextButton().setEnabled(false);
		tweetViewer.getPreviousButton().setEnabled(false);
		tweetViewer.setPreferredSize(new Dimension(260, 250));
		tweetViewer.setMinimumSize(new Dimension(260, 150));
		topView.add(tweetViewer);
		
		moodPanel = new MoodView();
		topView.add(moodPanel);
		moodPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		moodPanel.setMinimumSize(new Dimension(168, 90));
		moodPanel.setPreferredSize(new Dimension(168, 90));
		moodPanel.setMaximumSize(new Dimension(200, 168));
		
		panelGap = Box.createRigidArea(new Dimension(20, 20));
		panelGap.setMaximumSize(new Dimension(32767, 20));
		body.add(panelGap);
		
		bottomView = new JPanel();
		bottomView.setMinimumSize(new Dimension(10, 200));
		bottomView.setPreferredSize(new Dimension(10, 250));
		body.add(bottomView);
		bottomView.setLayout(new BoxLayout(bottomView, BoxLayout.X_AXIS));
		
		//initialize chart
		makeChart();
		chartPanel = new ChartPanel(sentimentGraph);
		chartPanel.setPreferredSize(new Dimension(300, 420));
		bottomView.add(chartPanel);
		
		legendLabel = new JLabel("<html>" + "You can keep track of emotions from a positive to negative spectrum<br/><br/>" +
				"Legend:<br/>" +
				"<b>-1.0</b> - Initializing <br>" +
				"<b>1.0</b> - " + emotions.getEmotionById(1).getEmotionName() + "<br/>" +
				"<b>2.0</b> - " + emotions.getEmotionById(2).getEmotionName() + "<br/>" +
				"<b>3.0</b> - " + emotions.getEmotionById(3).getEmotionName() + "<br/>" +
				"<b>4.0</b> - " + emotions.getEmotionById(4).getEmotionName() + "</html>");
		bottomView.add(legendLabel);
		legendLabel.setMinimumSize(new Dimension(150, 20));
		legendLabel.setPreferredSize(new Dimension(150, 70));
		legendLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		legendLabel.setMaximumSize(new Dimension(150, 168));
		legendLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
	}
	
	private void makeChart() {
		//this.emotionSeries = new DefaultKeyedValues2DDataset();
		this.emotionSeriesDataset = new TimeSeriesCollection();
		
		//initialize chart
		//this.sentimentGraph = ChartFactory.createLineChart("Predominant Sentiment Over Time", "Sentiment", "No data", this.emotionSeries, PlotOrientation.HORIZONTAL, true, false, false);
		this.sentimentGraph = ChartFactory.createTimeSeriesChart("Predominant Sentiment Over Time", "No data", "Sentiment", this.emotionSeriesDataset, true, false, false);
		
	}
	
	public void startChart() {
		//this.emotionSeries.clear();
		this.emotionSeriesDataset.removeAllSeries();
		
		SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yy hh:mm:ss");
		this.chartStartDate = Calendar.getInstance();
		String dateNow = format.format(chartStartDate.getTime());
		
		this.timeSeries = new TimeSeries("Predominant Mood Over Time", "Time", "Predominant Mood");
		
		this.emotionSeriesDataset.addSeries(this.timeSeries);
		
	//	this.sentimentGraph = ChartFactory.createLineChart("Predominant Sentiment Since " + dateNow, "Sentiment", "Seconds since " + dateNow, this.emotionSeries, PlotOrientation.HORIZONTAL, true, false, false);		
		this.sentimentGraph = ChartFactory.createTimeSeriesChart("Predominant Sentiment Over Time", "Seconds since " + dateNow, "Sentiment", this.emotionSeriesDataset, false, false, false);
		chartPanel.setChart(this.sentimentGraph);
	}
	
	public void stopChart() {
		this.chartStartDate = null;
	}
	
	public void addEmotionToChart(Calendar newTweetDate, int emotionId) {
		if (this.chartStartDate != null) {
			//double secondsSinceStart = (newTweetDate.getTimeInMillis() - chartStartDate.getTimeInMillis())/1000;
			//double secondsSinceStart = newTweetDate.getTimeInMillis();
			
			/*String emotionString;
			
			switch (emotionId) {
			case EmotionDAO.HAPPY:
				emotionString = "Happy";
				break;
			case EmotionDAO.SAD:
				emotionString = "Sad";
				break;
			case EmotionDAO.DISGUST:
				emotionString = "Disgust";
				break;
			case EmotionDAO.SURPRISE:
				emotionString = "Surprise";
				break;
				default:
					emotionString = "Unclassifiable";
			}*/
			
			this.timeSeries.addOrUpdate(RegularTimePeriod.createInstance(Second.class, newTweetDate.getTime(), newTweetDate.getTimeZone()), emotionId);
//			this.emotionSeries.addValue(secondsSinceStart, "Predominant Mood", emotionString);
		}
		else {
			System.out.println("The chart is not started");
		}
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
