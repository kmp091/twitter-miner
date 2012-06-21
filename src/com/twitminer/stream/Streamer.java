package com.twitminer.stream;

import java.util.Calendar;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TweetDAO;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class Streamer {
	
	TwitterStream twitStream;
	
	private TweetDAO tweetDAO;
	
	private int curEmotion = EmotionDAO.HAPPY;
	
	private int tweetCounter = 0;
	
	StatusListener statusListener = new StatusListener(){

		@Override
		public void onException(Exception arg0) {
			System.out.println("Connection terminated");
			arg0.printStackTrace();
		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatus(Status status) {
			// TODO Auto-generated method stub
			System.out.println(status.getText());
			//store on db code
			Calendar cal = Calendar.getInstance();
			cal.setTime(status.getCreatedAt());
			Tweet store = new Tweet(status.getId(), status.getUser().getId(), status.getText(), cal, curEmotion);
			
			tweetDAO.insertTweet(store);
			
			tweetCounter++;
		}

		@Override
		public void onTrackLimitationNotice(int numOfLimitedStatuses) {
			// TODO Auto-generated method stub
			System.out.println("Limit reached. Stopping...");
		}
		
	};
	
	public Streamer(String consumerKey, String consumerSecret, AccessToken token) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret);
		
		twitStream = new TwitterStreamFactory(cb.build()).getInstance(token);
		twitStream.addListener(statusListener);
		
		DAOFactory daoFactory = DAOFactory.getInstance(DAOFactory.MYSQL);
		tweetDAO = daoFactory.getTweetDAO();
	}
	
	public void filterAndAnnotate(int emotionId, String... filterString) {
		this.curEmotion = emotionId;
		this.filter(filterString);
	}
	
	public void filter(String... filterString) {
		FilterQuery filterQ = new FilterQuery();
		filterQ.track(filterString);
		
		twitStream.filter(filterQ);
		
	}
	
	public void shutdown() {
		twitStream.shutdown();
	}
	
	public void filterAndAnnotateUntil(int numOfTweets, int emotionId, String... filterString) {
		this.curEmotion = emotionId;
		this.filterUntil(numOfTweets, filterString);
	}
	
	public void filterUntil(int numOfTweets, String... filterString) {
		filter(filterString);

		System.out.println("Don't worry, system hasn't hung: we're just blocking the main thread until");
		System.out.println("the number of tweets that come in has reached " + numOfTweets);
		
		while (tweetCounter <= numOfTweets) {
			//this'll block the main thread, maybe in the future create a runnable thread
			System.out.println("Number of streamed tweets so far: " + tweetCounter);
		}
		
		shutdown();
	}
}
