package com.alltimeslucky.battletron.trainer.api;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;

public class TrainerGameListener implements GameListener {


    private TrainerApi trainerApi;

    TrainerGameListener(TrainerApi trainerApi) {
        this.trainerApi = trainerApi;
    }


    @Override
    public void onGameStateUpdate(Game game) throws BattletronException {
        trainerApi.update(new GameDto(game));
    }
}
