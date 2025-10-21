import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Screen extends JFrame {
    private int width = 1600;
    private int height = 1200;
    private BackgroundPanel backgroundPanel;
    private Image backgroundImage;
    private JButton startButton;

    public Screen() {
        setTitle("Tank Strike");    //set up title
        setSize(width, height);
        setLocationRelativeTo(null); //show up in the middle
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadBackGround();

        setButton();
        
        //monitor window's size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                //repaint when window change
                backgroundPanel.repaint();
            }
        });
    }

    public void loadBackGround() {

        backgroundImage = new ImageIcon("images/home.png").getImage();
        
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new FlowLayout());
        
        setContentPane(backgroundPanel);
    }

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (backgroundImage != null) {
                // get current size
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                
                g.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, this);
            }
        }
    }    
    
    private void setButton() {
        startButton = new JButton("New Game");

        startButton.setFont(new Font("微软雅黑", Font.BOLD, 36));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        updateButtonPosition();

        backgroundPanel.add(startButton);
        
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
                startGame();
            }
        });
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonPosition();
            }
        });        
    }

    private void updateButtonPosition() {
        //update button to middle
        if (startButton != null) {
            int buttonWidth = startButton.getWidth();
            int buttonHeight = startButton.getHeight();
            int x = (getWidth() - buttonWidth) / 2;
            int y = getHeight() - buttonHeight - 650; // 650 to bottom
            startButton.setLocation(x, y);
        }
    }
    
    private void startGame() {
        JOptionPane.showMessageDialog(this, "游戏开始！");
        remove(backgroundPanel);
    }    
}