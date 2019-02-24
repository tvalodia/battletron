package com.alltimeslucky.battletron.player.controller.remote.api;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
import com.alltimeslucky.battletron.server.game.api.dto.PlayerDto;

public class RemoteAiGameDto extends GameDto {

    private PlayerDto player;

    public RemoteAiGameDto(Game game, Player player) {
        super(game);
        this.player = new PlayerDto(player);
    }

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }
}
