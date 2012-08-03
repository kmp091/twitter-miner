package com.twitminer.viewer.algorithm;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import com.twitminer.beans.Emotion;
import weka.core.Attribute;
import weka.core.FastVector;

public class AlgoUtils {

	public AlgoUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static Set<String> getSchema(Reader reader) {
		Set<String> wordBag = new LinkedHashSet<String>();
		
		Scanner scan = new Scanner(reader);
		if (scan.hasNext()) {
			String allColumns = scan.nextLine();
			wordBag = new LinkedHashSet<String>(Arrays.asList(allColumns.split(",")));
			
			Iterator<String> it = wordBag.iterator();
			
			while (it.hasNext()) {
				if (it.next().contains("-class")) {
					it.remove();
				}
			}
			 
		}
		else {
			System.err.println("Error reading file");
		}
		
		scan.close();
		
		return wordBag;
	}
	
	public static FastVector getAttributes(Emotion emotion, Collection<String> strings) {
		FastVector attributes = new FastVector(strings.size() + 1);
		
		String emotionClass;
		
		emotionClass = emotion.getEmotionName() + "-class";
		
		FastVector emotionValues = new FastVector(2);
		emotionValues.addElement(emotion.getEmotionName().toLowerCase());
		emotionValues.addElement("others");
		
		Attribute classAttr = new Attribute(emotionClass, emotionValues);
		attributes.addElement(classAttr);
		
		for (String string : strings) {
			Attribute attr = new Attribute(string);
			attributes.addElement(attr);
		}
		
		return attributes;
	}
	
	

}
