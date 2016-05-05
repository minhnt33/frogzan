package vn.com.tuanminh.frogzan;


import vn.com.tuanminh.frogzan.game.Assets;
import vn.com.tuanminh.frogzan.screens.DirectedGame;
import vn.com.tuanminh.frogzan.screens.SplashScreen;
import vn.com.tuanminh.frogzan.transitions.ScreenTransition;
import vn.com.tuanminh.frogzan.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogzan.utils.ActivityHandler;
import vn.com.tuanminh.frogzan.utils.GamePreferences;

import com.badlogic.gdx.assets.AssetManager;

public class Frogzan extends DirectedGame {

	public Frogzan(ActivityHandler handler) {
		super(handler);
	}

	@Override
	public void create () {
		handler.showAd(false);
		Assets.instance.init(new AssetManager());
		GamePreferences.instance.load();
		
		ScreenTransition tran = ScreenTransitionFade.init(0.5f);
		this.setScreen(new SplashScreen(this), tran);
		
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void showAd(boolean show) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateSwarm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openURL() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrackerScreenName(boolean isSplashScreen) {
		// TODO Auto-generated method stub
		
	}

}
