package com.twitminer.beans;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizerFactory;

public class TokenizedTweet {

	private Set<String> tokens;
	private int emotion;
	
	public TokenizedTweet(Tweet tweet) {
		List<String> wordArray = new WordTokenizerFactory().newWordTokenizer().extractWords(tweet.getText());
		
		Iterator<String> it = wordArray.iterator();
		while (it.hasNext()) {
			String word = it.next();
			
			if (word.isEmpty() || word.equalsIgnoreCase("\n") || word.contains("\n")) {
				it.remove();
			}
		}
		
		tokens = new HashSet<String>(wordArray);
		emotion = tweet.getEmotionId();
	}
	
	public TokenizedTweet(Collection<String> tokens) {
		if (tokens.contains("")) {
			tokens.remove("");
		}
		
		this.tokens = new HashSet<String> (tokens);
		emotion = -1;
	}
	
	public TokenizedTweet(Collection<String> tokens, int emotionId) {
		this(tokens);
		this.emotion = emotionId;
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = this.tokens.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			
			if (it.hasNext()) {
				sb.append("(space)");
			}
		}
		
		return sb.toString();
	}
	
}
