package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.GameEngineFactory;
import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.engine.player.SimplePlayerAi;
import com.alltimeslucky.battletron.engine.player.WebSocketPlayerController;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameStateRouter;
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
    private WebSocketGameStateRouter webSocketGameStateRouter;

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
        for (GameEngine gameEngine : gameRepository.getAllGameEngines()) {
            GameDto gameDto = new GameDto(gameEngine.getGameState());
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
        GameState gameState = gameRepository.getGameEngine(id).getGameState();
        GameDto gameDto = new GameDto(gameState);
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
        GameEngine gameEngine = createAiGameEngine();
        long gameEngineId = gameEngine.getId();
        gameRepository.addGameEngine(gameEngineId, gameEngine);
        GameDto gameDto = new GameDto(gameEngine.getGameState());
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
        GameEngine gameEngine = createSinglePlayerGameEngine(singlePlayerWebSocketId);
        long gameEngineId = gameEngine.getId();
        gameRepository.addGameEngine(gameEngineId, gameEngine);
        GameDto gameDto = new GameDto(gameEngine.getGameState());
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }

    private GameEngine createAiGameEngine() {
        GameEngine gameEngine = GameEngineFactory.getOnlineGameEngine();
        gameEngine.start();
        return gameEngine;
    }


    private GameEngine createSinglePlayerGameEngine(String playerWebSocketId) {
        killAnyRunningGame(playerWebSocketId);

        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);

        PlayerController aiPlayerController = new SimplePlayerAi(player2);
        ClientWebSocket clientWebSocket = clientWebSocketRepository.getClientWebSocket(playerWebSocketId);
        WebSocketPlayerController webSocketPlayerController = new WebSocketPlayerController(player1, clientWebSocket);

        GameEngine gameEngine = GameEngineFactory.getLocalGameEngine(player1, player2, webSocketPlayerController, aiPlayerController);

        webSocketGameStateRouter.registerForUpdates(playerWebSocketId, gameEngine.getId());
        gameEngine.getGameState().registerListener(webSocketGameStateRouter);
        //gameEngine.getGameState().registerListener(new PrintGameStateListener());
        clientWebSocket.setCurrentGameId(gameEngine.getGameState().getId());
        clientWebSocket.setPlayer(player1);
        gameEngine.start();
        return gameEngine;
    }

    private void killAnyRunningGame(String playerWebSocketId) {
        //kill any existing game started by the player
        ClientWebSocket webSocketGameStateListener = clientWebSocketRepository.getClientWebSocket(playerWebSocketId);
        GameEngine runningGame = gameRepository.getGameEngine(webSocketGameStateListener.getCurrentGameId());
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
        GameEngine gameEngine = gameRepository.getGameEngine(id);
        webSocketGameStateRouter.registerForUpdates(spectateDto.getPlayerId(), gameEngine.getId());

        GameDto gameDto = new GameDto(gameEngine.getGameState());
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
            GameEngine gameEngine = gameRepository.getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.pauseThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("unpause")) {
            GameEngine gameEngine = gameRepository.getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.resumeThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("kill")) {
            GameEngine gameEngine = gameRepository.getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.kill();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        }
    }

}