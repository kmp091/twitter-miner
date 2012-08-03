package com.twitminer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.dao.TokenizedTweetDAO;
import com.twitminer.event.AuthorizationInputEvent;
import com.twitminer.event.TaskFinishEvent;
import com.twitminer.event.listener.AuthorizationInputEventListener;
import com.twitminer.event.listener.TaskFinishEventListener;
import com.twitminer.login.Authentication;
import com.twitminer.stream.Searcher;
import com.twitminer.stream.MineStreamer;
import com.twitminer.util.TweetCleaner;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class MinerInit {

	private List<AuthorizationInputEventListener> authListeners;
	private List<ChangeListener> changeListeners;
	private List<TaskFinishEventListener> finishListeners;
	
	private Searcher searcher;
	private MineStreamer stream;
	private TweetCleaner tweetCleaner;
	
	private TokenizedTweetDAO tokenTweetDAO;
	
	public static final int MAX_TWEETS = 100;
	public static final String TOPIC = "Harry Potter";
	
	private int tweetCount;
	private String topic;
	
	ChangeListener happyChange;	
	ChangeListener disgustChange;
	ChangeListener sadChange;
	ChangeListener surpriseChange;
	
	private int remainingHappy = MAX_TWEETS;
	private int remainingDisgust = MAX_TWEETS;
	private int remainingSad = MAX_TWEETS;
	private int remainingSurprise = MAX_TWEETS;
	
	private Twitter twitterInstance;
	
	public MinerInit(int numOfTweets, String tweetTopic) {
		this();
		this.tweetCount = numOfTweets;
		this.topic = tweetTopic;
	}
	
	public MinerInit () {
		this.topic = TOPIC;
		this.tweetCount = MAX_TWEETS;
		
		authListeners = new ArrayList<AuthorizationInputEventListener>();
		finishListeners = new ArrayList<TaskFinishEventListener>();
		changeListeners = new ArrayList<ChangeListener>();
		tweetCleaner = new TweetCleaner();
		
		tokenTweetDAO = DAOFactory.getInstance(DAOFactory.ARRAY_LIST).getTokenizedTweetDAO();
		
		happyChange = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				//System.out.println("Happy!");
				fireChange("Getting happy tweets ะส" + --remainingHappy + " remaining");
			}				
			
		};
		
		disgustChange = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				fireChange("Getting disgusted tweets ะส" + --remainingDisgust + " remaining");
			}				
			
		};
		
		sadChange = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				fireChange("Getting sad tweets ะส" + --remainingSad + " remaining");
			}				
			
		};
		
		surpriseChange = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				fireChange("Getting surprised tweets ะส" + --remainingSurprise + " remaining");
			}				
			
		};
	}
	
	
	public void initialize() {
		
		//try {
			twitterInstance = new TwitterFactory().getInstance();
			
			searcher = new Searcher(this.tokenTweetDAO, this.tweetCleaner, twitterInstance);
		//}
		/*catch (BackingStoreException e) {
			e.printStackTrace();
		}*/
		/*catch (IOException e) {
			System.out.println("Unexpected I/O error occurred.");
		}*/
		
	}
	
	private void initializeStreamer() throws TwitterException {
		//prefs.clear();
				
		twitterInstance = Authentication.setUpTwitterInstance(twitterInstance);
		
		//start mining
		stream = new MineStreamer(Authentication.CONSUMER_KEY, Authentication.CONSUMER_SECRET, twitterInstance.getOAuthAccessToken(), this.tokenTweetDAO, this.tweetCleaner);
		
		this.fireOnAuthorization(new AuthorizationInputEvent(this));
	}
	
	private Thread streamPrinterThread = new Thread(new Runnable() {
		public void run() {
			try {
				//filter stream with an emotion
				if (remainingHappy > 0) {
					stream.addChangeListener(happyChange);
					stream.filterAndAnnotateUntil(remainingHappy, EmotionDAO.HAPPY, TOPIC);
					stream.removeChangeListener(happyChange);
					
				}
				
				if (remainingDisgust > 0) {
					stream.addChangeListener(disgustChange);
					stream.filterAndAnnotateUntil(remainingDisgust, EmotionDAO.DISGUST, TOPIC);
					stream.removeChangeListener(disgustChange);
					
				}
				
				if (remainingSad > 0) {
					stream.addChangeListener(sadChange);
					stream.filterAndAnnotateUntil(remainingSad, EmotionDAO.SAD, TOPIC);
					stream.removeChangeListener(sadChange);

				}
				
				if (remainingSurprise > 0) {
					stream.addChangeListener(surpriseChange);
					stream.filterAndAnnotateUntil(remainingSurprise, EmotionDAO.SURPRISE, TOPIC);
					stream.removeChangeListener(surpriseChange);						
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			fireFinish();
		}
	});
	
	public void startStream() {
		streamPrinterThread.start();
	}
	
	public void waitForStreamEnd() throws InterruptedException {
		streamPrinterThread.join();
	}
	
	private Thread searchPrinterThread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				searcher.addChangeListener(happyChange);
				searcher.searchWithEmotionUntil(tweetCount, EmotionDAO.HAPPY, topic);
				searcher.removeChangeListener(happyChange);
				//remainingHappy -= searcher.getPreviousTweetCounter();
				
				searcher.addChangeListener(sadChange);
				searcher.searchWithEmotionUntil(tweetCount, EmotionDAO.SAD, topic);
				searcher.removeChangeListener(sadChange);
				//remainingSad -= searcher.getPreviousTweetCounter();
				//System.out.println("Sad tweets remaining: " + remainingSad);

				searcher.addChangeListener(disgustChange);
				searcher.searchWithEmotionUntil(tweetCount, EmotionDAO.DISGUST, topic);
				searcher.removeChangeListener(disgustChange);
				//remainingDisgust -= searcher.getPreviousTweetCounter();
				
				searcher.addTaskFinishListener(new TaskFinishEventListener() {

					@Override
					public void onTaskFinished(TaskFinishEvent evt) {
						try {
							initializeStreamer();
						} catch (TwitterException e) {
							JOptionPane.showMessageDialog(null, "You may not be connected to the Internet, or a network error occurred.");
							System.out.println("You may not be connected to the Internet, or a network error occurred. Please try running the application again.");
						}
					}
					
				});
				searcher.addChangeListener(surpriseChange);
				searcher.searchWithEmotionUntil(tweetCount, EmotionDAO.SURPRISE, topic);
				searcher.removeChangeListener(surpriseChange);
				//remainingSurprise -= searcher.getPreviousTweetCounter();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		
	});
	
	public void startSearch() {
		searchPrinterThread.start();
	}
	
	public void waitForSearchEnd() throws InterruptedException {
		searchPrinterThread.join();
	}
	
	private void fireChange(String description) {
		fireOnChange(new ChangeEvent(description));
	}
	
	private void fireFinish() {
		fireOnFinish(new TaskFinishEvent(this));
	}
	
	
	
	private void fireOnAuthorization (AuthorizationInputEvent evt) {
		for (AuthorizationInputEventListener auth: authListeners) {
			auth.onAuthorized(evt);
		}
	}
	
	private void fireOnFinish (TaskFinishEvent evt) {
		for (TaskFinishEventListener fin : finishListeners) {
			fin.onTaskFinished(evt);
		}
	}
	
	private void fireOnChange (ChangeEvent evt) {
		for (ChangeListener change : changeListeners) {
			change.stateChanged(evt);
		}
	}
	
	public void addOnAuthorizationListener (AuthorizationInputEventListener listener) {
		this.authListeners.add(listener);
	}
	
	public void removeOnAuthorizationListener (AuthorizationInputEventListener listener) {
		this.authListeners.remove(listener);
	}
	
	public void addTaskFinishListener (TaskFinishEventListener listener) {
		this.finishListeners.add(listener);
	}
	
	public void removeTaskFinishListener (TaskFinishEventListener listener) {
		this.finishListeners.remove(listener);
	}
	
	public void addChangeListener (ChangeListener change) {
		this.changeListeners.add(change);
	}
	
	public void removeChangeListener (ChangeListener change) {
		this.changeListeners.remove(change);
	}
	
	public List<TokenizedTweet> getTweets () {
		return this.tokenTweetDAO.getTweets();
	}

	public int getTweetCount() {
		return tweetCount;
	}

	public String getTopic() {
		return topic;
	}
	
}
