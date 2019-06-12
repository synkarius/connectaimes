package com.explosionduck.game.processing;

import com.explosionduck.game.input.GameInputProcessor;
import com.explosionduck.game.input.GameResetHandler;
import com.explosionduck.game.input.InputListenPredicate;
import com.explosionduck.game.processing.Board.EndState;
import com.explosionduck.game.screen.Assets;
import com.explosionduck.game.screen.Assets.Sounds;
import com.explosionduck.game.screen.Screen;
import com.explosionduck.game.screen.InputMode;
import com.explosionduck.game.screen.MusicFader;


/**
 * Created by synkarius on 7/19/17.
 */

public final class GameStateProcessor {

    public static final int W = 7, H = 6;

    //
    private final GameInputProcessor gip;
    private final Screen gfx;
    private final MusicFader music;
    private Board board;

    //
    private final Node[][] paths;
    private final Node[] roots;
    private final int maxDepth;
    private final int[] rewards;
    private int x;

    //
    private Integer columnPlayerSelected;
    private volatile Integer columnCPUSelected;
    private Thread cpuThread;
    private int cpuHoverTime;
    //private volatile int count;
    
	private InputListenPredicate listener = new InputListenPredicate() {
		@Override
		public boolean shouldListen() {
			return board.player() == Token.GREEN;
		}
	};
	
	private GameResetHandler resetHandler = new GameResetHandler() {
		@Override
		public void run() {
			if (gip.getMode() == InputMode.POST_GAME) {
                board = new Board();
                initialize(board);
            } else if (gip.getMode() == InputMode.SPLASH_SCREEN) {
            	music.startFade();
            }
            Sounds.START_GAME.play();
            gip.setMode(InputMode.GAME);
		}
	};

    public GameStateProcessor(int movesAhead) {
        gfx = new Screen();
        gip = new GameInputProcessor(gfx, listener, resetHandler);
        board = new Board();
        

        //
        //
        maxDepth = movesAhead;
        paths = new Node[W][maxDepth];
        roots = new Node[W];
        rewards = new int[maxDepth];

        int reward = (int) Math.pow(W, maxDepth);
        for (int d=0; d < maxDepth; d++) {
            // rewards should be large inversely to distance from root
            rewards[d] = reward;
            reward /= W;
        }

        Assets.INTRO_MUSIC.play();
        music = new MusicFader();
    }

    public void initialize(Board board) {
        //count = 0;
        for (x=0; x < W; x++) {
            for (int d=0; d < maxDepth; d++) {
                paths[x][d] = new Node(board.player());
                board.copyTo(paths[x][d].board);
            }
            roots[x]=paths[x][0];

            roots[x].selectable = true;
            if (roots[x].board.columnIsFull(x)) {
                roots[x].selectable = false;
                continue;
            }

            /* if we don't apply a move to each of the initial copies for the
            starting board player, each of the 7 root nodes will calculate
            the same exact path */
            roots[x].board.applyMove(x, board.player());
        }
    }

    public int chooseNextMoveFor(Token player) {
        int bestIndex = -1;
        long bestScore = 0;
        for (x=0; x<W; x++) {
            if (roots[x].selectable) {
                long score = roots[x].score(player);
                if (bestIndex == -1 || score > bestScore) {
                    bestScore = score;
                    bestIndex = x;
                }
            }
        }
        return bestIndex;
    }

    public void traverseRoots() {
        for (x=0; x<W; x++) {
            traversePath(x, 0);
        }
        //System.out.println("count: " + count);
    }

    public void traversePath(final int path, int depth) {
        Node root = roots[path];
        Node current = paths[path][depth];

        //count++;
        depth++;

        EndState es = current.board.getEndState();
        if (es.winner == null) {
            // no winner on this board
            int points = current.board.evaluate(current.board.player());
            root.addPoints(points, current.board.player());

            Node child = depth < maxDepth ? paths[path][depth] : null;
            if (child != null) {
                for (current.index=0; current.index<W; current.index++){
                    // if a column is unplayable, none of its children are valid
                    if (current.board.columnIsFull(current.index))
                        continue;

                    current.board.copyTo(child.board);
                    Token childPlayer = current.board.player() == Token.GREEN ? Token.YELLOW : Token.GREEN;
                    child.board.setPlayer(childPlayer);
                    child.board.applyMove(current.index, childPlayer);
                    traversePath(path, depth);
                }
            }
        } else if (Token.YELLOW == es.winner) {
            // self won - stop traversing children
            int reward = rewards[depth-1] * maxDepth;
            root.addPoints(reward, Token.YELLOW);
        } else {
            // other won - stop traversing children
            int reward = rewards[depth-1] * maxDepth;
            root.addPoints(-reward, Token.YELLOW);
        }
    }

    public GameInputProcessor getInputProcessor() {
        return gip;
    }
    
