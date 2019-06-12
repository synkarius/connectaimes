package com.explosionduck.game.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.explosionduck.game.processing.Token;

public class TokenDropAnimator {
	// if y=5, don't bother
    AnimState state = AnimState.OFF;
    private static final int MAX_FLASHES = 5;
    int flashes;
    
    //
    int colx;
    int starty, desty;
    float intervaly;
    Token dropAnimPlayer;
    final int wu, hu;
    int ticks;
    final SpriteBatch batch;
    private static final int SCREEN_HEIGHT = 240;
    
    public enum AnimState {
    	OFF, RUNNING, COMPLETE
    }
    
    public TokenDropAnimator(SpriteBatch batch, int wu, int hu) {
    	this.batch = batch;
    	this.wu = wu;
    	this.hu = hu;
    }
    
    public void initDropAnim(Token player, int x, int y) {
		colx = (x * wu) + 7;
		starty = SCREEN_HEIGHT - hu - 10;
		desty = (y * hu) + 3;
		intervaly = (starty - desty) / ((float) MAX_FLASHES);
		state = AnimState.RUNNING;
    	flashes = 0;
    	ticks = 0;
    	dropAnimPlayer = player;
    }
    
	public void updateDropAnim() {
		if (ticks++ > 2) {
			flashes += 1;
			ticks = 0;
		}
		
		if (flashes < MAX_FLASHES) {
			batch.setColor(1, 1, 1, .5f);
			batch.draw(dropAnimPlayer == Token.GREEN ? Assets.TOKEN_GREEN : Assets.TOKEN_BLUE, colx,
					SCREEN_HEIGHT - hu - (flashes * intervaly));
			batch.setColor(1, 1, 1, 1);
		} else {
			state = AnimState.COMPLETE;
    	}
    }
	
	public boolean isOn() {
		return state != AnimState.OFF;
	}
	
	public boolean isComplete() {
		return state == AnimState.COMPLETE;
	}
	
	public void reset() {
		state = AnimState.OFF;
	}
}
