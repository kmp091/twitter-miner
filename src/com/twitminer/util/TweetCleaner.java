package com.twitminer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import com.twitminer.beans.Emoticon;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;

import edu.northwestern.at.utils.MutableInteger;
import edu.northwestern.at.utils.ScoredString;
import edu.northwestern.at.utils.corpuslinguistics.adornedword.AdornedWord;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizer;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.Lemmatizer;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.LemmatizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.lexicon.DefaultWordLexicon;
import edu.northwestern.at.utils.corpuslinguistics.lexicon.Lexicon;
import edu.northwestern.at.utils.corpuslinguistics.lexicon.LexiconFactory;
import edu.northwestern.at.utils.corpuslinguistics.postagger.DefaultPartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.postagger.PartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizer;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizerFactory;

public class TweetCleaner {

	List<String> stopWords;
	List<String> emoticons;
	EmoticonDAO emoticonDAO;
	
	private LanguageRecognizer lang;
	private Lemmatizer lemmatizer;
	
	private PartOfSpeechTagger posTagger;
	private Lexicon englishLexicon;
	
	//useless parts of speech (refer to MorphAdorner's documentation, page 89 on its PDF)
	String[] uselessPOS;
	
	public TweetCleaner() {
		//load List<String> of stop words
		try {
			posTagger = new DefaultPartOfSpeechTagger();
		} catch (Exception e) {
			System.out.println("Error loading POS tagger");
			e.printStackTrace();
		}
		
		englishLexicon = posTagger.getLexicon();
		
		DAOFactory daos = DAOFactory.getInstance(DAOFactory.ARRAY_LIST);
		stopWords = daos.getStopWordDAO().getStopWordStrings();
		emoticonDAO = daos.getEmoticonDAO();
		emoticons = emoticonDAO.getEmoticonStrings();
		lang = new LanguageRecognizerFactory().newLanguageRecognizer();
		lemmatizer = new LemmatizerFactory().newLemmatizer();
		
		uselessPOS = new String[]{"av", "av-c", "av-d", "av-dc", "av-ds", "av-dx", "av-j", "av-jc",
				"av-jn", "av-js", "av-n1", "av-s", "avs-jn", "av-vvg", "av-vvn", "av-x", "c-acp",
				"cc", "cc-acp", "c-crq", "ccx", "crd", "cs", "cst", "d", "dc", "dg", "ds", "dt",
				"dx", "fw-fr", "fw-ge", "fw-gr", "fw-it", "fw-la", "fw-mi", "n2", "n-vdg", "n-vhg",
				"ord", "pi", "pc-acp", "pi2", "pi2x", "pig", "pigx", "pix", "pn22", "pn31", "png11",
				"png12", "png21", "png22", "png31", "png32", "pno11", "pno12", "pno21", "pno31",
				"pno32", "pns11", "pns12", "pns21", "pns31", "pns32", "po11", "po12", "po21", "po22",
				"po31", "po32", "pp", "pp-f", "px11", "px12", "px21", "px22", "px31", "px32", "pxg21",
				"q-crq", "r-crq", "sy", "uh", "uh-av", "uh-crq", "uh-dx", "uh-j", "uh-jn", "uh-n", "uh-v",
				"vb2", "vb2-imp", "vb2x", "vbb", "vbbx", "vbd", "vbd2", "vbd2x", "vbdp", "vbdx", "vbg",
				"vbi", "vbm", "vbmx", "vbn", "vbp", "vbz", "vbzx", "vd2", "vd2-imp", "vd2x", "vdb", "vdbx",
				"vdd", "vdd2", "vdd2x", "vddp", "vddx", "vdg", "vdi", "vdn", "vdp", "vdz", "vdzx", "vh2",
				"vh2-imp", "vh2x", "vhb", "vhbx", "vhd", "vhd2", "vhdb", "vhdx", "vhg", "vhi", "vhn", "vhp",
				"vhz", "vhzx", "vm2", "vm2x", "vmb", "vmb1", "vmbx", "vmd", "vmd2", "vmd2x", "vmdp",
				"vmdx", "vmi", "vmn", "vmp", "negative"};
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
		tweetText = tweetText.replaceAll("rt", " ");
		
		//remove symbols and basic :\w (colon followed by a word) emoticons
		tweetText = tweetText.replaceAll("(:[^\\s]+)", " ");
		
		tweetText = tweetText.replaceAll("[^a-zA-Z0-9\\s']", " ");
		
		//contractions
		tweetText = tweetText.replaceAll("'re", " are");
		tweetText = tweetText.replaceAll("n't", " not");
		tweetText = tweetText.replaceAll("'nt", " not");
		tweetText = tweetText.replaceAll("g'", "good ");
		tweetText = tweetText.replaceAll("'ve", " have");
		tweetText = tweetText.replaceAll("'s", " is");
		tweetText = tweetText.replaceAll("'d", "would");
		tweetText = tweetText.replaceAll("'", " ");
		
		//numbers (ordinals)
		tweetText = tweetText.replaceAll("\\d+(th|nd|st)", " ");
		
		//numbers
		tweetText = tweetText.replaceAll("\\d+", " ");
		
		//remove vowels that occur more than twice (and convert to just two)
		tweetText = tweetText.replaceAll("aa(a+)", "aa");
		tweetText = tweetText.replaceAll("bb(b+)", "bb");
		tweetText = tweetText.replaceAll("cc(c+)", "cc");
		tweetText = tweetText.replaceAll("dd(d+)", "dd");
		tweetText = tweetText.replaceAll("ee(e+)", "ee");
		tweetText = tweetText.replaceAll("ff(f+)", "ff");
		tweetText = tweetText.replaceAll("gg(g+)", "gg");
		tweetText = tweetText.replaceAll("hh(h+)", "hh");
		tweetText = tweetText.replaceAll("ii(i+)", "ii");
		tweetText = tweetText.replaceAll("jj(j+)", "jj");
		tweetText = tweetText.replaceAll("kk(k+)", "kk");
		tweetText = tweetText.replaceAll("ll(l+)", "ll");
		tweetText = tweetText.replaceAll("mm(m+)", "mm");
		tweetText = tweetText.replaceAll("nn(n+)", "nn");
		tweetText = tweetText.replaceAll("oo(o+)", "oo");
		tweetText = tweetText.replaceAll("pp(p+)", "pp");
		tweetText = tweetText.replaceAll("qq(q+)", "qq");
		tweetText = tweetText.replaceAll("rr(r+)", "rr");
		tweetText = tweetText.replaceAll("ss(s+)", "ss");
		tweetText = tweetText.replaceAll("tt(t+)", "tt");
		tweetText = tweetText.replaceAll("uu(u+)", "uu");
		tweetText = tweetText.replaceAll("vv(v+)", "vv");
		tweetText = tweetText.replaceAll("ww(w+)", "ww");
		tweetText = tweetText.replaceAll("xx(x+)", "xx");
		tweetText = tweetText.replaceAll("yy(y+)", "yy");
		tweetText = tweetText.replaceAll("zz(z+)", "zz");
		
		String lowerCasedTweet = tweetText.toLowerCase();
		
		//System.out.println(lowerCasedTweet);
		
		//initialize tokenizer
		WordTokenizer tokenizer = new WordTokenizerFactory().newWordTokenizer();
		List<String> tokens = tokenizer.extractWords(lowerCasedTweet);
		tokenizer.close();
		
		Iterator<String> it = tokens.listIterator();
		List<Emoticon> emoticonsInTweet = new ArrayList<Emoticon>();
		while (it.hasNext()) {
			String cur = it.next();
			if (stopWords.contains(cur) || Pattern.matches("'", cur) || cur.length() == 1) {
				it.remove();
			}
			else if (emoticons.contains(cur)) {
				Emoticon emoObj = emoticonDAO.getEmoticonByString(cur);
				if (emoObj != null) {
					emoticonsInTweet.add(emoObj);
				}
				else {
					System.err.println("I don't think this was supposed to happen.");
				}
			}
		}
		
		if (!compareEmoticons(emoticonsInTweet)) {
			//a tweet expressing different sentiments is pretty confusing
			return null;
		}

		int threshold = tokens.size() / 4;
		int nonEnglish = 0;
		
//		StringBuilder cleanedTweet = new StringBuilder();
		
		List<String> cleanedTokens = new ArrayList<String>();
		
		for (String token : tokens) {
			System.out.println("Token: " + token);
			
			//if (!stopWords.contains(token) && !emoticons.contains(token) && !Pattern.matches("'", token)) {
				if (Pattern.matches("'\\w+", token)) {
					token = token.substring(1);
				}
				
				boolean english = isEnglish(token);
				
				if (english) {
					if (!lemmatizer.cantLemmatize(token)) {
						token = lemmatizer.lemmatize(token);
					}
					System.out.println("Cleaned token: " + token);
					
					cleanedTokens.add(token);					
				}
				else {
					nonEnglish++;
				}
			//}
		}
		
		if (threshold >= 2 && nonEnglish >= threshold) {
			System.out.println("Above threshold of " + threshold + " (Non-English count: " + nonEnglish + ")");
			return null;
		}
		
		if (!cleanedTokens.isEmpty()) {
			//investigate the tokens together and eliminate dumb parts of speech
			List<AdornedWord> tags = this.posTagger.tagSentence(cleanedTokens);
			
			Iterator<AdornedWord> iter = tags.iterator();
			while (iter.hasNext()) {
				AdornedWord curEntry = iter.next();
				System.out.println(curEntry.getToken() + " - " + curEntry.getPartsOfSpeech());
				
				//remove useless parts of speech
				if (this.isOneOfThePartsOfSpeech(curEntry, uselessPOS)) {
					iter.remove();
				}
				
			}
			
//			cleanedTweet.delete(0, cleanedTweet.length());
			
			String cleanedTweet = this.wordListToString(cleanedTokens);
			
			System.out.println("Yet another successful tweet: " + cleanedTweet.toString());
			
			return cleanedTweet.toString();
		}
		
		return null;
	}
	
