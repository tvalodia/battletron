package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardPlayerController implements PlayerController, KeyListener {

    private Direction direction = Direction.RIGHT;

    @Override
    public Direction getDirection(GameState gameState) {
        return direction;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode()== KeyEvent.VK_D)
           direction = Direction.RIGHT;
        else if(e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()== KeyEvent.VK_A)
            direction = Direction.LEFT;
        else if(e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode()== KeyEvent.VK_S)
            direction = Direction.DOWN;
        else if(e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()== KeyEvent.VK_W)
            direction = Direction.UP;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
