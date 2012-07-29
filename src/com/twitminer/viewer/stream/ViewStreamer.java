package com.twitminer.viewer.stream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.beans.Tweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TweetDAO;
import com.twitminer.stream.Streamer;
import com.twitminer.util.TweetCleaner;
import com.twitminer.viewer.algorithm.ClassifierFactory;

import twitter4j.Status;
import twitter4j.auth.AccessToken;

public class ViewStreamer extends Streamer {
	
	private TweetCleaner tweetCleaner;
	private TweetDAO tweetDAO;
	private List<ChangeListener> listeners;
	
	public ViewStreamer(String consumerKey, String consumerSecret, AccessToken token, TweetDAO tweetStorage, TweetCleaner tweetCleaner) {
		super(consumerKey, consumerSecret, token);
		
		if (tweetCleaner == null) {
			this.tweetCleaner = new TweetCleaner();
		}
		else {
			this.tweetCleaner = tweetCleaner;
		}
		
		if (tweetStorage == null) {
			this.tweetDAO = DAOFactory.getInstance(DAOFactory.ARRAY_LIST).getTweetDAO();
		}
		else {
			this.tweetDAO = tweetStorage;
		}
		
		this.listeners = new ArrayList<ChangeListener>();
	}
	
	@Override
	protected boolean whileStreaming(Status status) {
		Tweet toBeStored = new Tweet();
		toBeStored.setTweetId(status.getId());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(status.getCreatedAt());
		toBeStored.setDateCreated(cal);
		
		toBeStored.setText(status.getText());
		
		toBeStored.setUserId(status.getUser().getId());
		
		List<String> tokens = tweetCleaner.tokenizeAndCleanTweet(status);
		if (tokens != null && tokens.size() > 0) {
			//TokenizedTweet tokenizedTweet = new TokenizedTweet(tokens);
			
			//classification code
			//Emotion emotion = ClassifierFactory.getInstance().classifyEmotion(tokenizedTweet);
		
			//toBeStored.setEmotionId(emotion.getEmotionId());
			toBeStored.setEmotionId(EmotionDAO.HAPPY);
			
			tweetDAO.insertTweet(toBeStored);
			
			this.fireOnChange(toBeStored);
		}
		
		return true;
	}

	public TweetDAO getTweetDAO() {
		return this.tweetDAO;
	}
	
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeChangeListener(ChangeListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireOnChange(Tweet status) {
		for (ChangeListener listener : listeners) {
			listener.stateChanged(new ChangeEvent(status));
		}
	}

}
