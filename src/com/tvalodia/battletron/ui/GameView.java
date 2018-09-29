package com.tvalodia.battletron.ui;

import com.tvalodia.battletron.engine.gamestate.GameStateListener;
import com.tvalodia.battletron.engine.gamestate.GameState;
import com.tvalodia.battletron.engine.GameStatus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class GameView extends JComponent implements GameStateListener {

    private static float FPS = 60;
    private static float TIME_PER_FRAME = 1000f / FPS;
    private long currentSystemTime = 0;
    private long lastDrawTime;
    private static final int BLOCK_SIZE = 8;
    // Image in which we're going to draw
    private Image image;
    // Graphics2D object ==> used to draw on
    private Graphics2D g2;

    public GameView() {
        setDoubleBuffered(false);
        lastDrawTime = System.currentTimeMillis();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            // image to draw null ==> we create
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    // now we create exposed methods
    public void clear() {
        g2.setPaint(Color.black);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        repaint();
    }

    @Override
    public void onGameStateUpdate(int tick, GameState gameState) {
        currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - lastDrawTime > TIME_PER_FRAME) {
            clear();
            g2.setPaint(Color.white);
            g2.drawString("Tick: " + tick + "; FPS: " + (TIME_PER_FRAME / (currentSystemTime - lastDrawTime)) * FPS, 0, 20);


            for (int x = 0; x < gameState.getWidth() - 1; x++) {
                for (int y = 0; y < gameState.getHeight() - 1; y++) {
                    if (gameState.getPlayingField()[x][y] == gameState.getPlayer1().getId()) {
                        g2.setPaint(Color.blue);
                        g2.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                    if (gameState.getPlayingField()[x][y] == gameState.getPlayer2().getId()) {
                        g2.setPaint(Color.magenta);
                        g2.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    }
                }
            }

            g2.setPaint(Color.cyan);
            g2.fillRect(gameState.getPlayer1().getPositionX() * BLOCK_SIZE,
                    gameState.getPlayer1().getPositionY() * BLOCK_SIZE,
                    BLOCK_SIZE, BLOCK_SIZE);

            g2.setPaint(Color.pink);
            g2.fillRect(gameState.getPlayer2().getPositionX() * BLOCK_SIZE,
                    gameState.getPlayer2().getPositionY() * BLOCK_SIZE,
                    BLOCK_SIZE, BLOCK_SIZE);

            if (gameState.getGameStatus() == GameStatus.WINNER) {
                g2.setPaint(Color.white);
                g2.drawString("Player " + gameState.getWinner().getId() + " wins!", 100, 200);
            }

            if (gameState.getGameStatus() == GameStatus.DRAW) {
                g2.setPaint(Color.white);
                g2.drawString("It's a DRAW!", 100, 200);
            }

            repaint();
            lastDrawTime = currentSystemTime;
        }
    }
}