package com.alltimeslucky.battletron.player.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerFactoryTest {

    private PlayerFactory playerFactory = new PlayerFactory();

    @Test
    public void testPlayerOneId() {

        Player playerOne = playerFactory.getPlayerOne();

        assertEquals(1, playerOne.getId());

    }

    @Test
    public void testPlayerTwoId() {

        Player playerTwo = playerFactory.getPlayerTwo();

        assertEquals(2, playerTwo.getId());

    }

    @Test
    public void testPlayerOnePositionX() {

        Player playerOne = playerFactory.getPlayerOne();

        assertEquals(33, playerOne.getPositionX());

    }

    @Test
    public void testPlayerOnePositionY() {

        Player playerOne = playerFactory.getPlayerOne();

        assertEquals(50, playerOne.getPositionY());

    }

    @Test
    public void testPlayerTwoPositionX() {

        Player playerTwo = playerFactory.getPlayerTwo();

        assertEquals(66, playerTwo.getPositionX());

    }

    @Test
    public void testPlayerTwoPositionY() {

        Player playerTwo = playerFactory.getPlayerTwo();

        assertEquals(50, playerTwo.getPositionY());

    }

}
