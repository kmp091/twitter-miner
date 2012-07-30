package com.twitminer.viewer.mood;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.Tweet;
import com.twitminer.dao.EmotionDAO;

public class MoodManager {

	private int previousPredominantMood = -1;
	private int currentPredominantMood = -1;
	
	private EmotionDAO emotions;
	
	private List<Tweet> tweetsWithinWindow;
	
	/**An interval size in seconds*/
	private AtomicInteger windowSize;
	private Calendar windowStart;
	private Calendar windowEnd;
	
	public MoodManager(EmotionDAO emotions) {
		this.emotions = emotions;
		this.windowSize = new AtomicInteger(5);
		this.tweetsWithinWindow = new Vector<Tweet>();
		this.windowStart = Calendar.getInstance();
		this.windowEnd = Calendar.getInstance();
		this.windowEnd.roll(Calendar.SECOND, windowSize.get());
	}
	
	public int getWindowSize() {
		return windowSize.get();
	}
	
	public int getWindowSizeInMillis() {
		return windowSize.get() * 1000;
	}
	
	public Emotion getPredominantMood() {
		return getPredominantMood(tweetsWithinWindow);
	}
	
	public int getPredominantMoodId() {
		return getPredominantMoodId(tweetsWithinWindow);
	}
	
	private Emotion getPredominantMood(List<Tweet> tweetsWithinTimePeriod) {
		
		return emotions.getEmotionById(getPredominantMoodId(tweetsWithinTimePeriod));
	}
	
	private int getPredominantMoodId (List<Tweet> tweetsWithinTimePeriod) {
		previousPredominantMood = currentPredominantMood;
		
		if (!tweetsWithinTimePeriod.isEmpty()) {
			
			Map<Integer, Integer> emotionCount = new HashMap<Integer, Integer>();
			emotionCount.put(EmotionDAO.HAPPY, 0);
			emotionCount.put(EmotionDAO.SAD, 0);
			emotionCount.put(EmotionDAO.SURPRISE, 0);
			emotionCount.put(EmotionDAO.DISGUST, 0);
			
			for (Tweet tweet : tweetsWithinTimePeriod) {
				switch(tweet.getEmotionId()) {
				case EmotionDAO.HAPPY:
					emotionCount.put(EmotionDAO.HAPPY, emotionCount.remove(EmotionDAO.HAPPY) + 1);
					break;
				case EmotionDAO.SAD:
					emotionCount.put(EmotionDAO.SAD, emotionCount.remove(EmotionDAO.SAD) + 1);
					break;
				case EmotionDAO.SURPRISE:
					emotionCount.put(EmotionDAO.SURPRISE, emotionCount.remove(EmotionDAO.SURPRISE) + 1);
					break;
				case EmotionDAO.DISGUST:
					emotionCount.put(EmotionDAO.DISGUST, emotionCount.remove(EmotionDAO.DISGUST) + 1);
					break;
					default:
						break;
				}
			}
			
			currentPredominantMood = getMaxInEmotionCountMap(emotionCount).getKey();
		}
		
		return currentPredominantMood;
	}
	
	/**Checks whether a tweet's timestamp is within the specified time window; if beyond the
	 * current window, starts a new window
	 * 
	 * @param tweet
	 */
	public void addTweet(Tweet tweet) {
		Calendar creationDate = tweet.getDateCreated();
		
		if (creationDate.before(windowStart)) {
			//the tweet was posted before the window of time specified
			return;
		}
		else if (creationDate.after(windowEnd)) {
			this.tweetsWithinWindow.clear();
			this.windowStart = (Calendar)creationDate.clone();
			this.windowEnd = (Calendar)creationDate.clone();
			this.windowEnd.roll(Calendar.SECOND, windowSize.get());
		}
		
		this.tweetsWithinWindow.add(tweet);
	}
	
	private Map.Entry<Integer, Integer> getMaxInEmotionCountMap(Map<Integer, Integer> map) {
		List<Map.Entry<Integer, Integer>> entries = new LinkedList<Map.Entry<Integer, Integer>>(map.entrySet());
		return Collections.max(entries, new Comparator<Map.Entry<Integer, Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
				if (o1.getValue() == o2.getValue()) {
					if (previousPredominantMood > -1) {
						if (o1.getKey() == previousPredominantMood) {
							return 1;
						}
						else if (o2.getKey() == previousPredominantMood) {
							return -1;
						}
					}
					else {
						return 0;
					}
				}
				return o1.getValue().compareTo(o2.getValue());
			}
			
		});
	}
	
/*	private <T> Map<T, Integer> sortMapDescending(Map<T, Integer> unsortedMap) {
		List<Map.Entry<T, Integer>> entries = new LinkedList<Map.Entry<T, Integer>>(unsortedMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<T, Integer>>() {

			@Override
			public int compare(Map.Entry<T, Integer> o1, Map.Entry<T, Integer> o2) {
				if (o1.getValue() < o2.getValue()) {
					return 1;
				}
				else if (o1.getValue() > o2.getValue()) {
					return -1;
				}
				else {
					return 0;
				}
			}
			
		});
		
		Map<T, Integer> sortedMap = new HashMap<T, Integer>();
		
		Iterator<Map.Entry<T, Integer>> mapIterator = entries.iterator();
		while (mapIterator.hasNext()) {
			Map.Entry<T, Integer> curEntry = mapIterator.next();
			sortedMap.put(curEntry.getKey(), curEntry.getValue());
		}

		return sortedMap;
	}*/
	
	/**Get all tweets within a specified duration from now to a specific date before today
	 * 
	 * @param allTweets All tweets in a collection to be filtered
	 * @param dateBefore a date before today serves as a starting range
	 * @return The tweets sent out from dateBefore to right now
	 * 
	 * @throws IllegalArgumentException when the dateBefore parameter is after the date today
	 */
	public List<Tweet> getTweetsWithinSpecifiedDuration(List<Tweet> allTweets, Calendar dateBefore) throws IllegalArgumentException {
		if (dateBefore.after(Calendar.getInstance())) {
			throw new IllegalArgumentException();
		}
		
		return getTweetsWithinSpecifiedDuration(allTweets, Calendar.getInstance().getTimeInMillis() - dateBefore.getTimeInMillis());
	}
	
	public List<Tweet> getTweetsWithinSpecifiedDuration(List<Tweet> allTweets, long millisecondsBefore) {
		long now = Calendar.getInstance().getTimeInMillis();
		return getTweetsWithinSpecifiedDuration(allTweets, now - millisecondsBefore, now);
	}
	
	public List<Tweet> getTweetsWithinSpecifiedDuration(List<Tweet> allTweets, long from, long to) {
		//in order to avoid ConcurrentModificationException, we need to copy the tweets
		//because if we continue adding tweets to the TweetDAO while iterating through the same
		//DAO, there will be inconsistencies
		//
		//might as well copy a snapshot of the tweets as they are given a moment of time and
		//then manipulating that copy instead of the original
		List<Tweet> copiedTweets = new ArrayList<Tweet>(allTweets);
		
		ListIterator<Tweet> iterator = copiedTweets.listIterator(copiedTweets.size());
		
		List<Tweet> tweetsWithinSpecifiedDuration = new ArrayList<Tweet>();
		
		while (iterator.hasPrevious()) {
			Tweet curTweet = iterator.previous();
			if (curTweet.getDateCreated().getTimeInMillis() >= from && curTweet.getDateCreated().getTimeInMillis() <= to) {
				tweetsWithinSpecifiedDuration.add(curTweet);				
			}
			else {
				break;
			}
		}
		
		return tweetsWithinSpecifiedDuration;
	}

}
