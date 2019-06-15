package com.alltimeslucky.battletron.server.trainer.api.dto;

import com.alltimeslucky.battletron.player.model.Direction;

public class MovePlayersDto {

    private Direction playerOneDirection;
    private Direction playerTwoDirection;

    public Direction getPlayerOneDirection() {
        return playerOneDirection;
    }

    public void setPlayerOneDirection(Direction playerOneDirection) {
        this.playerOneDirection = playerOneDirection;
    }

    public Direction getPlayerTwoDirection() {
        return playerTwoDirection;
    }

    public void setPlayerTwoDirection(Direction playerTwoDirection) {
        this.playerTwoDirection = playerTwoDirection;
    }
}
