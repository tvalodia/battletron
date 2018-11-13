package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class responds to keypress events and updates the direction of the player accordingly.
 * Both the WASD and Arrow keys can change the direction of the player.
 */
public class KeyboardPlayerController implements PlayerController, KeyListener {

    protected Player player;
    protected Direction direction;

    /**
     * Constructor. Requires the player instance to be controlled by this controller.
     * @param player The player that this class will control.
     */
    KeyboardPlayerController(Player player) {
        this.player = player;
    }

    @Override
    public void execute(Game game) {
        player.setDirection(direction);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            direction = Direction.RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            direction = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            direction = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            direction = Direction.UP;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
