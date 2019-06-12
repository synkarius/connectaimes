package com.explosionduck.game.screen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TauntAnimator {
	private static final List<Taunt> TAUNTS;
	static {
		List<Taunt> taunts = Arrays.asList(
				new Taunt("Nice move bro."),
				new Taunt("OMFG. Too easy."),
				new Taunt("Just stop."),
				new Taunt("I can't even.", "I can't even odd."),
				new Taunt("If I had a nuke for every", "bad move you make...", "Oh wait, I do. HAHAHA."),
				new Taunt("I get it. I get it.", "This may come as a shock, but...", "you're supposed to try to win."),
				new Taunt("Please."),
				new Taunt("Should have quit while you", "were ahead. Wait, when", "was that?")
				);
		Collections.shuffle(taunts);
		TAUNTS = Collections.unmodifiableList(taunts);
	}
	
	private static class Taunt {
		private Taunt(String... lines) {
			this.lines = Collections.unmodifiableList(Arrays.asList(lines));
		}

		private final List<String> lines;
		/** for multi-line taunts */
		public int lineIndex;
	}

	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	private final SpriteBatch batch;
	private final BitmapFont font;

	//
	private Taunt taunt;
	private int tauntIndex;
	
	public TauntAnimator(SpriteBatch batch, BitmapFont font) {
		this.batch = batch;
		this.font = font;
	}

	public void initTaunt() {
		taunt = TAUNTS.get(tauntIndex++);
		if (tauntIndex > TAUNTS.size() - 1)
			tauntIndex = 0;
	}
	
	public boolean isOn() {
		return taunt != null;
	}
	
	public void setTauntLineIndex(int tauntIndex) {
		taunt.lineIndex = tauntIndex;
		if (taunt.lineIndex > taunt.lines.size() - 1) {
			// reset the taunt
			taunt.lineIndex = 0;
			taunt = null;
		}
	}
	
	public void draw() {
		batch.setColor(1, 1, 1, 1);
		font.setColor(1, 1, 1, 1);
		
		batch.draw(Assets.VILLAIN, 94, 65);
		batch.draw(Assets.TEXTBOX, 6, 6);
		
		String line = taunt.lines.get(taunt.lineIndex);
		font.draw(batch, line, 15, 40);
	}
}
