package com.explosionduck.game.screen;

public class MusicFader {
	
	private float volume = 1;
	private int ticks;
	private boolean fading;
	

	public void startFade() {
		fading = true;
	}
	
	public void run() {
		if (fading) {
			if (ticks++ > 6) {
				ticks = 0;
				volume -= .05;
				if (volume <= .06) {
					Assets.INTRO_MUSIC.stop();
					Assets.INTRO_MUSIC.dispose();
					fading = false;
				} else {
					Assets.INTRO_MUSIC.setVolume(volume);
				}
			}
		}
	}
	
}
