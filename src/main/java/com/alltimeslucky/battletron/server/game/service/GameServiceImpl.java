package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.gamecontroller.GameController;
import com.alltimeslucky.battletron.gamecontroller.GameControllerFactory;
import com.alltimeslucky.battletron.gamecontroller.GameControllerRepository;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.player.controller.settings.PlayerControllerSettings;
import com.alltimeslucky.battletron.player.controller.settings.PlayerControllerSettingsFactory;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.service.validation.GameServiceInputValidator;
import com.alltimeslucky.battletron.server.session.repository.SessionRepository;
import com.alltimeslucky.battletron.server.session.service.Session;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketController;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameServiceImpl implements GameService {

    private static final Logger LOG = LogManager.getLogger();

    private final GameControllerRepository gameControllerRepository;
    private final SessionRepository sessionRepository;
    private ClientWebSocketController clientWebSocketController;
    private PlayerControllerFactory playerControllerFactory;
    private GameControllerFactory gameControllerFactory;
    private GameFactory gameFactory;
    private GameServiceInputValidator inputValidator;
    private PlayerControllerSettingsFactory playerControllerSettingsFactory;

    /**
     * Constructor.
     */
    @Autowired
    public GameServiceImpl(GameControllerRepository gameControllerRepository, SessionRepository sessionRepository,
                           ClientWebSocketController clientWebSocketController, PlayerControllerFactory playerControllerFactory,
                           GameControllerFactory gameControllerFactory, GameFactory gameFactory, GameServiceInputValidator inputValidator,
                           PlayerControllerSettingsFactory playerControllerSettingsFactory) {
        this.gameControllerRepository = gameControllerRepository;
        this.sessionRepository = sessionRepository;
        this.clientWebSocketController = clientWebSocketController;
        this.playerControllerFactory = playerControllerFactory;
        this.gameControllerFactory = gameControllerFactory;
        this.gameFactory = gameFactory;
        this.inputValidator = inputValidator;
        this.playerControllerSettingsFactory = playerControllerSettingsFactory;
    }

    @Override
    public List<Game> getAllGames() {
        List<Game> games = new LinkedList<>();
        for (GameController gameController : gameControllerRepository.getAllGameControllers()) {
            games.add(gameController.getGame());
        }
        return games;
    }

    @Override
    public List<Game> getOpenGames() {
        List<Game> games = new LinkedList<>();
        for (GameController gameController : gameControllerRepository.getAllGameControllers()) {
            if (gameController.isJoinable()) {
                games.add(gameController.getGame());
            }
        }
        return games;
    }

    @Override
    public Game getGame(long gameId) throws BattletronException {
        inputValidator.validateGameId(gameId);

        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController == null) {
            throw new BattletronException(ExceptionCode.NOT_FOUND);
        } else {
            return gameController.getGame();
        }
    }

    @Override
    public Game createGame(String sessionId, String playerOneType, String playerOneAiRemoteHost,
                                             String playerTwoType, String playerTwoAiRemoteHost) throws BattletronException {

        inputValidator.validateCreateGameInput(sessionId, playerOneType, playerOneAiRemoteHost, playerTwoType, playerTwoAiRemoteHost);
        Session session = sessionRepository.get(sessionId);

        killGamesStartedWithSession(session);

        Game game = gameFactory.get();

        ClientWebSocket clientWebSocket = session.getClientWebSocket();
        PlayerControllerSettings playerOneControllerSettings =
                playerControllerSettingsFactory.get(PlayerControllerType.valueOf(playerOneType));
        playerOneControllerSettings.setClientWebSocket(clientWebSocket);
        playerOneControllerSettings.setAiRemoteHost(playerOneAiRemoteHost);

        PlayerController playerOneController =
                playerControllerFactory.getPlayerController(playerOneControllerSettings, game.getPlayerOne());

        PlayerControllerSettings playerTwoControllerSettings =
                playerControllerSettingsFactory.get(PlayerControllerType.valueOf(playerTwoType));
        playerTwoControllerSettings.setClientWebSocket(clientWebSocket);
        playerTwoControllerSettings.setAiRemoteHost(playerTwoAiRemoteHost);
        PlayerController playerTwoController =
                playerControllerFactory.getPlayerController(playerTwoControllerSettings, game.getPlayerTwo());

        GameController gameController = gameControllerFactory.get(game, playerOneController, playerTwoController);
        gameControllerRepository.add(gameController.getGameId(), gameController);
        clientWebSocketController.registerForUpdates(sessionId, gameController.getGameId());
        game.registerListener(clientWebSocketController);
        //gameController.getGame().registerListener(new PrintGameListener());
        session.setGameId(gameController.getGame().getId());

        gameController.start();

        return gameController.getGame();
    }

    @Override
    public Game createTrainingGame(String aiRemoteHost) throws BattletronException {

        inputValidator.validateAiRemoteHost(aiRemoteHost);

        Game game = gameFactory.get();

        PlayerControllerSettings playerOneControllerSettings =
                playerControllerSettingsFactory.get(PlayerControllerType.AI_REMOTE);
        playerOneControllerSettings.setAiRemoteHost(aiRemoteHost);
        PlayerController playerOneController =
                playerControllerFactory.getPlayerController(playerOneControllerSettings, game.getPlayerOne());

        PlayerControllerSettings playerTwoControllerSettings =
                playerControllerSettingsFactory.get(PlayerControllerType.AI_REMOTE);
        playerTwoControllerSettings.setAiRemoteHost(aiRemoteHost);
        PlayerController playerTwoController =
                playerControllerFactory.getPlayerController(playerTwoControllerSettings, game.getPlayerTwo());

        GameController gameController = gameControllerFactory.get(game, playerOneController, playerTwoController);
        gameController.setTickIntervalMillis(0);
        gameControllerRepository.add(gameController.getGameId(), gameController);

        gameController.start();

        return gameController.getGame();
    }

    /**
     * Stops and removes a game that was started by the specified player.
     *
     * @param session The session used by the player
     */
    private void killGamesStartedWithSession(Session session) {
        //kill any existing game started by the player
        if (session.getGameId() != null) {
            GameController runningGame = gameControllerRepository.get(session.getGameId());
            if (runningGame != null) {
                runningGame.kill();
                // gameControllerRepository.delete(clientWebSocket.getCurrentGameId());
            }
        }
    }

    @Override
    public Game spectateGame(long gameId, String sessionId) throws BattletronException {
        inputValidator.validateSpectateGameInput(gameId, sessionId);
        Session session = sessionRepository.get(sessionId);
        killGamesStartedWithSession(session);
        GameController gameController = gameControllerRepository.get(gameId);
        clientWebSocketController.registerForUpdates(sessionId, gameController.getGameId());
        return gameController.getGame();
    }

    @Override
    public Game joinGame(long gameId, String sessionId) throws BattletronException {
        inputValidator.validateJoinGameInput(gameId, sessionId);
        Session session = sessionRepository.get(sessionId);
        killGamesStartedWithSession(session);
        leaveGame(sessionId);
        GameController gameController = gameControllerRepository.get(gameId);
        Player player  = gameController.getGame().getPlayerOne();
        if (player != null) {
            player  = gameController.getGame().getPlayerTwo();
        }

        PlayerControllerSettings playerControllerSettings = playerControllerSettingsFactory.get(PlayerControllerType.KEYBOARD);
        playerControllerSettings.setClientWebSocket(session.getClientWebSocket());
        PlayerController playerController = playerControllerFactory.getPlayerController(playerControllerSettings, player);
        clientWebSocketController.registerForUpdates(sessionId, gameController.getGameId());
        session.setGameId(gameId);
        session.setPlayerController(playerController);
        gameController.joinGame(playerController);
        return gameController.getGame();
    }

    @Override
    public void pauseGame(long gameId) throws Exception {
        inputValidator.validateGameId(gameId);

        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.pauseThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    @Override
    public void resumeGame(long gameId) throws Exception {
        inputValidator.validateGameId(gameId);

        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.resumeThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }


    @Override
    public void deleteGame(long gameId) throws BattletronException {
        inputValidator.validateGameId(gameId);

        GameController gameController = gameControllerRepository.get(gameId);
        gameController.kill();
        gameControllerRepository.delete(gameId);
        LOG.info(String.format("Game %d deleted", gameId));

    }

    @Override
    public void stopGame(long gameId) throws Exception {
        inputValidator.validateGameId(gameId);

        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.kill();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    private void leaveGame(String sessionId) {
        Session session = sessionRepository.get(sessionId);
        Long gameId = session.getGameId();
        if (gameId != null && gameControllerRepository.contains(gameId)) {
            GameController oldGameController = gameControllerRepository.get(gameId);
            oldGameController.leaveGame(session.getPlayerController());
            clientWebSocketController.deregisterForUpdates(sessionId);
        }
    }
}