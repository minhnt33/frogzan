package vn.com.tuanminh.frogzan.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {
	
	public static final GamePreferences instance = new GamePreferences();
	
	public boolean enableVol;
	public int highscore;

	private Preferences prefs;
	
	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}
	
	public void load() {
		enableVol = prefs.getBoolean("enableVol", true);
		highscore = prefs.getInteger("highscore", 0);
	}
	
	public void save() {
		prefs.putBoolean("enableVol", enableVol);
		prefs.putInteger("highscore", highscore);
		prefs.flush();
	}

}
