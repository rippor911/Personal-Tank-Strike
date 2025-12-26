package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.util.Logger;

public class EndPanel extends JPanel {
    private int tileSize = 16;
    private int maxCol = 16;
    private int maxRow = 12;
    private int scale = 3;
    private int width;
    private int height;

    private JButton backButton;

    private Screen window;

    public EndPanel(Screen window, String winner, long duration, boolean PVE) {
        this.window = window;
        initSize();
        this.setBackground(Color.white);
        setLayout(new BorderLayout());

        // 1. 创建并配置标题标签
        JLabel label = new JLabel(winner + " Wins !");
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label, BorderLayout.CENTER);

        // 2. 创建底部面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); 
        bottomPanel.setBorder(new EmptyBorder(20, 0, 50, 0)); 

        // 3. 创建并配置持续时间标签
        JLabel durationLabel = new JLabel("Duration: " + String.format("%.3f s", duration / 1000.0));
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        durationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. 创建并配置返回按钮
        backButton = new JButton("返回主菜单");
        initButton(backButton);
        backButton.setPreferredSize(new Dimension(200, 50)); 
        backButton.setMaximumSize(new Dimension(200, 50)); 
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            window.build();
        });

        // 5. 将标签和按钮添加到底部面板
        bottomPanel.add(durationLabel);
        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(backButton);

        // 6. 将底部面板添加到主面板
        this.add(bottomPanel, BorderLayout.SOUTH);

        if (PVE && winner.contains("1")) {
            Logger.writeRecord(duration);
        }

        System.out.println(winner);
        setPreferredSize(new Dimension(width, height));
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
