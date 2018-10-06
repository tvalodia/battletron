package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.engine.GameStatus;
import com.alltimeslucky.battletron.engine.player.Player;

import java.util.Arrays;

public class GameDto {

    //The unique identifier of this instance of a gamestate
    private long id;
    // The width of the playing field.
    private int width;
    // The height of the playing field.
    private int height;
    //The current phase in the lifecycle of the game
    private GameStatus gameStatus;
    private int tickCount;
    private Player player1;
    private Player player2;
    //Keeps track of players trails
    private int[][] playingField;
    //The winner of the current game is there is one.
    private Player winner;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int[][] getPlayingField() {
        return playingField;
    }

    public void setPlayingField(int[][] playingField) {
        this.playingField = playingField;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GameDto{"
                + "id=" + id
                + ", width=" + width
                + ", height=" + height
                + ", gameStatus=" + gameStatus
                + ", tickCount=" + tickCount
                + ", player1=" + player1
                + ", player2=" + player2
                + ", playingField=" + Arrays.toString(playingField)
                + ", winner=" + winner
                + '}';
    }
}
