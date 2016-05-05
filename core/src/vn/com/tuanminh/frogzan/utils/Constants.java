package vn.com.tuanminh.frogzan.utils;


public class Constants {
	public static final String TEXTURE_ATLAS = "texture/frogzan.pack"; 
	public static final String UI_JSON = "texture/ui.json";
	public static final String TEXTURE_ATLAS_UI = "texture/ui.pack";
	public static final String PREFERENCES = "frogzan.pref";
	
	//Box2D
	public static final short BIT_FROG = 2;
	public static final short BIT_GROUND = 4;
	public static final short BIT_OBSTACLE = 8;
	
	//Frog properties
	public static final float FROG_DENSITY = 1f;
	public static final float FROG_FRICTION = 0;
	public static final float FROG_RESTITUTION = 0.8f;
	public static final float REWIND_SPEED = 0.975f;
	
	//Obstacle properties
	public static final float RAD = 9f;
	public static final float ROTATION_SPEED = 8f;
	public static final float MAX_SPEED = 2f;
	public static final float MIN_SPEED = 1f;
	public static final float MAX_LENGHT = 1.5f;
	public static final float MIN_LENGTH = 0.8f;
	
	//Ground properties
	public static final float GROUND_DENSITY = 1f;
	public static final float GROUND_FRICTION = 0;
	public static final float GROUND_RESTITUTION = 0.8f;
	public static final float GAP_TO_CREATE = 100f;
	public static final float GAP_TO_DELETE = 90f;
	public static final float FIRST_GAP = 130f;
	public static final float GAP_GROUNDS = 100f;
	public static final float LONG_GROUND_WIDTH = 23f;
	public static final float SHORT_GROUND_WIDTH = 13f;
	public static final float NORMAL_GROUND_WIDTH = 18f;
	public static final float GROUND_HEIGHT = 12f;

	
}
