package com.tvalodia.battletron.client;

import com.tvalodia.battletron.engine.GameState;

public interface GameClient {

    void update(int tick, GameState gameState);
}
