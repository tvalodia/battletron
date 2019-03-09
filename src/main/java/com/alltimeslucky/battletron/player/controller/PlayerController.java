package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.model.Game;

public interface PlayerController {

    void execute(Game game) throws BattletronException;

}
