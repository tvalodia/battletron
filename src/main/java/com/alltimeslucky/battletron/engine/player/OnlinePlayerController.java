package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.server.websocket.OnlinePlayerWebSocket;

public class OnlinePlayerController implements PlayerController {

    private final Player player;
    private final OnlinePlayerWebSocket socket;

    public OnlinePlayerController(Player player, OnlinePlayerWebSocket socket) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void execute(GameState gameState) {
        this.player.setDirection(socket.getDirection());
    }
}
