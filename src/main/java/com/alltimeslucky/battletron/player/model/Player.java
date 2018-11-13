package com.alltimeslucky.battletron.player.model;

/**
 * This class models a player.
 */
public class Player {

    private int id;
    private int positionX;
    private int positionY;
    private Direction direction;

    /**
     * Default Constructor.
     */
    public Player() {}

    /**
     * Parameterised Constructor.
     * @param id The ID of the player.
     * @param positionX The starting X coordinate in the playing field.
     * @param positionY The starting Y coordinate in the playing field.
     */
    public Player(int id, int positionX, int positionY) {
        this.id = id;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getId() {
        return id;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    private void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    private void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Moves the player in the current direction.
     */
    public void move() {
        switch (direction) {
            case DOWN:
                setPositionY(getPositionY() - 1);
                break;
            case UP:
                setPositionY(getPositionY() + 1);
                break;
            case LEFT:
                setPositionX(getPositionX() - 1);
                break;
            case RIGHT:
                setPositionX(getPositionX() + 1);
                break;
            default:
                break;
        }
    }

    public boolean isReady() {
        return direction != null;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
