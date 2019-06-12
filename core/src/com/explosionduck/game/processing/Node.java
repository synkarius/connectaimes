package com.explosionduck.game.processing;

/**
 * Created by synkarius on 7/19/17.
 */

public class Node {
    public final Board board;
    public int index;
    /** cpu */
    private long scoreYellow;
    /** player */
    private long scoreGreen;
    /** column was not full prior to any root moves being played */
    public boolean selectable;

    public Node(Token player) {
        board = new Board(player);
    }

    public void addPoints(int points, Token player) {
        if (player == Token.GREEN)
            scoreGreen += points;
        else
            scoreYellow += points;
    }

    public long score(Token player) {
        return player == Token.GREEN ? scoreGreen : scoreYellow;
    }
}
