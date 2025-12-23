import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HomePanel extends JPanel {
    private int tileSize = 16;
    private int maxCol = 16;
    private int maxRow = 12;
    private int scale = 3;
    private int width;
    private int height;

    private Image backgroundImage;
    private JButton startButtonPvp;
    private JButton startButtonPve;
    private Screen window;

    public HomePanel(Screen window) {

        tileSize = window.getTileSize();
        maxCol = window.getMaxCol();
        maxRow = window.getMaxRow();
        scale = window.scale();

        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;

        backgroundImage = new ImageIcon(getClass().getResource("/images/home.png")).getImage();

        setPreferredSize(new Dimension(width,height));
        setDoubleBuffered(true);

        setLayout(null);

        this.window = window;
        

        createStartButton();
    }

    private void initButton(JButton startButton,String mode) {
        startButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 149, 237));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(70, 130, 180));
            }
        });
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("游戏开始！");
                removeStartButton();
                try {
                    window.gameStart(mode);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        add(startButton);
    }

    private void createStartButton() {
        startButtonPvp = new JButton("双人对战模式");
        startButtonPve = new JButton("人机对战模式");
        
        int buttonWidth = 150;
        int buttonHeight = 50;
        int x = (width - buttonWidth) / 2;
        int y = (height - buttonHeight) / 2;
        
        startButtonPvp.setBounds(x, y, buttonWidth, buttonHeight);
        startButtonPve.setBounds(x, y + buttonHeight * 2, buttonWidth, buttonHeight);
        
        initButton(startButtonPve,"PVE");
        initButton(startButtonPvp,"PVP");
    }
    
    public void removeStartButton() {
        this.remove(startButtonPvp);
        this.remove(startButtonPve);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0,this.getWidth(), this.getHeight(), this);

    }
}
