package com.twitminer.stream;

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
	}
	
	public void filter(String... filterString) {
		FilterQuery filterQ = new FilterQuery();
		filterQ.track(filterString);
		
		twitStream.filter(filterQ);
	}
}
