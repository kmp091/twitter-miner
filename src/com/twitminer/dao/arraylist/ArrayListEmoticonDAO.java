package com.twitminer.dao.arraylist;

import java.util.List;
import java.util.ArrayList;

import com.twitminer.beans.Emoticon;
import com.twitminer.dao.EmoticonDAO;
import com.twitminer.dao.mysql.MySQLEmotionDAO;

public class ArrayListEmoticonDAO extends EmoticonDAO {

	ArrayList<Emoticon> emoticons;
	
	ArrayListEmoticonDAO () {
		emoticons = new ArrayList<Emoticon>();
		Emoticon tempEmo;
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(1);
		tempEmo.setEmoticon(">:]");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(2);
		tempEmo.setEmoticon(":-)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(3);
		tempEmo.setEmoticon(":)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(4);
		tempEmo.setEmoticon(":o)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(5);
		tempEmo.setEmoticon(":]");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(6);
		tempEmo.setEmoticon(":3");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(7);
		tempEmo.setEmoticon(":c)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(8);
		tempEmo.setEmoticon(":>");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(9);
		tempEmo.setEmoticon("=]");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(10);
		tempEmo.setEmoticon("8)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(11);
		tempEmo.setEmoticon("=)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(12);
		tempEmo.setEmoticon(":}");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(13);
		tempEmo.setEmoticon(":^)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(14);
		tempEmo.setEmoticon(">:D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(15);
		tempEmo.setEmoticon(":-D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(16);
		tempEmo.setEmoticon(":D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(17);
		tempEmo.setEmoticon("8-D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(18);
		tempEmo.setEmoticon("8D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(19);
		tempEmo.setEmoticon("x-D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(20);
		tempEmo.setEmoticon("xD");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(21);
		tempEmo.setEmoticon("X-D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(22);
		tempEmo.setEmoticon("XD");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(23);
		tempEmo.setEmoticon("=-D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(24);
		tempEmo.setEmoticon("=D");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(25);
		tempEmo.setEmoticon("=-3");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(26);
		tempEmo.setEmoticon("=3");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(27);
		tempEmo.setEmoticon("8-)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(28);
		tempEmo.setEmoticon(":-))");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		/*tempEmo = new Emoticon();
		tempEmo.setEmoticonId(29);
		tempEmo.setEmoticon(">:[");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);*/
		/*tempEmo = new Emoticon();
		tempEmo.setEmoticonId(30);
		tempEmo.setEmoticon(":-(");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);*/
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(31);
		tempEmo.setEmoticon(":(");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		/*tempEmo = new Emoticon();
		tempEmo.setEmoticonId(32);
		tempEmo.setEmoticon(":-c");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);*/
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(33);
		tempEmo.setEmoticon(":c");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		/*tempEmo = new Emoticon();
		tempEmo.setEmoticonId(34);
		tempEmo.setEmoticon(":-<");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);*/
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(35);
		tempEmo.setEmoticon(":<");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		/*tempEmo = new Emoticon();
		tempEmo.setEmoticonId(36);
		tempEmo.setEmoticon(":-[");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);*/
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(37);
		tempEmo.setEmoticon(":[");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(38);
		tempEmo.setEmoticon(":{");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(39);
		tempEmo.setEmoticon(">.>");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(40);
		tempEmo.setEmoticon("<.<");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(41);
		tempEmo.setEmoticon(">.<");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(42);
		tempEmo.setEmoticon(":-||");
		tempEmo.setEmotionId(4);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(43);
		tempEmo.setEmoticon(":@");
		tempEmo.setEmotionId(4);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(44);
		tempEmo.setEmoticon("D:<");
		tempEmo.setEmotionId(4);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(45);
		tempEmo.setEmoticon("D:");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(46);
		tempEmo.setEmoticon("D8");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(47);
		tempEmo.setEmoticon("D;");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(48);
		tempEmo.setEmoticon("DX");
		tempEmo.setEmotionId(4);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(49);
		tempEmo.setEmoticon("v.v");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(50);
		tempEmo.setEmoticon("D-':");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(51);
		tempEmo.setEmoticon(">:o");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(52);
		tempEmo.setEmoticon(">:O");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(53);
		tempEmo.setEmoticon(":-O");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(54);
		tempEmo.setEmoticon(":O");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(55);
		tempEmo.setEmoticon(":o");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(56);
		tempEmo.setEmoticon("o_O");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(57);
		tempEmo.setEmoticon("o_0");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(58);
		tempEmo.setEmoticon("o.O");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(59);
		tempEmo.setEmoticon("8-0");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(60);
		tempEmo.setEmoticon("O:-)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(61);
		tempEmo.setEmoticon("0:-3");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(62);
		tempEmo.setEmoticon("0:3");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(63);
		tempEmo.setEmoticon("O:-)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(64);
		tempEmo.setEmoticon("O:)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(65);
		tempEmo.setEmoticon("0;^)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(66);
		tempEmo.setEmoticon(":'-(");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(67);
		tempEmo.setEmoticon(":'(");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(68);
		tempEmo.setEmoticon(":'-)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(69);
		tempEmo.setEmoticon(":')");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(70);
		tempEmo.setEmoticon("(^_^)");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(71);
		tempEmo.setEmoticon("^_^");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(72);
		tempEmo.setEmoticon("(^_^)/");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(73);
		tempEmo.setEmoticon("(^O^)/");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(74);
		tempEmo.setEmoticon("(^o^)/");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(75);
		tempEmo.setEmoticon("^^");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(76);
		tempEmo.setEmoticon("(^^)/");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(77);
		tempEmo.setEmoticon("('_')");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(78);
		tempEmo.setEmoticon("(/_;)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(79);
		tempEmo.setEmoticon("(T_T)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(80);
		tempEmo.setEmoticon("T_T");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(81);
		tempEmo.setEmoticon("(;_;)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(82);
		tempEmo.setEmoticon("(;_;");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(83);
		tempEmo.setEmoticon(";_;");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(84);
		tempEmo.setEmoticon("(;O;)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(85);
		tempEmo.setEmoticon("(:_;)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(86);
		tempEmo.setEmoticon("(ToT)");
		tempEmo.setEmotionId(2);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(87);
		tempEmo.setEmoticon("(?_?)");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(88);
		tempEmo.setEmoticon("\\(^o^)/");
		tempEmo.setEmotionId(1);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(89);
		tempEmo.setEmoticon("(@_@)");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(90);
		tempEmo.setEmoticon("@_@");
		tempEmo.setEmotionId(3);
		emoticons.add(tempEmo);
		
		/*Hashtags*/
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(91);
		tempEmo.setEmoticon("#happy");
		tempEmo.setEmoticonId(MySQLEmotionDAO.HAPPY);
		emoticons.add(tempEmo);
		
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(92);
		tempEmo.setEmoticon("#sad");
		tempEmo.setEmoticonId(MySQLEmotionDAO.SAD);
		emoticons.add(tempEmo);
		
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(93);
		tempEmo.setEmoticon("#surprise");
		tempEmo.setEmoticonId(MySQLEmotionDAO.SURPRISE);
		emoticons.add(tempEmo);
		
		tempEmo = new Emoticon();
		tempEmo.setEmoticonId(94);
		tempEmo.setEmoticon("#disgust");
		tempEmo.setEmoticonId(MySQLEmotionDAO.DISGUST);
		emoticons.add(tempEmo);
		
		emoticons.trimToSize();
	}
	
	@Override
	public List<String> getEmoticonStrings() {
		List<String> emoStrings = new ArrayList<String>();
		for (Emoticon emoticon : emoticons) {
			emoStrings.add(emoticon.getEmoticon());
		}
		
		return emoStrings;
	}

	@Override
	public List<String> getEmoticonStringsByEmotion(int emotion) {
		List<String> emoStrings = new ArrayList<String>();
		for (Emoticon emoticon : emoticons) {
			if (emoticon.getEmotionId() == emotion) {
				emoStrings.add(emoticon.getEmoticon());
			}			
		}
		
		return emoStrings;
	}

	@Override
	public List<Emoticon> getEmoticons() {
		
		return emoticons;
	}

	@Override
	public List<Emoticon> getEmoticonsByEmotion(int emotion) {
		List<Emoticon> emoStrings = new ArrayList<Emoticon>();
		for (Emoticon emoticon : emoticons) {
			if (emoticon.getEmotionId() == emotion) {
				emoStrings.add(emoticon);
			}			
		}
		
		return emoStrings;
	}

	@Override
	public Emoticon getEmoticonByString(String emoticonString) {
		for (Emoticon emo : this.emoticons) {
			if (emo.getEmoticon().equalsIgnoreCase(emoticonString)) {
				return emo;
			}
		}
		
		return null;
	}

}
