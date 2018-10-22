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
        player = new Player(1, X, Y, Direction.RIGHT);
    }

    @Test
    public void testGetId() {
        assertEquals(1, player.getId());
    }

    @Test
    public void testMoveUp() {
        player.setDirection(Direction.UP);
        player.move();

        assertEquals(Y + 1, player.getPositionY());
        assertEquals(X, player.getPositionX());
    }

    @Test
    public void testMoveDown() {
        player.setDirection(Direction.DOWN);
        player.move();

        assertEquals(X, player.getPositionX());
        assertEquals(Y - 1, player.getPositionY());
    }

    @Test
    public void testMoveLeft() {
        player.setDirection(Direction.LEFT);
        player.move();

        assertEquals(X - 1, player.getPositionX());
        assertEquals(Y, player.getPositionY());
    }

    @Test
    public void testMoveRight() {
        player.setDirection(Direction.RIGHT);
        player.move();

        assertEquals(Y, player.getPositionY());
        assertEquals(X + 1, player.getPositionX());
    }
}
