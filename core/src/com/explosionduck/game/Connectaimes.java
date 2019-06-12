package com.explosionduck.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.explosionduck.game.processing.GameStateProcessor;
import com.explosionduck.game.screen.Assets;
import com.explosionduck.game.screen.Screen;

public class Connectaimes extends ApplicationAdapter {
	private GameStateProcessor gsp;

	@Override
	public void create () {
        gsp = new GameStateProcessor(7);
        Gdx.input.setInputProcessor(gsp.getInputProcessor());
        resize(Screen.VIRTUAL_WIDTH, Screen.VIRTUAL_HEIGHT);
	}

	@Override
	public void render () {
        gsp.process();
		gsp.render();
	}
	
	@Override
	public void resize(int width, int height) {
		float aspectRatio = (float) width / (float) height;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);
		if (aspectRatio > Screen.ASPECT_RATIO) {
			scale = (float) height / (float) Screen.VIRTUAL_HEIGHT;
			crop.x = (width - Screen.VIRTUAL_WIDTH * scale) / 2f;
		} else if (aspectRatio < Screen.ASPECT_RATIO) {
			scale = (float) width / (float) Screen.VIRTUAL_WIDTH;
			crop.y = (height - Screen.VIRTUAL_HEIGHT * scale) / 2f;
		} else {
			scale = (float) width / (float) Screen.VIRTUAL_WIDTH;
		}

		float w = (float) Screen.VIRTUAL_WIDTH * scale;
		float h = (float) Screen.VIRTUAL_HEIGHT * scale;
		Rectangle viewport = new Rectangle(crop.x, crop.y, w, h);
		
		gsp.screen().setViewPort(viewport);
	}
	
	@Override
	public void dispose () {
		Assets.INTRO_MUSIC.dispose();
	}
}
