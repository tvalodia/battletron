package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;



public class PlayerTest {

    private static final int X = 10;
    private static final int Y = 20;
    private Player player;

    @Before
    public void setup() {
        player = new Player(1, X, Y);
    }

    @Test
    public void testGetId() {
        assertEquals(1, player.getId());
    }

    @Test
    public void testMoveUp() {
        player.move(Direction.UP);

        assertEquals(Y + 1, player.getPositionY());
        assertEquals(X, player.getPositionX());
    }

    @Test
    public void testMoveDown() {
        player.move(Direction.DOWN);

        assertEquals(X, player.getPositionX());
        assertEquals(Y - 1, player.getPositionY());
    }

    @Test
    public void testMoveLeft() {
        player.move(Direction.LEFT);

        assertEquals(X - 1, player.getPositionX());
        assertEquals(Y, player.getPositionY());
    }

    @Test
    public void testMoveRight() {
        player.move(Direction.RIGHT);

        assertEquals(Y, player.getPositionY());
        assertEquals(X + 1, player.getPositionX());
    }
}
