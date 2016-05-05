package vn.com.tuanminh.frogzan.screens;

import vn.com.tuanminh.frogzan.game.GameRenderer;
import vn.com.tuanminh.frogzan.game.GameWorld;
import vn.com.tuanminh.frogzan.game.InputHandler;
import vn.com.tuanminh.frogzan.utils.ResolutionHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class GameScreen extends AbstractGameScreen {
	private GameWorld myWorld;
	private GameRenderer renderer;
	private InputHandler input;
	private float gameHeight;
	private float runTime = 0;

	public GameScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		runTime += deltaTime;
		myWorld.update(deltaTime);
		renderer.render(deltaTime, runTime);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		System.out.println("Game screen show");
		
		// Create game world
		gameHeight = ResolutionHandler.instance.getGameHeight();
		myWorld = new GameWorld(gameHeight);
		renderer = new GameRenderer(myWorld, gameHeight);
		input = new InputHandler(myWorld, renderer, game);
		
		// Admob show
		game.handler.showAd(true);
		
		// Google Analytics
		game.handler.setTrackerScreenName(false);
		
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		System.out.println("Game screen hide");
		myWorld.dispose();
		renderer.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return input;
	}
}
