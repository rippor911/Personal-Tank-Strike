package com.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.GamePanel;
import com.Tank;
import com.item.Bullet;
import com.item.Item; 

public class UserAI implements TankPanel {

    private Tank me;
    private Tank target;
    private GamePanel gp;

    private int dx = 0;
    private int dy = 0;
    
    // Pathfinding
    private List<Node> path = new ArrayList<>();
    private long lastTime;
    private static final int RECALC_DELAY = 500; 

    // Anti-stuck
    private int lastX, lastY;
    private int stuckCnt = 0;
    private boolean recovering = false;
    private int recStep = 0;
    
    // Dodge settings
    private Random rand = new Random();
    private int safeDist = 180; 
    private int dodgeCool = 0;
    
    // AI Bias
    private static final int ITEM_BIAS = 100; 

    public UserAI(int x, int y, GamePanel gp) throws IOException {
        this.gp = gp;
        this.me = new Tank(x, y, gp);
        this.lastTime = System.currentTimeMillis();
        this.lastX = x;
        this.lastY = y;
    }

    @Override
    public void update() {
        target = findTarget();
        
        if (target == null) {
            stop();
            return;
        }

        // 1. escape bullets
        if (checkDodge()) {
            recPos();
            return;
        }

        // 2. prevent stuck
        if (checkStuck()) {
            solStuck();
            return;
        }

        // 3. attack
        tryAttack();

        // 4. decide destination
        int[] dest = calcDest();

        // 5. move along path
        followPath(dest[0], dest[1]);
        
        recPos();
    }

    private int[] calcDest() {
        int tx = target.getX();
        int ty = target.getY();
        
        double minCost = getDist(me.getX(), me.getY(), tx, ty);

        List<Item> items = gp.getItems(); 
        
        if (items != null && !items.isEmpty()) {
            for (Item it : items) {
                if (!it.isLive()) {
                    continue; 
                }

                double d = getDist(me.getX(), me.getY(), it.getX(), it.getY());
                double cost = d - ITEM_BIAS;

                if (cost < minCost) {
                    minCost = cost;
                    tx = it.getX();
                    ty = it.getY();
                }
            }
        }
        
        return new int[]{tx, ty};
    }

    private void stop() {
        dx = 0;
        dy = 0;
    }

    private void recPos() { 
        lastX = me.getX(); 
        lastY = me.getY(); 
    }

    // --- Dodge Logic ---

    private boolean checkDodge() {
        if (dodgeCool > 0) {
            dodgeCool--;
            if (me.move(dx, dy)) {
                me.update(dx, dy);
            }
            return true;
        }

        ArrayList<Bullet> bullets = gp.getBullets();
        if (bullets == null || bullets.isEmpty()) {
            return false;
        }

        int mx = me.getX();
        int my = me.getY();
        int ts = gp.getTileSize();

        for (Bullet b : bullets) {
            double dist = Math.sqrt(Math.pow(mx - b.getX(), 2) + Math.pow(my - b.getY(), 2));
            
            if (dist > safeDist) {
                continue;
            }

            boolean danger = false;
            int bdx = b.getDx();
            int bdy = b.getDy();

            boolean xThreat = (bdx != 0 && Math.abs(b.getY() - my) < ts);
            boolean yThreat = (bdy != 0 && Math.abs(b.getX() - mx) < ts);

            if (xThreat) {
                if ((bdx > 0 && b.getX() < mx) || (bdx < 0 && b.getX() > mx)) {
                    danger = true;
                }
            } else if (yThreat) {
                if ((bdy > 0 && b.getY() < my) || (bdy < 0 && b.getY() > my)) {
                    danger = true;
                }
            }

            if (danger) {
                return excDodge(bdx, bdy);
            }
        }
        return false;
    }

