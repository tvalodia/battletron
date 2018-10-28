package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

public class WebSocketPlayerController implements PlayerController {

    private final Player player;
    private final ClientWebSocket socket;

    public WebSocketPlayerController(Player player, ClientWebSocket socket) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void execute(GameState gameState) {
        this.player.setDirection(socket.getDirection());
    }
}
