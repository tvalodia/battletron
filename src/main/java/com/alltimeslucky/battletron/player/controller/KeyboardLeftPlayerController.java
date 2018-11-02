package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardLeftPlayerController extends KeyboardPlayerController implements KeyListener {

    public KeyboardLeftPlayerController(Player player) {
        super(player);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_R) {
            player.setReady(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            direction = Direction.RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            direction = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            direction = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            direction = Direction.UP;
        }
    }

}
