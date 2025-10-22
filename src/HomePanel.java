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
    private final int tileSize = 16;
    private final int maxCol = 16;
    private final int maxRow = 12;
    private final int scale = 5;
    private int width;
    private int height;

    private Image backgroundImage;
    private JButton startButton;
    private Screen window;

    public HomePanel(Screen window) {
        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;

        backgroundImage = new ImageIcon(getClass().getResource("images/home.png")).getImage();

        setPreferredSize(new Dimension(width,height));
        setDoubleBuffered(true);

        setLayout(null);

        this.window = window;

        createStartButton();
    }

    private void createStartButton() {
        startButton = new JButton("Game Start");
        
        // 设置按钮位置和大小
        int buttonWidth = 150;
        int buttonHeight = 50;
        int x = (width - buttonWidth) / 2;
        int y = (height - buttonHeight) / 2;
        
        startButton.setBounds(x, y, buttonWidth, buttonHeight);
        
        // 美化按钮
        startButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(70, 130, 180)); // 钢蓝色
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        // 添加鼠标悬停效果
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 149, 237)); // 浅蓝色
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(70, 130, 180)); // 恢复原色
            }
        });
        
        // 添加点击事件
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 开始游戏逻辑
                System.out.println("游戏开始！");
                removeStartButton();
                try {
                    window.gameStart();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        add(startButton);
    }
    
    public void removeStartButton() {
        this.remove(startButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0,this.getWidth(), this.getHeight(), this);

    }
}
