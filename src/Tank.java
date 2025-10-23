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
    private BufferedImage tankImage;
    private BufferedImage lastImage;
    private GamePanel gp;
    private int [][] map;

    public Tank(int x,int y,GamePanel gp) {
        this.x = x;
        this.y = y;
        score = 0;
        hp = initHp;
        speed = initSpeed;
        item = null;
        this.gp = gp;
        this.map = gp.getMap();
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

        //Frame Check:

        int nx = x + dx;
        int ny = y + dy;

        if (nx < 0 || nx > gp.getWidth() - gp.getTileSize()) {
            return false;
        }

        if (ny < 0 || ny > gp.getHeight() - gp.getTileSize()) {
            return false;
        }

        //Wall Check:

        if (map[ny / gp.getTileSize()][nx / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[(ny + gp.getTileSize()) / gp.getTileSize()][nx / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[ny / gp.getTileSize()][(nx + gp.getTileSize()) / gp.getTileSize()] == 1) {
            return false;
        }

        if (map[(ny + gp.getTileSize()) / gp.getTileSize()]
            [(nx + gp.getTileSize()) / gp.getTileSize()] == 1) {
            return false;
        }        

        return true;
    }

    public void shoot() {
        System.out.println("Fire!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
