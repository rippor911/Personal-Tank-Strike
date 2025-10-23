import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class User implements KeyListener {
    
    private boolean upPressed;
    private boolean leftPressed;
    private boolean downPressed;
    private boolean rightPressed;
    private boolean shootPressed;

    private int dx;
    private int dy;
    private Tank myTank;
    private GamePanel gp;

    public User(int x,int y,GamePanel gp) {
        myTank = new Tank(x, y,gp.getHeight(),gp.getWidth(),gp.getTileSize());
        dx = 0;
        dy = 0;
        this.gp = gp;
    }

    //control section:

    public void update() {

        dx = dy = 0;

        if (upPressed) {
            dy -= myTank.getSpeed();
        }
        if (downPressed) {
            dy += myTank.getSpeed();
        }
        if (leftPressed) {
            dx -= myTank.getSpeed();
        }
        if (rightPressed) {
            dx += myTank.getSpeed();
        }

        if (myTank.move(dx,dy)) {
            myTank.update(dx,dy);
        }

        if (shootPressed) {
            myTank.shoot();
        }
        
        System.out.println("(" + myTank.getX() + "," + myTank.getY() + ")");

    }

    public void draw(Graphics2D g2) {

        BufferedImage tankImage = myTank.getTankImage(dx,dy);

        if (tankImage != null) {
            g2.drawImage(tankImage, myTank.getX(), myTank.getY()
                ,gp.getTileSize(),gp.getTileSize(), null);

        } else {
            g2.setColor(Color.red);
            g2.fillRect(myTank.getX(), myTank.getY(), gp.getTileSize(), gp.getTileSize());
        }
    }

    //KeyListener:

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }        

        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        
        if (code == KeyEvent.VK_SPACE) {
            shootPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }        

        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        
        if (code == KeyEvent.VK_SPACE) {
            shootPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    boolean left() {
        return leftPressed;
    }

    boolean right() {
        return rightPressed;
    }
    
    boolean up() {
        return upPressed;
    }
    
    boolean down() {
        return downPressed;
    }    
    
}
