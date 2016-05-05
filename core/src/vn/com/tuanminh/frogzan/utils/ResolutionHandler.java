package vn.com.tuanminh.frogzan.utils;

import com.badlogic.gdx.Gdx;

public class ResolutionHandler {

	public static final ResolutionHandler instance = new ResolutionHandler();
	private static final float gameWidth = 136;
	private float gameHeight;
	private float scaleFactorX;
	private float scaleFactorY;

	private ResolutionHandler() {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		this.scaleFactorX = screenWidth / gameWidth;
		this.gameHeight = screenHeight / scaleFactorX;
		this.scaleFactorY = screenHeight / gameHeight;


	}

	public float getGameHeight() {
		return this.gameHeight;
	}

	public float getScaleFactorX() {
		return this.scaleFactorX;
	}

	public float getScaleFactorY() {
		return this.scaleFactorY;
	}
}
