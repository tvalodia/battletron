package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;

/**
 * This class models a player.
 */
public class Player {

    private int id;
    private int positionX;
    private int positionY;
    private boolean ready;
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
    public Player(int id, int positionX, int positionY, Direction startingDirection) {
        this.id = id;
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = startingDirection;
        this.ready = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Moves the player in the current direction.
     *
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
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
