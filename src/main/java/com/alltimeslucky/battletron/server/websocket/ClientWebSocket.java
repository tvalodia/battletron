package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.api.game.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * This class represents a connection to the web browser. The player will send direction updates over this connection.
 * This connection will also be used to send Game updates to the browser.
 */
public class ClientWebSocket extends WebSocketAdapter {

    private static final Logger LOG = LogManager.getLogger();

    private Direction direction = Direction.RIGHT;
    private ObjectMapper objectMapper;
    private String playerId = "";
    private long currentGameId;
    private Player player;

    public ClientWebSocket() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        LOG.debug("New WebSocket connection: " + getSession());
        playerId = UUID.randomUUID().toString();
        ClientWebSocketRepository.getInstance().addOnlinePlayerSocket(playerId, this);
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

        if (message.equals("READY")) {
            player.setReady(true);
        } else if (message.equals("UP")) {
            direction = Direction.UP;
        } else if (message.equals("LEFT")) {
            direction = Direction.LEFT;
        } else if (message.equals("DOWN")) {
            direction = Direction.DOWN;
        } else if (message.equals("RIGHT")) {
            direction = Direction.RIGHT;
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOG.info("Socket Closed: [" + statusCode + "] " + reason);
        ClientWebSocketRepository.getInstance().delete(playerId);
        WebSocketGameStateRouterFactory.getWebSocketGameStateUpdateRouter().deregisterForUpdates(playerId);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOG.error(cause);
        ClientWebSocketRepository.getInstance().delete(playerId);
    }

    /**
     * Sends the specified Game to the client browser.
     * @param game The instance Game to send.
     */
    public void sendGameState(Game game) {
        try {
            getSession().getRemote().sendString(objectMapper.writeValueAsString(new GameDto(game)));
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
