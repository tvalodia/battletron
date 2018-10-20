package com.alltimeslucky.battletron.ui;

import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class BattletronWindow {

    private GameView drawArea;

    private KeyListener keyboardPlayer1Controller;
    private KeyListener keyboardPlayer2Controller;

    public BattletronWindow(KeyListener keyboardPlayer1Controller, KeyListener keyboardPlayer2Controller) {
        this.keyboardPlayer1Controller = keyboardPlayer1Controller;
        this.keyboardPlayer2Controller = keyboardPlayer2Controller;
        drawArea = new GameView();
    }

    /**
     * Initialises the main window and displays it.
     */
    public void initialise() {

        // create main frame
        JFrame frame = new JFrame("Battletron");
        Container content = frame.getContentPane();
        // set layout on content pane
        content.setLayout(new BorderLayout());

        // add to content pane
        content.add(drawArea, BorderLayout.CENTER);

        frame.setSize(816, 840);
        // can close frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(keyboardPlayer1Controller);
        frame.addKeyListener(keyboardPlayer2Controller);
        // show the swing paint result
        frame.setVisible(true);
    }

    public GameStateListener getGameStateListener() {
        return drawArea;
    }
}
