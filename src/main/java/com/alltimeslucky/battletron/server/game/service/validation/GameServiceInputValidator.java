package com.alltimeslucky.battletron.server.game.service.validation;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerRepository;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.server.session.Session;
import com.alltimeslucky.battletron.server.session.SessionRepository;

import javax.inject.Inject;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameServiceInputValidator {

    private static final Logger LOG = LogManager.getLogger();

    private static final String SESSION_ID = "Session ID";
    private static final String PLAYER_ONE_CONTROLLER_TYPE = "Player One Type";
    private static final String PLAYER_TWO_CONTROLLER_TYPE = "Player Two Type";
    private static final String PLAYER_ONE_REMOTE_AI_HOST = "Player One Remote AI Host";
    private static final String PLAYER_TWO_REMOTE_AI_HOST = "Player Two Remote AI Host";


    private GameControllerRepository gameControllerRepository;
    private SessionRepository sessionRepository;

    @Inject
    public GameServiceInputValidator(GameControllerRepository gameControllerRepository,
                                     SessionRepository sessionRepository) {
        this.gameControllerRepository = gameControllerRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Validates the input of the CreateGame service.
     *
     * @param sessionId               The id of the web socket session
     * @param playerOneControllerType The type of controller for player one
     * @param playerOneRemoteAiHost  The URL of the remote ai for player one
     * @param playerTwoControllerType The type of controller for player two
     * @param playerTwoRemoteAiHost  The URL of the remote ai for player two
     * @throws BattletronException Thrown when the any of the input values do not pass validation
     */
    public void validateCreateGameInput(String sessionId, String playerOneControllerType, String playerOneRemoteAiHost,
                                        String playerTwoControllerType, String playerTwoRemoteAiHost) throws BattletronException {

        checkMandatoryValues(sessionId, playerOneControllerType, playerOneRemoteAiHost, playerTwoControllerType, playerTwoRemoteAiHost);
        confirmValid(sessionId, playerOneControllerType, playerOneRemoteAiHost, playerTwoControllerType, playerTwoRemoteAiHost);
    }

    private void checkMandatoryValues(String sessionId, String playerOneControllerType, String playerOneRemoteAiHost,
                                      String playerTwoControllerType, String playerTwoRemoteAiHost)
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
            LOG.error(validationException);
            throw validationException;
        }

        if (playerOneControllerType.equals(PlayerControllerType.AI_REMOTE.toString())
                && (playerOneRemoteAiHost == null || playerOneRemoteAiHost.isEmpty())) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, PLAYER_ONE_REMOTE_AI_HOST));
        }

        if (playerTwoControllerType.equals(PlayerControllerType.AI_REMOTE.toString())
                && (playerTwoRemoteAiHost == null || playerTwoRemoteAiHost.isEmpty())) {
            validationException.add(new BattletronException(ExceptionCode.MISSING_VALUE, PLAYER_TWO_REMOTE_AI_HOST));
        }

        if (validationException.exceptionCount() > 0) {
            LOG.error(validationException);
            throw validationException;
        }
    }

    private void confirmValid(String sessionId, String playerOneControllerType, String playerOneRemoteAiHost,
                              String playerTwoControllerType, String playerTwoRemoteAiHost)
            throws BattletronException {

        BattletronException validationException = new BattletronException(ExceptionCode.VALIDATION);

        if (!sessionRepository.contains(sessionId)) {
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
            LOG.error(validationException);
            throw validationException;
        }

        String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
        if (playerOneControllerType.equals(PlayerControllerType.AI_REMOTE.toString()) && !urlValidator.isValid(playerOneRemoteAiHost)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, PLAYER_ONE_REMOTE_AI_HOST));
        }

        if (playerTwoControllerType.equals(PlayerControllerType.AI_REMOTE.toString()) && !urlValidator.isValid(playerTwoRemoteAiHost)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, PLAYER_TWO_REMOTE_AI_HOST));
        }


        if (validationException.exceptionCount() > 0) {
            LOG.error(validationException);
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
            LOG.error(validationException);
            throw validationException;
        }

        if (!sessionRepository.contains(sessionId)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            LOG.error(validationException);
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
            LOG.error(validationException);
            throw validationException;
        }

        if (!sessionRepository.contains(sessionId)) {
            validationException.add(new BattletronException(ExceptionCode.INVALID_VALUE, SESSION_ID));
        }

        if (validationException.exceptionCount() > 0) {
            LOG.error(validationException);
            throw validationException;
        }

        Session session = sessionRepository.get(sessionId);
        GameController gameController = gameControllerRepository.get(gameId);
        if (session.getPlayerController() != null
                && (gameController.getPlayerOneController() == session.getPlayerController()
                || gameController.getPlayerTwoController() == session.getPlayerController())) {
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
            LOG.error("Invalid game id: " + id);
            throw new BattletronException(ExceptionCode.NOT_FOUND);
        }
    }
}
