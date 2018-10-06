package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.GameEngineFactory;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

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

    protected static final Logger LOG = LogManager.getLogger();

    /**
     * Fetches a list of all games.
     * @return The complete list of games.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameDto> getAllGames() {
        List<GameDto> dtos = new LinkedList<>();
        for (GameEngine gameEngine : GameRepository.getInstance().getAllGameEngines()) {
            GameDto gameDto = newGameDto(gameEngine.getGameState());
            gameDto.setPlayingField(null);
            dtos.add(gameDto);
        }
        LOG.debug("Response: " +  dtos);
        return dtos;
    }

    /**
     * Fetches a list of all games.
     * @return The complete list of games.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto getGame(@PathParam("id") long id) {
        GameState gameState = GameRepository.getInstance().getGameEngine(id).getGameState();
        GameDto gameDto = newGameDto(gameState);
        LOG.debug("Response: " +  gameDto);
        return gameDto;
    }

    /**
     * Starts a new game.
     * @return A GameDto object.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto startGame() {
        GameEngine gameEngine = startEngine();
        long gameEngineId = gameEngine.getId();
        GameRepository.getInstance().addGameEngine(gameEngineId, gameEngine);
        GameDto gameDto = newGameDto(gameEngine.getGameState());
        gameDto.setPlayingField(null);
        LOG.debug("Response: " +  gameDto);
        return gameDto;
    }

    private GameEngine startEngine() {
        GameEngine gameEngine = GameEngineFactory.getGameEngine();
        gameEngine.start();
        return gameEngine;
    }

    /**
     * Controls the running-state of a game.
     * @param gameEngineId The id of the game.
     * @param command The command issued to the game.
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
            GameEngine gameEngine = GameRepository.getInstance().getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.pauseThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("unpause")) {
            GameEngine gameEngine = GameRepository.getInstance().getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.resumeThread();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        } else if (command.getCommandString().toLowerCase().equals("kill")) {
            GameEngine gameEngine = GameRepository.getInstance().getGameEngine(gameEngineId);
            if (gameEngine != null) {
                gameEngine.kill();
            } else {
                throw new WebApplicationException("Invalid game ID");
            }
        }
    }

    private GameDto newGameDto(GameState gameState) {
        GameDto gameDto = new GameDto();
        gameDto.setId(gameState.getId());
        gameDto.setWidth(gameState.getWidth());
        gameDto.setHeight(gameState.getHeight());
        gameDto.setPlayer1(gameState.getPlayer1());
        gameDto.setPlayer2(gameState.getPlayer2());
        gameDto.setTickCount(gameState.getTickCount());
        gameDto.setPlayingField(gameState.getPlayingField());
        gameDto.setGameStatus(gameState.getGameStatus());
        gameDto.setWinner(gameDto.getWinner());
        return gameDto;
    }

}