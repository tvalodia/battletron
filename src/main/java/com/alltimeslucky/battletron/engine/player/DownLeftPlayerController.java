package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

import java.util.Random;

public class DownLeftPlayerController implements PlayerController {

    private Player player;

    public DownLeftPlayerController(Player player) {
        this.player = player;
        this.player.setReady(true);
    }

    public void execute(GameState gameState) {
        player.setDirection(Direction.values()[new Random().nextInt(2)]);
    }

}
