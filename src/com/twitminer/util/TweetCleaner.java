package com.twitminer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.xeustechnologies.googleapi.spelling.Configuration;
import org.xeustechnologies.googleapi.spelling.Language;
import org.xeustechnologies.googleapi.spelling.SpellCheckException;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellRequest;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

import twitter4j.EntitySupport;
import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.Tweet;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

import com.twitminer.beans.Emoticon;
import com.twitminer.dao.DAOFactory;
import com.twitminer.dao.EmoticonDAO;

import edu.northwestern.at.utils.ScoredString;
import edu.northwestern.at.utils.corpuslinguistics.adornedword.AdornedWord;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizer;
import edu.northwestern.at.utils.corpuslinguistics.languagerecognizer.LanguageRecognizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.Lemmatizer;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.LemmatizerFactory;
import edu.northwestern.at.utils.corpuslinguistics.lexicon.Lexicon;
import edu.northwestern.at.utils.corpuslinguistics.postagger.DefaultPartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.postagger.PartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizer;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizerFactory;

public class TweetCleaner {

	List<String> stopWords;
	List<String> emoticons;
	EmoticonDAO emoticonDAO;
	
	List<String> temporaryStopWords;
	
	private LanguageRecognizer lang;
	private Lemmatizer lemmatizer;
	SpellChecker checker;
	
	private PartOfSpeechTagger posTagger;
	private Lexicon englishLexicon;
	
	//useless parts of speech (refer to MorphAdorner's documentation, page 89 on its PDF)
	String[] uselessPOS;
	String[] unlemmatizable;
	
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
		temporaryStopWords = new ArrayList<String>();
		emoticonDAO = daos.getEmoticonDAO();
		emoticons = emoticonDAO.getEmoticonStrings();
		lang = new LanguageRecognizerFactory().newLanguageRecognizer();
		lemmatizer = new LemmatizerFactory().newLemmatizer();
		Configuration config = new Configuration();
		//config.setProxy("proxy.dlsu.edu.ph", 80, "http");
		
		checker = new SpellChecker( config );
		checker.setLanguage( Language.ENGLISH ); // Use English (default)

		
		uselessPOS = new String[]{"av", "av-c", "av-d", "av-dc", "av-ds", /*"av-dx",*/ "av-j", "av-jc",
				"av-jn", "av-js", "av-n1", "av-s", "avs-jn", "av-vvg", "av-vvn", /*"av-x",*/ "c-acp",
				"cc", "cc-acp", "c-crq", "ccx", "crd", "cs", "cst", "d", "dc", "dg", "ds", "dt",
				"dx", "fw-fr", "fw-ge", "fw-gr", "fw-it", "fw-la", "fw-mi", "n2", "n-vdg", "n-vhg",
				"ord", "pi", "pc-acp", "p-acp", "pi2", "pi2x", "pig", "pigx", "pix", "pn22", "pn31", "png11",
				"png12", "png21", "png22", "png31", "png32", "pno11", "pno12", "pno21", "pno31",
				"pno32", "pns11", "pns12", "pns21", "pns31", "pns32", "po11", "po12", "po21", "po22",
				"po31", "po32", "pp", "pp-f", "px11", "px12", "px21", "px22", "px31", "px32", "pxg21",
				"q-crq", "r-crq", "sy", "uh", "uh-av", "uh-crq", "uh-dx", "uh-j", "uh-jn", "uh-n", "uh-v",
				"vb2", "vb2-imp", "vb2x", "vbb", "vbbx", "vbd", "vbd2", "vbd2x", "vbdp", "vbdx", "vbg",
				"vbi", "vbm", "vbmx", "vbn", "vbp", "vbz", "vbzx", "vd2", "vd2-imp", "vd2x", "vdb", "vdbx",
				"vdd", "vdd2", "vdd2x", "vddp", "vddx", "vdg", "vdi", "vdn", "vdp", "vdz", "vdzx", "vh2",
				"vh2-imp", "vh2x", "vhb", "vhbx", "vhd", "vhd2", "vhdb", "vhdx", "vhg", "vhi", "vhn", "vhp",
				"vhz", "vhzx", "vm2", "vm2x", "vmb", "vmb1", "vmbx", "vmd", "vmd2", "vmd2x", "vmdp",
				"vmdx", "vmi", "vmn", "vmp"};
		
