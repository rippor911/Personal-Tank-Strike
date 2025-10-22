import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class User implements KeyListener {
    
    private boolean upPressed;
    private boolean leftPressed;
    private boolean downPressed;
    private boolean rightPressed;

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

    }

    public void draw(Graphics2D g2) {
        
        if (g2 == null) {
            System.err.println("g2 is null");
        } else {
            g2.setColor(Color.red);
            if (gp == null) {
                System.err.println("gp is null");
            }
            else if (myTank == null) {
                System.err.println("myTank is null");
            } else {
                g2.fillRect(myTank.getX(), myTank.getY(), gp.getTileSize(), gp.getTileSize());
            }
        }
    }

    //KeyListener:

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        System.out.println("keyPressed!");
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        System.out.println("keyReleased!");
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
