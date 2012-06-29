package com.twitminer.util;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import com.twitminer.dao.DAOFactory;

import edu.northwestern.at.utils.ScoredString;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizer;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.Lemmatizer;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.LemmatizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizer;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizerFactory;

public class TweetCleaner {

	List<String> stopWords;
	List<String> emoticons;
	
	private LanguageRecognizer lang;
	private Lemmatizer lemmatizer;
	
	public TweetCleaner() {
		//load List<String> of stop words
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.MYSQL);
		stopWords = daos.getStopWordDAO().getStopWordStrings();
		emoticons = daos.getEmoticonDAO().getEmoticonStrings();
		lang = new LanguageRecognizerFactory().newLanguageRecognizer();
		lemmatizer = new LemmatizerFactory().newLemmatizer();
	}
	
	public String tokenizeAndCleanTweet(Status tweet) {
		String tweetText = tweet.getText();
		
		//remove hashtags
		HashtagEntity[] hashtags = tweet.getHashtagEntities();
		for (HashtagEntity hashtag : hashtags) {
			tweetText = tweetText.replaceAll("#" + hashtag.getText(), " ");
		}
		
		URLEntity[] urls = tweet.getURLEntities();
		for (URLEntity url : urls) {
			System.out.println("URL: " + url.toString());
			tweetText = tweetText.replaceAll(url.getDisplayURL(), " ");
			tweetText = tweetText.replaceAll(url.getURL().toString(), " ");
			tweetText = tweetText.replaceAll(url.getExpandedURL().toString(), " ");
		}
		
		UserMentionEntity[] mentions = tweet.getUserMentionEntities();
		for (UserMentionEntity mention : mentions) {
			tweetText = tweetText.replaceAll("@" + mention.getScreenName(), " ");
		}
		
		//remove RT keyword
		tweetText = tweetText.replaceAll("RT : ", " ");
		tweetText = tweetText.replaceAll("RT", " ");
		
		//remove symbols and basic :\w (colon followed by a word) emoticons
		tweetText = tweetText.replaceAll("(:[^\\s]+)", " ");
		tweetText = tweetText.replaceAll("[^a-zA-Z0-9\\s']", " ");
		
		//remove vowels that occur more than twice (and convert to just two)
		tweetText = tweetText.replaceAll("aa(a+)", "aa");
		tweetText = tweetText.replaceAll("ee(e+)", "ee");
		tweetText = tweetText.replaceAll("ii(i+)", "ii");
		tweetText = tweetText.replaceAll("oo(o+)", "oo");
		tweetText = tweetText.replaceAll("uu(u+)", "uu");
		
		String lowerCasedTweet = tweetText.toLowerCase();
		
		//System.out.println(lowerCasedTweet);
		
		//initialize tokenizer
		WordTokenizer tokenizer = new WordTokenizerFactory().newWordTokenizer();
		List<String> tokens = tokenizer.extractWords(lowerCasedTweet);
		tokenizer.close();
		
		Iterator<String> it = tokens.listIterator();
		while (it.hasNext()) {
			String cur = it.next();
			if (stopWords.contains(cur) || emoticons.contains(cur) || Pattern.matches("'", cur)) {
				it.remove();
			}
		}
		int threshold = tokens.size() / 4;
		int nonEnglish = 0;
		
		StringBuilder cleanedTweet = new StringBuilder();
		
		for (String token : tokens) {
			System.out.println("Token: " + token);
			
			//if (!stopWords.contains(token) && !emoticons.contains(token) && !Pattern.matches("'", token)) {
				if (Pattern.matches("'\\w+", token)) {
					token = token.substring(1);
				}
				
				ScoredString[] langCandidates = lang.recognizeLanguage(token);
				boolean english = false;
				
				for (ScoredString scoreString : langCandidates) {
					if (scoreString.getString().equalsIgnoreCase("english")) {
						english = true;
					}
				}
				
				if (english) {
					if (!lemmatizer.cantLemmatize(token)) {
						token = lemmatizer.lemmatize(token);
					}
					System.out.println("Cleaned token: " + token);
					
					cleanedTweet.append(token);
					cleanedTweet.append(" ");					
				}
				else {
					nonEnglish++;
				}
			//}
		}
		
		System.out.println("Cleared tweet: " + cleanedTweet.toString());
		
		if (threshold >= 2 && nonEnglish >= threshold) {
			System.out.println("Above threshold of " + threshold + " (Non-English count: " + nonEnglish + ")");
			return null;
		}
		
		return cleanedTweet.toString();
	}
	
}
