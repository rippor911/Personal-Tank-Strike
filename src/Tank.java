import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Tank {
    private int tankX;
    private int tankY;
    private int speed = 4;
    private int bulletSpeed = 6;
    private int hp = 3;
    private int score;
    private int delta = 8;
    private int bulletSpan = 300;
    private int state;

    private Item item;
    private BufferedImage [] tankImage;
    private GamePanel gp;
    private int [][] map;
    private int fireCd;

    public Tank(int x,int y,GamePanel gp) throws IOException {
        this.tankX = x;
        this.tankY = y;
        score = 0;
        item = null;
        this.gp = gp;
        this.map = gp.getMap();
        this.fireCd = 0;
        
        imageInit();
        
    }

    //image section:

    public void imageInit() throws IOException {

        state = 0;

        tankImage = new BufferedImage[8];

        tankImage[0] = ImageIO.read(getClass().getResource("/images/tankLeft.png"));
        tankImage[1] = ImageIO.read(getClass().getResource("/images/tankLeftDown.png"));
        tankImage[2] = ImageIO.read(getClass().getResource("/images/tankDown.png"));
        tankImage[3] = ImageIO.read(getClass().getResource("/images/tankRightDown.png"));
        tankImage[4] = ImageIO.read(getClass().getResource("/images/tankRight.png"));
        tankImage[5] = ImageIO.read(getClass().getResource("/images/tankRightUp.png"));
        tankImage[6] = ImageIO.read(getClass().getResource("/images/tankUp.png"));
        tankImage[7] = ImageIO.read(getClass().getResource("/images/tankLeftUp.png"));
    }

    public BufferedImage getTankImage(int dx,int dy) {
        if (dx == -speed && dy == 0) {
            state = 0;
        }
        if (dx == -speed && dy == speed) {
            state = 1;
        }
        if (dx == 0 && dy == speed) {
            state = 2;
        }
        if (dx == speed && dy == speed) {
            state = 3;
        }
        if (dx == speed && dy == 0) {
            state = 4;
        }
        if (dx == speed && dy == -speed) {
            state = 5;
        }
        if (dx == 0 && dy == -speed) {
            state = 6;
        }
        if (dx == -speed && dy == -speed) {
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

    public void shoot() {
        if (fireCd == 0) {
            System.out.println("Fire!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.fireCd = 30;            

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

}
