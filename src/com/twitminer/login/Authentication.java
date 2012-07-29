package com.twitminer.login;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import com.twitminer.event.AuthorizationEvent;
import com.twitminer.event.listener.AuthorizationEventListener;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Authentication {
	
	public static final String CONSUMER_KEY = "RTgaSgTvDS1CPbj4rGJKg";
	public static final String CONSUMER_SECRET = "SqYNOYlaJT7RwhzeaJaHqjrxFlAYOcndxqR2MJOU";

	public static final String TOKEN_PREFS_KEY = "token";
	public static final String TOKEN_SECRET_PREFS_KEY = "tokenSecret";
	
	private static List<AuthorizationEventListener> listeners = new ArrayList<AuthorizationEventListener>();
	;
	
	
	public Authentication() {
		// TODO Auto-generated constructor stub
	}

	protected static AccessToken getAccessTokenFromPrefs(Preferences prefs) {
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
	
	public static Twitter setUpTwitterInstance(Twitter twitterInstance) throws TwitterException {
		twitterInstance.setOAuthConsumer(Authentication.CONSUMER_KEY, Authentication.CONSUMER_SECRET);
		
		Preferences prefs = Preferences.userNodeForPackage(Authentication.class);
		
		/*try {
			prefs.clear();
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		AccessToken accToken = getAccessTokenFromPrefs(prefs);
		
		if (accToken == null) {
			System.out.println("No access token from preferences");
			
			//get a request token and ask user to authenticate
			RequestToken reqToken = twitterInstance.getOAuthRequestToken();
			
			while (accToken == null) {
				
				fireOnAuthorizationRequest(reqToken);
/*					
				//while access token is unavailable, keep asking user to enter a PIN/go to authentication URL
				BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
				
				System.out.println("Please log in to your account and enter the PIN provided.");
				System.out.println("Log in with this URL: " + reqToken.getAuthorizationURL());
				System.out.println("Kindly type in the provided PIN here (or just press Enter if not provided): ");
				String pin = buf.readLine();
				System.out.println(pin + " was typed.");
*/
				AuthLogic auth = AuthLogicFactory.getInstance(reqToken);
				
				String pin = auth.getAuthenticationToken();
				
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
						JOptionPane.showMessageDialog(null, "An error of a general nature occurred. The application will exit.");
						System.exit(1);
					}
				}
			}
			
			//at this point, the access token is valid
			//store the access token and token secret for future use
			storeAccessToken(accToken, prefs);
			
		}

		//set access token
		twitterInstance.setOAuthAccessToken(accToken);
		
		return twitterInstance;
	}
	
	public static void addOnAuthorizationRequestListener(AuthorizationEventListener listener) {
		listeners.add(listener);
	}
	
	public static void removeOnAuthorizationRequestListener(AuthorizationEventListener listener) {
		listeners.remove(listener);
	}
	
	private static void fireOnAuthorizationRequest (RequestToken request) {
		for (AuthorizationEventListener auth : listeners) {
			auth.OnAuthorizeRequest(new AuthorizationEvent(AuthorizationEvent.class, request));
		}
	}

}
