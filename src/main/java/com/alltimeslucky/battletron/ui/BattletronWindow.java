package com.alltimeslucky.battletron.ui;

import com.alltimeslucky.battletron.game.model.GameListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class BattletronWindow {

    private GameView drawArea;

    private KeyListener playerOneKeyListener;
    private KeyListener playerTwoKeyListener;

    /**
     * Constructor. Takes implementations of KeyListener for handling keys initiated by the two players.
     * @param playerOneKeyListener The KeyListener for handling keys pressed by player 1.
     * @param playerTwoKeyListener The KeyListener for handling keys pressed by player 2.
     */
    public BattletronWindow(KeyListener playerOneKeyListener, KeyListener playerTwoKeyListener) {
        this.playerOneKeyListener = playerOneKeyListener;
        this.playerTwoKeyListener = playerTwoKeyListener;
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
        frame.addKeyListener(playerOneKeyListener);
        frame.addKeyListener(playerTwoKeyListener);
        // show the swing paint result
        frame.setVisible(true);
    }

    public GameListener getGameStateListener() {
        return drawArea;
    }
}
