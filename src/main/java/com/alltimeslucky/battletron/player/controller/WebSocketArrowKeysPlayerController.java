package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

public class WebSocketArrowKeysPlayerController implements PlayerController {

    private final Player player;
    private final ClientWebSocket socket;

    public WebSocketArrowKeysPlayerController(Player player, ClientWebSocket socket) {
        this.player = player;
        this.socket = socket;
    }

    @Override
    public void execute(Game game) {
        this.player.setDirection(socket.getArrowKeysDirection());
    }
}
