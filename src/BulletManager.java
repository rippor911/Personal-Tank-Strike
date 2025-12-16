import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class BulletManager {

    private GamePanel gp;
    private ArrayList<Bullet> bullets;
    private int timeStamp;
    private final int bigMod = 1000000007;

    public BulletManager(GamePanel gp) {
        this.gp = gp;
        bullets = new ArrayList<>();
        timeStamp = 0;
    }

    public void shootAction(int x, int y, int vx, int vy, int standTime) {
        Bullet bt = new Bullet(x, y, vx, vy, gp, timeStamp, standTime);
        synchronized (bullets) {
            bullets.add(bt);
        }
    }

    public void update() {
        timeStamp = (timeStamp + 1) % bigMod;
        synchronized (bullets) { 
            Iterator<Bullet> iterator = bullets.iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                if (bullet.death(timeStamp)) {
                    iterator.remove(); 
                } else {
                    bullet.move();
                    bullet.goal(timeStamp);
                }
            }
        }
    }

    public void draw(Graphics2D g2) throws IOException {
        synchronized (bullets) {  
            Iterator<Bullet> iterator = bullets.iterator();
            while (iterator.hasNext()) {
                Bullet bt = iterator.next();
                if (bt != null) {
                    bt.draw(g2);
                }            
            }
        }
    }

    //getter:
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

}