		unlemmatizable = new String[]{"n1", "n1-j", "ng1-an", "n-jn", "njp", "np1", "np-n1", "av", "av-an",
				"av-dx", "av-jn", "av-x", "j", "j-av"};
	}
	
	public TweetCleaner(String... extraBlockedKeywords) {
		this();
		
		addBlockedWords(extraBlockedKeywords);
	}
	
	private String removeNoisyEntities (String tweetText, EntitySupport tweet) {
		//remove hashtags
		HashtagEntity[] hashtags = tweet.getHashtagEntities();
		if (hashtags != null) {
			for (HashtagEntity hashtag : hashtags) {
				tweetText = tweetText.replaceAll("#" + hashtag.getText(), " ");
			}			
		}
		
		URLEntity[] urls = tweet.getURLEntities();
		if (urls != null) {
			for (URLEntity url : urls) {
				System.out.println("URL: " + url.toString());
				tweetText = tweetText.replaceAll(url.getDisplayURL(), " ");
				tweetText = tweetText.replaceAll(url.getURL().toString(), " ");
				tweetText = tweetText.replaceAll(url.getExpandedURL().toString(), " ");
			}
		}
		
		UserMentionEntity[] mentions = tweet.getUserMentionEntities();
		if (mentions != null) {
			for (UserMentionEntity mention : mentions) {
				tweetText = tweetText.replaceAll("@" + mention.getScreenName(), " ");
			}
		}
		
		//remove RT keyword
		tweetText = tweetText.replaceAll("RT : ", " ");
		tweetText = tweetText.replaceAll("RT", " ");
		tweetText = tweetText.replaceAll("rt", " ");
		
		return tweetText;
	}
	
	private String tweetHousekeeping (Status tweet) {
		String tweetText = tweet.getText();
		
		tweetText = removeNoisyEntities(tweetText, tweet);
		
		return tweetText;
	}
	
	private String tweetHousekeeping (Tweet tweet) {
		String tweetText = tweet.getText();
		
		tweetText = removeNoisyEntities(tweetText, tweet);
		
		return tweetText;
	}
	
	private String expandContractions (String tweetText) {
		//contractions
		tweetText = tweetText.replaceAll("'re", " are");
		tweetText = tweetText.replaceAll("n't", " not");
		tweetText = tweetText.replaceAll("'nt", " not");
		tweetText = tweetText.replaceAll("g'", " good ");
		tweetText = tweetText.replaceAll("'ve", " have");
		tweetText = tweetText.replaceAll("'s", " is");
		tweetText = tweetText.replaceAll("'d", " would");
		tweetText = tweetText.replaceAll("'", " ");
		return tweetText;
	}
	
	private String removeOrdinals (String tweetText) {
		//numbers (ordinals)
		tweetText = tweetText.replaceAll("\\d+(th|nd|st)", " ");
		return tweetText;
	}
	
	private String removeNumbers (String tweetText) {

		//numbers
		tweetText = tweetText.replaceAll("\\d+", " ");
		
		return tweetText;
	}
	
	private String doubleDownLetters (String tweetText) {
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
			return tweetText;
	}
	
	private String terminateEmoticon(String emoticon) {
		//[, ], (, ), *, +, \, /, {, }
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i=0; i < emoticon.length(); i++) {
			if (emoticon.charAt(i) == '[' ||
					emoticon.charAt(i) == ']' ||
					emoticon.charAt(i) == '(' ||
					emoticon.charAt(i) == ')' ||
					emoticon.charAt(i) == '*' ||
					emoticon.charAt(i) == '+' ||
					emoticon.charAt(i) == '/' ||
					emoticon.charAt(i) == '\\' ||
					emoticon.charAt(i) == '{' ||
					emoticon.charAt(i) == '}') {
				stringBuilder.append('\\');
			}
			stringBuilder.append(emoticon.charAt(i));
		}
		
		return stringBuilder.toString();
	}
	
	public List<String> tokenizeAndCleanTweet(Tweet tweet) {
		String tweetText = tweetHousekeeping(tweet);
		
		return tokenizeAndCleanTweetString(tweetText);
	}
	
	public List<String> tokenizeAndCleanTweet(Status tweet) {
		String tweetText = tweetHousekeeping(tweet);
		
		return tokenizeAndCleanTweetString(tweetText);
	}
	
	private List<String> tokenizeAndCleanTweetString(String tweetText) {
		tweetText = tweetText.toLowerCase();
		
		List<Emoticon> emoticonsInTweet = new ArrayList<Emoticon>();
		
		/**EMOTICON STRIP AND COMPARISON**/
		for (String emo : emoticons) {
			
			String emoticonregex = terminateEmoticon(emo);
			
			if (Pattern.matches(emoticonregex, tweetText) || Pattern.matches(emoticonregex.toLowerCase(), tweetText)) {
				Emoticon emoObj = emoticonDAO.getEmoticonByString(emo);
				if (emoObj != null) {
					emoticonsInTweet.add(emoObj);
				}
				else {
					System.err.println("I don't think this was supposed to happen.");
				}
				
				tweetText = tweetText.replaceAll(emoticonregex, " ");
			}
		}
		
		/**EMOTICON COMPARISON**/
		if (!compareEmoticons(emoticonsInTweet)) {
			//a tweet expressing different sentiments is pretty confusing
			return null;
		}
		
		//remove symbols and basic :\w (colon followed by a word) emoticons
		//tweetText = tweetText.replaceAll("(:[^\\s]+)", " ");
		
		tweetText = tweetText.replaceAll("[^a-zA-Z0-9\\s']", " ");
		
		tweetText = expandContractions(tweetText);
		
		tweetText = removeOrdinals(tweetText);
		
		tweetText = removeNumbers(tweetText);
		
		tweetText = doubleDownLetters (tweetText);
		
		//initialize tokenizer
		WordTokenizer tokenizer = new WordTokenizerFactory().newWordTokenizer();
		List<String> tokens = tokenizer.extractWords(tweetText);
		tokenizer.close();
		
		Iterator<String> it = tokens.listIterator();
		while (it.hasNext()) {
			String cur = it.next();
			if (stopWords.contains(cur) || Pattern.matches("'", cur) || cur.length() <= 2) {
				it.remove();
			}
/*			else if (emoticons.contains(cur)) {
				Emoticon emoObj = emoticonDAO.getEmoticonByString(cur);
				if (emoObj != null) {
					emoticonsInTweet.add(emoObj);
				}
				else {
					System.err.println("I don't think this was supposed to happen.");
				}
			}*/
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
					System.out.println("Cleaned token: " + token);
					
					if (!cleanedTokens.contains(token)) {
						cleanedTokens.add(token);			
					}
				}
				else if (isStatisticallyEnglish(token)) {
					String respelledToken = attemptSpellCheck(token);
					
					if (respelledToken != null) {
						System.out.println("Respelled token: " + respelledToken);
						
						respelledToken = respelledToken.replaceAll("[^a-zA-Z]", "");
						
						if (!cleanedTokens.contains(respelledToken) && !respelledToken.isEmpty() && !(respelledToken.length() <= 2) && !(this.stopWords.contains(respelledToken))) {
							cleanedTokens.add(respelledToken);
						}
					}
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
			
			boolean precedingNeg = false;
			
			while (iter.hasNext()) {
				AdornedWord curEntry = iter.next();
				System.out.println(curEntry.getToken() + " - " + curEntry.getPartsOfSpeech());
				
				//change negative adverbs and negative determiners into N-
				if (this.isOneOfThePartsOfSpeech(curEntry, new String[] {"av-dx", "av-x"})) {
					//curEntry.setToken("N-");
					iter.remove();
					precedingNeg = true;
					continue;
				}
				
				//automatically remove words with less than 2 characters (likely useless)
				//that aren't identified as negative adverbs/determiners (like "no")
				if (curEntry.getToken().isEmpty() || curEntry.getToken().length() <= 2) {
					iter.remove();
					precedingNeg = false;
					continue;
				}
				
				//remove useless parts of speech
				if (this.isOneOfThePartsOfSpeech(curEntry, uselessPOS)) {
					iter.remove();
					precedingNeg = false;
					continue;
				}
				//lemmatize if POS is not part of the unlemmatizable list
				else if (!this.isOneOfThePartsOfSpeech(curEntry, unlemmatizable)) {
					if (!lemmatizer.cantLemmatize(curEntry.getToken())) {
						curEntry.setToken(lemmatizer.lemmatize(curEntry.getToken()));
					}
				}
				
				if (curEntry.getToken().length() <= 2) {
					iter.remove();
					continue;
				}
				
				//if the preceding token was a negator, add "n-" to a word that it describes
				//which is usually the word it is next to (unfortunately, this cannot be reliable
				//especially for some strangely worded phrases
				if (precedingNeg) {
					String subject = curEntry.getToken();
					StringBuilder sb = new StringBuilder();
					sb.append("n-").append(subject);
					
					curEntry.setToken(sb.toString());
				}
				
			}
			
//			cleanedTweet.delete(0, cleanedTweet.length());
			
			cleanedTokens = this.adornedListToStringList(tags);
			
			System.out.println("Yet another successful tweet: " + this.wordListToString(cleanedTokens));
			
			return cleanedTokens;
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
		SpellRequest request = new SpellRequest();
		request.setText( token );
		request.setIgnoreDuplicates( true ); // Ignore duplicates

		try {
			SpellResponse spellResponse = checker.check( request );
	
			SpellCorrection[] corrections = spellResponse.getCorrections();
			
			if (corrections != null && corrections.length > 0) {
				return corrections[0].getWords()[0];
			}
		}
		catch (SpellCheckException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private List<String> adornedListToStringList (List<AdornedWord> words) {
		List<String> stringList = new ArrayList<String>();
		
		for (AdornedWord word : words) {
			stringList.add(word.getToken());
		}
		
		return stringList;
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
	
	public void addBlockedWords (String... blockedWords) {
		for (String keyword : blockedWords) {
			WordTokenizer extraKeywordTokenizer = new WordTokenizerFactory().newWordTokenizer();
			List<String> words = extraKeywordTokenizer.extractWords(keyword);
			
			for (String word : words) {
				if (!stopWords.contains(word)) {
					stopWords.add(word);
					this.temporaryStopWords.add(word);
				}
			}
			extraKeywordTokenizer.close();
		}
	}
	
	public void removeTemporaryBlockedWords () {
		this.stopWords.removeAll(this.temporaryStopWords);
		this.temporaryStopWords.clear();
	}
	
}
