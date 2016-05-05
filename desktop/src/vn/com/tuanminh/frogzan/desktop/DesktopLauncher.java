package vn.com.tuanminh.frogzan.desktop;

import vn.com.tuanminh.frogzan.Frogzan;
import vn.com.tuanminh.frogzan.utils.ActivityHandler;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher implements ActivityHandler {
	private static DesktopLauncher app;

	public static void main(String[] arg) {
		if (app == null)
			app = new DesktopLauncher();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Frogzan(app), config);

		config.width = 320;
		config.height = 480;
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
