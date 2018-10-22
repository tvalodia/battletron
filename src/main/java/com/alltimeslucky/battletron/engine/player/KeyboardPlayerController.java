package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

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
    public KeyboardPlayerController(Player player) {
        this.player = player;
        this.direction = player.getDirection();
        player.setReady(false);
    }

    @Override
    public void execute(GameState gameState) {
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

        if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_ENTER) {
            player.setReady(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
