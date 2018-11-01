package com.alltimeslucky.battletron.engine.gamestate;

import com.alltimeslucky.battletron.engine.GameStatus;
import com.alltimeslucky.battletron.engine.player.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class depicts the state of a game.
 */
public class GameState {

    //The unique identifier of this instance of a gamestate
    private long id;
    // The width of the playing field.
    private int width;
    // The height of the playing field.
    private int height;
    //The current phase in the lifecycle of the game
    private GameStatus gameStatus;
    private int tickCount;
    private final Player player1;
    private final Player player2;

    //Keeps track of players trails
    private int[][] playingField;

    //The winner of the current game is there is one.
    private Player winner;

    private final List<GameStateListener> gameStateListeners;

    /**
     * Constructor.
     * Initialises the playingField.
     *
     * @param id      The unique identifier of the game state
     * @param width   The width of the playing field
     * @param height  The height of the playing field
     * @param player1 The first player of the game
     * @param player2 The second player of the game
     */
    public GameState(long id, int width, int height, Player player1, Player player2) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.tickCount = 0;
        this.player1 = player1;
        this.player2 = player2;
        this.playingField = new int[width][height];
        this.playingField[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        this.playingField[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        this.winner = null;
        this.gameStateListeners = Collections.synchronizedList(new LinkedList<>());
        this.gameStatus = GameStatus.WAITING_FOR_READY;
    }

    /**
     * Determines whether the specified player collides with any trails or the playing
     * field boundaries.
     *
     * @param player The player for which to test collisions
     * @return true if the player is colliding with any obstacles
     */
    public boolean isColliding(Player player) {
        if (player.getPositionX() >= width || player.getPositionX() < 0) {
            return true;
        } else if (player.getPositionY() >= height || player.getPositionY() < 0) {
            return true;
        } else if (playingField[player.getPositionX()][player.getPositionY()] != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines whether the two players are colliding.
     *
     * @param player1 Player 1
     * @param player2 Player 3
     * @return true is the two players are colliding
     */
    public boolean isColliding(Player player1, Player player2) {
        return player1.getPositionX() == player2.getPositionX() && player1.getPositionY() == player2.getPositionY();
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
            player1.move();
            player2.move();

            //Check for collisions on the playing field
            if (isColliding(player1) && isColliding(player2) || isColliding(player1, player2)) {
                setGameStatus(GameStatus.COMPLETED_DRAW);
            } else if (isColliding(player1)) {
                setGameStatus(GameStatus.COMPLETED_WINNER);
                setWinner(player2);
            } else if (isColliding(player2)) {
                setGameStatus(GameStatus.COMPLETED_WINNER);
                setWinner(player1);
            }

            if (player1.getPositionX() >= 0 && player1.getPositionX() < width
                    && player1.getPositionY() >= 0 && player1.getPositionY() < height) {
                playingField[player1.getPositionX()][player1.getPositionY()] = player1.getId();
            }

            if (player2.getPositionX() >= 0 && player2.getPositionX() < width
                    && player2.getPositionY() >= 0 && player2.getPositionY() < height) {
                playingField[player2.getPositionX()][player2.getPositionY()] = player2.getId();
            }

            tickCount++;
        }

        //send an update to the observers. This loop is synchronised to allow for new spectators to be added to the
        // list by another thread without modifying the list while the iterator is reading the list.
        synchronized (gameStateListeners) {
            gameStateListeners.forEach(gameStateListener -> gameStateListener.onGameStateUpdate(this));
        }

    }

    private boolean isReadyToStart() {
        return player1.isReady() && player2.isReady();
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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getTickCount() {
        return tickCount;
    }

    public void registerListener(GameStateListener listener) {
        gameStateListeners.add(listener);
    }

    public void deregisterListener(GameStateListener listener) {
        gameStateListeners.remove(listener);
    }

}
