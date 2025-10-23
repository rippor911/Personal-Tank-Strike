import java.io.IOException;

import javax.swing.JFrame;

public class Screen extends JFrame {

    private HomePanel homePanel;
    private GamePanel gamePanel;

    private int tileSize = 48;
    private int maxCol = 12;
    private int maxRow = 9;
    private int scale = 2;    

    public Screen() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tank Strike");

        this.setSize(2304,1728);

        homePanel = new HomePanel(this);
        add(homePanel);
        setVisible(true);

        gamePanel = null;

        pack();

        setLocationRelativeTo(null);
        
        setVisible(true);
    }

    void build() {
        homePanel = new HomePanel(this);
        add(homePanel);
        if (gamePanel != null) {
            remove(gamePanel);
        }
        setVisible(true);       
    }

    void gameStart() throws IOException {
        gamePanel = new GamePanel(this);
        add(gamePanel);
        if (homePanel != null) {
            remove(homePanel);
        }
        setVisible(true);
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
