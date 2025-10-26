import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EndPanel extends JPanel {
    private int tileSize = 16;
    private int maxCol = 16;
    private int maxRow = 12;
    private int scale = 3;
    private int width;
    private int height;

    private Screen window;

    public EndPanel(Screen window, String winner) {
        this.window = window;
        initSize();

        this.setBackground(Color.white);

        JLabel label = new JLabel(winner + " Wins !");
        label.setFont(new Font("Arial", Font.BOLD, 36)); // 设置大字体
        label.setHorizontalAlignment(JLabel.CENTER); // 让标签中的文字居中

        // 使用BorderLayout，将标签放在中间
        setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);

        System.out.println(winner);

        setPreferredSize(new Dimension(width, height));
        setDoubleBuffered(true);
    }

    public void initSize() {
        tileSize = window.getTileSize();
        maxCol = window.getMaxCol();
        maxRow = window.getMaxRow();
        scale = window.scale();

        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;        
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
