package com.item;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.GamePanel;
import com.Tank;
import com.util.ImageLoader;

public class Item {
    private int itemX;
    private int itemY;
    private int duration;
    private long bornTime;
    private long usedTime;
    private int effect;
    private GamePanel gp;
    private String type;
    private String state;

    private Tank goalTank;

    private BufferedImage imgItem;

    public Item(int x, int y, String type, int effect, int duration, long now, GamePanel gp) {
        this.itemX = x;
        this.itemY = y;
        this.type = type;
        this.effect = effect;
        this.duration = duration;
        this.bornTime = now;
        this.state = "uncollected";
        this.gp = gp;

        if (type.equals("hp")) {
            imgItem = ImageLoader.loadImage("HealthPotion");
        } else if (type.equals("speed")) {
            imgItem = ImageLoader.loadImage("WingBoots");
        }
    }

    public void touch(long now) {
        if (state.equals("uncollected")) {
            ArrayList<Tank> set = gp.getTankSet();
            for (Tank tk : set) {
                if (tk.touchItem(itemX, itemY)) {
                    if (type.equals("hp")) {
                        tk.addHp(effect);
                    } else if (type.equals("speed")) {
                        tk.addSpeed(effect);
                    }
                    goalTank = tk;
                    state = "used";
                    usedTime = now;
                }
            }            
        }
    }

    public void update(long now) {
        if (state.equals("uncollected")) {
            touch(now);
            if (abs(now - bornTime) >= 4 * duration) {
                state = "expired";
            }
        }

        if (state.equals("used")) {
            if (abs(now - usedTime) >= duration) {
                state = "expired";
                if (type.equals("speed")) {
                    goalTank.addSpeed(-effect);
                }
            }
        }
    }

    private long abs(long x) {
        if (x < 0) {
            return -x;
        } else {
            return x;
        }
    }

    //draw:
    public void draw(Graphics2D g2) {
        if (state.equals("uncollected")) {
            g2.drawImage(imgItem,itemX, itemY, gp.getTileSize() / 2, gp.getTileSize() / 2, gp);
        }
    }

    // Getters
    public int getX() {
        return itemX;
    }

    public int getY() {
        return itemY;
    }

    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }    

}