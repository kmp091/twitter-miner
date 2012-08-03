package com.twitminer.viewer.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Debug.Random;

import com.twitminer.beans.Emotion;
import com.twitminer.beans.TokenizedTweet;
import com.twitminer.dao.EmotionDAO;

public class NaiveBayes implements Classifier {

	private EmotionDAO emotionDAO;
	private weka.classifiers.Classifier happyClassifier;
	private weka.classifiers.Classifier sadClassifier;
	private weka.classifiers.Classifier disgustClassifier;
	private weka.classifiers.Classifier surprisedClassifier;
	
	private static final String disgustedPath = "disgusted-bayes.model";
	private static final String happyPath = "happy-bayes.model";
	private static final String sadPath = "sad-bayes.model";
	private static final String surprisedPath = "surprise-bayes.model";
	
	private FastVector happySchema;
	private FastVector sadSchema;
	private FastVector surprisedSchema;
	private FastVector disgustedSchema;
	
	private ModelReader<weka.classifiers.Classifier> reader;
	
	public NaiveBayes(EmotionDAO emotion) throws FileNotFoundException, IOException, ClassNotFoundException, URISyntaxException, Exception {
		this.reader = new ModelReader<weka.classifiers.Classifier>();
		
		this.emotionDAO = emotion;
		
		InputStream trainingFile = this.getClass().getResourceAsStream("harry-potter-tweets.csv");
		Set<String> schema = AlgoUtils.getSchema(new InputStreamReader(trainingFile));
		this.happySchema = AlgoUtils.getAttributes(emotion.getEmotionById(EmotionDAO.HAPPY), schema);
		this.sadSchema = AlgoUtils.getAttributes(emotion.getEmotionById(EmotionDAO.SAD), schema);
		this.surprisedSchema = AlgoUtils.getAttributes(emotion.getEmotionById(EmotionDAO.SURPRISE), schema);
		this.disgustedSchema = AlgoUtils.getAttributes(emotion.getEmotionById(EmotionDAO.DISGUST), schema);

		this.disgustClassifier = this.getClassifierModel(this.getClass().getResourceAsStream(disgustedPath)); 
		//this.disgustClassifier.turnChecksOff();
		this.sadClassifier = this.getClassifierModel(this.getClass().getResourceAsStream(sadPath));
		//this.sadClassifier.turnChecksOff();
		this.happyClassifier = this.getClassifierModel(this.getClass().getResourceAsStream(happyPath));
		//this.happyClassifier.turnChecksOff();
		this.surprisedClassifier = this.getClassifierModel(this.getClass().getResourceAsStream(surprisedPath));				
		//this.surprisedClassifier.turnChecksOff();
	}
	
	@SuppressWarnings("unused")
	private weka.classifiers.Classifier getClassifierModel(File file) throws FileNotFoundException, IOException, ClassNotFoundException, Exception {
		return this.reader.getModelObj(file);
	}
	
	private weka.classifiers.Classifier getClassifierModel(InputStream input) throws Exception {
		return this.reader.getModelObj(input);
	}
	
	
	private Instance createInstanceFromTokenizedTweet(FastVector schema, TokenizedTweet tokenizedTweet, Instances dataset) {
		Instance instance = new Instance(schema.size());
		instance.setDataset(dataset);
		instance.setClassMissing();
		
		Set<String> tokens = tokenizedTweet.getSetOfTokens();
		
		@SuppressWarnings("unused")
		Attribute classAttr = (Attribute)schema.elementAt(0);
		instance.setValue(0, "others");
		
		for (int i = 1; i < schema.size(); i++) {
			Attribute word = (Attribute)schema.elementAt(i);
			
			boolean wordPresent = false;
			
			if (tokens.contains(word.name())) {
				wordPresent = true;
			}
			else {
				wordPresent = false;
			}
			
			instance.setValue(i, wordPresent ? 0.0 : 1.0);
		}
		
		/*Enumeration attributes = instance.enumerateAttributes();
		System.out.println("Number of attributes: " + schema.size() +
				"\n Number of actual attributes: " + instance.numAttributes());
		int ctr = 0;
		while (attributes.hasMoreElements()) {
			System.out.println((++ctr) + " - " + attributes.nextElement());
		}*/
		//System.out.println(instance.hasMissingValue());
		
		return instance;
	}
	
