package com.twitminer.stream;

import java.util.concurrent.atomic.AtomicInteger;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public abstract class Streamer implements StatusListener {

	TwitterStream twitStream;
	
	private AtomicInteger tweetCounter = new AtomicInteger(0);
	private int tweetsRequested = -1;
	
	public Streamer(String consumerKey, String consumerSecret, AccessToken accToken) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret);
		
		twitStream = new TwitterStreamFactory(cb.build()).getInstance(accToken);

		twitStream.addListener(this);
		
	}
	
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
		if (tweetsRequested != -1 && tweetCounter.get() >= tweetsRequested) {
			System.out.println("Over the requested number");
		}
		else {
			System.out.println(status.getText());
			
			if (whileStreaming(status)) {
				tweetCounter.incrementAndGet();				
			}
			
			System.out.println("Number of streamed tweets so far: " + tweetCounter);
		}
		
	}
	
	protected int getTweetCounterValue() {
		return this.tweetCounter.get();
	}
	
	/**Dictate a condition that, if not fulfilled, will not increment the tweet counter
	 * 
	 * @param status The status object containing the received status message
	 * @return the outcome of a condition stated within the method
	 */
	protected boolean whileStreaming(Status status) {
		return true;
	}

	@Override
	public void onTrackLimitationNotice(int numOfLimitedStatuses) {
		// TODO Auto-generated method stub
		System.out.println("Limit reached. Stopping...");
	}

	public void shutdown() {
		twitStream.shutdown();
	}

	public void filter(String... filterString) {
		FilterQuery filterQ = new FilterQuery();
		filterQ.track(filterString);
		
		twitStream.filter(filterQ);
		
	}
	
	public void filterUntil(int numOfTweets, String... filterString) throws InterruptedException {
		this.tweetsRequested = numOfTweets;
		filter(filterString);

		System.out.println("Don't worry, system hasn't hung: we're just blocking the main thread until");
		System.out.println("the number of tweets that come in has reached " + numOfTweets);
		
		while (tweetCounter.get() < this.tweetsRequested) {
			//this'll block the main thread, maybe in the future create a runnable thread
		}
		
		System.out.println("Resetting counter and shutting down");
		shutdown();
		tweetCounter.set(0);
	}
	
}
