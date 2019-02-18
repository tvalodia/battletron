package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
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

    private Direction direction;
    private Direction arrowKeysDirection;
    private Direction wasdKeysDirection;
    private ObjectMapper objectMapper;
    private String sessionId = "";
    private ClientWebSocketListener listener;
    private boolean ignoreInput;

    /**
     * Constructor.
     */
    public ClientWebSocket() {
        objectMapper = new ObjectMapper();
        ignoreInput = true;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        LOG.debug("New WebSocket connection: " + getSession());
        sessionId = UUID.randomUUID().toString();
        try {
            session.getRemote().sendString("id=" + sessionId);
            listener.onConnect(this);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            getSession().close(-1, "IO Error");
        }
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        LOG.debug("Message from player (" + sessionId + "): " + message);

        if (ignoreInput) {
            return;
        }

        if (message.equals("W")) {
            direction = Direction.UP;
            wasdKeysDirection = Direction.UP;
        } else if (message.equals("A")) {
            direction = Direction.LEFT;
            wasdKeysDirection = Direction.LEFT;
        } else if (message.equals("S")) {
            direction = Direction.DOWN;
            wasdKeysDirection = Direction.DOWN;
        } else if (message.equals("D")) {
            direction = Direction.RIGHT;
            wasdKeysDirection = Direction.RIGHT;
        }

        if (message.equals("UP")) {
            direction = Direction.UP;
            arrowKeysDirection = Direction.UP;
        } else if (message.equals("LEFT")) {
            direction = Direction.LEFT;
            arrowKeysDirection = Direction.LEFT;
        } else if (message.equals("DOWN")) {
            direction = Direction.DOWN;
            arrowKeysDirection = Direction.DOWN;
        } else if (message.equals("RIGHT")) {
            direction = Direction.RIGHT;
            arrowKeysDirection = Direction.RIGHT;
        }

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOG.info("Socket Closed: [" + statusCode + "] " + reason);
        listener.onDisconnect(this);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOG.error(cause);
        listener.onDisconnect(this);
    }

    /**
     * Sends the specified Game to the client browser. Clears the player direction values when it detects that the game has ended.
     *
     * @param game The instance of Game to send.
     */
    public void sendGameState(Game game) {
        if (game.getGameStatus().equals(GameStatus.COMPLETED_WINNER) || game.getGameStatus().equals(GameStatus.COMPLETED_DRAW)) {
            wasdKeysDirection = null;
            arrowKeysDirection = null;
            direction = null;
            ignoreInput = true;
        } else {
            ignoreInput = false;
        }

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

    public Direction getArrowKeysDirection() {
        return arrowKeysDirection;
    }

    public Direction getWasdKeysDirection() {
        return wasdKeysDirection;
    }

    public void setListener(ClientWebSocketListener listener) {
        this.listener = listener;
    }

    public String getSessionId() {
        return sessionId;
    }

}
