package vn.com.tuanminh.frogzan.game;

import vn.com.tuanminh.frogzan.game.GameWorld.GAME_STATE;
import vn.com.tuanminh.frogzan.objects.Frog;
import vn.com.tuanminh.frogzan.screens.DirectedGame;
import vn.com.tuanminh.frogzan.screens.MenuScreen;
import vn.com.tuanminh.frogzan.transitions.ScreenTransition;
import vn.com.tuanminh.frogzan.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;

public class InputHandler implements InputProcessor {
	private GameWorld myWorld;
	private GameRenderer render;
	private DirectedGame game;
	private OrthographicCamera camera;
	private Frog frog;
	private Body touchedBd = null;
	private Vector3 testPoint;
	private QueryCallback query;
	private int inputCount;

	public InputHandler(GameWorld world, GameRenderer renderer, DirectedGame game) {
		this.myWorld = world;
		this.render = renderer;
		this.game = game;
		this.camera = render.getCamera();
		this.inputCount = 0;
		initObjects();
		testPoint = new Vector3();
		query = new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {

				if (fixture.testPoint(testPoint.x, testPoint.y)
						&& (fixture.getBody().getType() == BodyType.KinematicBody || (fixture
								.getBody().getType() == BodyType.DynamicBody)
								&& fixture.getFilterData().categoryBits != Constants.BIT_FROG)) {
					touchedBd = fixture.getBody();
					return false;

				} else
					return true;
			}
		};
	}

	public void initObjects() {
		frog = myWorld.getFrog();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.BACKSPACE || keycode == Keys.BACK){
			ScreenTransition transition = ScreenTransitionFade.init(0.25f);
			game.setScreen(new MenuScreen(game), transition);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		inputCount++;
		if (myWorld.isRunning() && (inputCount == 1 || inputCount == 2)) {
			frog.rewindHook();
			camera.unproject(testPoint.set(screenX, screenY, 0));
			touchedBd = null;
			myWorld.getWorld().QueryAABB(query, testPoint.x - 0.01f,
					testPoint.y - 0.01f, testPoint.x + 0.01f,
					testPoint.y + 0.01f);

			if (touchedBd != null)
				frog.fireHook(touchedBd, testPoint);
		}

		else if ((myWorld.isGameOver() || myWorld.isHighScore())) {
			myWorld.restart();
			render.restart();
			this.restart();
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(myWorld.isReady())
			myWorld.setState(GAME_STATE.RUNNING);
		
		if (inputCount > 0)
			inputCount--;
		if (inputCount == 0)
			frog.rewindHook();

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		camera.zoom -= amount;
		return true;
	}

	public void restart() {
		if (frog != null) {
			frog = null;
			initObjects();
		}
	}

	public int getInputCount() {
		return inputCount;
	}
}
