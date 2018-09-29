package com.tvalodia.tronbattle.client;

import com.tvalodia.tronbattle.engine.GameState;

public interface GameClient {

    void update(int tick, GameState gameState);
}
