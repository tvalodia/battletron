package com.alltimeslucky.battletron.server.game.api;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.server.game.api.dto.GameCommandDto;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
import com.alltimeslucky.battletron.server.game.api.dto.NewGameDto;
import com.alltimeslucky.battletron.server.game.api.dto.SpectateDto;
import com.alltimeslucky.battletron.server.game.service.GameService;

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
    private final GameService gameService;

    /**
     * Constructor.
     *
     * @param gameService The GameService instance that provides business logic
     */
    @Inject
    public GameApi(GameService gameService) {
        this.gameService = gameService;
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
        for (Game game : gameService.getAllGames()) {
            GameDto gameDto = new GameDto(game);
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
        Game game = gameService.getGame(id);
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
        Game game = gameService.createGame(dto.getPlayerId(), dto.getPlayer1Type(), dto.getPlayer2Type());
        GameDto gameDto = new GameDto(game);
        gameDto.setPlayingField(null);
        LOG.debug("Response: " + gameDto);
        return gameDto;
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
    public GameDto spectateGame(@PathParam("id") long id, SpectateDto spectateDto) throws Exception {
        Game game = gameService.spectateGame(id, spectateDto.getPlayerId());
        GameDto gameDto = new GameDto(game);
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
    public void updateGameStatus(@PathParam("id") long gameEngineId, GameCommandDto command) throws Exception {
        LOG.debug("Request: id = " + gameEngineId + " " + command);
        if (command == null || command.getCommandString() == null || command.getCommandString().isEmpty()) {
            throw new WebApplicationException("Invalid command");
        }

        if (command.getCommandString().toLowerCase().equals("pause")) {
            gameService.pauseGame(gameEngineId);
        } else if (command.getCommandString().toLowerCase().equals("unpause")) {
             gameService.resumeGame(gameEngineId);
        } else if (command.getCommandString().toLowerCase().equals("kill")) {
            gameService.deleteGame(gameEngineId);
        }
    }

}