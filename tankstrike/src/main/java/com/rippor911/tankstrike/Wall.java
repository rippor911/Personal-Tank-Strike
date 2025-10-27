package com.rippor911.tankstrike;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Wall implements Tile {

    private int tx;
    private int ty;
    private BufferedImage image;
    
    public Wall(int x,int y) throws IOException {
        this.tx = x;
        this.ty = y;
        this.image = ImageIO.read(getClass().getResource("/images/wall.png"));
    }

    @Override
    public int getX() {
        return tx;
    }

    @Override
    public int getY() {
        return ty;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
    
}