    private boolean excDodge(int bdx, int bdy) {
        int spd = me.getSpeed();
        int col = me.getX() / gp.getTileSize();
        int row = me.getY() / gp.getTileSize();
        
        dx = 0; 
        dy = 0;

        if (bdx != 0) { 
            boolean canUp = !isWall(col, row - 1);
            boolean canDown = !isWall(col, row + 1);
            
            if (canUp && canDown) {
                dy = rand.nextBoolean() ? -spd : spd;
            } else if (canUp) {
                dy = -spd;
            } else if (canDown) {
                dy = spd;
            } else {
                return false;
            }
        } else {
            boolean canLeft = !isWall(col - 1, row);
            boolean canRight = !isWall(col + 1, row);

            if (canLeft && canRight) {
                dx = rand.nextBoolean() ? -spd : spd;
            } else if (canLeft) {
                dx = -spd;
            } else if (canRight) {
                dx = spd;
            } else {
                return false;
            }
        }

        if (me.move(dx, dy)) { 
            me.update(dx, dy);
            dodgeCool = 10;
            return true;
        }
        return false;
    }

    // --- Anti-Stuck Logic ---

    private boolean checkStuck() {
        if ((dx != 0 || dy != 0) && me.getX() == lastX && me.getY() == lastY) {
            stuckCnt++;
        } else {
            stuckCnt = 0;
        }
        
        if (stuckCnt > 15) {
            recovering = true;
            recStep = 20;
            stuckCnt = 0;
            path.clear();
            return true;
        }
        return recovering;
    }

    private void solStuck() {
        if (recStep > 0) {
            recStep--;
            if (recStep % 5 == 0) { 
                int dir = rand.nextInt(4);
                int spd = me.getSpeed();
                
                dx = 0;
                dy = 0;
                
                if (dir == 0) { dx = spd; }
                else if (dir == 1) { dx = -spd; }
                else if (dir == 2) { dy = spd; }
                else if (dir == 3) { dy = -spd; }
            }
            
            if (me.move(dx, dy)) {
                me.update(dx, dy);
            }
        } else {
            recovering = false;
        }
    }

    // --- Movement & Pathfinding ---

    private void followPath(int tx, int ty) {
        long now = System.currentTimeMillis();
        int ts = gp.getTileSize();
        
        int startCol = (me.getX() + ts / 2) / ts;
        int startRow = (me.getY() + ts / 2) / ts;
        int tCol = (tx + ts / 2) / ts;
        int tRow = (ty + ts / 2) / ts;

        boolean needRecalc = (now - lastTime > RECALC_DELAY) || path.isEmpty();
        
        if (!path.isEmpty()) {
            Node end = path.get(path.size() - 1);
            if (end.col != tCol || end.row != tRow) {
                needRecalc = true;
            }
        }

        if (needRecalc) {
            path = findPath(startCol, startRow, tCol, tRow);
            lastTime = now;
        }

        if (!path.isEmpty()) {
            Node next = path.get(0);
            
            double d = getDist(me.getX(), me.getY(), next.col * ts, next.row * ts);
            if (d < me.getSpeed()) {
                path.remove(0);
                if (path.isEmpty()) {
                    return;
                }
                next = path.get(0);
            }
            
            moveTo(next.col, next.row);
        }
    }

    private void moveTo(int tCol, int tRow) {
        int ts = gp.getTileSize();
        int spd = me.getSpeed();
        int tx = tCol * ts;
        int ty = tRow * ts;
        int mx = me.getX();
        int my = me.getY();

        dx = 0; 
        dy = 0;

        // grid alignment
        if (tx != mx) {
            if (Math.abs(my - ty) > spd) {
                dy = (my < ty) ? spd : -spd; 
            } else {
                if (my != ty) {
                    dy = (my < ty) ? 1 : -1;
                }
                dx = (mx < tx) ? spd : -spd;
            }
        } else if (ty != my) {
            if (Math.abs(mx - tx) > spd) {
                dx = (mx < tx) ? spd : -spd; 
            } else {
                if (mx != tx) {
                    dx = (mx < tx) ? 1 : -1;
                }
                dy = (my < ty) ? spd : -spd;
            }
        }
        
        if (me.move(dx, dy)) {
            me.update(dx, dy);
        }
    }

    // --- A* Algorithm ---
    
    public class Node {
        int col, row;
        int g, h, f;
        Node parent;

        public Node(int col, int row) { 
            this.col = col; 
            this.row = row; 
        }
    }

