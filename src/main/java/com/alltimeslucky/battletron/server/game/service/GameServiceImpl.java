package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
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
    private GameFactory gameFactory;

    /**
     * Constructor.
     */
    @Inject
    public GameServiceImpl(GameControllerRepository gameControllerRepository, ClientWebSocketRepository clientWebSocketRepository,
                           WebSocketGameUpdateRouter webSocketGameStateRouter, PlayerControllerFactory playerControllerFactory,
                           GameControllerFactory gameControllerFactory, GameFactory gameFactory) {
        this.gameControllerRepository = gameControllerRepository;
        this.clientWebSocketRepository = clientWebSocketRepository;
        this.webSocketGameStateRouter = webSocketGameStateRouter;
        this.playerControllerFactory = playerControllerFactory;
        this.gameControllerFactory = gameControllerFactory;
        this.gameFactory = gameFactory;
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

        Game game = gameFactory.get();
        PlayerController player1Controller = playerControllerFactory.getPlayerController(player1Type, playerId, game.getPlayer1());
        PlayerController player2Controller = playerControllerFactory.getPlayerController(player2Type, playerId, game.getPlayer2());
        GameController gameController = gameControllerFactory.get(game, player1Controller, player2Controller);
        gameControllerRepository.add(gameController.getGameId(), gameController);
        webSocketGameStateRouter.registerForUpdates(playerId, gameController.getGameId());
        game.registerListener(webSocketGameStateRouter);
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

    /**
     * Pauses the game.
     * @param gameId The id of the game to pause
     * @throws Exception Thrown in the event of an error.
     */
    public void pauseGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.pauseThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    /**
     * Resumes a game.
     * @param gameId The id of the game to resume
     * @throws Exception Thrown in the event of an error.
     */
    public void resumeGame(long gameId) throws Exception {
        GameController gameController = gameControllerRepository.get(gameId);
        if (gameController != null) {
            gameController.resumeThread();
        } else {
            throw new Exception("Invalid game ID");
        }
    }

    /**
     * Deletes a game from the system.
     * @param gameId The id of the game to delete
     * @throws Exception Thrown in the event of an error.
     */
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