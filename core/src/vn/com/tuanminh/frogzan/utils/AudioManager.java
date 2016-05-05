package vn.com.tuanminh.frogzan.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	public static final AudioManager instance = new AudioManager();

	private Music playingMusic;

	// singleton: prevent instantiation from other classes
	private AudioManager() {
	}

	public void play(Sound sound) {
		if (!GamePreferences.instance.enableVol) return;
		sound.play(1f);
	}

	public void play(Music music) {
		playingMusic = music;
		if (GamePreferences.instance.enableVol) {
			music.setLooping(true);
			music.play();
		}
	}

	public void stopMusic() {
		if (playingMusic != null)
			playingMusic.stop();
	}

	public Music getPlayingMusic() {
		return playingMusic;
	}

	public void onSettingsUpdated() {
		if (playingMusic == null)
			return;
		if (GamePreferences.instance.enableVol) {
			if (!playingMusic.isPlaying() )
				playingMusic.play();
		} else {
			playingMusic.pause();
		}
	}
}
