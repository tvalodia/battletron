package com.alltimeslucky.battletron.server.trainer.service;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.game.repository.GameRepository;
import com.alltimeslucky.battletron.player.model.Direction;

import org.springframework.stereotype.Component;

@Component
public class TrainingGameServiceImpl implements TrainingGameService {

    private GameRepository gameRepository;
    private GameFactory gameFactory;

    public TrainingGameServiceImpl(GameRepository gameRepository, GameFactory gameFactory) {
        this.gameRepository = gameRepository;
        this.gameFactory = gameFactory;
    }

    @Override
    public Game createGame() {
        Game game = gameFactory.get();
        gameRepository.add(game.getId(), game);
        return game;
    }

    @Override
    public Game movePlayers(long gameId, Direction playerOneDirection, Direction playerTwoDirection) {
        Game game = gameRepository.get(gameId);
        game.setPlayerDirections(playerOneDirection, playerTwoDirection);
        game.update();
        return game;
    }


    @Override
    public void deleteGame(long gameId) {
        gameRepository.delete(gameId);
    }
}
