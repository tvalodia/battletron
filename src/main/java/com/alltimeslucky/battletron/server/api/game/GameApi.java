package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerControllerFactory;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameStateRouterFactory;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameUpdateRouter;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
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

    private final GameControllerRepository gameControllerRepository;
    private final ClientWebSocketRepository clientWebSocketRepository;
    private WebSocketGameUpdateRouter webSocketGameStateRouter;

    /**
     * Constructor.
     */
    @Inject
    public GameApi() {
        gameControllerRepository = GameControllerRepository.getInstance();
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
        for (GameController gameController : gameControllerRepository.getAllGames()) {
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
        Game game = gameControllerRepository.get(id).getGame();
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
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto createGame(NewGameDto dto) {
        killAnyRunningGame(dto.getPlayerId());
        Player player1 = new Player(1, 33, 50);
        Player player2 = new Player(2, 66, 50);
        Game game = GameFactory.getGame(player1, player2);

        PlayerController player1Controller = PlayerControllerFactory.getPlayerController(dto.getPlayer1Type(), dto.getPlayerId(), player1);
        PlayerController player2Controller = PlayerControllerFactory.getPlayerController(dto.getPlayer2Type(), dto.getPlayerId(), player2);
        GameController gameController = GameControllerFactory.getGameController(game, player1Controller, player2Controller);
        gameControllerRepository.add(gameController.getGameId(), gameController);

        webSocketGameStateRouter.registerForUpdates(dto.getPlayerId(), gameController.getGameId());
        game.registerListener(webSocketGameStateRouter);
        //gameController.getGame().registerListener(new PrintGameListener());
        ClientWebSocket clientWebSocket = clientWebSocketRepository.get(dto.getPlayerId());
        clientWebSocket.setCurrentGameId(gameController.getGame().getId());

        gameController.start();

        GameDto gameDto = new GameDto(gameController.getGame());
        gameDto.setPlayingField(null);
        LOG.debug("Response: " + gameDto);
        return gameDto;
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
     * Registers for spectating a game.
     *
     * @return A GameDto object.
     */
    @POST
    @Path("{id}/spectate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GameDto spectateGame(@PathParam("id") long id, SpectateDto spectateDto) {
        killAnyRunningGame(spectateDto.getPlayerId());
        GameController gameController = gameControllerRepository.get(id);
        webSocketGameStateRouter.registerForUpdates(spectateDto.getPlayerId(), gameController.getGameId());

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
            GameController gameController = gameControllerRepository.get(gameEngineId);
            if (gameController != null) {
                gameController.pauseThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("unpause")) {
            GameController gameController = gameControllerRepository.get(gameEngineId);
            if (gameController != null) {
                gameController.resumeThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("kill")) {
            GameController gameController = gameControllerRepository.get(gameEngineId);
            if (gameController != null) {
                gameController.kill();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        }
    }

}