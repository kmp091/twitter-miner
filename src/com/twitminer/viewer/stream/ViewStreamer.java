package com.twitminer.viewer.stream;

import java.net.URISyntaxException;
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
import com.twitminer.util.TweetAndTokens;
import com.twitminer.util.TweetCleaner;
import com.twitminer.viewer.algorithm.Classifier;
import com.twitminer.viewer.algorithm.ClassifierFactory;

import twitter4j.Status;
import twitter4j.auth.AccessToken;

public class ViewStreamer extends Streamer {
	
	private TweetCleaner tweetCleaner;
	private TweetDAO tweetDAO;
	private EmotionDAO emotionDAO;
	private List<ChangeListener> listeners;
	private Classifier classifier;	
	
	public ViewStreamer(String consumerKey, String consumerSecret, AccessToken token, TweetDAO tweetStorage, EmotionDAO emotionStorage, TweetCleaner tweetCleaner, int algorithm) {
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
		
		if (emotionStorage == null) {
			this.emotionDAO = DAOFactory.getInstance(DAOFactory.ARRAY_LIST).getEmotionDAO();
		}
		else {
			this.emotionDAO = emotionStorage;
		}
		
		this.listeners = new ArrayList<ChangeListener>();
		
		try {
			this.classifier = ClassifierFactory.getInstance(algorithm, emotionDAO);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			TokenizedTweet tokenizedTweet = new TokenizedTweet(tokens);
			
			//classification code
			Emotion emotion = null;
			try {
				emotion = classifier.classifyEmotion(tokenizedTweet);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			toBeStored.setEmotionId(emotion.getEmotionId());
			
			tweetDAO.insertTweet(toBeStored);
			
			this.fireOnChange(toBeStored, tokenizedTweet);
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
	
	public void setClassifier(int algorithm) throws URISyntaxException, Exception {
		this.classifier = ClassifierFactory.getInstance(algorithm, emotionDAO);
	}
	
	protected void fireOnChange(Tweet status, TokenizedTweet statusTokens) {
		for (ChangeListener listener : listeners) {
			listener.stateChanged(new ChangeEvent(new TweetAndTokens(status, statusTokens)));
		}
	}

}
