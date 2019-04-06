package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * This class represents a connection to the web browser. The player will send direction updates over this connection.
 * This connection will also be used to send Game updates to the browser.
 */
public class ClientWebSocket {

    private static final Logger LOG = LogManager.getLogger();

    private Direction direction;
    private Direction arrowKeysDirection;
    private Direction wasdKeysDirection;
    private ObjectMapper objectMapper;
    private String sessionId = "";
    private boolean ignoreInput;

    private WebSocketSession webSocketSession;

    /**
     * Constructor.
     * @param webSocketSession The WebSocketSession used for socket communication
     */
    public ClientWebSocket(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
        this.objectMapper = new ObjectMapper();
        this.ignoreInput = true;
    }

    /**
     * Called when a new text message is received from the client.
     * @param message The message received by the client.
     */
    public void onWebSocketText(String message) {
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

    /**
     * Sends the specified Game to the client browser. Clears the player direction values when it detects that the game has ended.
     *
     * @param game The instance of Game to send.
     */
    public void sendGameState(Game game) throws IOException {
        if (game.getGameStatus().equals(GameStatus.COMPLETED_WINNER) || game.getGameStatus().equals(GameStatus.COMPLETED_DRAW)) {
            wasdKeysDirection = null;
            arrowKeysDirection = null;
            direction = null;
            ignoreInput = true;
        } else {
            ignoreInput = false;
        }

        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new GameDto(game))));
        } catch (IOException e) {
            LOG.error(e.getMessage());
            webSocketSession.close(CloseStatus.SERVER_ERROR);
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

    public String getSessionId() {
        return sessionId;
    }

}
