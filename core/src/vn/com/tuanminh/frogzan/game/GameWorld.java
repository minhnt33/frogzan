package vn.com.tuanminh.frogzan.game;

import vn.com.tuanminh.frogzan.objects.Frog;
import vn.com.tuanminh.frogzan.objects.SpawnGround;
import vn.com.tuanminh.frogzan.utils.GamePreferences;
import vn.com.tuanminh.frogzan.utils.TimeHelper;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.swarmconnect.SwarmLeaderboard;

public class GameWorld extends InputAdapter implements Disposable {
	private World world;
	private Box2DContactListener cl;
	private Frog frog;
	private SpawnGround spawner;
	private TimeHelper timer;
	private float runTime;
	private int score;
	private static final float PPM = 32;
	private float gameHeight;
	
	private static final long ONE_SECOND_NS = 1000000000;
	private static final int MAX_FPS = 60;
	private int maxUpdates = 40;
	
	private long lastTime = System.nanoTime();

	public enum GAME_STATE {
		READY, RUNNING, PAUSED, GAME_OVER, HIGH_SCORE
	};

	private GAME_STATE curState;
	private static final float GAP_TO_DIE = 300f;

	public GameWorld(float myGameHeight) {
		this.gameHeight = myGameHeight;
		createWorld();
	}

	public void createWorld() {
		world = new World(new Vector2(0, 9.8f), true);
		cl = new Box2DContactListener(this);
		world.setContactListener(cl);
		timer = new TimeHelper();
		curState = GAME_STATE.READY;
		this.runTime = 0;
		this.score = 0;
		frog = new Frog(world, 136f / 2, gameHeight - 60f, 8f, 8f, PPM);
		spawner = new SpawnGround(this, gameHeight, PPM);
	}

	public void update(float delta) {
		runTime += delta;

		switch (curState) {
		case RUNNING:
			updateRunning(delta);
		case PAUSED:
			break;
		default:
			break;
		}
	}

	public void updateRunning(float delta) {
//		long time = System.nanoTime();
//		long timeDelta = time - lastTime;
//		float timeDeltaSeconds = timeDelta / (float) ONE_SECOND_NS;
//		lastTime = time;
//		
//		int updateCount = 0;
//		
//		while(timeDelta > 0 && (maxUpdates <= 0 || updateCount < maxUpdates)) {
//			long updateTimeStep = Math.min(timeDelta, ONE_SECOND_NS / MAX_FPS);
//			float updateTimeStepSeconds = updateTimeStep / (float) ONE_SECOND_NS;
//			
//			// World step + update objects
//
//			
//			timeDelta -= updateTimeStep;
//			updateCount++;
//		}
		world.step(1 / 60f, 8, 3);
		frog.update(delta);
		spawner.update(delta);

//		long sleepTime = Math.round((ONE_SECOND_NS / MAX_FPS) - (System.nanoTime() - lastTime));
//		if(sleepTime <= 0)
//			return;
//		long prevTime = System.nanoTime();
//		while(System.nanoTime() - prevTime <= sleepTime)
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		if (frog.getY() - spawner.getGroundArray().get(0).getY() > GAP_TO_DIE
				/ PPM) {
			frog.dieByFalling();
			curState = GAME_STATE.GAME_OVER;
		}

		if (frog.isBooming()) {
			timer.startRunTime();
			if (timer.getRunTimeMillis() > 800)
				curState = GAME_STATE.GAME_OVER;
		}

		if (score > GamePreferences.instance.highscore && isGameOver()) {
			curState = GAME_STATE.HIGH_SCORE;
			GamePreferences.instance.highscore = score;
			GamePreferences.instance.save();
		}

//		 if(curState == GAME_STATE.GAME_OVER || curState ==
//		 GAME_STATE.HIGH_SCORE)
//		 SwarmLeaderboard.submitScore(17959, score);
	}

	public void restart() {
		destroyBodies();
		destroyObjects();
		createWorld();
	}

	public void destroyBodies() {
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);

		for (Body bd : bodies) {
			world.destroyBody(bd);
			bd.setUserData(null);
		}
	}

	public void destroyObjects() {
		world = null;
		cl = null;
		frog = null;
		spawner = null;
	}

	public void addScore() {
		score++;
	}

	public void setScore(int sc) {
		score = sc;
	}

	public Frog getFrog() {
		return frog;
	}

	public SpawnGround getSpawnGround() {
		return spawner;
	}

	public World getWorld() {
		return world;
	}

	public float getRunTime() {
		return runTime;
	}

	public int getScore() {
		return score;
	}

	public float getPPM() {
		return PPM;
	}

	public float getGameHeight() {
		return gameHeight;
	}

	public boolean isReady() {
		return curState == GAME_STATE.READY;
	}

	public boolean isRunning() {
		return curState == GAME_STATE.RUNNING;
	}

	public boolean isPaused() {
		return curState == GAME_STATE.PAUSED;
	}

	public boolean isHighScore() {
		return curState == GAME_STATE.HIGH_SCORE;
	}

	public boolean isGameOver() {
		return curState == GAME_STATE.GAME_OVER;
	}

	public void setState(GAME_STATE gameState) {
		curState = gameState;
	}

	@Override
	public void dispose() {
		System.out.println("game world dispose");
		world.dispose();
	}
}
