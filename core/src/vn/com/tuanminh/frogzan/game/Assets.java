package vn.com.tuanminh.frogzan.game;

import vn.com.tuanminh.frogzan.utils.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
	public static final Assets instance = new Assets();
	private AssetManager assetManager;

	public AssetFont font;
	public AssetFrog frog;
	public AssetGround ground;
	public AssetLevel level;
	public AssetSound sound;
	public AssetMusic music;

	public class AssetFont {
		public final BitmapFont fontGreen;
		public final BitmapFont fontWhite;

		public AssetFont() {
			fontGreen = new BitmapFont(Gdx.files.internal("font/text.fnt"));
			fontGreen.setScale(0.25f, -0.25f);
			fontGreen.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			fontWhite = new BitmapFont(Gdx.files.internal("font/whitetext.fnt"));
			fontWhite.setScale(0.15f, -0.15f);
			fontWhite.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public class AssetFrog {
		public final AtlasRegion frog1;
		public final AtlasRegion frog2;

		public AssetFrog(TextureAtlas atlas) {
			frog1 = atlas.findRegion("frog1");
			frog2 = atlas.findRegion("frog2");
		}
	}

	public class AssetGround {
		public final AtlasRegion ground;
		public final AtlasRegion obstacle;
		public final Array<AtlasRegion> regions;
		public final Animation boomEffect;

		public AssetGround(TextureAtlas atlas) {
			ground = atlas.findRegion("ground");
			obstacle = atlas.findRegion("obstacle");

			regions = new Array<AtlasRegion>();
			regions.add(atlas.findRegion("boom1"));
			regions.add(atlas.findRegion("boom2"));
			regions.add(atlas.findRegion("boom3"));
			regions.add(atlas.findRegion("boom4"));
			regions.add(atlas.findRegion("boom5"));
			regions.add(atlas.findRegion("boom6"));
			regions.add(atlas.findRegion("boom7"));
			regions.add(atlas.findRegion("boom8"));
			regions.add(atlas.findRegion("boom9"));

			boomEffect = new Animation(1 / 10f, regions,
					Animation.PlayMode.LOOP);
		}
	}

	public class AssetLevel {
		public final AtlasRegion background;
		public final AtlasRegion scoreBoard;
		public final AtlasRegion frogzanText;
		public final AtlasRegion gameoverText;
		public final AtlasRegion highscoreText;
		public final AtlasRegion retryText;
		public final AtlasRegion readyText;
		public final Skin uiSkin;

		public AssetLevel(TextureAtlas atlas, TextureAtlas uiAtlas) {
			background = atlas.findRegion("background");
			scoreBoard = atlas.findRegion("scoreboard");
			frogzanText = atlas.findRegion("frogzan");
			gameoverText = atlas.findRegion("gameover");
			highscoreText = atlas.findRegion("highscore");
			retryText = atlas.findRegion("retry");
			readyText = atlas.findRegion("ready");
			uiSkin = new Skin(Gdx.files.internal(Constants.UI_JSON), uiAtlas);
		}
	}

	public class AssetSound {
		public final Sound shoot;
		public final Sound score;
		public final Sound die;
		public final Sound boom;

		public AssetSound(AssetManager am) {
			shoot = am.get("sound/shoot.wav", Sound.class);
			score = am.get("sound/score.wav", Sound.class);
			die = am.get("sound/die.wav", Sound.class);
			boom = am.get("sound/boom.mp3", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music backgroundMusic;

		public AssetMusic(AssetManager am) {
			backgroundMusic = am.get("music/song1.mp3", Music.class);
		}
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;

		assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
		assetManager.load(Constants.TEXTURE_ATLAS_UI, TextureAtlas.class);
		assetManager.load("sound/shoot.wav", Sound.class);
		assetManager.load("sound/score.wav", Sound.class);
		assetManager.load("sound/die.wav", Sound.class);
		assetManager.load("sound/boom.mp3", Sound.class);
		assetManager.load("music/song1.mp3", Music.class);
		assetManager.finishLoading();

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		TextureAtlas uiAtlas = assetManager.get(Constants.TEXTURE_ATLAS_UI);
		for (Texture t : uiAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		font = new AssetFont();
		frog = new AssetFrog(atlas);
		ground = new AssetGround(atlas);
		level = new AssetLevel(atlas, uiAtlas);
		sound = new AssetSound(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		font.fontGreen.dispose();
		font.fontWhite.dispose();
		level.uiSkin.dispose();
	}
}
