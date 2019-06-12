package com.explosionduck.game.strategy;

import com.explosionduck.game.processing.Board;

/**
 * Created by synkarius on 7/17/17.
 */

public interface MoveSelector {
    int select(Board board);
}
