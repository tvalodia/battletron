package org.tvalodia.tronbattle.engine;

public class GameState {

    private int width;
    private int height;
    private final Player player1;
    private final Player player2;
    private LevelStatus levelStatus;
    private int[][] level;
    private Player winner;

    public GameState(int width, int height,
                     Player player1, Player player2) {
        this.width = width;
        this.height = height;
        this.player1 = player1;
        this.player2 = player2;
        level = new int[width][height];
        level[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        level[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        winner = null;
    }

    public boolean isCollision(Player player) {
        if (player.getPositionX() >= width || player.getPositionX() < 0) {
            return true;
        } else if (player.getPositionY() >= height || player.getPositionY() < 0) {
            return true;
        } else if (level[player.getPositionX()][player.getPositionY()] != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCollision(Player player1, Player player2) {
        return player1.getPositionX() == player2.getPositionX() && player1.getPositionY() == player2.getPositionY();
    }

    private void updateTrail(Player player, Direction direction) {
        switch (direction) {
            case DOWN:
                level[player.getPositionX()][player.getPositionY() - 1] = player.getId();
                break;
            case UP:
                level[player.getPositionX()][player.getPositionY() + 1] = player.getId();
                break;
            case LEFT:
                level[player.getPositionX() - 1][player.getPositionY()] = player.getId();
                break;
            case RIGHT:
                level[player.getPositionX() + 1][player.getPositionY()] = player.getId();
                break;
        }

    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public LevelStatus getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(LevelStatus levelStatus) {
        this.levelStatus = levelStatus;
    }

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

    public void updateLevel() {

        if (player1.getPositionX() >= 0 && player1.getPositionX() < width
        && player1.getPositionY() >= 0 && player1.getPositionY() < height) {
            level[player1.getPositionX()][player1.getPositionY()] = player1.getId();
        }

        if (player2.getPositionX() >= 0 && player2.getPositionX() < width
                && player2.getPositionY() >= 0 && player2.getPositionY() < height) {
            level[player2.getPositionX()][player2.getPositionY()] = player2.getId();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getLevel() {
        return level;
    }

    public void setWinner(Player player) {
        winner = player;
    }

    public Player getWinner() {
        return winner;
    }
}
