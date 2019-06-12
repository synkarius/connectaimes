package com.explosionduck.game.processing;

/**
 * Created by synkarius on 7/29/17.
 */

public class ReferenceBoard {
    public static final BoardAndVector LDIAG, RDIAG, VERT, HORIZ;
    // reference boards for the single-loop
    private static final Board BOARD_LDIAG  = new Board(null);
    private static final Board BOARD_RDIAG  = new Board(null);
    private static final Board BOARD_VERT  = new Board(null);
    private static final Board BOARD_HORIZ  = new Board(null);
    public static final int[][] AROUND = new int[][]{
            new int[]{1, 1},
            new int[]{1, 0},
            new int[]{1, -1},
            new int[]{0, -1},
            new int[]{-1, -1},
            new int[]{-1, 0},
            new int[]{-1, 1},
            new int[]{0, 1}
    };
    private static final int[][] DIAG_LEFT = new int[][] {
            new int[]{0, 5},
            new int[]{0, 4},
            new int[]{0, 3},
            new int[]{1, 5},
            new int[]{1, 4},
            new int[]{1, 3},
            new int[]{2, 5},
            new int[]{2, 4},
            new int[]{2, 3},
            new int[]{3, 5},
            new int[]{3, 4},
            new int[]{3, 3}
    };
    private static final int[][] DIAG_RIGHT = new int[][] {
            new int[]{3, 3},
            new int[]{3, 4},
            new int[]{3, 5},
            new int[]{4, 3},
            new int[]{4, 4},
            new int[]{4, 5},
            new int[]{5, 3},
            new int[]{5, 4},
            new int[]{5, 5},
            new int[]{6, 3},
            new int[]{6, 4},
            new int[]{6, 5}
    };
    static {
        for (int i=0; i<DIAG_LEFT.length; i++)
            BOARD_LDIAG.setPosition(DIAG_LEFT[i][0], DIAG_LEFT[i][1], Token.YELLOW);
        for (int i=0; i<DIAG_RIGHT.length; i++)
            BOARD_RDIAG.setPosition(DIAG_RIGHT[i][0], DIAG_RIGHT[i][1], Token.YELLOW);
        // vert board:
        for (int x=0; x<Board.W; x++) {
            for (int y=0; y<=2; y++) {
                BOARD_VERT.setPosition(x, y, Token.YELLOW);
            }
        }
        // horiz board:
        for (int x=0; x<=3; x++) {
            for (int y=0; y<Board.H; y++) {
                BOARD_HORIZ.setPosition(x, y, Token.YELLOW);
            }
        }
        LDIAG = new BoardAndVector(BOARD_LDIAG, 1, -1);
        RDIAG  = new BoardAndVector(BOARD_RDIAG, -1, -1);
        VERT  = new BoardAndVector(BOARD_VERT, 0, 1);
        HORIZ = new BoardAndVector(BOARD_HORIZ, 1, 0);
    }

    public static class BoardAndVector {
        public final Board board;
        public final int x, y;
        BoardAndVector(Board board, int x, int y) {
            this.board = board;
            this.x = x;
            this.y = y;
        }
    }


}
