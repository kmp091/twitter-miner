package com.twitminer.stream;

import java.util.Calendar;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
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
	
	public static int HAPPY = 1;
	public static int SAD = 2;
	public static int DISGUST = 3;
	public static int SURPRISE = 4;
	
	private TweetDAO tweetDAO;
	
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
			Tweet store = new Tweet(status.getId(), status.getUser().getId(), status.getText(), cal, HAPPY);
			
			tweetDAO.insertTweet(store);
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
	
	public void filter(String... filterString) {
		FilterQuery filterQ = new FilterQuery();
		filterQ.track(filterString);
		
		twitStream.filter(filterQ);
	}
}
