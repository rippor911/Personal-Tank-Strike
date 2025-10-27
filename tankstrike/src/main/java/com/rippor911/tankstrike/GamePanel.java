package com.rippor911.tankstrike;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private int tileSize;
    private int maxCol;
    private int maxRow;
    private int scale;
    private int width;
    private int height;
    private int fps = 60;
    private int [][] map;
    private boolean gameState;

    private Thread gameThread;
    private Screen window;

    private TileManager tm = null;
    private BulletManager bm = null;

    private ArrayList<Tank> tankSet;
    private ArrayList<TankPanel> userSet;

    public GamePanel(Screen screen) throws IOException {

        tileSize = screen.getTileSize();
        maxCol = screen.getMaxCol();
        maxRow = screen.getMaxRow();
        scale = screen.scale();

        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;        

        this.window = screen;
        
        tm = new TileManager(this);
        tm.buildMap();
        map = tm.getMap();
        bm = new BulletManager(this);

        tankSet = new ArrayList<>();
        userSet = new ArrayList<>();

        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setRequestFocusEnabled(true);

        this.addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (isShowing()) {
                        requestFocusInWindow();
                    }
                }
            }
        });

    }

    //Game Page

    public void createTankPvp() throws IOException {

        //TODO You need a better user logic.

        userSet.add(new User(0 * tileSize + 10
                ,0 * tileSize + 15,this));    //warning : to do random logic

        userSet.add(new User2(23 * tileSize - 5
                ,16 * tileSize - 5,this));       //warning : to do random logic        

        for (TankPanel tp : userSet) {
            tankSet.add(tp.getTank());
            if (tp instanceof KeyListener) {
                this.addKeyListener((KeyListener)tp);
            }
        }
        
    }

    public void startGameThread() throws IOException {
        createTankPvp();        
        gameState = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGame() {
        // TODO complete endGame
        
        System.out.println("Game Over.");
        
        gameState = false;

        String winner = null;
        char i = '0';
        
        for (TankPanel user : userSet) {
            i += 1;
            if (user.getTank().getHp() > 0) {
                winner = "Player " + i;
            }
        }        

        try {
            window.endGame(winner);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps; //1s = 10^9 ns
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {

            if (!gameState) {
                break;
            }
            // update information
            update();

            //draw the picture with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;    //ns to ms
                if (remainingTime < 0) {
                    remainingTime = 0;
                }                
                Thread.sleep((long) remainingTime);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            nextDrawTime = System.nanoTime() + drawInterval;
        }
    }

    public void update() {

        tm.update();

        int cnt = 0;

        for (TankPanel user : userSet) {
            user.update();
            if (user.getTank().getHp() > 0) {
                cnt += 1;
            }
        }

        if (cnt <= 1) {
            this.endGame();
        }

        bm.update();
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tm.draw(g2);

        for (TankPanel user : userSet) {
            user.draw(g2);
        }        

        try {
            bm.draw(g2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.dispose();
    }

    //getter:

    public int getTileSize() {
        return tileSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxRow() {
        return maxRow * scale;
    }

    public int getMaxCol() {
        return maxCol * scale;
    }

    public int [][] getMap() {
        return map;
    }

    public int getFps() {
        return fps;
    }

    public BulletManager getBulletManager() {
        return bm;
    }

    public ArrayList<Tank> getTankSet() {
        return tankSet;
    }

    public Screen getWindow() {
        return window;
    }
}
