import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private int tileSize = 48;
    private int maxCol = 16;
    private int maxRow = 12;
    private int scale = 3;
    private int width;
    private int height;
    private int fps = 60;
    private int [][] map;

    private Thread gameThread;
    private Screen window;

    private User user = null;
    private TileManager tm = null;

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

        user = new User(100,100,this);   //warning : to do random logic
        this.addKeyListener(user);        

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
        
        startGameThread();
    }

    //Game Page

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGame() {
        try {
            window.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps; //1s = 10^9 ns
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            
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

        user.update();
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tm.draw(g2);

        user.draw(g2);

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
}
