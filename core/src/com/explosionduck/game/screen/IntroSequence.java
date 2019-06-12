package com.explosionduck.game.screen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IntroSequence {
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private BitmapFont font;
	
	//http://freemusicarchive.org/genre/Chiptune/
	int textIndex;
	private static final int TEXT_HIGH_Y = 200, TEXT_MED_Y = 150, TEXT_LOW_Y = 100, TEXT_BOX = 50;
	private static final float END_1 = .30f, END_2 = .34f, END_3 = .50f, END_3p5 = .85f;
	private static final float END_4 = END_3p5;
	private static final float END_5 = END_4 + .40f;
	final static List<TimedItem> INTRO_TEXT = Collections.unmodifiableList(Arrays.asList(
			new TimedItem("It is the year 2025.", .02f, END_1,100,TEXT_HIGH_Y),
			new TimedItem("We have ascended to the stars...", .10f,END_2,50,TEXT_MED_Y),
			new TimedItem("... and are at war with the wicked starlord", .18f,END_2,30,TEXT_LOW_Y),
			new TimedItem(Assets.VILLAIN, END_1,END_3,90,TEXT_BOX+20),
			new TimedItem("         VEOX", END_1,END_3,100,TEXT_BOX),
			new TimedItem("We are losing. There is little hope.", END_3,END_3+.20f,50,TEXT_HIGH_Y),
			new TimedItem("Only one man can save us now...", END_3+.10f,END_3+.25f,45,TEXT_MED_Y),
			new TimedItem(Assets.HERO, END_3+.20f,END_3p5,90,TEXT_BOX+20),
			new TimedItem("NAMELESS HERO", END_3+.20f,END_3p5,100,TEXT_BOX),
			new TimedItem("It is a battle for the fate of humanity.", END_4,END_4+.20f,45,TEXT_HIGH_Y),
			new TimedItem("It is a fight which will CONNECT the past", END_4+.10f,END_4+.30f,30,TEXT_MED_Y),
			new TimedItem("... and the future...", END_4+.20f,END_5,100,TEXT_LOW_Y)
			));
	private float timer, bgAlpha;
	//
	private int timer2;
	private boolean drawTapText;
	
	public IntroSequence(OrthographicCamera cam, SpriteBatch batch, BitmapFont font) {
		this.cam = cam;
		this.batch = batch;
		this.font = font;
	}
	
	// assume called 1x/frame
	private void update() {
		if (timer < END_5) {
			timer += .0005;
			if (bgAlpha < .99f)
				bgAlpha = timer * .40f;
		}
			
	}
	
	public void render() {
		update();
		batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, bgAlpha);
        batch.draw(Assets.SPLASH_BG, 0f, 0f);
        font.setColor(1, 1, 1, 1);
        
        renderEverything();
        batch.end();
	}
	
	private void renderEverything() {
		if (timer < END_5) {
			for (textIndex=0; textIndex<INTRO_TEXT.size(); textIndex++) {
				TimedItem timed = INTRO_TEXT.get(textIndex);
				if (timed.end > timer && timer > timed.start) {
					final boolean isText = timed.text != null;
					final float mp = isText ? .3f/4 : .1f/4;
					
					final float range = (timed.end - timed.start);
					final float midPoint = range * mp;
					final float progress = (timer - timed.start);
					final float waxingEnd = timed.start + midPoint;
					final float alpha;
					if (timer < waxingEnd) {
						alpha = progress / midPoint;
					} else {
						final float waningProgress = timer - waxingEnd;
						final float waningPeriod = timed.end - waxingEnd;
						alpha = 1f - (waningProgress / waningPeriod);
					}
					
					if (isText) {
						font.setColor(1f, 1f, 1f, alpha);
						font.draw(batch, timed.text, timed.x, timed.y);
					} else {
						batch.setColor(1, 1, 1, alpha);
						batch.draw(timed.img, timed.x, timed.y);
					}
				}
			}
		} else {
			batch.setColor(1, 1, 1, 1);
			batch.draw(Assets.SPLASH_TITLE, 9, TEXT_MED_Y-40);
			
			timer2++;
			if (timer2 > 60) {
				drawTapText = !drawTapText;
				timer2 = 0;
			}
			if (drawTapText) {
				font.setColor(1, 1, 1, 1);
				font.draw(batch, "Double tap to play!", 100, 90);
			}
		}
	}
	
	private static class TimedItem {
		final float x, y;
		final float start, end;
		String text;
		Texture img;
		TimedItem(String text, float start, float end, float x, float y) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.start = start;
			this.end = end;
		}
		TimedItem(Texture img, float start, float end, float x, float y) {
			this.img = img;
			this.x = x;
			this.y = y;
			this.start = start;
			this.end = end;
		}
		
	}
}