    private List<Node> findPath(int startCol, int startRow, int endCol, int endRow) {
        if (!isValid(endCol, endRow) || isWall(endCol, endRow)) {
            return new ArrayList<>();
        }
        
        List<Node> open = new ArrayList<>();
        List<Node> closed = new ArrayList<>();
        
        Node start = new Node(startCol, startRow);
        open.add(start);

        while (!open.isEmpty()) {
            Node cur = open.get(0);
            for (Node n : open) {
                if (n.f < cur.f) {
                    cur = n;
                }
            }
            
            open.remove(cur);
            closed.add(cur);

            if (cur.col == endCol && cur.row == endRow) {
                return buildPath(cur);
            }

            int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
            
            for (int[] d : dirs) {
                int nc = cur.col + d[0];
                int nr = cur.row + d[1];

                if (!isValid(nc, nr) || isWall(nc, nr)) {
                    continue;
                }
                if (isInList(closed, nc, nr)) {
                    continue;
                }

                int newG = cur.g + 1;
                Node nb = getFromList(open, nc, nr);
                
                if (nb == null) {
                    nb = new Node(nc, nr);
                    nb.g = newG;
                    nb.h = Math.abs(nc - endCol) + Math.abs(nr - endRow);
                    nb.f = nb.g + nb.h;
                    nb.parent = cur;
                    open.add(nb);
                } else if (newG < nb.g) {
                    nb.g = newG;
                    nb.f = nb.g + nb.h;
                    nb.parent = cur;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Node> buildPath(Node end) {
        List<Node> ret = new ArrayList<>();
        Node cur = end;
        while (cur.parent != null) { 
            ret.add(0, cur); 
            cur = cur.parent; 
        }
        return ret;
    }
    
    private boolean isInList(List<Node> list, int c, int r) { 
        return getFromList(list, c, r) != null; 
    }
    
    private Node getFromList(List<Node> list, int c, int r) {
        for (Node n : list) {
            if (n.col == c && n.row == r) {
                return n;
            }
        }
        return null;
    }

    // --- Attack & Utils ---

    private boolean tryAttack() {
        int ts = gp.getTileSize();
        int mx = me.getX() + ts / 2;
        int my = me.getY() + ts / 2;
        int tx = target.getX() + ts / 2;
        int ty = target.getY() + ts / 2;
        int margin = ts / 2;

        boolean alignX = Math.abs(mx - tx) < margin;
        boolean alignY = Math.abs(my - ty) < margin;

        if ((alignX || alignY) && hasLos(mx, my, tx, ty)) {

            if (alignX) {
                me.getTankImage(0, (ty > my) ? 1 : -1);
            } else {
                me.getTankImage((tx > mx) ? 1 : -1, 0);
            }
            
            me.shoot();
            return true;
        }
        return false;
    }

    private boolean hasLos(int x1, int y1, int x2, int y2) {
        int ts = gp.getTileSize();
        int steps = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) / (ts / 2);
        
        if (steps == 0) {
            return true;
        }
        
        float xi = (float)(x2 - x1) / steps;
        float yi = (float)(y2 - y1) / steps;
        float cx = x1;
        float cy = y1;
        
        for (int i = 0; i < steps; i++) {
            cx += xi; 
            cy += yi;
            if (isWall((int)cx / ts, (int)cy / ts)) {
                return false;
            }
        }
        return true;
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = me.getTankImage(dx, dy);
        if (img != null) {
            g2.drawImage(img, me.getX(), me.getY(), gp.getTileSize(), gp.getTileSize(), null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(me.getX(), me.getY(), gp.getTileSize(), gp.getTileSize());
        }
    }

    @Override
    public Tank getTank() { 
        return me; 
    }
    
    public Tank getTargetTank() { 
        return target; 
    }
    
    public GamePanel getGp() { 
        return gp; 
    }

    private Tank findTarget() {
        if (gp.getTankSet() == null) {
            return null;
        }
        for (Tank t : gp.getTankSet()) {
            if (t != me && t.getHp() > 0) {
                return t;
            }
        }
        return null;
    }    

    private boolean isValid(int c, int r) {
        int mc = gp.getMaxCol() / gp.getWindow().scale();
        int mr = gp.getMaxRow() / gp.getWindow().scale();
        return (c >= 0 && c < mc && r >= 0 && r < mr);
    }
    
    private boolean isWall(int c, int r) {
        try { 
            return gp.getMap()[r][c] == 1; 
        } catch (Exception e) { 
            return true; 
        }
    }
    
    private double getDist(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}