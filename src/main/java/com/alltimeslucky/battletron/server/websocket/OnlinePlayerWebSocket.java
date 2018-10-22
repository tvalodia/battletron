package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.server.api.game.GameDto;
import com.alltimeslucky.battletron.server.api.game.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class OnlinePlayerWebSocket extends WebSocketAdapter implements GameStateListener {

    private static final Logger LOG = LogManager.getLogger();

    private Direction direction = Direction.RIGHT;
    private ObjectMapper objectMapper;
    private String playerId = "";
    private long currentGameId;
    private Player player;

    public OnlinePlayerWebSocket() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        LOG.debug("New WebSocket connection: " + getSession());
        playerId = UUID.randomUUID().toString();
        OnlinePlayerSocketRepository.getInstance().addOnlinePlayerSocket(playerId, this);
        try {
            session.getRemote().sendString("id=" + playerId);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            getSession().close(-1, "IO Error");
        }
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        LOG.debug("Message from player (" + playerId + "): " + message);

        if (message.equals("UP")) {
            direction = Direction.UP;
        } else if (message.equals("LEFT")) {
            direction = Direction.LEFT;
        } else if (message.equals("DOWN")) {
            direction = Direction.DOWN;
        } else if (message.equals("RIGHT")) {
            direction = Direction.RIGHT;
        }

       if (message.equals("READY")) {
           player.setReady(true);
       }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOG.info("Socket Closed: [" + statusCode + "] " + reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOG.error(cause);
    }

    @Override
    public void onGameStateUpdate(GameState gameState) {
        try {
            getSession().getRemote().sendString(objectMapper.writeValueAsString(new GameDto(gameState)));
        } catch (IOException e) {
            LOG.error(e.getMessage());
            getSession().close(-1, "IO Error");
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public long getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(long currentGameId) {
        this.currentGameId = currentGameId;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
