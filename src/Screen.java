import javax.swing.JFrame;

public class Screen extends JFrame {

    private HomePanel homePanel;
    private GamePanel gamePanel;

    public Screen() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tank Strike");

        /*gamePanel = new GamePanel();
        add(gamePanel);*/

        homePanel = new HomePanel(this);
        add(homePanel);

        pack();

        setLocationRelativeTo(null);
        
        setVisible(true);
    }

    void build() {
        
    }

    void gameStart() {

    }
}
