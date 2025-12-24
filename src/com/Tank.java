package com;

import java.io.IOException;

import com.util.ImageLoader;

import java.awt.image.BufferedImage;

public class Tank {
    private int tankX;
    private int tankY;
    private final static int initSpeed = 4;
    private int speed = initSpeed;
    private int bulletSpeed = 6;
    private int hp = 5;
    private int score;
    private int delta = 6;
    private int bulletSpan = 300;
    private int state;
    private final static int maxHp = 10;

    private BufferedImage [] tankImage;
    private GamePanel gp;
    private int [][] map;
    private int fireCd;

    public Tank(int x,int y,GamePanel gp) throws IOException {
        this.tankX = x;
        this.tankY = y;
        score = 0;
        this.gp = gp;
        this.map = gp.getMap();
        this.fireCd = 0;
        
        imageInit();
        
    }

    //image section:

    public void imageInit() throws IOException {

        state = 0;

        tankImage = new BufferedImage[8];

        tankImage[0] = ImageLoader.loadImage("tankLeft");
        tankImage[1] = ImageLoader.loadImage("tankLeftDown");
        tankImage[2] = ImageLoader.loadImage("tankDown");
        tankImage[3] = ImageLoader.loadImage("tankRightDown");
        tankImage[4] = ImageLoader.loadImage("tankRight");
        tankImage[5] = ImageLoader.loadImage("tankRightUp");
        tankImage[6] = ImageLoader.loadImage("tankUp");
        tankImage[7] = ImageLoader.loadImage("tankLeftUp");
    }

    public BufferedImage getTankImage(int dx,int dy) {
        if (dx < 0 && dy == 0) {
            state = 0;
        }
        if (dx < 0 && dy > 0) {
            state = 1;
        }
        if (dx == 0 && dy > 0) {
            state = 2;
        }
        if (dx < 0 && dy > 0) {
            state = 3;
        }
        if (dx > 0 && dy == 0) {
            state = 4;
        }
        if (dx > 0 && dy < 0)  {
            state = 5;
        }
        if (dx == 0 && dy < 0)  {
            state = 6;
        }
        if (dx < 0 && dy < 0) {
            state = 7;
        }
        return tankImage[state];
    }

    //action section:

    public boolean move(int dx,int dy) {

        //Frame Check:

        int nx = tankX + dx;
        int ny = tankY + dy;

        if (nx < 0 || nx > gp.getWidth() - gp.getTileSize()) {
            return false;
        }

        if (ny < 0 || ny > gp.getHeight() - gp.getTileSize()) {
            return false;
        }

        //Wall Check:

        if (map[(ny + delta) / gp.getTileSize()][(nx + delta) / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[(ny + gp.getTileSize() - delta) / gp.getTileSize()]
            [(nx + delta) / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[(ny + delta) / gp.getTileSize()]
            [(nx + gp.getTileSize() - delta) / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[(ny + gp.getTileSize() - delta) / gp.getTileSize()]
            [(nx + gp.getTileSize() - delta) / gp.getTileSize()] == 1) {
            return false;
        }        

        return true;
    }

    public boolean touchBullt(int x,int y) {
        return (tankX + delta <= x && x <= tankX + gp.getTileSize() - delta 
            && tankY + delta <= y && y <= tankY + gp.getTileSize() - delta);
    }

    public boolean touchItem(int x,int y) {
        return (tankX <= x + delta && x - delta <= tankX + gp.getTileSize() 
            && tankY <= y + delta && y - delta <= tankY + gp.getTileSize());
    }

    public void shoot() {
        if (fireCd == 0) {
            this.fireCd = 20;            

            switch (state) {
                case 0://Left
                    gp.getBulletManager().shootAction(tankX,tankY + gp.getTileSize() / 2,
                        -bulletSpeed,0,bulletSpan);
                    break;

                case 1://LeftDown
                    gp.getBulletManager().shootAction(tankX,tankY + gp.getTileSize(),
                        -bulletSpeed,bulletSpeed,bulletSpan);
                    break;
                    
                case 2:

                    gp.getBulletManager().shootAction(tankX + gp.getTileSize() / 2
                        , tankY + gp.getTileSize(), 0, bulletSpeed,bulletSpan);
                    break; 
                    
                case 3:
                    gp.getBulletManager().shootAction(tankX + gp.getTileSize(), 
                        tankY + gp.getTileSize(), bulletSpeed, bulletSpeed,bulletSpan);
                    break; 
                    
                case 4:
                    gp.getBulletManager().shootAction(tankX + gp.getTileSize(), 
                        tankY + gp.getTileSize() / 2, bulletSpeed, 0,bulletSpan);
                    break; 
                    
                case 5:
                    gp.getBulletManager().shootAction(tankX + gp.getTileSize(), 
                        tankY, bulletSpeed, -bulletSpeed,bulletSpan);
                    break; 
                    
                case 6:
                    gp.getBulletManager().shootAction(tankX + gp.getTileSize() / 2,
                        tankY, 0, -bulletSpeed,bulletSpan);
                    break; 
                    
                case 7:
                    gp.getBulletManager().shootAction(tankX, tankY, -bulletSpeed, 
                        -bulletSpeed,bulletSpan);
                    break;                     
            
                default:
                    break;
            }
        }
    }

    public void beingShot() {
        hp -= 1;
        if (hp <= 0) {
            gp.endGame();
        }
    }

    public void update(int dx,int dy) {
        tankX += dx;
        tankY += dy;
        if (fireCd > 0) {
            fireCd -= 1;
        }
    }

    //getter:

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return tankX;
    }

    public int getY() {
        return tankY;
    }

    public int getHp() {
        return hp;
    }

    public void addHp(int deltaHp) {
        if (hp < maxHp) {
            hp += deltaHp;
        }
    }

	public void addSpeed(int deltaSpeed) {
        if (speed != initSpeed) {
            speed += deltaSpeed;
        }
	}

}
