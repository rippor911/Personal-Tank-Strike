package com;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.util.ImageLoader;

public class ShowHeart {
    GamePanel gp;
    BufferedImage heartImage;
    public ShowHeart(GamePanel gp) {
        this.gp = gp;
        this.heartImage = ImageLoader.loadImage("heart");
    }

    public void draw(Graphics2D g2) {
        ArrayList<Tank> tanks = gp.getTankSet();
        int hpA = tanks.get(0).getHp();
        int hpB = tanks.get(1).getHp();
        int tileSize = gp.getTileSize();
        int y = (gp.getMaxRow() - 1) * tileSize;
        int maxX = (gp.getMaxCol() - 1) * tileSize;
        for (int i = 0; i < hpA; i++) {
            g2.drawImage(heartImage, 10 + i * tileSize, y, tileSize, tileSize, null);
        }
        for (int i = 0; i < hpB; i++) {
            g2.drawImage(heartImage, maxX - i * tileSize, y, tileSize, tileSize, null);
        }
    }
}
