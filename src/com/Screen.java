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

    public void build() {
        homePanel = new HomePanel(this);
        add(homePanel);
        if (endPanel != null) {
            remove(endPanel);
            endPanel = null;
        }
        setVisible(true);       
    }

    void gameStart(String mode) throws IOException {
        gamePanel = new GamePanel(this);
        add(gamePanel);
        gamePanel.startGameThread(mode);
        if (homePanel != null) {
            remove(homePanel);
            homePanel = null;
        }
        setVisible(true);
    }

    void endGame(String winner) throws InterruptedException {
        if (gamePanel != null) {
            remove(gamePanel);
            gamePanel = null;
        }

        EndPanel ep = new EndPanel(this, winner);
        this.add(ep);
        this.setVisible(true);

        Thread.sleep(3000);

        this.build();
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
