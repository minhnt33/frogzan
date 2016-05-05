package vn.com.tuanminh.frogzan;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import vn.com.tuanminh.frogzan.utils.ActivityHandler;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate implements ActivityHandler{
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new Frogzan(null), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
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
	public void setTrackerScreenName(String path) {
		// TODO Auto-generated method stub
		
	}
}