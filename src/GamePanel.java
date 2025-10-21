import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int tileSize = 48;
    private final int maxCol = 16;
    private final int maxRow = 12;
    private final int scale = 3;
    private int width;
    private int height;
    private int fps = 60;

    private Thread gameThread;
    private Screen window;

    private User user = null;

    public GamePanel(Screen screen) {
        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;

        this.window = screen;

        user = new User();

        setPreferredSize(new Dimension(width,height));
        setBackground(Color.white);
        setDoubleBuffered(true);
        addKeyListener(user);
        setFocusable(true);
        
        startGameThread();
    }

    //Game Page

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGame() {
        window.build();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            
            // update information
            update();

            //draw the picture with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;    //nm to mm
                if (remainingTime < 0) {
                    remainingTime = 0;
                }                
                Thread.sleep((long) remainingTime);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
    }
}
