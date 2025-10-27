package com.rippor911.tankstrike;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class User2 implements KeyListener,TankPanel {
    
    private boolean upPressed;
    private boolean leftPressed;
    private boolean downPressed;
    private boolean rightPressed;
    private boolean shootPressed;

    private int dx;
    private int dy;
    private Tank myTank;
    private GamePanel gp;

    public User2(int x,int y,GamePanel gp) throws IOException {
        myTank = new Tank(x, y,gp);
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
        if (code == KeyEvent.VK_UP) {
            upPressed = true;
        }

        if (code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }

        if (code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }        

        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        
        if (code == KeyEvent.VK_ENTER) {
            shootPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }

        if (code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }

        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }        

        if (code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        
        if (code == KeyEvent.VK_ENTER) {
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
    
    //getter:

    public Tank getTank() {
        return myTank;
    }
}
