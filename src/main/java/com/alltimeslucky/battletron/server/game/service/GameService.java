package com.alltimeslucky.battletron.server.game.service;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.controller.PlayerControllerType;

import java.util.List;

public interface GameService {

    /**
     * Gets all stored instances of Game objects.
     *
     * @return A list of Game
     */
    List<Game> getAllGames();

    /**
     * Gets all Game objects that have an available (null) PlayerController.
     *
     * @return A list of GameControllers
     */
    List<Game> getOpenGames();

    /**
     * Gets the GameControlelr with the specified ID.
     *
     * @return The complete list of games.
     */
    Game getGame(long id) throws BattletronException;

    /**
     * Creates a new GameController with a Game for the two given player types.
     *
     * @param playerId    The id of the player creating the game
     * @param playerOneType The type of player for player 1
     * @param playerTwoType The type pf player for player 2
     * @return A new GameController
     */
    Game createGame(String playerId, String playerOneType, String playerTwoType) throws BattletronException;

    /**
     * Start spectating a game.
     *
     * @return The GameController of the game to spectate
     */
    Game spectateGame(long gameId, String playerId) throws Exception;

    /**
     * Join a game.
     *
     * @return The game that the player is joining
     */
    Game joinGame(long gameId, String playerId) throws BattletronException;

    /**
     * Pauses the specified game.
     *
     * @param gameId The id of the game to pause
     * @throws Exception Thrown when the gameId is invalid
     */
    void pauseGame(long gameId) throws Exception;

    /**
     * Resumes the specified game.
     *
     * @param gameId The id of the game to resume
     * @throws Exception Thrown when the gameId is invalid
     */
    void resumeGame(long gameId) throws Exception;

    /**
     * Deletes the specified game.
     *
     * @param gameId The id of the game to delete
     * @throws Exception Thrown when the gameId is invalid
     */
    void deleteGame(long gameId) throws Exception;

    /**
     * Stops the specified game.
     *
     * @param gameId The id of the game to stop
     * @throws Exception Thrown when the gameId is invalid
     */
    void stopGame(long gameId) throws Exception;
}
