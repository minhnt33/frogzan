package vn.com.tuanminh.frogzan.game;

import net.dermetfan.utils.libgdx.graphics.Box2DSprite;
import vn.com.tuanminh.frogzan.objects.Frog;
import vn.com.tuanminh.frogzan.objects.SpawnGround;
import vn.com.tuanminh.frogzan.utils.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer implements Disposable {
	private GameWorld myWorld;
	private OrthographicCamera camera;
	private OrthographicCamera bgCamera;
	private ShapeRenderer shapeRen;
	private Frog frog;
	private SpawnGround spawner;
	private float gameHeight;
	private float PPM;
	private SpriteBatch batch;
	private TextureRegion frog1, frog2;
	private Sprite frogSprite;
	private TextureRegion background, scoreboard, gameover, highscore, retry,
			ready;

	private final String r1 = "SUPER NOOB";
	private final String r2 = "NOOB";
	private final String r3 = "PRETTY NOOB";
	private final String r4 = "GOOD";
	private final String r5 = "NICE";
	private final String r6 = "AWESOME";
	private final String r7 = "UNSTOPABLE";
	
	public GameRenderer(GameWorld world, float myGameHeight) {
		myWorld = world;
		gameHeight = myGameHeight;
		PPM = myWorld.getPPM();
		camera = new OrthographicCamera();
		camera.setToOrtho(true, 136f / PPM, gameHeight / PPM);
		bgCamera = new OrthographicCamera();
		bgCamera.setToOrtho(true, 136f, gameHeight);
		shapeRen = new ShapeRenderer();
		batch = new SpriteBatch();
		initObjects();
		initAssets();
	}

	public void initObjects() {
		frog = myWorld.getFrog();
		spawner = myWorld.getSpawnGround();
	}

	public void initAssets() {
		frog1 = Assets.instance.frog.frog1;
		frog1.flip(false, true);
		frog2 = Assets.instance.frog.frog2;
		frog2.flip(false, true);

		background = Assets.instance.level.background;
		background.flip(false, true);

		scoreboard = Assets.instance.level.scoreBoard;
		scoreboard.flip(false, true);

		gameover = Assets.instance.level.gameoverText;
		gameover.flip(false, true);

		highscore = Assets.instance.level.highscoreText;
		highscore.flip(false, true);

		retry = Assets.instance.level.retryText;
		retry.flip(false, true);

		ready = Assets.instance.level.readyText;
		ready.flip(false, true);
	}

	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// GUI camera
		batch.setProjectionMatrix(bgCamera.combined);
		batch.begin();
		batch.disableBlending();
		drawBackground();
		batch.enableBlending();
		drawFPS();

		if (myWorld.isReady()) {
			drawReady();
		}

		else if (myWorld.isRunning()) {
			drawScore();
		}

		else if (myWorld.isGameOver()) {
			drawScoreBoard();
			drawGameOver();
			drawRetry();
		}

		else if (myWorld.isHighScore()) {
			drawScoreBoard();
			drawHighscore();
			drawRetry();
		}

		batch.end();

		// Game camera
		batch.setProjectionMatrix(camera.combined);
		shapeRen.setProjectionMatrix(camera.combined);
		shapeRen.begin(ShapeType.Filled);
		batch.begin();
		this.moveCamera();

		if (myWorld.isRunning() || myWorld.isReady()) {
			// draw stuff
			// drawObstacleJoint();
			Box2DSprite.draw(batch, myWorld.getWorld());
			if (frog.isHooked()) {
				drawFrog(frog2);
				drawTongue();

			} else
				drawFrog(frog1);

			if (frog.isBooming()) {
				drawBoomEffect(runTime);
				frog.getFrogBd().setUserData(null);
			}
		}

		else if (myWorld.isPaused()) {

		}

		batch.end();
		shapeRen.end();
	}

	public void restart() {
		if (frog != null || spawner != null) {
			frog = null;
			spawner = null;
			initObjects();
			moveCamera();
		}
	}

	public void drawFrog(TextureRegion region) {
		frogSprite = new Sprite(region);
		frog.getFrogBd().setUserData(new Box2DSprite(frogSprite));
	}

	public void drawBackground() {
		batch.draw(background, 0, 0, 136f, gameHeight);
	}

	public void drawTongue() {
		shapeRen.setColor(Color.RED);
		shapeRen.rectLine(new Vector2(frog.getDistanceJoint().getAnchorA().x,
				frog.getDistanceJoint().getAnchorA().y), new Vector2(frog
				.getDistanceJoint().getAnchorB().x, frog.getDistanceJoint()
				.getAnchorB().y), 2f / PPM);
	}

	// public void drawObstacleJoint() {
	// for (Obstacle obstacle : spawner.getObstacleArray()) {
	// if(obstacle != null) {
	// shapeRen.setColor(Color.ORANGE);
	// shapeRen.rectLine(new Vector2(
	// obstacle.getDisJoint().getAnchorA().x, obstacle
	// .getDisJoint().getAnchorA().y), new Vector2(
	// obstacle.getDisJoint().getAnchorB().x, obstacle
	// .getDisJoint().getAnchorB().y), 1f / PPM);
	// }
	// }
	// }

	public void drawScore() {
		int lenght = ("" + myWorld.getScore()).length();
		Assets.instance.font.fontGreen.draw(batch, "" + myWorld.getScore(),
				136 / 2 - lenght * 3, 40f);
	}

	public void drawScoreBoard() {
		int midRankX = 49;
		int midRankY = (int) ((gameHeight - 40) / 2 + 20);

		// Draw board
		batch.draw(scoreboard, (136f - 110f) / 2, (gameHeight - 40f) / 2, 110f,
				40f);
		
		// Draw rank
		if (myWorld.getScore() <= 5)
			Assets.instance.font.fontWhite.draw(batch, r1, midRankX
					- r1.length() * 3, midRankY);
		else if (myWorld.getScore() > 5 && myWorld.getScore() <= 10)
			Assets.instance.font.fontWhite.draw(batch, r2, midRankX
					- r2.length() * 3, midRankY);
		else if (myWorld.getScore() > 10 && myWorld.getScore() <= 20)
			Assets.instance.font.fontWhite.draw(batch, r3, midRankX
					- r3.length() * 3, midRankY);
		else if (myWorld.getScore() > 20 && myWorld.getScore() <= 50)
			Assets.instance.font.fontWhite.draw(batch, r4, midRankX
					- r4.length() * 3, midRankY);
		else if (myWorld.getScore() > 50 && myWorld.getScore() <= 80)
			Assets.instance.font.fontWhite.draw(batch, r5, midRankX
					- r5.length() * 3, midRankY);
		else if (myWorld.getScore() > 80 && myWorld.getScore() <= 120)
			Assets.instance.font.fontWhite.draw(batch, r6, midRankX
					- r6.length() * 3, midRankY);
		else if (myWorld.getScore() > 120)
			Assets.instance.font.fontWhite.draw(batch, r7, midRankX
					- r7.length() * 3, midRankY);

		int midScoreX = 104;
		// Draw score
		Assets.instance.font.fontWhite.draw(batch, "" + myWorld.getScore(),
				midScoreX - ("" + myWorld.getScore()).length() * 3,
				midRankY - 10);
		Assets.instance.font.fontWhite.draw(batch, ""
				+ GamePreferences.instance.highscore, midScoreX
				- ("" + GamePreferences.instance.highscore).length() * 3,
				midRankY + 10);
	}

	public void drawGameOver() {
		batch.draw(gameover, (136 - 110) / 2, gameHeight / 2 - 60, 110, 20);
	}

	public void drawHighscore() {
		batch.draw(highscore, (136f - 110) / 2, gameHeight / 2 - 60, 110, 20);
	}

	public void drawRetry() {
		batch.draw(retry, (136 - 60) / 2, gameHeight / 2 + 20, 60, 15);
	}

	public void drawReady() {
		batch.draw(ready, (136 - 60) / 2, 130, 60, 15);
	}

	public void drawFPS() {
		Assets.instance.font.fontWhite.draw(batch,
				"" + Gdx.graphics.getFramesPerSecond(), 5, 20);
	}

	public void drawBoomEffect(float runTime) {
		batch.draw(Assets.instance.ground.boomEffect.getKeyFrame(runTime), frog
				.getFrogBd().getWorldCenter().x - 50 / PPM, frog.getFrogBd()
				.getWorldCenter().y - 50 / PPM,
				Assets.instance.ground.boomEffect.getKeyFrame(runTime)
						.getRegionWidth() / PPM,
				Assets.instance.ground.boomEffect.getKeyFrame(runTime)
						.getRegionHeight() / PPM);
	}

	public void moveCamera() {
		if (frog.getX() > 136 / PPM - frog.getWidth())
			frog.getFrogBd().setLinearVelocity(-3, 0);
		if (frog.getX() - frog.getWidth() / 2 < 0)
			frog.getFrogBd().setLinearVelocity(3, 0);

		camera.position.y = frog.getY() - gameHeight / 2 / PPM + 35 / PPM;
		camera.update();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public OrthographicCamera getGUICamera() {
		return bgCamera;
	}

	@Override
	public void dispose() {
		System.out.println("renderer dispose");
		camera = null;
		bgCamera = null;
		batch.dispose();
		shapeRen.dispose();
	}
}
