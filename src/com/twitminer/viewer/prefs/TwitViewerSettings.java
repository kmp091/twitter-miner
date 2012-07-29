package com.twitminer.viewer.prefs;

import java.util.prefs.Preferences;

public class TwitViewerSettings {

	private static Preferences prefs = Preferences.userNodeForPackage(TwitViewerSettings.class);
	
	private static final String AUTOSCROLL_KEY = "autoScroll";
	private static final String CHART_VISIBLE_KEY = "chartVisible";
	
	public static boolean getAutoScrollSetting() {
		return prefs.getBoolean(AUTOSCROLL_KEY, false);
	}
	
	public static boolean getChartVisibilitySetting() {
		return prefs.getBoolean(CHART_VISIBLE_KEY, true);
	}
	
	public static void setAutoScrollSetting(boolean autoscrollSetting) {
		setBooleanPreference(AUTOSCROLL_KEY, autoscrollSetting);
	}
	
	public static void setChartVisibilitySetting(boolean chartSetting) {
		setBooleanPreference(CHART_VISIBLE_KEY, chartSetting);
	}

	private static void setBooleanPreference(String key, boolean value) {
		prefs.remove(key);
		prefs.putBoolean(key, value);
	}
	
}
