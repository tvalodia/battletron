package com.alltimeslucky.battletron.ui;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;
import com.alltimeslucky.battletron.game.model.GameStatus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class GameView extends JComponent implements GameListener {

    private static float FPS_LIMIT = 60;
    private static float TIME_PER_FRAME = 1000f / FPS_LIMIT;
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

    /**
     * Clears the simulation view by drawing a filled rectangle in black over the drawing space.
     */
    public void clear() {
        g2.setPaint(Color.black);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
    }

    @Override
    public void onGameStateUpdate(Game game) {
        currentSystemTime = System.currentTimeMillis();
        if (currentSystemTime - lastDrawTime > TIME_PER_FRAME) {
            clear();

            g2.setPaint(Color.white);
            g2.drawString("Tick: " + game.getTickCount()
                    + "; FPS: " + (TIME_PER_FRAME / (currentSystemTime - lastDrawTime)) * FPS_LIMIT, 0, 20);

            //Draw the players' trails
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (game.getPlayingField()[x][y] == game.getPlayerOne().getId()) {
                        drawBlock(getScreenX(x), getScreenY(game.getHeight(), y), Color.blue);
                    }
                    if (game.getPlayingField()[x][y] == game.getPlayerTwo().getId()) {
                        drawBlock(getScreenX(x), getScreenY(game.getHeight(), y), Color.magenta);
                    }
                }
            }

            //player 1's head
            drawBlock(getScreenX(game.getPlayerOne().getPositionX()),
                      getScreenY(game.getHeight(), game.getPlayerOne().getPositionY()),
                      Color.cyan);
            //player 2's head
            drawBlock(getScreenX(game.getPlayerTwo().getPositionX()),
                      getScreenY(game.getHeight(), game.getPlayerTwo().getPositionY()),
                      Color.pink);

            if (game.getGameStatus() == GameStatus.COMPLETED_WINNER) {
                g2.setPaint(Color.white);
                g2.drawString("Player " + game.getWinner().getId() + " wins!", 100, 200);
            }

            if (game.getGameStatus() == GameStatus.COMPLETED_DRAW) {
                g2.setPaint(Color.white);
                g2.drawString("It's a DRAW!", 100, 200);
            }

            repaint();
            lastDrawTime = currentSystemTime;
        }
    }

    private int getScreenY(int playingFieldHeight, int gameYCoordinate) {
        return ((playingFieldHeight - 1) * BLOCK_SIZE) - (gameYCoordinate * BLOCK_SIZE);
    }

    private int getScreenX(int gameX) {
        return gameX * BLOCK_SIZE;
    }

    private void drawBlock(int x, int y, Color color) {
        g2.setPaint(color);
        g2.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
    }
}