package com.alltimeslucky.battletron.server.trainer.service;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Direction;

public interface TrainingGameService {

    Game createGame();
    Game movePlayers(long gameId, Direction playerOneDirection, Direction playerTwoDirection);
    void deleteGame(long gameId);

}
