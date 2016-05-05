package vn.com.tuanminh.frogzan.screens;

import vn.com.tuanminh.frogzan.transitions.ScreenTransition;
import vn.com.tuanminh.frogzan.transitions.ScreenTransitionSlide;
import vn.com.tuanminh.frogzan.utils.TimeHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SplashScreen extends AbstractGameScreen {
	private Texture texture;
	private Stage stage;
	private TimeHelper timer;

	public SplashScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		System.out.println("Splash show");
		// stage
		stage = new Stage(new StretchViewport(480, 800));

		// texture
		texture = new Texture(Gdx.files.internal("splash/splash.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Actor splash = new Image(texture);
		stage.addActor(splash);
		timer = new TimeHelper();
		timer.startRunTime();
		
		// Google Analytics
		game.handler.setTrackerScreenName(true);
	}

	@Override
	public void hide() {
		System.out.println("Splash hide");
		stage.dispose();
		texture.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputProcessor getInputProcessor() {
		return this;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		ScreenTransition transition = ScreenTransitionSlide.init(1f,
				ScreenTransitionSlide.DOWN, false, Interpolation.bounceOut);
		if (timer.getRunTimeSeconds() > 2)
			game.setScreen(new MenuScreen(game), transition);
		return true;
	}
}
