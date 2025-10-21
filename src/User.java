import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class User implements KeyListener {
    
    private boolean upPressed;
    private boolean leftPressed;
    private boolean downPressed;
    private boolean rightPressed;

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
