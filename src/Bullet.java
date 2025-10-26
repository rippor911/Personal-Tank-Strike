import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Bullet {
    
    private int bulletX;
    private int bulletY;
    private int vx;
    private int vy;
    private int bornTime;
    private int standTime;
    private int shootInterval = 2;
    private int explosionTime = 10;
    private boolean explosionFlag;
    private GamePanel gp;
    private int [][] map;
    private int tagX;
    private int tagY;
    
    public Bullet(int x,int y,int vx,int vy,GamePanel gp,int bt,int st) {
        bulletX = x;
        bulletY = y;
        this.vx = vx;
        this.vy = vy;
        this.gp = gp;
        standTime = st;
        bornTime = bt;
        explosionFlag = false;
        this.map = gp.getMap();
    }

    public void move() {
        if (explosionFlag == false) {
            if (canMove(bulletX + vx,bulletY + vy)) {
                bulletX += vx;
                bulletY += vy;
                tagX = -1000;
                tagY = -1000;
            }
            else {
                reverse(bulletX,bulletY);
                if (bulletX == tagX && bulletY == tagY) {
                    standTime = -1;
                }
                tagX = bulletX;
                tagY = bulletY;
            }            
        }
    }

    public void goal(int now) {
        if (Math.abs(now - bornTime) >= shootInterval) {
            ArrayList<Tank> set = gp.getTankSet();
            for (Tank tk : set) {
                if (explosionFlag == false && tk.touchBullet(bulletX, bulletY)) {
                    tk.beingShot();
                    bornTime = now - standTime + explosionTime; 
                    explosionFlag = true;
                }
            }
        }
    }

    public boolean death(int now) {
        return Math.abs(now - bornTime) >= standTime;
    }

    public boolean canMove(int nx,int ny) {
        int hw = gp.getTileSize() / 8;
        int centerX = nx + hw;
        int centerY = ny + hw;
        if (map[centerY / gp.getTileSize()][centerX / gp.getTileSize()] == 1) {
            return false;
        }
        if (nx < 0 || nx > gp.getWidth() - gp.getTileSize() / 4) {
            return false;
        }

        if (ny < 0 || ny > gp.getHeight() - gp.getTileSize() / 4) {
            return false;
        }
        
        return true;
    }

    public void reverse(int nx,int ny) {
        if (canMove(nx + vx,ny) && !canMove(nx,ny + vy)) {
            vy = -vy;
        }
        if (!canMove(nx + vx,ny) && canMove(nx,ny + vy)) {
            vx = -vx;
        }
        if (!canMove(nx + vx,ny) && !canMove(nx,ny + vy)) {
            vx = -vx;
            vy = -vy;
        }
        
    }

    //Draw:

    public void draw(Graphics2D g2) throws IOException {
        BufferedImage img;
        if (explosionFlag == false) {
            img = ImageIO.read(getClass().getResource("/images/bullet.png"));
            g2.drawImage(img, bulletX, bulletY,gp.getTileSize() / 4,gp.getTileSize() / 4, null);
        }
        else {
            img = ImageIO.read(getClass().getResource("/images/explosion.png"));
            g2.drawImage(img, bulletX, bulletY,gp.getTileSize(),gp.getTileSize(), null);
        }
    }
}
