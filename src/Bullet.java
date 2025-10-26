import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet {
    
    private int bulletX;
    private int bulletY;
    private int vx;
    private int vy;
    private int bornTime;
    private int standTime;
    private GamePanel gp;
    private int [][] map;
    
    
    public Bullet(int x,int y,int vx,int vy,GamePanel gp,int bt,int st) {
        bulletX = x;
        bulletY = y;
        this.vx = vx;
        this.vy = vy;
        this.gp = gp;
        standTime = st;
        bornTime = bt;
        this.map = gp.getMap();
    }

    public void move() {
        if (canMove(bulletX + vx,bulletY + vy)) {
            bulletX += vx;
            bulletY += vy;
        }
        else {
            reverse(bulletX,bulletY);
        }
    }

    public boolean death(int now) {
        return Math.abs(now - bornTime) >= standTime;
    }

    public boolean canMove(int nx,int ny) {
        int hw= gp.getTileSize()/8;
        int centerX = nx + hw;
        int centerY = ny + hw;
        if(map[centerY / gp.getTileSize()][centerX / gp.getTileSize()] == 1) {
            return false;
        }
        if (nx < 0 || nx > gp.getWidth() - gp.getTileSize()/4) {
            return false;
        }

        if (ny < 0 || ny > gp.getHeight() - gp.getTileSize()/4) {
            return false;
        }
        
        return true;
    }

    public void reverse(int nx,int ny) {
        if (canMove(nx+vx,ny)&&!canMove(nx,ny+vy)) {
            vy = -vy;
        }
        if (!canMove(nx+vx,ny)&&canMove(nx,ny+vy)) {
            vx = -vx;
        }
        if(!canMove(nx+vx,ny)&&!canMove(nx,ny+vy)) {
            vx = -vx;
            vy = -vy;
        }
        
    }

    //Draw:

    public void draw(Graphics2D g2) throws IOException {

        BufferedImage img = ImageIO.read(getClass().getResource("/images/bullet.png"));

        g2.drawImage(img, bulletX, bulletY,gp.getTileSize() / 4,gp.getTileSize() / 4, null);
    }
}
