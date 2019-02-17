package com.alltimeslucky.battletron.server.game.service.validation;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.server.game.repository.GameControllerRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;

import javax.inject.Inject;

public class GameServiceInputValidator {

    private static final String SESSION_ID = "Session ID";
    private static final String PLAYER_ONE_CONTROLLER_TYPE = "Player One Type";
    private static final String PLAYER_TWO_CONTROLLER_TYPE = "Player Two Type";

    private GameControllerRepository gameControllerRepository;
    private ClientWebSocketRepository clientWebSocketRepository;

    @Inject
    public GameServiceInputValidator(GameControllerRepository gameControllerRepository,
                                     ClientWebSocketRepository clientWebSocketRepository) {
        this.gameControllerRepository = gameControllerRepository;
        this.clientWebSocketRepository = clientWebSocketRepository;
    }

    /**
     * Validates the input of the CreateGame service.
     *
     * @param sessionId               The id of the web socket session
     * @param playerOneControllerType The type of controller for player one
     * @param playerTwoControllerType The type of controller for player one
     * @throws BattletronException Thrown when the any of the input values do not pass validation
     */
    public void validateCreateGameInput(String sessionId, String playerOneControllerType, String playerTwoControllerType)
            throws BattletronException {

        confirmPresence(sessionId, playerOneControllerType, playerTwoControllerType);
        confirmValid(sessionId, playerOneControllerType, playerTwoControllerType);
    }

    private void confirmPresence(String sessionId, String playerOneControllerType, String playerTwoControllerType)
            throws BattletronException {

        BattletronException validationException = new BattletronException(ExceptionCode.VALIDATION);

        if (sessionId == null || sessionId.isEmpty()) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, SESSION_ID));
        }

        if (playerOneControllerType == null) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, PLAYER_ONE_CONTROLLER_TYPE));
        }

        if (playerTwoControllerType == null) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, PLAYER_TWO_CONTROLLER_TYPE));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }
    }

    private void confirmValid(String sessionId, String playerOneControllerType, String playerTwoControllerType)
            throws BattletronException {

        BattletronException validationException = new BattletronException(ExceptionCode.VALIDATION);

        if (!clientWebSocketRepository.contains(sessionId)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, SESSION_ID));
        }

        try {
            PlayerControllerType.valueOf(playerOneControllerType);
        } catch (IllegalArgumentException e) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, PLAYER_ONE_CONTROLLER_TYPE));
        }

        try {
            PlayerControllerType.valueOf(playerTwoControllerType);
        } catch (IllegalArgumentException e) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, PLAYER_TWO_CONTROLLER_TYPE));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }
    }

    /**
     * Validates the input of the SpectateGame service.
     *
     * @param gameId    The id of the game to spectate
     * @param sessionId The id of the web socket session
     * @throws BattletronException Thrown when the any of the input values do not pass validation
     */
    public void validateSpectateGameInput(long gameId, String sessionId) throws BattletronException {

        BattletronException validationException = new BattletronException(ExceptionCode.VALIDATION);

        validateGameId(gameId);

        if (sessionId == null || sessionId.isEmpty()) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }

        if (!clientWebSocketRepository.contains(sessionId)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }

    }

    /**
     * Validates the input of the JoinGame service.
     *
     * @param gameId    The id of the game to spectate
     * @param sessionId The id of the web socket session
     * @throws BattletronException Thrown when the any of the input values do not pass validation
     */
    public void validateJoinGameInput(long gameId, String sessionId) throws BattletronException {

        BattletronException validationException = new BattletronException(ExceptionCode.VALIDATION);

        validateGameId(gameId);

        if (sessionId == null || sessionId.isEmpty()) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }

        if (!clientWebSocketRepository.contains(sessionId)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            throw validationException;
        }

        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(sessionId);
        GameController gameController = gameControllerRepository.get(gameId);
        if (clientWebSocket.getPlayerController() != null
                && (gameController.getPlayerOneController() == clientWebSocket.getPlayerController()
                || gameController.getPlayerTwoController() == clientWebSocket.getPlayerController())) {
            throw new BattletronException(ExceptionCode.ALREADY_JOINED_GAME);
        } else if (gameController.getPlayerOneController() != null
                && gameController.getPlayerTwoController() != null) {
            throw new BattletronException(ExceptionCode.GAME_ALREADY_FULL);
        }
    }

    /**
     * Validates the specified game id.
     *
     * @param id The id of the game to validate
     * @throws BattletronException Thrown when a game does not exist for the given id.
     */
    public void validateGameId(long id) throws BattletronException {

        if (!gameControllerRepository.contains(id)) {
            throw new BattletronException(ExceptionCode.NOT_FOUND);
        }
    }
}
