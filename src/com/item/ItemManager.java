package com.item;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.GamePanel;

public class ItemManager {
    private ArrayList<Item> items;
    private GamePanel gp;
    private int itemCount;
    private int maxItems = 4;
    private int interval = 1000;
    private long lastTime;
    private final static int deltaHp = 1;
    private final static int deltaSpeed = 6;
    private final static int duration = 8000;

    private String[] types = {"hp", "speed"};
    

    public ItemManager(GamePanel gp) {
        this.gp = gp;
        this.items = new ArrayList<>();
        this.itemCount = 0;
        this.lastTime = 0;
    }

    public boolean isBlocked(int x, int y) {
        int col = x / gp.getTileSize();
        int row = y / gp.getTileSize();
        int tileNum = gp.getMap()[row][col];
        if (tileNum != 0) {
            return true;
        }
        return false;
    }

    public void createItem(long now) {
        int x = (int)(Math.random() * gp.getMaxCol()) * gp.getTileSize();
        int y = (int)(Math.random() * gp.getMaxRow()) * gp.getTileSize();
        
        String type = types[(int)(Math.random() * types.length)];
        int effect = type.equals("hp") ? deltaHp : deltaSpeed;
        int realDuration = duration + (int)(Math.random() % 5000);

        while (isBlocked(x, y)) {
            x = (int)(Math.random() * gp.getMaxCol()) * gp.getTileSize();
            y = (int)(Math.random() * gp.getMaxRow()) * gp.getTileSize();
        }

        Item newItem = new Item(x, y, type, effect, realDuration, now, gp);
        items.add(newItem);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void update(long now) {
        
        items.removeIf(item -> item.getState().equals("expired"));
        itemCount = items.size();        

        // create new item
        if (itemCount < maxItems && now - lastTime >= interval) {
            createItem(now);
            itemCount++;
            lastTime = now;
        }

        // normal update
        for (Item item : items) {
            item.update(now);
        }
    }

    public void draw(Graphics2D g2) {
        for (Item item : items) {
            item.draw(g2);
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}