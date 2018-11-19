package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;
import com.alltimeslucky.battletron.server.game.repository.GameControllerRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketController;

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

    /**
     * Constructor.
     */
    @Inject
    public GameServiceImpl(GameControllerRepository gameControllerRepository, ClientWebSocketRepository clientWebSocketRepository,
                           ClientWebSocketController clientWebSocketController, PlayerControllerFactory playerControllerFactory,
                           GameControllerFactory gameControllerFactory, GameFactory gameFactory) {
        this.gameControllerRepository = gameControllerRepository;
        this.clientWebSocketRepository = clientWebSocketRepository;
        this.clientWebSocketController = clientWebSocketController;
        this.playerControllerFactory = playerControllerFactory;
        this.gameControllerFactory = gameControllerFactory;
        this.gameFactory = gameFactory;
    }

    public List<Game> getAllGames() {
        List<Game> games = new LinkedList<>();
        for (GameController gameController : gameControllerRepository.getAllGameControllers()) {
            games.add(gameController.getGame());
        }
        return games;
    }

    public Game getGame(long id) {
        return gameControllerRepository.get(id).getGame();
    }

    public Game createGame(String playerId, PlayerControllerType player1Type, PlayerControllerType player2Type) {
        killAnyRunningGame(playerId);

        Game game = gameFactory.get();
        PlayerController player1Controller = playerControllerFactory.getPlayerController(player1Type, playerId, game.getPlayer1());
        PlayerController player2Controller = playerControllerFactory.getPlayerController(player2Type, playerId, game.getPlayer2());
        GameController gameController = gameControllerFactory.get(game, player1Controller, player2Controller);
        gameControllerRepository.add(gameController.getGameId(), gameController);
        clientWebSocketController.registerForUpdates(playerId, gameController.getGameId());
        game.registerListener(clientWebSocketController);
        //gameController.getGame().registerListener(new PrintGameListener());
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(playerId);
        clientWebSocket.setCurrentGameId(gameController.getGame().getId());

        gameController.start();

        return gameController.getGame();
    }

    /**
     * Stops and removes a game that was started by the specified player.
     * @param playerId The id of the player
     */
    private void killAnyRunningGame(String playerId) {
        //kill any existing game started by the player
        ClientWebSocket webSocketGameStateListener = clientWebSocketRepository.get(playerId);
        if (webSocketGameStateListener.getCurrentGameId() != null) {
            GameController runningGame = gameControllerRepository.get(webSocketGameStateListener.getCurrentGameId());
            if (runningGame != null) {
                runningGame.kill();
                // gameControllerRepository.delete(webSocketGameStateListener.getCurrentGameId());
            }
        }
    }

    public Game spectateGame(long gameId, String playerId) {
        killAnyRunningGame(playerId);
        GameController gameController = gameControllerRepository.get(gameId);
        clientWebSocketController.registerForUpdates(playerId, gameController.getGameId());
        return gameController.getGame();
    }

    public void pauseGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.pauseThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    public void resumeGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.resumeThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    public void deleteGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.kill();
            gameControllerRepository.delete(gameId);
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    public void stopGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.kill();
        } else {
            throw new Exception("Invalid game ID");
        }
    }
}