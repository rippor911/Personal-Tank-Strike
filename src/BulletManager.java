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

    public void shootAction(int x,int y,int vx,int vy,int standTime) {
        Bullet bt = new Bullet(x, y, vx, vy, gp,timeStamp,standTime);
        bullets.add(bt);
    }

    //update:

    public void update() {
        timeStamp = (timeStamp + 1) % (bigMod);
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (bullet.death(timeStamp)) {
                iterator.remove();
            } else {
                bullet.move();
            }
        }
    }

    public void draw(Graphics2D g2) throws IOException {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bt = iterator.next();
            if (bt != null) {
                bt.draw(g2);
            }            
        }
    }

}
