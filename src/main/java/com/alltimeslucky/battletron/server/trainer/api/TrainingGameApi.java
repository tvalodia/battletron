package com.alltimeslucky.battletron.server.trainer.api;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.server.trainer.api.dto.GameDto;
import com.alltimeslucky.battletron.server.trainer.api.dto.MovePlayersDto;
import com.alltimeslucky.battletron.server.trainer.service.TrainingGameService;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Path("/training/game")
public class TrainingGameApi {

    private static final Logger LOG = LogManager.getLogger();
    private final TrainingGameService gameService;

    /**
     * Constructor.
     *
     * @param gameService The GameService instance that provides business logic
     */
    @Autowired
    public TrainingGameApi(TrainingGameService gameService) {
        this.gameService = gameService;
    }


    /**
     * Starts a new game.
     *
     * @return A GameDto object.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto createGame() throws BattletronException {
        Game game = gameService.createGame();
        GameDto gameDto = new GameDto(game);
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/move")
    public GameDto movePlayers(@PathParam("id") long gameId, MovePlayersDto dto) throws BattletronException {
        Game game = gameService.movePlayers(gameId, dto.getPlayerOneDirection(), dto.getPlayerTwoDirection());
        GameDto gameDto = new GameDto(game);
        LOG.debug("Response: " + gameDto);
        return gameDto;
    }


    /**
     * Deletes a game from the system.
     * @param id The ID of the game to delete.
     * @throws BattletronException Thrown if an error occurs.
     */
    @DELETE
    @Path("{id}")
    public void deleteGame(@PathParam("id") long id) throws BattletronException {
        gameService.deleteGame(id);
    }

}