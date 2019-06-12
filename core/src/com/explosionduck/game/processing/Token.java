package com.explosionduck.game.processing;

/**
 * Created by synkarius on 7/17/17.
 */

public enum Token {
    YELLOW, GREEN;
    Token invert() {
        return this == YELLOW ? GREEN : YELLOW;
    }
}
