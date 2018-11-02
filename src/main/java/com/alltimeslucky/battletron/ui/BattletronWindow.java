package com.alltimeslucky.battletron.ui;

import com.alltimeslucky.battletron.game.model.GameListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class BattletronWindow {

    private GameView drawArea;

    private KeyListener player1KeyListener;
    private KeyListener player2KeyListener;

    /**
     * Constructor. Takes implementations of KeyListener for handling keys initiated by the two players.
     * @param player1KeyListener The KeyListener for handling keys pressed by player 1.
     * @param player2KeyListener The KeyListener for handling keys pressed by player 2.
     */
    public BattletronWindow(KeyListener player1KeyListener, KeyListener player2KeyListener) {
        this.player1KeyListener = player1KeyListener;
        this.player2KeyListener = player2KeyListener;
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
        frame.addKeyListener(player1KeyListener);
        frame.addKeyListener(player2KeyListener);
        // show the swing paint result
        frame.setVisible(true);
    }

    public GameListener getGameStateListener() {
        return drawArea;
    }
}
