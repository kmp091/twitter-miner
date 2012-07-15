package com.twitminer.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.twitminer.beans.Tweet;

public class TokenizedTweet {

	private Set<String> tokens;
	private int emotion;
	
	public TokenizedTweet(Tweet tweet) {
		List<String> wordArray = new LinkedList<String>(Arrays.asList(tweet.getText().split(" ")));
		
		Iterator<String> it = wordArray.iterator();
		while (it.hasNext()) {
			String word = it.next();
			
			if (word.isEmpty() || word.equalsIgnoreCase("\n")) {
				it.remove();
			}
		}
		
		tokens = new HashSet<String>(wordArray);
		emotion = tweet.getEmotionId();
	}
	
	/**Get all tokens of a tweet
	 * 
	 * @return A Set of the tokens of a tweet
	 */
	public Set<String> getSetOfTokens() {
		return this.tokens;
	}
	
	/**Get all tokens of a tweet
	 * 
	 * @return An array of tokens
	 */
	public String[] getArrayOfTokens() {
		Collection<String> tokens = getSetOfTokens();
		return tokens.toArray(new String[tokens.size()]);
	}
	
	/**Ascertains whether a specified word is contained in
	 * a tokenized tweet
	 * 
	 * @param word The word to be found in the tweet
	 * @return whether the word is in the tweet or not
	 */
	public boolean containsWord(String word) {
		return tokens.contains(word);
	}
	
	/**Get the annotated emotion of the tweet
	 * 
	 * @return annotated emotion number of the tweet
	 */
	public int getEmotionID() {
		return this.emotion;
	}
	
}
