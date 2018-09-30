package com.tvalodia.battletron;

import com.tvalodia.battletron.engine.GameEngine;
import com.tvalodia.battletron.engine.player.DownLeftPlayerAI;
import com.tvalodia.battletron.engine.player.Player;
import com.tvalodia.battletron.engine.player.PlayerAI;
import com.tvalodia.battletron.engine.player.SmartPlayerAI;
import com.tvalodia.battletron.ui.GameView;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class Battletron {

    private GameView drawArea;
    private GameEngine gameEngine;

    public static void main(String[] args) {
        new Battletron().play();
    }

    public Battletron() {
        initialiseWindow();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameEngine = createEngine();
    }

    public void play() {

        gameEngine = createEngine();
        gameEngine.start();

        System.out.println("Complete");
    }

    public void initialiseWindow() {
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

    public GameEngine createEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        PlayerAI player1Controller = new SmartPlayerAI(player1);
        PlayerAI player2Controller = new SmartPlayerAI(player2);
        GameEngine gameEngine = new GameEngine(drawArea, player1, player2, player1Controller, player2Controller);
        return gameEngine;
    }
}
