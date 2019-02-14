package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.repository.GameControllerRepository;
import com.alltimeslucky.battletron.server.game.service.validation.GameServiceInputValidator;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketController;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameServiceImpl implements GameService {

    private static final Logger LOG = LogManager.getLogger();

    private final GameControllerRepository gameControllerRepository;
    private final ClientWebSocketRepository clientWebSocketRepository;
    private ClientWebSocketController clientWebSocketController;
    private PlayerControllerFactory playerControllerFactory;
    private GameControllerFactory gameControllerFactory;
    private GameFactory gameFactory;
    private GameServiceInputValidator inputValidator;

    /**
     * Constructor.
     */
    @Inject
    public GameServiceImpl(GameControllerRepository gameControllerRepository, ClientWebSocketRepository clientWebSocketRepository,
                           ClientWebSocketController clientWebSocketController, PlayerControllerFactory playerControllerFactory,
                           GameControllerFactory gameControllerFactory, GameFactory gameFactory, GameServiceInputValidator inputValidator) {
        this.gameControllerRepository = gameControllerRepository;
        this.clientWebSocketRepository = clientWebSocketRepository;
        this.clientWebSocketController = clientWebSocketController;
        this.playerControllerFactory = playerControllerFactory;
        this.gameControllerFactory = gameControllerFactory;
        this.gameFactory = gameFactory;
        this.inputValidator = inputValidator;
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
    public Game createGame(String sessionId, String playerOneType, String playerTwoType) throws BattletronException {
        inputValidator.validateCreateGameInput(sessionId, playerOneType, playerTwoType);
        killGamesStartedWithSessionId(sessionId);

        Game game = gameFactory.get();
        PlayerController playerOneController = playerControllerFactory.getPlayerController(
                PlayerControllerType.valueOf(playerOneType), sessionId, game.getPlayerOne());
        PlayerController playerTwoController = playerControllerFactory.getPlayerController(
                PlayerControllerType.valueOf(playerTwoType), sessionId, game.getPlayerTwo());
        GameController gameController = gameControllerFactory.get(game, playerOneController, playerTwoController);
        gameControllerRepository.add(gameController.getGameId(), gameController);
        clientWebSocketController.registerForUpdates(sessionId, gameController.getGameId());
        game.registerListener(clientWebSocketController);
        //gameController.getGame().registerListener(new PrintGameListener());
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(sessionId);
        clientWebSocket.setCurrentGameId(gameController.getGame().getId());

        gameController.start();

        return gameController.getGame();
    }

    /**
     * Stops and removes a game that was started by the specified player.
     *
     * @param sessionId The id of the player
     */
    private void killGamesStartedWithSessionId(String sessionId) {
        //kill any existing game started by the player
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(sessionId);
        if (clientWebSocket.getCurrentGameId() != null) {
            GameController runningGame = gameControllerRepository.get(clientWebSocket.getCurrentGameId());
            if (runningGame != null) {
                runningGame.kill();
                // gameControllerRepository.delete(clientWebSocket.getCurrentGameId());
            }
        }
    }

    @Override
    public Game spectateGame(long gameId, String playerId) throws BattletronException {
        inputValidator.validateSpectateGameInput(gameId, playerId);

        killGamesStartedWithSessionId(playerId);
        GameController gameController = gameControllerRepository.get(gameId);
        clientWebSocketController.registerForUpdates(playerId, gameController.getGameId());
        return gameController.getGame();
    }

    @Override
    public Game joinGame(long gameId, String sessionId) throws BattletronException {
        inputValidator.validateJoinGameInput(gameId, sessionId);
        killGamesStartedWithSessionId(sessionId);
        GameController gameController = gameControllerRepository.get(gameId);
        Player player  = gameController.getGame().getPlayerOne();
        if (player != null) {
            player  = gameController.getGame().getPlayerTwo();
        }
        PlayerController playerController = playerControllerFactory.getPlayerController(PlayerControllerType.KEYBOARD, sessionId, player);
        clientWebSocketController.registerForUpdates(sessionId, gameController.getGameId());
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(sessionId);
        clientWebSocket.setCurrentGameId(gameId);
        clientWebSocket.setPlayerController(playerController);
        gameController.leaveGame(clientWebSocket.getPlayerController());
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
}