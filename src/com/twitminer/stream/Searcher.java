package com.twitminer.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.event.TaskFinishEvent;
import com.twitminer.event.listener.TaskFinishEventListener;
import com.twitminer.util.TweetCleaner;

public class Searcher {

	private TweetCleaner tweetCleaner;
	private Twitter twitterInstance;
	
	private List<ChangeListener> changeListeners;
	private List<TaskFinishEventListener> finishListeners;
	
	private List<Long> skimmedTweetIds;
	
	private int tweetCounter = 0;
	private int tweetsRequested = -1;
	private int previousTweetCounterValue = 0;
	
	private int curEmotion = EmotionDAO.HAPPY;
	
	private TokenizedTweetDAO tweetDAO;
	
	private AtomicBoolean firingEvent = new AtomicBoolean(false);
	
	private Searcher(TokenizedTweetDAO storage) {
		this.tweetDAO = storage;
		this.changeListeners = new ArrayList<ChangeListener>();
		this.finishListeners = new ArrayList<TaskFinishEventListener>();
		this.skimmedTweetIds = new ArrayList<Long>();
	}
	
	public Searcher(TokenizedTweetDAO storage, TweetCleaner cleaner, Twitter twittrInstance) {
		this(storage);
		if (cleaner == null) {
			this.tweetCleaner = new TweetCleaner();
		}
		else {
			this.tweetCleaner = cleaner;			
		}
		
		if (twittrInstance == null) {
			this.twitterInstance = new TwitterFactory().getInstance();
		}
		else {
			this.twitterInstance = twittrInstance;
		}
	}
	
	public static void main(String[] args) {
		Searcher searcher = new Searcher(DAOFactory.getInstance(DAOFactory.ARRAY_LIST).getTokenizedTweetDAO(), new TweetCleaner(), new TwitterFactory().getInstance());
		searcher.searchWithEmotionUntil(500, EmotionDAO.SAD, "basketball");
	}
	
	public void searchWithEmotionUntil(int requestedTweets, int emotionId, String... filterString) {
		String queryString = this.appendEmoticonsToListOfKeywords(emotionId, filterString);
		
		this.searchUntil(requestedTweets, queryString);
	}
	
	/**Inserts OR operators in between filter strings
	 * 
	 * @param filterString A list of filter strings for search
	 * @throws TwitterException twitter can't search (internet connection issue?)
	 */
	public void searchUntil(int requestedTweets, String filterString) {
		this.tweetsRequested = requestedTweets;
		
		Query query = querify(filterString);
		
		final int RPP = 100;
		
		query.setLang("en");
		query.setRpp(RPP);
		
		if (tweetsRequested <= 0) {
			tweetsRequested = 100;
		}
		
		int page = 1;
		boolean noMoreResults = false;
		
		try {
			while (tweetCounter < tweetsRequested && !noMoreResults) {
				QueryResult qr = twitterInstance.search(query);
			
				System.out.println("Currently in page number " + qr.getPage());
				
				for (Tweet tweet : qr.getTweets()) {
					if (qr.getPage() != page) {
						System.err.println("For whatever reason, real page number doesn't match with requested page number.");
					}
					
					if (qr.getTweets().size() == 0 && !noMoreResults) {
						noMoreResults = true;
						break;
					}
					
					if (tweetsRequested <= 0 || tweetCounter >= tweetsRequested) {
						break;
					}
					
					if (tweet != null) {
						if (this.skimmedTweetIds.contains(tweet.getId())) {
							continue;
						}
						
						this.skimmedTweetIds.add(tweet.getId());
						
						List<String> tokenizedTweet = tweetCleaner.tokenizeAndCleanTweet(tweet);
						
						if (tokenizedTweet != null && !tokenizedTweet.isEmpty()) {
							tokenizedTweet.remove("");
							
							TokenizedTweet tokenized = new TokenizedTweet(tokenizedTweet, curEmotion);
							
							this.tweetDAO.insertTweet(tokenized);
							
							tweetCounter++;
							
							fireChangeEvent(new ChangeEvent(Integer.toString(tweetCounter)));
							System.out.println("Number of scanned tweets so far: " + tweetCounter);
						}
					}
					
					//System.out.println(tweet.getId() + ": " + tweet.getText());
				}
				
				//System.out.println("No more results? " + noMoreResults);
				
				query.setPage(++page);
			}
		} catch (TwitterException e) {
			if (e.exceededRateLimitation()) {
				System.err.println("Exceeded rate limit.");
			}
			
			e.printStackTrace();
		}
		finally {
			System.out.println("Tweets gathered: " + this.tweetCounter);
			this.previousTweetCounterValue = this.tweetCounter;
			this.clearTweetCounter();
			this.clearNumberOfTweetsRequested();
			fireTaskFinishedListener(new TaskFinishEvent(this));
		}
	}
	
	public int getNumberOfScannedTweets() {
		return this.tweetCounter;
	}
	
	/**Converts a set of filter strings to OR query
	 * 
	 * @param filterString The set of filter strings that will be OR'd
	 * @return A Query object for searching
	 */
	private Query querify(String filterString) {
		System.out.println("Query: " + filterString);
		
		return new Query(filterString);
	}
	
	private String appendEmoticonsToListOfKeywords(int emotionId, String... filterString) {
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		EmoticonDAO emo = daos.getEmoticonDAO();
		List<String> emoticons = emo.getEmoticonStringsByEmotion(emotionId);
		this.curEmotion = emotionId;
		
		this.tweetCleaner.addBlockedWords(filterString);
		
		StringBuilder sb = new StringBuilder();
		
		if (filterString != null && filterString.length != 0) {
			if (filterString.length > 1) {
				sb.append(filterString);
			}
			else {
				for (int i = 0; i < filterString.length; i++) {
					if (i == 0) {
						sb.append('(');
					}
					
					sb.append(filterString[i]);
					
					if (i < filterString.length - 1) {
						sb.append(" OR ");
					}				
					else {
						sb.append(')');
					}
				}
			}
			
			sb.append(' ');
		}
		
		if (emoticons.size() == 1) {
			sb.append(emoticons.get(0));
		}
		else {
			for (int i = 0; i < emoticons.size(); i++) {
				if (i == 0) {
					sb.append("( ");
				}
				
				sb.append(emoticons.get(i));
				
				if (i < emoticons.size() - 1) {
					sb.append(" OR ");
				}				
				else {
					sb.append(" )");
				}
			}
		}
		
		return sb.toString();
	}
	
	public int getTweetCounter() {
		return tweetCounter;
	}
	
	private void clearTweetCounter() {
		this.tweetCounter = 0;
	}
	
	public int getPreviousTweetCounter() {
		return this.previousTweetCounterValue;
	}
	
	private void clearNumberOfTweetsRequested() {
		this.tweetsRequested = -1;
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
	
	public void addTaskFinishListener (TaskFinishEventListener changeListener) {
		this.finishListeners.add(changeListener);
	}
	
	public void removeTaskFinishListener (TaskFinishEventListener changeListener) {
 		this.finishListeners.remove(changeListener);
	}
	
	private void fireTaskFinishedListener (TaskFinishEvent evt) {
		//System.out.println("On change!");
		this.firingEvent.set(true);
		for (TaskFinishEventListener change : finishListeners) {
			change.onTaskFinished(evt);
		}
		this.firingEvent.set(false);
	}
	
}
