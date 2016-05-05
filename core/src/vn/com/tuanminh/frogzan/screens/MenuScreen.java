package vn.com.tuanminh.frogzan.screens;

import vn.com.tuanminh.frogzan.game.Assets;
import vn.com.tuanminh.frogzan.transitions.ScreenTransition;
import vn.com.tuanminh.frogzan.transitions.ScreenTransitionFade;
import vn.com.tuanminh.frogzan.utils.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.swarmconnect.Swarm;

public class MenuScreen extends AbstractGameScreen {
	private Stage stage;
	private Skin skin;
	private Button playBut;
	private Button volBut;
	private Button rateBut;
	private Button leaderboardBut;
	private Button muteBut;
	private Image imgBackground;
	private Image frogzanText;
	private InputMultiplexer mulInput;

	public MenuScreen(DirectedGame game) {
		super(game);
	}

	public void buildStage() {

		skin = Assets.instance.level.uiSkin;

		Table layerBackground = buildBackground();
		Table layerObjects = buildObjects();
		Table layerControl = buildControl();

		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(480, 800);
		stack.addActor(layerBackground);
		stack.addActor(layerObjects);
		stack.addActor(layerControl);
	}

	public Table buildBackground() {
		Table layer = new Table();
		// background
		imgBackground = new Image(skin, "background");
		imgBackground.setSize(480, 800);
		layer.addActor(imgBackground);

		// frogzan text
		frogzanText = new Image(skin, "frogzan");
		frogzanText.setPosition(480 / 2 - frogzanText.getWidth() / 2,
				800 - 2.5f * frogzanText.getHeight());
		layer.addActor(frogzanText);
		return layer;
	}

	public Table buildObjects() {
		Table layer = new Table();
		return layer;
	}

	public Table buildControl() {
		Table layer = new Table();

		// Play Button
		playBut = new Button(skin, "play");
		playBut.setCenterPosition(480 / 2, 800 / 2 + 20);
		layer.addActor(playBut);
		playBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();

			}

		});

		// rate
		rateBut = new Button(skin, "rate");
		rateBut.setCenterPosition(playBut.getCenterX(),
				playBut.getCenterY() - 110);
		layer.addActor(rateBut);
		rateBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				onRateClicked();
			}

		});

		// leaderboard
		leaderboardBut = new Button(skin, "leaderboard");
		leaderboardBut.setCenterPosition(playBut.getCenterX(),
				rateBut.getCenterY() - 110);
		layer.addActor(leaderboardBut);
		leaderboardBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				onLeaderboardClicked();
			}

		});

		// mute
		muteBut = new Button(skin, "mute");
		layer.addActor(muteBut);

		if (GamePreferences.instance.enableVol)
			muteBut.setVisible(false);
		else
			muteBut.setVisible(true);

		muteBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onMuteClicked();

			}

		});

		// vol
		volBut = new Button(skin, "vol");
		layer.addActor(volBut);
		volBut.setVisible(GamePreferences.instance.enableVol);
		volBut.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onVolClicked();

			}

		});
		return layer;
	}

	public void onMuteClicked() {
		setVisible(true, false, true);
	}

	public void onVolClicked() {
		setVisible(false, true, false);
	}

	private void setVisible(boolean volVisible, boolean muteVisible,
			boolean enableVol) {
		volBut.setVisible(volVisible);
		muteBut.setVisible(muteVisible);
		GamePreferences.instance.enableVol = enableVol;
		GamePreferences.instance.save();
	}

	public void onPlayClicked() {
		ScreenTransition transition = ScreenTransitionFade.init(0.25f);
		game.setScreen(new GameScreen(game), transition);
	}

	public void onRateClicked() {
		game.handler.openURL();
	}

	public void onLeaderboardClicked() {
		if (!Swarm.isLoggedIn()) {
			game.handler.activateSwarm();
			;
			Swarm.showLeaderboards();
		} else
			Swarm.showLeaderboards();

	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		System.out.println("Menu show");
		game.handler.showAd(false);
		stage = new Stage(new StretchViewport(480, 800));
		Gdx.input.setCatchBackKey(true);
		mulInput = new InputMultiplexer();
		mulInput.addProcessor(stage);
		mulInput.addProcessor(this);
		buildStage();
	}

	@Override
	public void hide() {
		System.out.println("Menu hide");
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public InputProcessor getInputProcessor() {
		// TODO Auto-generated method stub
		return mulInput;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.BACKSPACE)
			Gdx.app.exit();
		return true;
	}
}