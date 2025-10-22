import javax.swing.JFrame;

public class Screen extends JFrame {

    private HomePanel homePanel;
    private GamePanel gamePanel;

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

    void gameStart() {
        gamePanel = new GamePanel(this);
        add(gamePanel);
        if (homePanel != null) {
            remove(homePanel);
        }
        setVisible(true);
    }
}
