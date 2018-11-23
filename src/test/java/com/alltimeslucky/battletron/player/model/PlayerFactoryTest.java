package com.alltimeslucky.battletron.player.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlayerFactoryTest {

    private PlayerFactory playerFactory = new PlayerFactory();

    @Test
    public void testPlayerOneId() {

        Player player1 = playerFactory.getPlayer1();

        assertEquals(1, player1.getId());

    }

    @Test
    public void testPlayerTwoId() {

        Player player2 = playerFactory.getPlayer2();

        assertEquals(2, player2.getId());

    }

    @Test
    public void testPlayerOnePositionX() {

        Player player1 = playerFactory.getPlayer1();

        assertEquals(33, player1.getPositionX());

    }

    @Test
    public void testPlayerOnePositionY() {

        Player player1 = playerFactory.getPlayer1();

        assertEquals(50, player1.getPositionY());

    }

    @Test
    public void testPlayerTwoPositionX() {

        Player player2 = playerFactory.getPlayer2();

        assertEquals(66, player2.getPositionX());

    }

    @Test
    public void testPlayerTwoPositionY() {

        Player player2 = playerFactory.getPlayer2();

        assertEquals(50, player2.getPositionY());

    }

}
