package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.player.model.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class depicts the state of a game.
 */
public class Game {

    //The unique identifier of this instance of a gamestate
    private long id;
    // The width of the playing field.
    private int width;
    // The height of the playing field.
    private int height;
    //The current phase in the lifecycle of the game
    private GameStatus gameStatus;
    private int tickCount;
    private final Player playerOne;
    private final Player playerTwo;

    //Keeps track of players trails
    private int[][] playingField;

    //The winner of the current game is there is one.
    private Player winner;

    private final List<GameListener> gameListeners;

    /**
     * Constructor.
     * Initialises the playingField.
     *
     * @param id        The unique identifier of the game state
     * @param width     The width of the playing field
     * @param height    The height of the playing field
     * @param playerOne The first player of the game
     * @param playerTwo The second player of the game
     */
    public Game(long id, int width, int height, Player playerOne, Player playerTwo) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.tickCount = 0;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playingField = new int[width][height];
        this.playingField[playerOne.getPositionX()][playerOne.getPositionY()] = playerOne.getId();
        this.playingField[playerTwo.getPositionX()][playerTwo.getPositionY()] = playerTwo.getId();
        this.winner = null;
        this.gameListeners = Collections.synchronizedList(new LinkedList<>());
        this.gameStatus = GameStatus.WAITING_FOR_READY;
    }

    /**
     * Determines whether the specified player collides with any trails or the playing
     * field boundaries.
     *
     * @param player The player for which to test collisions
     * @return true if the player is colliding with any obstacles
     */
    private boolean isColliding(Player player) {
        if (player.getPositionX() >= width || player.getPositionX() < 0) {
            return true;
        } else if (player.getPositionY() >= height || player.getPositionY() < 0) {
            return true;
        } else {
            return (playingField[player.getPositionX()][player.getPositionY()] != 0);
        }
    }

    /**
     * Determines whether the two players are colliding.
     *
     * @param playerOne Player 1
     * @param playerTwo Player 2
     * @return true is the two players are colliding
     */
    private boolean isColliding(Player playerOne, Player playerTwo) {
        return playerOne.getPositionX() == playerTwo.getPositionX() && playerOne.getPositionY() == playerTwo.getPositionY();
    }

    /**
     * Updates the playing field with players' trails.
     */
    public void update() {

        if (isReadyToStart()) {
            gameStatus = GameStatus.STARTED;
        }

        if (gameStatus == GameStatus.STARTED) {
            //update the player given the AI's direction input
            playerOne.move();
            playerTwo.move();

            //Check for collisions on the playing field
            if (isColliding(playerOne) && isColliding(playerTwo) || isColliding(playerOne, playerTwo)) {
                setGameStatus(GameStatus.COMPLETED_DRAW);
            } else if (isColliding(playerOne)) {
                setGameStatus(GameStatus.COMPLETED_WINNER);
                setWinner(playerTwo);
            } else if (isColliding(playerTwo)) {
                setGameStatus(GameStatus.COMPLETED_WINNER);
                setWinner(playerOne);
            }

            if (playerOne.getPositionX() >= 0 && playerOne.getPositionX() < width
                    && playerOne.getPositionY() >= 0 && playerOne.getPositionY() < height) {
                playingField[playerOne.getPositionX()][playerOne.getPositionY()] = playerOne.getId();
            }

            if (playerTwo.getPositionX() >= 0 && playerTwo.getPositionX() < width
                    && playerTwo.getPositionY() >= 0 && playerTwo.getPositionY() < height) {
                playingField[playerTwo.getPositionX()][playerTwo.getPositionY()] = playerTwo.getId();
            }

            tickCount++;
        }

        //send an update to the observers. This loop is synchronised to allow for new spectators to be added to the
        // list by another thread without modifying the list while the iterator is reading the list.
        synchronized (gameListeners) {
            gameListeners.forEach(gameStateListener -> gameStateListener.onGameStateUpdate(this));
        }

    }

    private boolean isReadyToStart() {
        return playerOne.isReady() && playerTwo.isReady();
    }

    public long getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getPlayingField() {
        return playingField;
    }

    public void setWinner(Player player) {
        winner = player;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void registerListener(GameListener listener) {
        gameListeners.add(listener);
    }

    public void stop() {
        setGameStatus(GameStatus.STOPPED);
    }

    /* public void deregisterListener(GameListener listener) {
        gameListeners.remove(listener);
    } */

}
