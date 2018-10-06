package com.alltimeslucky.battletron.ui;

import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class BattletronWindow {

    private GameView drawArea;

    /**
     * Initialises the main window and displays it.
     */
    public void initialise() {

        // create main frame
        JFrame frame = new JFrame("Battletron");
        Container content = frame.getContentPane();
        // set layout on content pane
        content.setLayout(new BorderLayout());
        // create draw area
        drawArea = new GameView();

        // add to content pane
        content.add(drawArea, BorderLayout.CENTER);

        frame.setSize(816, 840);
        // can close frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // show the swing paint result
        frame.setVisible(true);
    }

    public GameStateListener getGameStateListener() {
        return drawArea;
    }
}