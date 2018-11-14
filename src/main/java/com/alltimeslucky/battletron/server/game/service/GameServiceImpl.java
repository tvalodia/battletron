package com.alltimeslucky.battletron.server.game.service;


import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.repository.GameControllerRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameUpdateRouter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameServiceImpl implements GameService {

    private static final Logger LOG = LogManager.getLogger();

    private final GameControllerRepository gameControllerRepository;
    private final ClientWebSocketRepository clientWebSocketRepository;
    private WebSocketGameUpdateRouter webSocketGameStateRouter;
    private PlayerControllerFactory playerControllerFactory;
    private GameControllerFactory gameControllerFactory;

    /**
     * Constructor.
     */
    @Inject
    public GameServiceImpl(GameControllerRepository gameControllerRepository, ClientWebSocketRepository clientWebSocketRepository,
                           WebSocketGameUpdateRouter webSocketGameStateRouter, PlayerControllerFactory playerControllerFactory,
                           GameControllerFactory gameControllerFactory) {
        this.gameControllerRepository = gameControllerRepository;
        this.clientWebSocketRepository = clientWebSocketRepository;
        this.webSocketGameStateRouter = webSocketGameStateRouter;
        this.playerControllerFactory = playerControllerFactory;
        this.gameControllerFactory = gameControllerFactory;
    }

    /**
     * Gets all stored instances of GameController objects.
     *
     * @return A list of GameController
     */
    public List<Game> getAllGames() {
        List<Game> games = new LinkedList<>();
        for (GameController gameController : gameControllerRepository.getAllGameControllers()) {
            games.add(gameController.getGame());
        }
        return games;
    }

    /**
     * Gets the GameControlelr with the specified ID.
     *
     * @return The complete list of games.
     */
    public Game getGame(long id) {
        return gameControllerRepository.get(id).getGame();
    }

    /**
     * Creates a new GameController with a Game for the two given player types.
     *
     * @param playerId    The id of the player creating the game
     * @param player1Type The type of player for player 1
     * @param player2Type The type pf player for player 2
     * @return A new GameController
     */
    public Game createGame(String playerId, String player1Type, String player2Type) {
        killAnyRunningGame(playerId);
        Player player1 = new Player(1, 33, 50);
        Player player2 = new Player(2, 66, 50);
        Game game = GameFactory.getGame(player1, player2);

        PlayerController player1Controller = playerControllerFactory.getPlayerController(player1Type, playerId, player1);
        PlayerController player2Controller = playerControllerFactory.getPlayerController(player2Type, playerId, player2);
        GameController gameController = gameControllerFactory.getGameController(game, player1Controller, player2Controller);
        gameControllerRepository.add(gameController.getGameId(), gameController);

        webSocketGameStateRouter.registerForUpdates(playerId, gameController.getGameId());
        game.registerListener(webSocketGameStateRouter);
        //gameController.getGame().registerListener(new PrintGameListener());
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(playerId);
        clientWebSocket.setCurrentGameId(gameController.getGame().getId());

        gameController.start();

        return gameController.getGame();
    }

    private void killAnyRunningGame(String playerWebSocketId) {
        //kill any existing game started by the player
        ClientWebSocket webSocketGameStateListener = clientWebSocketRepository.get(playerWebSocketId);
        GameController runningGame = gameControllerRepository.get(webSocketGameStateListener.getCurrentGameId());
        if (runningGame != null) {
            runningGame.kill();
            // gameControllerRepository.delete(webSocketGameStateListener.getCurrentGameId());
        }
    }

    /**
     * Start spectating a game.
     *
     * @return The GameController of the game to spectate
     */
    public Game spectateGame(long gameId, String playerId) {
        killAnyRunningGame(playerId);
        GameController gameController = gameControllerRepository.get(gameId);
        webSocketGameStateRouter.registerForUpdates(playerId, gameController.getGameId());
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
}