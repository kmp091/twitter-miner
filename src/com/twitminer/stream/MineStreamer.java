package com.twitminer.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.util.TweetCleaner;

import twitter4j.Status;
import twitter4j.auth.AccessToken;

public class MineStreamer extends Streamer {
	
	TweetCleaner tweetCleaner;
	
	private TokenizedTweetDAO tweetDAO;
	
	private int curEmotion = EmotionDAO.HAPPY;
	
	private List<ChangeListener> changeListeners;
	
	private AtomicBoolean firingEvent = new AtomicBoolean(false);
	
	private MineStreamer(String consumerKey, String consumerSecret, AccessToken token, TokenizedTweetDAO tokenizedTweetStorage) {
		super(consumerKey, consumerSecret, token);
		
		this.changeListeners = new ArrayList<ChangeListener>();
		this.tweetDAO = tokenizedTweetStorage;
	}
	
	public MineStreamer(String consumerKey, String consumerSecret, AccessToken token, TokenizedTweetDAO tokenizedTweetStorage, TweetCleaner cleaner) {
		this(consumerKey, consumerSecret, token, tokenizedTweetStorage);
		if (cleaner == null) {
			tweetCleaner = new TweetCleaner();
		}
		else {
			this.tweetCleaner = cleaner;			
		}
	}
	
	protected boolean whileStreaming(Status status) {
		System.out.println(status.getText());
		//store on db code
		//Calendar cal = Calendar.getInstance();
		//cal.setTime(status.getCreatedAt());
					
		List<String> cleanedTweet = tweetCleaner.tokenizeAndCleanTweet(status);				
		
		if (cleanedTweet != null && !cleanedTweet.isEmpty()) {
			cleanedTweet.remove("");
			
			//Tweet store = new Tweet(status.getId(), status.getUser().getId(), cleanedTweet, cal, curEmotion);
			TokenizedTweet tokenized = new TokenizedTweet(cleanedTweet, curEmotion);
			
			tweetDAO.insertTweet(tokenized);
			
			fireChangeEvent(new ChangeEvent(Integer.toString(getTweetCounterValue())));
			return true;
		}
		
		return false;
	}
	
/*	public void filterAndAnnotate(int emotionId, String... filterString) {
		this.curEmotion = emotionId;
		this.filter(filterString);
	}*/
	
	public void filterAndAnnotate(int emotionId, String... otherFilters) {
		System.out.println("Hold on to your pants, we're loading some emoticons :)");
		List<String> emoticons = this.appendEmoticonsToListOfKeywords(emotionId, otherFilters);
		filter(emoticons.toArray(new String[emoticons.size()]));
	}
	
	/**
	 * 
	 * @param numOfTweets
	 * @param emotionId
	 * @throws InterruptedException 
	 */
	public void filterAndAnnotateUntil(int numOfTweets, int emotionId, String... filterString) throws InterruptedException {
		this.curEmotion = emotionId;
		System.out.println("Hold on to your pants, we're loading some emoticons :)");
		
		List<String> emoticons = this.appendEmoticonsToListOfKeywords(emotionId, filterString);
		
		this.filterUntil(numOfTweets, emoticons.toArray(new String[emoticons.size()]));
	}
	
	private List<String> appendEmoticonsToListOfKeywords(int emotionId, String... filterString) {
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		EmoticonDAO emo = daos.getEmoticonDAO();
		List<String> emoticons = emo.getEmoticonStringsByEmotion(emotionId);
		
		this.tweetCleaner.addBlockedWords(filterString);
		
		if (filterString != null && filterString.length != 0) {
			//emoticons.addAll(Arrays.asList(filterString));
			List<String> filterWithEmoticons = new ArrayList<String>();
			
			Iterator<String> it = emoticons.iterator();
			while (it.hasNext()) {
				String curEmoticon = it.next();
				
				for (String extraFilter : filterString) {
					filterWithEmoticons.add(extraFilter + " " + curEmoticon);					
				}
			}
			
			System.out.println("Query consists of: ");
			for (String filter : filterWithEmoticons) {
				System.out.println(filter);
			}
			
			return filterWithEmoticons;
		}
		else {
			return emoticons;
		}
	}
	
	public void filterUntil(int numOfTweets, String... filterString) throws InterruptedException {
		this.tweetCleaner.addBlockedWords(filterString);
		super.filterUntil(numOfTweets, filterString);
		this.tweetCleaner.removeTemporaryBlockedWords();
	}
	
	public void addChangeListener (ChangeListener changeListener) {
		while (this.firingEvent.get()) {
			
		}
		this.changeListeners.add(changeListener);
	}
	
	public void removeChangeListener (ChangeListener changeListener) {
		while (this.firingEvent.get()) {
			
		}
 		this.changeListeners.remove(changeListener);
	}
	
	private void fireChangeEvent (ChangeEvent evt) {
		//System.out.println("On change!");
		this.firingEvent.set(true);
		for (ChangeListener change : changeListeners) {
			change.stateChanged(evt);
		}
		this.firingEvent.set(false);
	}
}
