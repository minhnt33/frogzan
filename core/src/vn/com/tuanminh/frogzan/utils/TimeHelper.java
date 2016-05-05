package vn.com.tuanminh.frogzan.utils;


public class TimeHelper {
	private long startTime;
	private int count = 0;

	public TimeHelper() {
	}

	public void startRunTime() {
		count++;
		if(count == 1) {
			this.startTime = 0;
			this.startTime = System.currentTimeMillis();
		}
	}
	
	public long getRunTimeMillis() {
		return System.currentTimeMillis() - startTime;
	}

	public long getRunTimeSeconds() {
		return (System.currentTimeMillis() - startTime)/1000;
	}
	
	public long getStartTime() {
		return startTime;
	}
}
