package com.alltimeslucky.battletron.server.game.api.dto;

import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import java.util.Map;

public class PlayerDto {

    private int id;
    private int positionX;
    private int positionY;
    private Direction direction;
    private Map<String, String> parameters;

    /**
     * Constructor.
     * @param player The Player object to convert.
     */
    public PlayerDto(Player player) {
        setId(player.getId());
        setPositionX(player.getPositionX());
        setPositionY(player.getPositionY());
        setDirection(player.getDirection());
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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
