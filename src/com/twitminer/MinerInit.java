package com.twitminer;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;*/
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;
import com.twitminer.event.AuthorizationEvent;
import com.twitminer.event.AuthorizationInputEvent;
import com.twitminer.event.TaskFinishEvent;
import com.twitminer.event.listener.AuthorizationEventListener;
import com.twitminer.event.listener.AuthorizationInputEventListener;
import com.twitminer.event.listener.TaskFinishEventListener;
import com.twitminer.stream.Streamer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MinerInit {

	private final String CONSUMER_KEY = "RTgaSgTvDS1CPbj4rGJKg";
	private final String CONSUMER_SECRET = "SqYNOYlaJT7RwhzeaJaHqjrxFlAYOcndxqR2MJOU";
	
	private final String TOKEN_PREFS_KEY = "token";
	private final String TOKEN_SECRET_PREFS_KEY = "tokenSecret";
	
	private List<AuthorizationEventListener> listeners;
	private List<AuthorizationInputEventListener> authListeners;
	private List<ChangeListener> changeListeners;
	private List<TaskFinishEventListener> finishListeners;
	
	private Streamer stream;
	
	public static final int MAX_TWEETS = 2000;
	
	public MinerInit () {
		listeners = new ArrayList<AuthorizationEventListener>();
		authListeners = new ArrayList<AuthorizationInputEventListener>();
		finishListeners = new ArrayList<TaskFinishEventListener>();
		changeListeners = new ArrayList<ChangeListener>();
	}
	
	
	public void initialize() {
		
		try {
			Preferences prefs = Preferences.userNodeForPackage(MinerInit.class);
			//prefs.clear();
			
			Twitter twitterInstance = new TwitterFactory().getInstance();
			
			twitterInstance.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			
			AccessToken accToken = getAccessTokenFromPrefs(prefs);
			
			if (accToken == null) {
				//get a request token and ask user to authenticate
				RequestToken reqToken = twitterInstance.getOAuthRequestToken();
				
				while (accToken == null) {
					
					this.fireOnAuthorizationRequest(reqToken);
/*					
					//while access token is unavailable, keep asking user to enter a PIN/go to authentication URL
					BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
					
					System.out.println("Please log in to your account and enter the PIN provided.");
					System.out.println("Log in with this URL: " + reqToken.getAuthorizationURL());
					System.out.println("Kindly type in the provided PIN here (or just press Enter if not provided): ");
					String pin = buf.readLine();
					System.out.println(pin + " was typed.");
*/
					AuthLogic auth = new AuthLogic(reqToken);
					
					String pin = auth.getAuthToken();
					
					System.out.println(pin);
					
					try {
						if (pin.length() > 0) {
							accToken = twitterInstance.getOAuthAccessToken(reqToken, pin);
						}
						else {
							accToken = twitterInstance.getOAuthAccessToken();
						}
					}
					catch (TwitterException e) {
						if (e.getStatusCode() == 401) {
							System.out.println("The access token was not acquired. Did you put in the pin correctly?");
							JOptionPane.showMessageDialog(null, "The access token was not acquired. Did you put in the number code correctly? Please try again.", "Incorrect number code", JOptionPane.WARNING_MESSAGE);
						}
						else {
							e.printStackTrace();
							System.out.println("An error occurred. Terminating...");
							JOptionPane.showMessageDialog(null, "An error of a general nature occurred.");
							return;
						}
					}
				}
				
				//at this point, the access token is valid
				//store the access token and token secret for future use
				storeAccessToken(accToken, prefs);
			}
			
			//set access token
			twitterInstance.setOAuthAccessToken(accToken);
			
			//start mining
			stream = new Streamer(CONSUMER_KEY, CONSUMER_SECRET, accToken);
			
			this.fireOnAuthorization(new AuthorizationInputEvent(this));
		}
		/*catch (BackingStoreException e) {
			e.printStackTrace();
		}*/
		catch (TwitterException e) {
			JOptionPane.showMessageDialog(null, "You may not be connected to the Internet, or a network error occurred.");
			System.out.println("You may not be connected to the Internet, or a network error occurred. Please try running the application again.");
		}
		/*catch (IOException e) {
			System.out.println("Unexpected I/O error occurred.");
		}*/
		
	}
	
	public void startStream() {
		Thread streamPrinterThread = new Thread(new Runnable() {
			public void run() {
				ChangeListener happyChange = new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						System.out.println("Happy!");
						fireChange("Getting happy tweets ะส" + e.getSource().toString() + " out of " + MAX_TWEETS);
					}				
					
				};
				
				ChangeListener disgustChange = new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						fireChange("Getting disgusted tweets ะส" + e.getSource().toString() + " out of " + MAX_TWEETS);
					}				
					
				};
				
				ChangeListener sadChange = new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						fireChange("Getting sad tweets ะส" + e.getSource().toString() + " out of " + MAX_TWEETS);
					}				
					
				};
				
				ChangeListener surpriseChange = new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						fireChange("Getting surprised tweets ะส" + e.getSource().toString() + " out of " + MAX_TWEETS);
					}				
					
				};
				
				//filter stream with an emotion
				stream.addChangeListener(happyChange);
				stream.filterAndAnnotateUntil(MAX_TWEETS, EmotionDAO.HAPPY);
				stream.removeChangeListener(happyChange);
				stream.addChangeListener(disgustChange);
				stream.filterAndAnnotateUntil(MAX_TWEETS, EmotionDAO.DISGUST);
				stream.removeChangeListener(disgustChange);
				stream.addChangeListener(sadChange);
				stream.filterAndAnnotateUntil(MAX_TWEETS, EmotionDAO.SAD);
				stream.removeChangeListener(sadChange);
				stream.addChangeListener(surpriseChange);
				stream.filterAndAnnotateUntil(MAX_TWEETS, EmotionDAO.SURPRISE);
				stream.removeChangeListener(surpriseChange);
				
				fireFinish();
			}
			
			private void fireChange(String description) {
				fireOnChange(new ChangeEvent(description));
			}
			
			private void fireFinish() {
				fireOnFinish(new TaskFinishEvent(this));
			}
		});
		
		streamPrinterThread.start();
	}
	
	private void fireOnAuthorizationRequest (RequestToken request) {
		for (AuthorizationEventListener auth : listeners) {
			auth.OnAuthorizeRequest(new AuthorizationEvent(this, request));
		}
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
	
	private AccessToken getAccessTokenFromPrefs(Preferences prefs) {
		//retrieve access token if exists
		String token = prefs.get(TOKEN_PREFS_KEY, null);
		String tokenSecret = prefs.get(TOKEN_SECRET_PREFS_KEY, null);
		
		if (token == null) {
			return null;
		}
		else {
			return new AccessToken(token, tokenSecret);
		}
	}
	
	private void storeAccessToken (AccessToken token, Preferences prefs) {
		//store token and token secret to prefs
		prefs.put(TOKEN_PREFS_KEY, token.getToken());
		prefs.put(TOKEN_SECRET_PREFS_KEY, token.getTokenSecret());
	}
	
	public List<Tweet> getTweets () {
		if (stream.getTweetDAO() == null) {
			return null;
		}
		return stream.getTweetDAO().getTweets();
	}
	
}
