package com.explosionduck.game.processing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.explosionduck.game.processing.ReferenceBoard.AROUND;
import static com.explosionduck.game.processing.ReferenceBoard.HORIZ;
import static com.explosionduck.game.processing.ReferenceBoard.LDIAG;
import static com.explosionduck.game.processing.ReferenceBoard.RDIAG;
import static com.explosionduck.game.processing.ReferenceBoard.VERT;

/**
 * Created by synkarius on 7/17/17.
 */

public class Board implements Serializable {
    public static final int W = 7;
    public static final int H = 6;
    public static final int TOP = H - 1;
    public static final int NONE_AVAILABLE = -1;

//opera maria and draco
    private Token[][] slots = new Token[W][H];
    private Token player;
    /** reusable width, height index */
    private int x, y, k, j, x2, y2;
    private int wcount;
    private Token wt;
    private final List<ReferenceBoard.BoardAndVector> refBoards = Arrays.asList(LDIAG, RDIAG, VERT, HORIZ);
    
    //
    public Token winner;
    
    // taunt limiting
    public boolean taunted;
    private int turns;
    public int turns() {
    	return turns;
    }

    public Board() {
        this(null);
    }

    public Board(Token startingToken) {
        if (startingToken == null)
            startingToken =  Math.random() > .7 ? Token.YELLOW : Token.GREEN;
        player = startingToken;
    }

    /** used by the 4 reference boards */
    public Token getToken(int x, int y) {
        return slots[x][y];
    }

    /** copies whole board, flips active player */
    public void copyTo(Board other) {
        for(x=0; x<W; x++) {
            for(y=0; y<H; y++) {
                other.slots[x][y] = slots[x][y];
            }
        }
    }

    public boolean applyMove(int x, Token token) {
        for (y=0; y<H; y++) {
            if (slots[x][y] == null) {
                slots[x][y] = token;
                turns++;
                return true;
            }
        }
        return false;
    }
    
    public int getLowestOpenY(int x) {
        for (y=0; y<H; y++) {
            if (slots[x][y] == null) {
                return y;
            }
        }
        return NONE_AVAILABLE;
    }

    public void print() {
        // 'y' loop first b/c we're printing across a y-row
        for(y=H-1; y>=0; y--) {
            for(x=0; x<W; x++) {

                String out = ".";
                if (slots[x][y] == Token.YELLOW)
                    out = "b";
                else if (slots[x][y] == Token.GREEN)
                    out = "G";

                System.out.print(out + "  ");
            }
            System.out.println();
        }
        System.out.println("score BLUE: " + evaluate(Token.YELLOW));
        System.out.println("score GREEN: " + evaluate(Token.GREEN));
    }

    /** note: test scenarios must be realistic: no floating pieces -- or else the
     * loop optimization will skip the column
     */
    public int evaluate(Token player) {
        int score = 0;
        xloop: for (x=0; x<W; x++) {
            yloop: for(y=0; y<H; y++) {
                if (slots[x][y] == null)
                    continue xloop; // xloop b/c we're counting up from the bottom -- null means there will be no more above
                if (slots[x][y] != player)
                    continue yloop;
                kloop: for (k=0; k<AROUND.length; k++) {
                    x2 = x+AROUND[k][0];
                    y2 = y+AROUND[k][1];
                    if (x2 < 0 || y2 < 0 || x2 >=W || y2 >=H)
                        continue kloop;
                    Token neighbor = slots[x2][y2];
                    if (player == neighbor)
                        score++;
                    //else if (neighbor != null)
                    //    score--;
                }
            }
        }
        return score;
    }

    public Token player() {
        return player;
    }

    public void setPlayer(Token player) {
        this.player = player;
    }

    public void setPosition(int x, int y, Token token) {
        slots[x][y] = token;
    }

    /** starts at a point and checks a vector for a win for 4 spaces */
    private Token checkVector(int x, int y, int vx, int vy) {
        wt = null;
        wcount = 0;
        for (j=0; j<4; j++) {
            x2 = x + j * vx;
            y2 = y + j * vy;
            if (wt == slots[x2][y2]) {
                wcount++;
                if (wcount == 4)
                    return slots[x2][y2];
            } else {
                wcount = 1;
            }
            wt = slots[x2][y2];
        }
        return null;
    }


    public EndState getEndState() {
    	EndState es = new EndState();
    	
    	
        for (x=0; x<W; x++) {
            for (y=0; y<H; y++) {
                if (slots[x][y] == null) {
                	es.slotsStillAvailable = true;
                    continue;
                }
                // check each direction for a win, if dir is relevant
                for (k=0; k<4; k++) {
                    ReferenceBoard.BoardAndVector rBoard = refBoards.get(k);
                    if (rBoard.board.getToken(x, y) != null) {
                        Token win = checkVector(x, y, rBoard.x, rBoard.y);
                        if (win != null) {
                        	es.winner = win;
                        	es.slotsStillAvailable = false;
                        	return es;
                        }
                    }
                }
            }
        }
        return es;
    }
    
    public static class EndState {
    	public Token winner;
    	public boolean slotsStillAvailable;
    }

    public boolean columnIsFull(final int column) {
        return slots[column][TOP] != null;
    }

}
