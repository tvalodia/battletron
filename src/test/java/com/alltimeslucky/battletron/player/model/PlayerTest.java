package com.alltimeslucky.battletron.player.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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
        player.setDirection(Direction.LEFT); // Arrange

        player.move(); // Act

        assertEquals(X - 1, player.getPositionX()); // Assert
        assertEquals(Y, player.getPositionY()); // Assert (again)
    }

    @Test
    public void testMoveRight() {
        player.setDirection(Direction.RIGHT); // Arrange

        player.move(); // Act

        assertEquals(Y, player.getPositionY()); // Assert
        assertEquals(X + 1, player.getPositionX()); // Assert
    }

    @Test
    public void testIsReadyReturnsFalse() {
        // No Arrange required

        boolean result = player.isReady(); // Act

        assertFalse(result); // Assert
    }

    @Test
    public void testIsReadyReturnsTrueWhenSetToRight() {
        player.setDirection(Direction.RIGHT); // Arrange

        boolean result = player.isReady(); // Act

        assertTrue(result); // Assert
    }

    @Test
    public void testIsReadyReturnsTrueWhenSetToLeft() {
        player.setDirection(Direction.LEFT); // Arrange

        boolean result = player.isReady(); // Act

        assertTrue(result); // Assert
    }

    @Test
    public void testIsReadyReturnsTrueWhenSetToUp() {
        player.setDirection(Direction.UP); // Arrange

        boolean result = player.isReady(); // Act

        assertTrue(result); // Assert
    }

    @Test
    public void testIsReadyReturnsTrueWhenSetToDown() {
        player.setDirection(Direction.DOWN); // Arrange

        boolean result = player.isReady(); // Act

        assertTrue(result); // Assert
    }

    @Test
    public void testSetAndGetDirectionDown() {
        player.setDirection(Direction.DOWN); // Arrange

        Direction result = player.getDirection(); // Act

        assertEquals(Direction.DOWN, result); // Assert
    }

    @Test
    public void testSetAndGetDirectionUp() {
        player.setDirection(Direction.UP); // Arrange

        Direction result = player.getDirection(); // Act

        assertEquals(Direction.UP, result); // Assert
    }

    @Test
    public void testSetAndGetDirectionLeft() {
        player.setDirection(Direction.LEFT); // Arrange

        Direction result = player.getDirection(); // Act

        assertEquals(Direction.LEFT, result); // Assert
    }

    @Test
    public void testSetAndGetDirectionRight() {
        player.setDirection(Direction.RIGHT); // Arrange

        Direction result = player.getDirection(); // Act

        assertEquals(Direction.RIGHT, result); // Assert
    }

}
