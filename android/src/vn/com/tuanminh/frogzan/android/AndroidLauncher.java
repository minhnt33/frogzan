package vn.com.tuanminh.frogzan.android;

import vn.com.tuanminh.frogzan.Frogzan;
import vn.com.tuanminh.frogzan.utils.ActivityHandler;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;

public class AndroidLauncher extends AndroidApplication implements ActivityHandler {

	private static final String AD_UNIT_ID = "ca-app-pub-4558757041968526/2527951895";
	private static final String TEST_ID = "06FE19FA12041E01C8BA6CE6A81891A5";
	private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=TheInvader360";
	
	protected AdView adView;
	protected View gameView;
	
//	protected GoogleAnalyticsApplication googleAnalyticsApp;
//	protected Tracker tracker;
//	protected Tracker globalTracker;

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private final int ACTIVE_SWARM = 2;
	private final int OPEN_URL = 3;
	private final int SET_TRACKER_SPLASH_SCREEN = 4;
	private final int SET_TRACKER_GAME_SCREEN = 5;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_ADS: {
				adView.setVisibility(View.VISIBLE);
				break;
			}
			case HIDE_ADS: {
				adView.setVisibility(View.GONE);
				break;
			}
			case ACTIVE_SWARM: {
				Swarm.setActive(AndroidLauncher.this);
				Swarm.setAllowGuests(true);
				Swarm.init(AndroidLauncher.this, 13761, "8d6213eb031b8878e8f640900b48f500");
				break;
			}
			case OPEN_URL: {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
				break;
			}
			case SET_TRACKER_SPLASH_SCREEN: {
//				globalTracker.setScreenName("vn.com.tuanminh.frogzan.screens.SplashScreen");
//				globalTracker.send(new HitBuilders.AppViewBuilder().build());
				break;
			}
			case SET_TRACKER_GAME_SCREEN:{
//				globalTracker.setScreenName("vn.com.tuanminh.frogzan.screens.GameScreen");
//				globalTracker.send(new HitBuilders.AppViewBuilder().build());
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		
		// Admob implement
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		FrameLayout layout = new FrameLayout(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		

		View gameView = createGameView(config);
		layout.addView(gameView);
		AdView admobView = createAdView();
		layout.addView(admobView);

		setContentView(layout);
		startAdvertising(admobView);
		
		//Google Analytics implement
//		googleAnalyticsApp = (GoogleAnalyticsApplication) getApplication();
//		tracker = googleAnalyticsApp.getTracker(TrackerName.APP_TRACKER);
//		globalTracker = googleAnalyticsApp.getTracker(TrackerName.GLOBAL_TRACKER);
	}

	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, android.view.Gravity.BOTTOM);

		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.TRANSPARENT);
		return adView;
	}

	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new Frogzan(this), cfg);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_ID).build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
 
	@Override
	public void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (adView != null)
			adView.resume();
		
		if (Swarm.isLoggedIn()) {
			Swarm.setActive(this);
			Swarm.setAllowGuests(true);
			Swarm.init(this, 13761, "8d6213eb031b8878e8f640900b48f500");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (adView != null)
			adView.pause();

			Swarm.setInactive(this);
	}

	@Override
	public void onDestroy() {
		if (adView != null)
			adView.destroy();
		super.onDestroy();
	}

	@Override
	public void showAd(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void activateSwarm() {
		handler.sendEmptyMessage(ACTIVE_SWARM);
	}

	@Override
	public void openURL() {
		handler.sendEmptyMessage(OPEN_URL);
	}

	@Override
	public void setTrackerScreenName(boolean isSplashScreen) {
		handler.sendEmptyMessage(isSplashScreen ? SET_TRACKER_SPLASH_SCREEN : SET_TRACKER_GAME_SCREEN);
	}
}
