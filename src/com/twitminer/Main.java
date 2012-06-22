package com.twitminer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.twitminer.dao.EmotionDAO;
import com.twitminer.stream.Streamer;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Main {

	private static final String CONSUMER_KEY = "RTgaSgTvDS1CPbj4rGJKg";
	private static final String CONSUMER_SECRET = "SqYNOYlaJT7RwhzeaJaHqjrxFlAYOcndxqR2MJOU";
	
	private static final String TOKEN_PREFS_KEY = "token";
	private static final String TOKEN_SECRET_PREFS_KEY = "tokenSecret";
	
	public static void main(String[] args) throws BackingStoreException {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		//prefs.clear();
		
		try {
			Twitter twitterInstance = new TwitterFactory().getInstance();
			
			twitterInstance.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			
			AccessToken accToken = getAccessTokenFromPrefs(prefs);
			
			if (accToken == null) {
				//get a request token and ask user to authenticate
				RequestToken reqToken = twitterInstance.getOAuthRequestToken();
				
				while (accToken == null) {
					//while access token is unavailable, keep asking user to enter a PIN/go to authentication URL
					BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
					
					System.out.println("Please log in to your account and enter the PIN provided.");
					System.out.println("Log in with this URL: " + reqToken.getAuthorizationURL());
					System.out.println("Kindly type in the provided PIN here (or just press Enter if not provided): ");
					String pin = buf.readLine();
					System.out.println(pin + " was typed.");
					
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
						}
						else {
							e.printStackTrace();
							System.out.println("An error occurred. Terminating...");
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
			Streamer stream = new Streamer(CONSUMER_KEY, CONSUMER_SECRET, accToken);
			
			//filter stream with an emotion
			stream.filterAndAnnotateUntil(2000, EmotionDAO.HAPPY);
			stream.filterAndAnnotateUntil(2000, EmotionDAO.DISGUST);
			stream.filterAndAnnotateUntil(2000, EmotionDAO.SAD);
			stream.filterAndAnnotateUntil(2000, EmotionDAO.SURPRISE);
		}
		catch (TwitterException e) {
			System.out.println("You may not be connected to the Internet, or a network error occurred. Please try running the application again.");
		}
		catch (IOException e) {
			System.out.println("Unexpected I/O error occurred.");
		}
		
	}
	
	private static AccessToken getAccessTokenFromPrefs(Preferences prefs) {
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
	
	private static void storeAccessToken (AccessToken token, Preferences prefs) {
		//store token and token secret to prefs
		prefs.put(TOKEN_PREFS_KEY, token.getToken());
		prefs.put(TOKEN_SECRET_PREFS_KEY, token.getTokenSecret());
	}
	
}
