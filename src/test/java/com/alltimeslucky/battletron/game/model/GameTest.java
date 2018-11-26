package com.alltimeslucky.battletron.game.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.alltimeslucky.battletron.player.model.PlayerFactory;
import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private PlayerFactory playerFactory = new PlayerFactory();
    private Game game;

    @Before
    public void setup() {
        game = new Game(1,
                WIDTH, HEIGHT, playerFactory.getPlayerOne(), playerFactory.getPlayer2());
    }

    @Test
    public void testGetId() {
        assertEquals(1, game.getId());
    }

    @Test
    public void testGetWidth() {
        assertEquals(WIDTH, game.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(HEIGHT, game.getHeight());
    }

    @Test
    public void testGetTickCount() {
        assertEquals(0, game.getTickCount());
    }

    @Test
    public void testGetWinner() {
        assertNull(game.getWinner());
    }

}