package com;
import java.io.IOException;

import javax.swing.JFrame;

public class Screen extends JFrame {

    private HomePanel homePanel;
    private GamePanel gamePanel;
    private EndPanel endPanel;

    private int tileSize = 36;
    private int maxCol = 28;
    private int maxRow = 21;
    private int scale = 1;    
    private boolean PVE = false;

    public Screen() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tank Strike");

        this.setSize(2304,1728);

        homePanel = new HomePanel(this);
        add(homePanel);
        setVisible(true);

        gamePanel = null;
        endPanel = null;

        pack();

        setLocationRelativeTo(null);
        
        setVisible(true);
    }

    //JPanel manager:

    public void removeAll() {
        if (homePanel != null) {
            remove(homePanel);
            homePanel = null;
        }
        if (gamePanel != null) {
            gamePanel.stopGame();
            remove(gamePanel);
            gamePanel = null;
        }
        if (endPanel != null) {
            remove(endPanel);
            endPanel = null;
        }
    }

    public void build() {
        removeAll();
        homePanel = new HomePanel(this);
        add(homePanel);
        setVisible(true);       
    }

    void gameStart(String mode) throws IOException {
        removeAll();

        gamePanel = new GamePanel(this);
        add(gamePanel);
        gamePanel.startGameThread(mode);
        if (mode == "PVE") {
            PVE = true;
        } else {
            PVE = false;
        }


        setVisible(true);
    }

    void endGame(String winner, long duration) throws InterruptedException {
        removeAll();

        EndPanel ep = new EndPanel(this, winner, duration, PVE);
        this.add(ep);
        this.setVisible(true);
        this.endPanel = ep;
    }

    //getter:

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxCol() {
        return maxCol;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public int scale() {
        return scale;
    }
}
