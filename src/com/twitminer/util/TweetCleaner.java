package com.twitminer.util;

import java.io.StringReader;
import java.util.List;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import com.twitminer.dao.DAOFactory;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class TweetCleaner {

	List<String> stopWords;
	List<String> emoticons;
	@SuppressWarnings("rawtypes")
	PTBTokenizer tokenizer;
	
	public TweetCleaner() {
		//load List<String> of stop words
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.MYSQL);
		stopWords = daos.getStopWordDAO().getStopWordStrings();
		emoticons = daos.getEmoticonDAO().getEmoticonStrings();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String tokenizeAndCleanTweet(Status tweet) {
		String tweetText = tweet.getText();
		
		//remove hashtags
		HashtagEntity[] hashtags = tweet.getHashtagEntities();
		for (HashtagEntity hashtag : hashtags) {
			tweetText = tweetText.replaceAll("#" + hashtag.getText(), "");
		}
		
		URLEntity[] urls = tweet.getURLEntities();
		for (URLEntity url : urls) {
			System.out.println("URL: " + url.toString());
			tweetText = tweetText.replaceAll(url.getDisplayURL(), "");
			tweetText = tweetText.replaceAll(url.getURL().toString(), "");
			tweetText = tweetText.replaceAll(url.getExpandedURL().toString(), "");
		}
		
		UserMentionEntity[] mentions = tweet.getUserMentionEntities();
		for (UserMentionEntity mention : mentions) {
			tweetText = tweetText.replaceAll("@" + mention.getScreenName(), "");
		}
		
		//remove RT keyword
		tweetText = tweetText.replaceAll("rt : ", "");
		tweetText = tweetText.replaceAll("rt", "");
		
		//remove symbols and basic :\w (colon followed by a word) emoticons
		tweetText = tweetText.replaceAll("[^a-zA-Z0-9\\s]", "");
		tweetText = tweetText.replaceAll("(:\\w+) | (:[^a-zA-Z0-9\\s]+)", "");
		
		//remove vowels that occur more than twice (and convert to just three to maintain emphasis)
		tweetText = tweetText.replaceAll("aa(a+)", "aaa");
		tweetText = tweetText.replaceAll("ee(e+)", "eee");
		tweetText = tweetText.replaceAll("ii(i+)", "iii");
		tweetText = tweetText.replaceAll("oo(o+)", "ooo");
		tweetText = tweetText.replaceAll("uu(u+)", "uuu");
		
		String lowerCasedTweet = tweetText.toLowerCase();
		
		//initialize tokenizer
		tokenizer = new PTBTokenizer(new StringReader(lowerCasedTweet), new CoreLabelTokenFactory(), "americanize=true," +
				"normalizeParentheses=false,invertible=true,unicodeQuotes=true,untokenizable=allDelete");

		StringBuilder cleanedTweet = new StringBuilder();
		
		for (CoreLabel label; tokenizer.hasNext(); ) {
			label = (CoreLabel)tokenizer.next();
			String value = label.value();
			
			if (!stopWords.contains(value) && !emoticons.contains(value)) {
				cleanedTweet.append(value);
				cleanedTweet.append(" ");
			}
		}
		
		System.out.println("Cleared tweet: " + cleanedTweet.toString());
		
		return cleanedTweet.toString();
	}
	
}
