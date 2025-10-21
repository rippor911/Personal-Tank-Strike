import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    private final int tileSize = 16;
    private final int maxCol = 16;
    private final int maxRow = 12;
    private final int scale = 5;
    private int width;
    private int height;

    private Thread gameThread;

    public GamePanel() {
        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;

        setPreferredSize(new Dimension(width,height));
        setBackground(Color.white);
        setDoubleBuffered(true);

    }

    //Game Page

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            System.out.println("The Game loop is running.");
        }
    }

    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
