package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;

/**
 * This class models a player.
 */
public class Player {

    private int id;
    private int positionX;
    private int positionY;

    /**
     * Constructor.
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
     * Moves the player in the specified direction.
     *
     * @param direction The direction in which to move the player
     */
    public void move(Direction direction) {
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

}