	/**
	 * Checks whether a List of Emoticon objects express the same emotion
	 * @param emoObjs A collection of Emoticon objects
	 * @return true if all the emoticons express the same sentiment. false otherwise.
	 */
	private boolean compareEmoticons(List<Emoticon> emoObjs) {
		int emoId = -1;
		
		for (Emoticon emoObj : emoObjs) {
			if (emoId == -1) {
				emoId = emoObj.getEmotionId();
			}
			else if (emoObj.getEmotionId() != emoId) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isEnglish(String word) {
		
		return englishLexicon.containsEntry(word);
		
	}
	
	private boolean isStatisticallyEnglish(String token) {
		ScoredString[] langCandidates = lang.recognizeLanguage(token);
		boolean english = false;
		
		for (ScoredString scoreString : langCandidates) {
			if (scoreString.getString().equalsIgnoreCase("english")) {
				english = true;
				System.out.println("English certainty: " + scoreString.getScore());
			}
		}
		
		return english;
	}
	
	private String attemptSpellCheck(String token) {
		return null;
	}
	
	private String wordListToString(List<String> words) {
		StringBuilder stringify = new StringBuilder();
		
		for (String word : words) {
			stringify.append(word).append(' ');
		}
		
		return stringify.toString();
	}
	
	private boolean isOneOfThePartsOfSpeech(AdornedWord word, String[] partsOfSpeech) {
		String pos = word.getPartsOfSpeech();
		String[] delimitedPos = pos.split("|");
		
		for (String pospeech : partsOfSpeech) {
			for (String onePos : delimitedPos) {
				if (onePos.equalsIgnoreCase(pospeech)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
