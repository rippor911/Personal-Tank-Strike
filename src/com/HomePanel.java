package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import com.util.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.util.ImageLoader;

public class HomePanel extends JPanel {
    private int tileSize = 16;
    private int maxCol = 16;
    private int maxRow = 12;
    private int scale = 3;
    private int width;
    private int height;

    private Image backgroundImage;
    
    // 按钮组件
    private JButton startButtonPvp;
    private JButton startButtonPve;
    private JButton recordButton;
    private JButton backButton;
    
    private Screen window;

    // 记录相关
    private boolean showRec = false;
    private Long record = -1L;

    public HomePanel(Screen window) {
        this.window = window;

        tileSize = window.getTileSize();
        maxCol = window.getMaxCol();
        maxRow = window.getMaxRow();
        scale = window.scale();

        width = tileSize * maxCol * scale;
        height = tileSize * maxRow * scale;

        try {
            backgroundImage = new ImageIcon(ImageLoader.loadImage("home")).getImage();
        } catch (Exception e) {
            System.err.println("背景图加载失败");
            setBackground(Color.BLACK);
        }

        setPreferredSize(new Dimension(width, height));
        setDoubleBuffered(true);
        setLayout(null);

        createStartButton();
        createRecordUI();
    }

    private void initButton(JButton btn) {
        btn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 130, 180));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void createStartButton() {
        int buttonWidth = 150;
        int buttonHeight = 50;
        int x = (width - buttonWidth) / 2;
        int startY = (height - (buttonHeight * 3 + 40)) / 2; 

        // 1. PVP
        startButtonPvp = new JButton("双人对战模式");
        startButtonPvp.setBounds(x, startY, buttonWidth, buttonHeight);
        initButton(startButtonPvp);
        startButtonPvp.addActionListener(e -> startGame("PVP"));
        add(startButtonPvp);

        // 2. PVE
        startButtonPve = new JButton("人机对战模式");
        startButtonPve.setBounds(x, startY + buttonHeight + 20, buttonWidth, buttonHeight);
        initButton(startButtonPve);
        startButtonPve.addActionListener(e -> startGame("PVE"));
        add(startButtonPve);

        // 3. 查看记录
        recordButton = new JButton("最快通关记录");
        recordButton.setBounds(x, startY + (buttonHeight + 20) * 2, buttonWidth, buttonHeight);
        initButton(recordButton);
        
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePanel.this.requestFocusInWindow();

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            record = Logger.getRecord();
                        } catch (Exception ex) {
                            record = -1L;
                        }
                        toggleView(true);
                        HomePanel.this.paintImmediately(0, 0, getWidth(), getHeight());
                    }
                });
            }
        });
        add(recordButton);
    }
    
    private void createRecordUI() {
        backButton = new JButton("返回主菜单");
        int buttonWidth = 150;
        int buttonHeight = 50;
        int x = (width - buttonWidth) / 2;
        int y = height - 100;
        
        backButton.setBounds(x, y, buttonWidth, buttonHeight);
        initButton(backButton);
        backButton.setVisible(false);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleView(false);
            }
        });
    }

    private void startGame(String mode) {
        System.out.println("游戏开始：" + mode);
        this.removeAll(); 
        try {
            window.gameStart(mode);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // --- 视图切换 ---
    private void toggleView(boolean showRecord) {
        this.showRec = showRecord;
        this.removeAll();

        if (showRecord) {
            if (backButton != null) {
                this.add(backButton);
                backButton.setVisible(true);
            }
        } else {
            if (startButtonPvp != null) {
                this.add(startButtonPvp);
                startButtonPvp.setVisible(true);
            }
            if (startButtonPve != null) {
                this.add(startButtonPve);
                startButtonPve.setVisible(true);
            }
            if (recordButton != null) {
                this.add(recordButton);
                recordButton.setVisible(true);
            }
        }

        this.validate(); 
        this.paintImmediately(0, 0, getWidth(), getHeight());
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. 绘制背景
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        } else {
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        // 2. 绘制记录信息
        if (showRec) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            String title = "人机模式最快获胜时间：";
            
            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, (width - titleWidth) / 2, height / 3);

            g.setColor(new Color(255, 215, 0));
            g.setFont(new Font("Arial", Font.BOLD, 60));
            
            String logString;
            if (record == null || record == -1L) {
                logString = "暂无记录";
            } else {
                double sec = record / 1000.0;
                logString = String.format("%.3f s", sec);
            }
            
            int timeWidth = g.getFontMetrics().stringWidth(logString);
            g.drawString(logString, (width - timeWidth) / 2, height / 2);
        }
    }
}