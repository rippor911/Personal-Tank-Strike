import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Tank {
    private int x;
    private int y;
    private int speed;
    private int hp;
    private int score;
    private int initHp = 3;
    private int initSpeed = 4;
    private Item item;
    private int maxRow;
    private int maxCol;
    private int tileSize;
    private BufferedImage tankImage;
    private BufferedImage lastImage;

    public Tank(int x,int y,int maxRow,int maxCol,int tileSize) {
        this.x = x;
        this.y = y;
        score = 0;
        hp = initHp;
        speed = initSpeed;
        item = null;
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        this.tileSize = tileSize;
        try {
            lastImage = ImageIO.read(getClass().getResource("/images/tankLeft.png"));
        } catch (IOException e) {
            e.printStackTrace();
            lastImage = null;
        }
        
    }

    //image section:

    public BufferedImage getTankImage(int dx,int dy) {
        try {

            if (dx == -speed && dy == 0) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankLeft.png"));
            }
            if (dx == -speed && dy == speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankLeftDown.png"));
            }
            if (dx == 0 && dy == speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankDown.png"));
            }
            if (dx == speed && dy == speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankRightDown.png"));
            }
            if (dx == speed && dy == 0) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankRight.png"));
            }
            if (dx == speed && dy == -speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankRightUp.png"));
            }
            if (dx == 0 && dy == -speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankUp.png"));
            }
            if (dx == -speed && dy == -speed) {
                tankImage = ImageIO.read(getClass().getResource("/images/tankLeftUp.png"));
            }
            if (dx == 0 && dy == 0) {
                tankImage = lastImage;
            } else {
                lastImage = tankImage;
            }
            
            return tankImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //action section:

    public boolean move(int dx,int dy) {
        //TO DO: wall check!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //Frame

        if (x + dx < 0 || x + dx > maxCol - tileSize || y + dy < 0 || y + dy > maxRow - tileSize) {
            return false;
        }

        return true;
    }

    public void shoot() {

    }

    public void update(int dx,int dy) {
        x += dx;
        y += dy;
    }

    //getter:

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }    

}
