package com.explosionduck.game.screen;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

	static final Texture BACKGROUND = new Texture("background.png");
	static final Texture VILLAIN = new Texture("villain.png");
	static final Texture HERO = new Texture("hero.png");
	static final Texture FOREGROUND = new Texture("foreground.png");
	static final Texture SPLASH_BG = new Texture("splash_bg.png");
	static final Texture SPLASH_TITLE = new Texture("splash_title.png");
	static final Texture TOKEN_BLUE = new Texture("tblue.png");
	static final Texture TOKEN_GREEN = new Texture("tgreen.png");
	static final Texture TEXTBOX = new Texture("textbox.png");
	
	public static final Music INTRO_MUSIC = 
			Gdx.audio.newMusic(Gdx.files.getFileHandle("Creo_-_Showdown.mp3", FileType.Local));
	
	public static final class Sounds {
		public static final Sound START_GAME = Gdx.audio.newSound(Gdx.files.local("startgame0.wav"));
		public static final Sound REJECT_SELECTION = Gdx.audio.newSound(Gdx.files.local("reject.wav"));
		public static final Sound PLAYER_SELECT_COLUMN = Gdx.audio.newSound(Gdx.files.local("select1.wav"));
		public static final Sound PLAYER_FIRE_SHOT = Gdx.audio.newSound(Gdx.files.local("fire01.wav"));
		public static final Sound CPU_SELECT_COLUMN = Gdx.audio.newSound(Gdx.files.local("select2.wav"));
		public static final Sound CPU_FIRE_SHOT = Gdx.audio.newSound(Gdx.files.local("fire02.wav"));
		public static final Sound TOKEN_LANDING = Gdx.audio.newSound(Gdx.files.local("landing02.wav"));
	}

}
