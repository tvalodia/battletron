package org.tvalodia.tronbattle.client;

import org.tvalodia.tronbattle.engine.GameState;

public interface GameClient {

    void update(int tick, GameState gameState);
}