	@Override
	public Emotion classifyEmotion(TokenizedTweet tokenizedTweet)
			throws Exception {
List<Integer> identifiedEmotions = new ArrayList<Integer>();
		
		int identifiedNumber = 0;
		double highestProbability = -1;
		
		Instances instances = new Instances("test_tweets", happySchema, 1);
		instances.setClassIndex(0);
		Instance tweetInstance = createInstanceFromTokenizedTweet(happySchema, tokenizedTweet, instances);
		double[] results = happyClassifier.distributionForInstance(tweetInstance);
		
		if (results[0] > highestProbability) {
			highestProbability = results[0];
			identifiedEmotions.clear();
			identifiedNumber = 1;
			identifiedEmotions.add(EmotionDAO.HAPPY);
		}
		else if (results[0] == highestProbability) {
			identifiedNumber++;
			identifiedEmotions.add(EmotionDAO.HAPPY);
		}
		
		System.out.println(results.length);
		System.out.println("happy-class : " + results[0]);
		System.out.println("others : " + results[1]);
		
		instances = new Instances("test_tweets2", sadSchema, 1);
		instances.setClassIndex(0);
		tweetInstance = createInstanceFromTokenizedTweet(sadSchema, tokenizedTweet, instances);
		double[] sadResults = sadClassifier.distributionForInstance(tweetInstance);
		
		if (sadResults[0] > highestProbability) {
			highestProbability = sadResults[0];
			identifiedEmotions.clear();
			identifiedNumber = 1;
			identifiedEmotions.add(EmotionDAO.SAD);
		}
		else if (sadResults[0] == highestProbability) {
			identifiedNumber++;
			identifiedEmotions.add(EmotionDAO.SAD);
		}
		
		System.out.println("sad-class : " + sadResults[0]);
		System.out.println("others : " + sadResults[1]);
		
		instances = new Instances("test_tweets3", disgustedSchema, 1);
		instances.setClassIndex(0);
		tweetInstance = createInstanceFromTokenizedTweet(disgustedSchema, tokenizedTweet, instances);
		double[] disgustResults = disgustClassifier.distributionForInstance(tweetInstance);
		
		if (disgustResults[0] > highestProbability) {
			highestProbability = disgustResults[0];
			identifiedEmotions.clear();
			identifiedNumber = 1;
			identifiedEmotions.add(EmotionDAO.DISGUST);
		}
		else if (disgustResults[0] == highestProbability) {
			identifiedNumber++;
			identifiedEmotions.add(EmotionDAO.DISGUST);
		}
		
		System.out.println("disgust-class : " + disgustResults[0]);
		System.out.println("others : " + disgustResults[1]);
		
		instances = new Instances("test_tweets4", surprisedSchema, 1);
		instances.setClassIndex(0);
		tweetInstance = createInstanceFromTokenizedTweet(surprisedSchema, tokenizedTweet, instances);
		double[] surprisedResults = surprisedClassifier.distributionForInstance(tweetInstance);
		
		if (surprisedResults[0] > highestProbability) {
			highestProbability = surprisedResults[0];
			identifiedEmotions.clear();
			identifiedNumber = 1;
			identifiedEmotions.add(EmotionDAO.SURPRISE);
		}
		else if (surprisedResults[0] == highestProbability) {
			identifiedNumber++;
			identifiedEmotions.add(EmotionDAO.SURPRISE);
		}
		
		System.out.println("surprised-class : " + surprisedResults[0]);
		System.out.println("others : " + surprisedResults[1]);
		
		Emotion classifiedEmotion;
		
		if (identifiedNumber > 0) {
			Random rand = new Random(System.currentTimeMillis());
			
			int emotion = rand.nextInt(identifiedEmotions.size());
			classifiedEmotion = emotionDAO.getEmotionById(identifiedEmotions.get(emotion));
		}
		else {
			classifiedEmotion = emotionDAO.getEmotionById(identifiedEmotions.get(0));
		}
		
		System.out.println(classifiedEmotion.getEmotionName());
		
		return classifiedEmotion;
	}

}
