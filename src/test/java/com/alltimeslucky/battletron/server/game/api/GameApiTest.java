package com.alltimeslucky.battletron.server.game.api;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
import com.alltimeslucky.battletron.server.game.api.dto.JoinGameDto;
import com.alltimeslucky.battletron.server.game.api.dto.NewGameDto;
import com.alltimeslucky.battletron.server.game.api.dto.SpectateGameDto;
import com.alltimeslucky.battletron.server.game.service.GameService;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameApiTest {

    private GameApi gameApi;

    @Mock
    private GameService mockGameService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        gameApi = new GameApi(mockGameService);
    }

    @Test
    public void testGetAllGames() {
        Game mockGame1 = mock(Game.class);
        when(mockGame1.getId()).thenReturn(1L);
        Game mockGame2 = mock(Game.class);
        when(mockGame2.getId()).thenReturn(2L);

        List<Game> expectedGames = Arrays.asList(mockGame1, mockGame2);

        when(mockGameService.getAllGames()).thenReturn(expectedGames);

        List<GameDto> games = gameApi.getAllGames();
        assertEquals(1L, games.get(0).getId());
        assertEquals(2L, games.get(1).getId());
    }

    @Test
    public void testGetOpenGames() {
        Game mockGame1 = mock(Game.class);
        when(mockGame1.getId()).thenReturn(1L);
        Game mockGame2 = mock(Game.class);
        when(mockGame2.getId()).thenReturn(2L);

        List<Game> expectedGames = Arrays.asList(mockGame1, mockGame2);

        when(mockGameService.getOpenGames()).thenReturn(expectedGames);

        List<GameDto> games = gameApi.getOpenGames();
        assertEquals(1L, games.get(0).getId());
        assertEquals(2L, games.get(1).getId());
    }

    @Test
    public void testGetGameSuccessful() throws Exception {
        Game mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(123L);
        when(mockGameService.getGame(1L)).thenReturn(mockGame);

        GameDto gameDto = gameApi.getGame(1L);

        assertEquals(123L, gameDto.getId());
    }


    @Test
    public void testCreateGameSuccessful() throws BattletronException {
        Player player1 = new Player();
        player1.setDirection(Direction.UP);
        Player player2 = new Player();
        player2.setDirection(Direction.DOWN);

        Game mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(123L);

        NewGameDto newGameDto = new NewGameDto();
        newGameDto.setPlayerId("1");
        newGameDto.setPlayerOneType("playerOneType");
        newGameDto.setPlayerTwoType("playerTwoType");
        when(mockGameService.createGame("1", "playerOneType", "playerTwoType")).thenReturn(mockGame);

        GameDto gameDto = gameApi.createGame(newGameDto);

        assertEquals(123L, gameDto.getId());
        assertNull(gameDto.getPlayingField());
    }

    @Test
    public void testSpectateGameSuccessful() throws Exception {
        Player player1 = new Player();
        player1.setDirection(Direction.UP);
        Player player2 = new Player();
        player2.setDirection(Direction.DOWN);

        Game mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(123L);

        SpectateGameDto spectateGameDto = new SpectateGameDto();
        spectateGameDto.setPlayerId("2");
        when(mockGameService.spectateGame(1L, "2")).thenReturn(mockGame);

        GameDto gameDto = gameApi.spectateGame(1L, spectateGameDto);

        assertEquals(123L, gameDto.getId());
    }

    @Test
    public void testJoinGameSuccessful() throws Exception {
        Player player1 = new Player();
        player1.setDirection(Direction.UP);
        Player player2 = new Player();
        player2.setDirection(Direction.DOWN);

        Game mockGame = mock(Game.class);
        when(mockGame.getId()).thenReturn(123L);

        JoinGameDto joinGameDto = new JoinGameDto();
        joinGameDto.setPlayerId("2");
        when(mockGameService.joinGame(1L, "2")).thenReturn(mockGame);

        GameDto gameDto = gameApi.joinGame(1L, joinGameDto);

        assertEquals(123L, gameDto.getId());
    }
}
