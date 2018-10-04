package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.GameEngineFactory;

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

@Path("/game")
public class GameApi {

    /**
     * Fetches a list of all games.
     * @return The complete list of games.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameDto> getGames() {
        List<GameDto> dtos = new LinkedList<>();
        for (GameEngine gameEngine : GameRepository.getInstance().getAllGameEngines()) {
            GameDto gameDto = new GameDto();
            gameDto.setId(gameEngine.getId());
            dtos.add(gameDto);
        }
        return dtos;
    }

    /**
     * Starts a new game.
     * @return A GameDto object.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto newGame() {
        GameEngine gameEngine = startEngine();
        long gameEngineId = gameEngine.getId();
        GameRepository.getInstance().addGameEngine(gameEngineId, gameEngine);
        GameDto gameDto = new GameDto();
        gameDto.setId(gameEngineId);

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

}