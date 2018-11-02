package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Player;

import java.util.Random;

public class DownLeftPlayerController implements PlayerController {

    private Player player;

    public DownLeftPlayerController(Player player) {
        this.player = player;
        this.player.setReady(true);
    }

    public void execute(Game game) {
        player.setDirection(Direction.values()[new Random().nextInt(2)]);
    }

}
