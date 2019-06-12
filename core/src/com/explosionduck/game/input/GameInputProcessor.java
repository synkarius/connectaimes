package com.explosionduck.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.explosionduck.game.processing.Board;
import com.explosionduck.game.processing.Token;
import com.explosionduck.game.screen.InputMode;
import com.explosionduck.game.screen.Screen;
import com.explosionduck.game.screen.Assets.Sounds;

import static com.explosionduck.game.processing.GameStateProcessor.W;

public class GameInputProcessor implements InputProcessor{

   
	private final OrthographicCamera unprojCam;
	private final InputListenPredicate listener;
	private final GameResetHandler resetHandler;
	private final ViewPortGetter viewportGetter;

	// double click detection
	private long time = System.currentTimeMillis();
	private InputMode mode = InputMode.SPLASH_SCREEN;
	//
	public static final int NONE = -1;
	private int columnTapped = NONE;
	private int priorColumnTapped = NONE;
	//
	private int taps;
    
    
    public GameInputProcessor(final Screen gfx, InputListenPredicate listener, GameResetHandler resetHandler) {
    	this.unprojCam = gfx.getCamera();
    	this.listener = listener;
    	this.resetHandler = resetHandler;
    	this.viewportGetter = new ViewPortGetter() {
			@Override
			public Rectangle getViewport() {
				return gfx.getViewport();
			}
		};
    }
    
    
    public int columnTapped() {
    	return columnTapped;
    }
    
    public void resetColumnTapped() {
    	columnTapped = NONE;
    }
    
    public int priorColumnTapped() {
    	return priorColumnTapped;
    }
    
    public void resetPriorColumnTapped() {
    	priorColumnTapped = NONE;
    }
    
    public void setPriorToCurrentColumnTapped() {
    	priorColumnTapped = columnTapped;
    }
    
    public void setMode(InputMode mode) {
    	this.mode = mode;
    }

    public InputMode getMode() {
        return mode;
    }
    
    public void resetTapCount() {
    	taps = 0;
    }
    
    public int getTapCount() {
    	return taps;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	if (mode == InputMode.TAUNT) {
    		taps++;
		} else if (mode == InputMode.SPLASH_SCREEN || mode == InputMode.POST_GAME) {
            long now = System.currentTimeMillis();
            if (now - time > 500) {
                time = now;
            } else {
                resetHandler.run();
            }
        } else if (mode == InputMode.GAME) {
            if (listener.shouldListen()) {
            	
            	Rectangle viewport = viewportGetter.getViewport();
            	int realScreenWidth = Gdx.graphics.getWidth();
            	int buffer = (realScreenWidth - (int)viewport.width) / 2;
            	
            	if (screenX < buffer || screenX > buffer + viewport.width) {
            		return false;
            	}
            	
            	//Vector3 click = unprojCam.unproject(new Vector3((float) screenX, (float) screenY, 0f));
            	// no unproject - don't need this kind of accuracy
                
            	int widthUnit = (int) (viewport.width / W);
            	// set to 6 to trap those last 5 pixels on the right side
                int column = 6;
                for (int c = 0; c < W; c++) {
                    int c0 = buffer + (c * widthUnit);
                    int c1 = buffer + (c + 1) * widthUnit;
                    if (screenX >= c0 && screenX <= c1) {
                        column = c;
                        break;
                    }
                }
                columnTapped = column;
            }
        }
		return false;
	}

	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////
	/////////////// UNUSED EVENTS ///////////////////////////////////////////

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}