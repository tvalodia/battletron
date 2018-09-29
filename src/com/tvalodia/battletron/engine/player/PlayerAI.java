package com.tvalodia.battletron.engine.player;

import com.tvalodia.battletron.engine.Direction;
import com.tvalodia.battletron.engine.gamestate.GameState;

public interface PlayerAI {

    Direction getDirection(int tick, GameState gameState);

}
