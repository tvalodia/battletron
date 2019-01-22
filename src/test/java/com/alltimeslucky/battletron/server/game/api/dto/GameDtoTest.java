package com.alltimeslucky.battletron.server.game.api.dto;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameDtoTest {

    @Test
    public void testConstructorSuccessful() {
        Player player1 = new Player();
        player1.setDirection(Direction.UP);
        Player player2 = new Player();
        player2.setDirection(Direction.DOWN);

        Game mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(123L);
        when(mockGame.getPlayerOne()).thenReturn(player1);
        when(mockGame.getPlayerTwo()).thenReturn(player2);
        when(mockGame.getGameStatus()).thenReturn(GameStatus.WAITING_FOR_READY);
        when(mockGame.getHeight()).thenReturn(100);
        when(mockGame.getWidth()).thenReturn(200);
        when(mockGame.getWinner()).thenReturn(player2);

        GameDto gameDto = new GameDto(mockGame);

        assertEquals(123L, gameDto.getId());
        assertEquals(GameStatus.WAITING_FOR_READY, gameDto.getGameStatus());
        assertEquals(100, gameDto.getHeight());
        assertEquals(200, gameDto.getWidth());
        assertEquals(0, gameDto.getTickCount());
        assertEquals(Direction.UP, gameDto.getPlayerOne().getDirection());
        assertEquals(Direction.DOWN, gameDto.getPlayerTwo().getDirection());
        assertSame(player2, gameDto.getWinner());
        assertNull(gameDto.getPlayingField());
    }
}