    private void applyPlayerMove() {
		board.applyMove(columnPlayerSelected, Token.GREEN);
		columnPlayerSelected = null;
		processEndState();
		
		// there was a bug where clicking really fast made the player select a
		// column and that column stay selected during the CPU's turn
		gip.resetColumnTapped();
		gip.resetPriorColumnTapped();
		
		if (!board.taunted && board.turns() > 10 && Math.random() > .85) {
			gfx.initRandomTaunt();
			gip.setMode(InputMode.TAUNT);
			board.taunted = true;
		}
    }
    
    private void applyCPUMove() {
    	cpuThread = null;
    	board.applyMove(columnCPUSelected, Token.YELLOW);
    	columnCPUSelected = null;
		processEndState();
    }
    
    private void processEndState() {
    	EndState es = board.getEndState(); 
    	if (es.winner != null) {
			gip.setMode(InputMode.POST_GAME);
			board.winner = es.winner;;
		} else if (!es.slotsStillAvailable) {
			gip.setMode(InputMode.POST_GAME);
			board.winner = null; // tie
		} else {
			Token currentPlayer = board.player();
			board.setPlayer(currentPlayer.invert());
		}
    }
    
    private class Determiner implements Runnable {
		@Override
		public void run() {
			determineCPUMove();
		}
    }
    
    private void determineCPUMove() {
    	//board.print();
        initialize(board);
        traverseRoots();
        columnCPUSelected = chooseNextMoveFor(Token.YELLOW);
    }

	public void process() {
		if (gip.getMode() == InputMode.TAUNT) {
			//
			gfx.setTauntIndex(gip.getTapCount());
			if (!gfx.tauntIsOn()) {
				gip.setMode(InputMode.GAME);
				gip.resetTapCount();
			}
		} else if (gip.getMode() == InputMode.GAME) {
			// fade music if just started:
			music.run();
    		
	        if (board.player() == Token.YELLOW) { // cpu
	        	if (cpuThread == null) {
	        		cpuThread = new Thread(new Determiner());
		        	cpuThread.start();
	        	} else if (cpuThread.getState() == Thread.State.TERMINATED) {
	        		
	        		if (gfx.isAnimationOn()) {
						if (gfx.isAnimationComplete()) {
							applyCPUMove();
							gfx.clearAnimation();
							Sounds.TOKEN_LANDING.play();
						}
					} else if (cpuHoverTime++ > 30) {
						cpuHoverTime = 0;
						gfx.glow(null);
						
						int lowestAvailY = board.getLowestOpenY(columnCPUSelected);
						if (lowestAvailY == Board.TOP) {
							applyCPUMove();
						} else {
							gfx.initDropAnimation(Token.YELLOW, columnCPUSelected, lowestAvailY);
						}
						Sounds.CPU_FIRE_SHOT.play();
					} else {
						gfx.glow(columnCPUSelected);
					}
	        	}
	        } else { // player
	        	if (gfx.isAnimationOn()) {
	        		if (gfx.isAnimationComplete()) {
	        			applyPlayerMove();
	        			gfx.clearAnimation();
	        			Sounds.TOKEN_LANDING.play();
	        		} 
	        	} else if (gip.columnTapped() != GameInputProcessor.NONE) {

					if (gip.priorColumnTapped() == gip.columnTapped()) {
						// TODO: here start the drop animation
						// -- at the end of it, play the move
						int lowestAvailY = board.getLowestOpenY(gip.columnTapped());
						if (lowestAvailY != Board.NONE_AVAILABLE) {
							columnPlayerSelected = gip.priorColumnTapped();
							
							if (lowestAvailY == Board.TOP) {
								applyPlayerMove();
							} else {
								gfx.initDropAnimation(Token.GREEN, gip.columnTapped(), lowestAvailY);
							}
							Sounds.PLAYER_FIRE_SHOT.play();
						} else {
							Sounds.REJECT_SELECTION.play();
						}
						
						gfx.glow(null);
						gip.resetPriorColumnTapped();
					} else {
						gfx.glow(gip.columnTapped());
						gip.setPriorToCurrentColumnTapped();
						Sounds.PLAYER_SELECT_COLUMN.play();
					}
					
					
					gip.resetColumnTapped();
				}
	        }
    	}
    }

    public void render() {
        gfx.clearScreen();

        if (gip.getMode() == InputMode.SPLASH_SCREEN) {
            gfx.renderSplash();
        } else if (gip.getMode() == InputMode.GAME || gip.getMode() == InputMode.TAUNT) {
            gfx.renderBoard(board);
        } else if (gip.getMode() == InputMode.POST_GAME) {
            gfx.renderBoard(board);
            gfx.renderPostGame(board.winner);
        }

        gfx.camUpdate();
    }
    
    public Screen screen() {
    	return gfx;
    }
}
