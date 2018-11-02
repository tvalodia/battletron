package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.player.controller.WebSocketPlayerController;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameUpdateRouter;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameStateRouterFactory;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/game")
public class GameApi {

    private static final Logger LOG = LogManager.getLogger();

    private final GameRepository gameRepository;
    private final ClientWebSocketRepository clientWebSocketRepository;
    private WebSocketGameUpdateRouter webSocketGameStateRouter;

    /**
     * Constructor.
     */
    public GameApi() {
        gameRepository = GameRepository.getInstance();
        clientWebSocketRepository = ClientWebSocketRepository.getInstance();
        webSocketGameStateRouter = WebSocketGameStateRouterFactory.getWebSocketGameStateUpdateRouter();
    }

    /**
     * Fetches a list of all games.
     *
     * @return The complete list of games.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameDto> getAllGames() {
        List<GameDto> dtos = new LinkedList<>();
        for (GameController gameController : gameRepository.getAllGameEngines()) {
            GameDto gameDto = new GameDto(gameController.getGame());
            gameDto.setPlayingField(null);
            dtos.add(gameDto);
        }
        LOG.debug("Response: " + dtos);
        return dtos;
    }

    /**
     * Fetches a list of all games.
     *
     * @return The complete list of games.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto getGame(@PathParam("id") long id) {
        Game game = gameRepository.getGameEngine(id).getGame();
        GameDto gameDto = new GameDto(game);
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }

    /**
     * Starts a new game.
     *
     * @return A GameDto object.
     */
    @POST
    @Path("ai")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto startAiGame() {
        GameController gameController = createAiGameEngine();
        long gameEngineId = gameController.getId();
        gameRepository.addGameEngine(gameEngineId, gameController);
        GameDto gameDto = new GameDto(gameController.getGame());
        gameDto.setPlayingField(null);
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }

    /**
     * Starts a new game.
     *
     * @return A GameDto object containing details about the new game.
     */
    @POST
    @Path("singleplayer/{singlePlayerWebSocketId}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto startSinglePlayerGame(@PathParam("singlePlayerWebSocketId") String singlePlayerWebSocketId) {
        GameController gameController = createSinglePlayerGameEngine(singlePlayerWebSocketId);
        long gameEngineId = gameController.getId();
        gameRepository.addGameEngine(gameEngineId, gameController);
        GameDto gameDto = new GameDto(gameController.getGame());
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }

    private GameController createAiGameEngine() {
        GameController gameController = GameControllerFactory.getOnlineGameEngine();
        gameController.start();
        return gameController;
    }


    private GameController createSinglePlayerGameEngine(String playerWebSocketId) {
        killAnyRunningGame(playerWebSocketId);

        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);

        PlayerController aiPlayerController = new SimplePlayerAi(player2);
        ClientWebSocket clientWebSocket = clientWebSocketRepository.getClientWebSocket(playerWebSocketId);
        WebSocketPlayerController webSocketPlayerController = new WebSocketPlayerController(player1, clientWebSocket);

        GameController gameController = GameControllerFactory.getLocalGameEngine(player1, player2, webSocketPlayerController, aiPlayerController);

        webSocketGameStateRouter.registerForUpdates(playerWebSocketId, gameController.getId());
        gameController.getGame().registerListener(webSocketGameStateRouter);
        //gameController.getGame().registerListener(new PrintGameListener());
        clientWebSocket.setCurrentGameId(gameController.getGame().getId());
        clientWebSocket.setPlayer(player1);
        gameController.start();
        return gameController;
    }

    private void killAnyRunningGame(String playerWebSocketId) {
        //kill any existing game started by the player
        ClientWebSocket webSocketGameStateListener = clientWebSocketRepository.getClientWebSocket(playerWebSocketId);
        GameController runningGame = gameRepository.getGameEngine(webSocketGameStateListener.getCurrentGameId());
        if (runningGame != null) {
            runningGame.kill();
            gameRepository.delete(webSocketGameStateListener.getCurrentGameId());
        }
    }

    /**
     * Registers for spectating a game.
     *
     * @return A GameDto object.
     */
    @POST
    @Path("{id}/spectate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GameDto spectateGame(@PathParam("id") long id, SpectateDto spectateDto) {
        GameController gameController = gameRepository.getGameEngine(id);
        webSocketGameStateRouter.registerForUpdates(spectateDto.getPlayerId(), gameController.getId());

        GameDto gameDto = new GameDto(gameController.getGame());
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }


    /**
     * Controls the running-state of a game.
     *
     * @param gameEngineId The id of the game.
     * @param command      The command issued to the game.
     */
    @PUT
    @Path("{id}/command")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateGameStatus(@PathParam("id") long gameEngineId, GameCommandDto command) {
        LOG.debug("Request: id = " + gameEngineId + " " + command);
        if (command == null || command.getCommandString() == null || command.getCommandString().isEmpty()) {
            throw new WebApplicationException("Invalid command");
        }

        if (command.getCommandString().toLowerCase().equals("pause")) {
            GameController gameController = gameRepository.getGameEngine(gameEngineId);
            if (gameController != null) {
                gameController.pauseThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("unpause")) {
            GameController gameController = gameRepository.getGameEngine(gameEngineId);
            if (gameController != null) {
                gameController.resumeThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("kill")) {
            GameController gameController = gameRepository.getGameEngine(gameEngineId);
            if (gameController != null) {
                gameController.kill();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        }
    }

}