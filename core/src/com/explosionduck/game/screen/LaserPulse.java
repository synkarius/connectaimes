package com.explosionduck.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LaserPulse {
	
	private ShapeRenderer shapeRenderer;
    private final int x0, y0;
    // width unit, height unit
    private final int wu, hu;
    
    public enum PulseColor {
    	BLUE, PURPLE
    }
	
	public LaserPulse(ShapeRenderer shapeRenderer, int wu, int hu, int x0, int y0) {
		this.shapeRenderer = shapeRenderer;
		this.wu = wu;
		this.hu = hu;
		this.x0 = x0;
		this.y0 = y0;
	}

    // gets to 30, inc all pulses
    int pulseTick;
    final PulseStat 
            b1p = new PulseStat(13, 2, 0),
    		b2p = new PulseStat(12, 3, 7),
    		w1p = new PulseStat(3, 1, 15),
    		w2p = new PulseStat(1, 1, 19);
    
    
    private float[] b1xw, b2xw, w1xw, w2xw;
    
    private float[] getPulseXAndWidth(float startx, float offset, int pulse) {
    	offset -= pulse;
    	float x = startx + offset;
    	float x2 = x + wu - offset;
    	float width = x2 - x - offset;
    	return new float[] {x, width};
    }
    
    private void pulseTickUp(PulseStat pulse) {
    	if (pulse.current <= 0 || pulse.current >= pulse.max)
    		pulse.up = !pulse.up;
    	pulse.current += pulse.up ? pulse.inc : -pulse.inc;
    }
    
    public  void pulsingSelectedColumn(int column, PulseColor color) {
    	Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        if (pulseTick++ > 4) {
        	pulseTick = 0;
        	pulseTickUp(b1p);
        	pulseTickUp(b2p);
        	pulseTickUp(w1p);
        	pulseTickUp(w2p);
        }
        
        float originx = x0 + ( column * wu) + 2;
        b1xw = getPulseXAndWidth(originx ,b1p.offset, b1p.current);
        b2xw = getPulseXAndWidth(originx, b2p.offset, b2p.current);
        w1xw = getPulseXAndWidth(originx, w1p.offset, w1p.current);
        w2xw = getPulseXAndWidth(originx, w2p.offset, w2p.current);

        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // color
        float red = PulseColor.PURPLE.equals(color) ? .6f : 0;
        shapeRenderer.setColor(red,0,1,.3f);
        shapeRenderer.rect(b1xw[0], y0, b1xw[1], Screen.VIRTUAL_HEIGHT);
        shapeRenderer.setColor(red,0,1,.4f);
        shapeRenderer.rect(b2xw[0], y0, b2xw[1], Screen.VIRTUAL_HEIGHT);
        
        // white
        shapeRenderer.setColor(1,1,1,.3f);
        shapeRenderer.rect(w1xw[0], y0, w1xw[1], Screen.VIRTUAL_HEIGHT);
        shapeRenderer.rect(w2xw[0], y0, w2xw[1], Screen.VIRTUAL_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    
    private static class PulseStat {
    	int current;
    	final int max;
    	final int inc;
    	boolean up;
    	final float offset;
    	PulseStat(int max, int inc, float offset) {
    		this.max = max;
    		this.inc = inc;
    		this.offset = offset;
    	}
    }
}
