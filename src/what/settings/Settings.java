package what.settings;

import java.util.HashMap;

import what.gui.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
	private static SharedPreferences settings;
	private static SharedPreferences.Editor settingsEditor;

	protected static HashMap<String, Integer> themes;
	static {
		themes = new HashMap<String, Integer>();
		themes.put("Moldy Walls", R.style.Theme_moldy_walls);
		themes.put("Gwindow Loves Tom", R.style.Theme_gwindow_loves_tom);
		themes.put("Old E and 4 Blunts", R.style.Theme_old_e_and_four_blunts);
		themes.put("Watermelon", R.style.Theme_watermelon);
		themes.put("Ridejck's Barbie Convertible", R.style.Theme_ridejkcls_barbie_convertible);
		themes.put("Wonder Orange", R.style.Theme_wonder_orange);
		themes.put("DarkCD", R.style.Theme_darkcd);
		themes.put("Light", R.style.LightTheme);
		themes.put("Dark", R.style.DarkTheme);

	}

	/**
	 * Initialize the settings reader and writer, should only be done once
	 * 
	 * @param c
	 *            Context
	 */
	public static void init(Context c) {
		settings = PreferenceManager.getDefaultSharedPreferences(c);
		settingsEditor = settings.edit();

	}

	public static int getTheme() {
		return settings.getInt("theme_preference", R.style.LightTheme);
	}

	public static void saveTheme(int theme) {
		settingsEditor.putInt("theme_preference", theme);
		commit();
	}

	public static boolean getDebugPreference() {
		return settings.getBoolean("debug_preference", false);
	}

	public static boolean getQuickSearch() {
		return settings.getBoolean("quickSearch_preference", true);
	}

	public static boolean getSpotifyButton() {
		return settings.getBoolean("spotifyButton_preference", true);
	}

	public static boolean getLastfmButton() {
		return settings.getBoolean("lastfmButton_preference", true);
	}

	public static boolean getAnnouncementsService() {
		return settings.getBoolean("announcementsService_preference", true);
	}

	public static String getAnnouncementsServiceInterval() {
		return settings.getString("announcementsService_interval", "180");
	}

	public static boolean getInboxService() {
		return settings.getBoolean("inboxService_preference", true);
	}

	public static String getInboxServiceInterval() {
		return settings.getString("inboxService_interval", "60");
	}

	public static boolean getNotificationsService() {
		return settings.getBoolean("notificationsService_preference", false);
	}

	public static String getNotificationsServiceInterval() {
		return settings.getString("notificationsService_interval", "180");
	}

	public static boolean getCustomBackground() {
		return settings.getBoolean("customBackground_preference", false);
	}

	public static boolean getTileBackground() {
		return settings.getBoolean("tileBackground_preference", true);
	}

	public static String getCustomBackgroundPath() {
		return settings.getString("customBackground_path", "");
	}

	public static void saveQuickScannerFirstRun(boolean b) {
		settingsEditor.putBoolean("quickScannerFirstRun", b);
		commit();
	}

	public static boolean getQuickScannerFirstRun() {
		return settings.getBoolean("quickScannerFirstRun", true);
	}

	public static void saveFirstRun(boolean b) {
		settingsEditor.putBoolean("firstRun", b);
		commit();
	}

	public static boolean getFirstRun() {
		return settings.getBoolean("firstRun", true);
	}

	public static boolean getAvatarsEnabled() {
		return settings.getBoolean("avatarsEnabled_preference", true);
	}

	public static boolean getGesturesEnabled() {
		return settings.getBoolean("gesturesEnabled_preference", true);
	}

	public static boolean getSubscriptionsEnabled() {
		return settings.getBoolean("subscriptionsEnabled_preference", true);
	}

	public static void saveNumberOfAnnouncements(int i) {
		settingsEditor.putInt("numberOfA", i);
	}

	public static void saveCustomBackgroundPath(String s) {
		settingsEditor.putString("customBackground_path", s);
		commit();
	}

	public static String getHostPreference() {
		return settings.getString("host_preference", "");
	}

	public static String getPortPreference() {
		return settings.getString("port_preference", "");
	}

	public static String getPasswordPreference() {
		return settings.getString("password_preference", "");
	}

	public static int getNumberOfAnnouncements() {
		try {
			return settings.getInt("numberOfA", 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void saveNumberOfBlogPosts(int i) {
		settingsEditor.putInt("numberOfB", i);
	}

	public static int getNumberOfBlogPosts() {
		try {
			return settings.getInt("numberOfB", 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void saveMessageHashCode(int hash) {
		settingsEditor.putInt("messageHashCode", hash);
	}

	public static int getMessageHashCode() {
		try {
			return settings.getInt("messageHashCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static void saveUserId(int id) {
		settingsEditor.putInt("userId", id);
	}

	public static int getUserId() {
		return settings.getInt("userId", 0);
	}

	public static void saveUsername(String username) {
		settingsEditor.putString("username", username);
	}

	public static String getUsername() {
		return settings.getString("username", "");
	}

	public static void savePassword(String password) {
		settingsEditor.putString("password", password);
	}

	public static String getPassword() {
		return settings.getString("password", "");

	}

	public static void saveRememberMe(boolean b) {
		settingsEditor.putBoolean("rememberMe", b);
	}

	public static boolean getRememberMe() {
		return settings.getBoolean("rememberMe", false);
	}

	public static void saveSessionId(String id) {
		settingsEditor.putString("sessionId", id);
	}

	public static String getSessionId() {
		return settings.getString("sessionId", "");
	}

	public static void saveAuthKey(String key) {
		settingsEditor.putString("authKey", key);
	}

	public static String getAuthKey() {
		return settings.getString("authKey", "");
	}

	public static void commit() {
		settingsEditor.commit();
	}

	/**
	 * @return the settings
	 */
	public static SharedPreferences getSettings() {
		return settings;
	}

	/**
	 * @return the settingsEditor
	 */
	public static SharedPreferences.Editor getSettingsEditor() {
		return settingsEditor;
	}

	public static float getGestureSensitivity() {
		return settings.getFloat("gestureSensitivity_preference", 2.5f);
	}

}
