package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardRightPlayerController extends KeyboardPlayerController implements KeyListener {

    public KeyboardRightPlayerController(Player player) {
        super(player);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direction = Direction.RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            direction = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            direction = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            direction = Direction.UP;
        }
    }
}
