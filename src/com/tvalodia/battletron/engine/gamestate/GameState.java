package com.tvalodia.battletron.engine.gamestate;

import com.tvalodia.battletron.engine.Direction;
import com.tvalodia.battletron.engine.GameStatus;
import com.tvalodia.battletron.engine.Player;

/**
 * This class depicts the state of a game.
 */
public class GameState {

    // The width of the playing field.
    private int width;
    // The height of the playing field.
    private int height;
    //The current phase in the lifecycle of the game
    private GameStatus gameStatus;

    private final Player player1;
    private final Player player2;

    //Keeps track of players trails
    private int[][] playingField;

    //The winner of the current game is there is one.
    private Player winner;

    /**
     * Constructor.
     * Initialises the playingField.
     *
     * @param width   The width of the playing field
     * @param height  The height of the playing field
     * @param player1 The first player of the game
     * @param player2 The second player of the game
     */
    public GameState(int width, int height, Player player1, Player player2) {
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        playingField = new int[width][height];
        playingField[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        playingField[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        winner = null;
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
     * Updates the specified players position
     *
     * @param player
     * @param direction
     */
    public void updatePlayer(Player player, Direction direction) {
        switch (direction) {
            case DOWN:
                player.setPositionY(player.getPositionY() - 1);
                break;
            case UP:
                player.setPositionY(player.getPositionY() + 1);
                break;
            case LEFT:
                player.setPositionX(player.getPositionX() - 1);
                break;
            case RIGHT:
                player.setPositionX(player.getPositionX() + 1);
                break;
        }
    }

    /**
     * Updates the playing field with players' trails.
     */
    public void updatePlayingField() {

        if (player1.getPositionX() >= 0 && player1.getPositionX() < width
                && player1.getPositionY() >= 0 && player1.getPositionY() < height) {
            playingField[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        }

        if (player2.getPositionX() >= 0 && player2.getPositionX() < width
                && player2.getPositionY() >= 0 && player2.getPositionY() < height) {
            playingField[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        }
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
}
