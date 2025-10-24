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
    
    public Bullet(int x,int y,int vx,int vy,GamePanel gp,int bt,int st) {
        bulletX = x;
        bulletY = y;
        this.vx = vx;
        this.vy = vy;
        this.gp = gp;
        standTime = st;
        bornTime = bt;
    }

    public void move() {
        if (canMove(bulletX + vx,bulletY + vy)) {
            bulletX += vx;
            bulletY += vy;
        }
        else {
            reverse();
        }
    }

    public boolean death(int now) {
        return Math.abs(now - bornTime) >= standTime;
    }

    public boolean canMove(int nx,int ny) {
        return true;
    }

    public void reverse() {

    }

    //Draw:

    public void draw(Graphics2D g2) throws IOException {

        BufferedImage img = ImageIO.read(getClass().getResource("/images/bullet.png"));

        g2.drawImage(img, bulletX, bulletY,gp.getTileSize() / 4,gp.getTileSize() / 4, null);
    }
}
