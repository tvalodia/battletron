package com.alltimeslucky.battletron.engine.gamestate;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.GameStatus;
import com.alltimeslucky.battletron.engine.player.Player;

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
    private int tickCount;
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
        this.tickCount = 0;
        this.player1 = player1;
        this.player2 = player2;
        this.playingField = new int[width][height];
        this.playingField[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        this.playingField[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        this.winner = null;
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
     * @param player1Direction The direction that player 1 wants to move.
     * @param player2Direction The direction that player 2 wants to move.
     */
    public void update(Direction player1Direction, Direction player2Direction) {
        //update the player given the AI's direction input
        player1.move(player1Direction);
        player2.move(player2Direction);

        //Check for collisions on the playing field
        if (isColliding(player1)) {
            setGameStatus(GameStatus.WINNER);
            setWinner(player2);
        } else if (isColliding(player2)) {
            setGameStatus(GameStatus.WINNER);
            setWinner(player1);
        } else if (isColliding(player1, player2)) {
            setGameStatus(GameStatus.DRAW);
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
}
