package com.explosionduck.game.screen;

import static com.explosionduck.game.processing.Board.H;
import static com.explosionduck.game.processing.Board.W;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.explosionduck.game.processing.Board;
import com.explosionduck.game.processing.Token;
import com.explosionduck.game.screen.LaserPulse.PulseColor;

/**
 * Created by synkarius on 7/30/17.
 */

public class Screen {
	
	public static final int VIRTUAL_WIDTH = 320;
    public static final int VIRTUAL_HEIGHT = 240;
	public static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;

	//
    private final OrthographicCamera cam;
    private Rectangle viewport;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch batch;
    //
    private final int w, h, x0, y0, widthUnit, heightUnit;

    //
    
    
    // Render Helper Classes:
    //
	private final IntroSequence intro;
    //
    private final LaserPulse pulse;
    private Integer glowColumn;
    //
    private final TokenDropAnimator tdAnimator;
    private final TauntAnimator tauntAnimator;
    

    public Screen() {
        this.w = VIRTUAL_WIDTH;
        this.h = VIRTUAL_HEIGHT;
        // x0, y0 necessary for ShapeRenderer but not SpriteBatch for some reason...
        x0 = -(w/2);
        y0 = (-h/2);
        widthUnit = w/7;
        heightUnit = h/6;
        cam = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        cam.update();

        font = new BitmapFont();
        batch = new SpriteBatch();
        intro = new IntroSequence(cam, batch, font);
        pulse = new LaserPulse(shapeRenderer, widthUnit, heightUnit, x0, y0);
        tdAnimator = new TokenDropAnimator(batch, widthUnit, heightUnit);
        tauntAnimator = new TauntAnimator(batch, font);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    private void renderBlock(int x, int y, Token player) {
        int xx = (x * widthUnit) + 7;
        int yy = (y * heightUnit) + 3;
        batch.draw(player == Token.YELLOW ? Assets.TOKEN_BLUE : Assets.TOKEN_GREEN, xx, yy);
    }

    public void renderSplash() {
        intro.render();
    }

    public void renderPostGame(Token winner) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        font.setColor(1,1,1,1);
        String msg = winner != null ? winner + " wins!" : "Tie game!";
        font.draw(batch, msg, 120, 140);
        font.draw(batch, "Double tap to play again!", 90, 100);
        batch.end();
    }

    public void renderBoard(Board board) {
    	batch.begin();
    	batch.setColor(1, 1, 1, 1);
    	batch.draw(Assets.BACKGROUND, 0, 0);
    	
        for (int x=0; x<W; x++) {
            for (int y=0; y<H; y++) {
                Token player = board.getToken(x,y);
                if (player != null)
                    renderBlock(x,y,player);
            }
        }
        
        if (tdAnimator.isOn() && !tdAnimator.isComplete()) {
        	tdAnimator.updateDropAnim();
        }

        batch.draw(Assets.FOREGROUND, 0, 0);
        
        if (tauntAnimator.isOn())
        	tauntAnimator.draw();
        
        batch.end();
        
        if (glowColumn != null)
        	pulse.pulsingSelectedColumn(glowColumn, 
        			board.player() == Token.GREEN ? PulseColor.BLUE : PulseColor.PURPLE);
    }
    
    

    public void clearScreen() {

        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

        // clear previous frame
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void camUpdate() {
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
    }


    public int getWidthUnit() {
        return widthUnit;
    }

    public int getHeightUnit() {
        return heightUnit;
    }
    
    public void glow(Integer column) {
    	glowColumn = column;
    }
    
    public void setViewPort(Rectangle viewport) {
		this.viewport = viewport;
	}
	
	public Rectangle getViewport() {
		return viewport;
	}
    
	/////////////// ACCESS METHODS FOR ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR ANIMATOR ///////////////////
	public boolean isAnimationOn() {
		return tdAnimator.isOn();
	}

	public boolean isAnimationComplete() {
		return tdAnimator.isComplete();
	}

	public void clearAnimation() {
		tdAnimator.reset();
	}

	public void initDropAnimation(Token player, int x, int y) {
		tdAnimator.initDropAnim(player, x, y);
	}
	
	/////////////// ACCESS METHODS FOR TAUNT ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR TAUNT ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR TAUNT ANIMATOR ///////////////////
	/////////////// ACCESS METHODS FOR TAUNT ANIMATOR ///////////////////
	
	public void initRandomTaunt() {
		tauntAnimator.initTaunt();
	}
	
	public void setTauntIndex(int tapCount) {
		tauntAnimator.setTauntLineIndex(tapCount);
	}
	
	public boolean tauntIsOn() {
		return tauntAnimator.isOn();
	}

}
