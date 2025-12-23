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

public class UserAI implements TankPanel {

    private Tank me;
    private Tank target;
    private GamePanel gp;

    private int dx = 0;
    private int dy = 0;
    
    //pathfinding
    private List<Node> path = new ArrayList<>();
    private long lastCalcTime;
    private static final int RECALC_DELAY = 500; 

    //anti-stuck
    private int lastX, lastY;
    private int stuckCnt = 0;
    private boolean recovering = false;
    private int recStep = 0;
    
    //dodge settings
    private Random rand = new Random();
    private int safeDist = 180; 
    private int dodgeCool = 0;

    public UserAI(int x, int y, GamePanel gp) throws IOException {
        this.gp = gp;
        this.me = new Tank(x, y, gp);
        this.lastCalcTime = System.currentTimeMillis();
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

        // 1. Dodge bullets
        if (checkDodge()) {
            updatePosRecord();
            return;
        }

        // 2. Unstuck
        if (checkStuck()) {
            doUnstuck();
            return;
        }

        // 3. Attack
        if (tryAttack()) {
            stop();
            return;
        }

        // 4. Move along path
        followPath();
        updatePosRecord();
    }

    private void stop() { dx = 0; dy = 0; }
    private void updatePosRecord() { lastX = me.getX(); lastY = me.getY(); }

    //Dodge Logic

    private boolean checkDodge() {
        if (dodgeCool > 0) {
            dodgeCool--;
            if (me.move(dx, dy)) me.update(dx, dy);
            return true;
        }

        ArrayList<Bullet> bullets = gp.getBullets();
        if (bullets == null || bullets.isEmpty()) return false;

        int mx = me.getX();
        int my = me.getY();
        int ts = gp.getTileSize();

        for (Bullet b : bullets) {
            // distance check
            double dist = Math.sqrt(Math.pow(mx - b.getX(), 2) + Math.pow(my - b.getY(), 2));
            if (dist > safeDist) continue;

            // threat check
            boolean danger = false;
            int bdx = b.getDx();
            int bdy = b.getDy();

            if (bdx != 0 && Math.abs(b.getY() - my) < ts) {
                if ((bdx > 0 && b.getX() < mx) || (bdx < 0 && b.getX() > mx)) danger = true;
            }
            else if (bdy != 0 && Math.abs(b.getX() - mx) < ts) {
                if ((bdy > 0 && b.getY() < my) || (bdy < 0 && b.getY() > my)) danger = true;
            }

            if (danger) return executeDodge(bdx, bdy);
        }
        return false;
    }

    private boolean executeDodge(int bdx, int bdy) {
        int spd = me.getSpeed();
        int col = me.getX() / gp.getTileSize();
        int row = me.getY() / gp.getTileSize();
        
        dx = 0; dy = 0;

        if (bdx != 0) { 
            // bullet horizontal
            boolean canUp = !isWall(col, row - 1);
            boolean canDown = !isWall(col, row + 1);
            
            if (canUp && canDown) dy = rand.nextBoolean() ? -spd : spd;
            else if (canUp) dy = -spd;
            else if (canDown) dy = spd;
            else return false;
        } else {
            //bullet vertical
            boolean canLeft = !isWall(col - 1, row);
            boolean canRight = !isWall(col + 1, row);

            if (canLeft && canRight) dx = rand.nextBoolean() ? -spd : spd;
            else if (canLeft) dx = -spd;
            else if (canRight) dx = spd;
            else return false;
        }

        if (me.move(dx, dy)) { 
            //lock direction for short time
            me.update(dx, dy);
            dodgeCool = 10;
            return true;
        }
        return false;
    }

    //Anti-Stuck Logic

    private boolean checkStuck() {
        // trying to move but pos not changing
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

    private void doUnstuck() {
        if (recStep > 0) {
            recStep--;
            // random move every 5 frames
            if (recStep % 5 == 0) { 
                int dir = rand.nextInt(4);
                int s = me.getSpeed();
                dx = (dir==0?s : dir==1?-s : 0);
                dy = (dir==2?s : dir==3?-s : 0);
            }
            if (me.move(dx, dy)) me.update(dx, dy);
        } else {
            recovering = false;
        }
    }

    //Movement & Pathfinding

    private void followPath() {
        long now = System.currentTimeMillis();
        int ts = gp.getTileSize();
        
        // calc grid pos
        int startCol = (me.getX() + ts/2) / ts;
        int startRow = (me.getY() + ts/2) / ts;
        int targetCol = (target.getX() + ts/2) / ts;
        int targetRow = (target.getY() + ts/2) / ts;

        // recalc path periodically
        if (now - lastCalcTime > RECALC_DELAY || path.isEmpty()) {
            path = findPath(startCol, startRow, targetCol, targetRow);
            lastCalcTime = now;
        }

        if (!path.isEmpty()) {
            Node next = path.get(0);
            // if reached next node (close enough)
            if (getDistance(me.getX(), me.getY(), next.col * ts, next.row * ts) < me.getSpeed()) {
                path.remove(0);
                if (path.isEmpty()) return;
                next = path.get(0);
            }
            moveTo(next.col, next.row);
        }
    }

    private void moveTo(int tCol, int tRow) {
        int ts = gp.getTileSize();
        int s = me.getSpeed();
        int tx = tCol * ts;
        int ty = tRow * ts;
        int cx = me.getX();
        int cy = me.getY();

        dx = 0; dy = 0;

        //to avoid corner stuck
        if (tx != cx) {
            if (Math.abs(cy - ty) > s) {
                dy = (cy < ty) ? s : -s; 
            } else {
                if (cy != ty) dy = (cy < ty) ? 1 : -1;
                dx = (cx < tx) ? s : -s;
            }
        } else if (ty != cy) {
            if (Math.abs(cx - tx) > s) {
                dx = (cx < tx) ? s : -s; 
            } else {
                if (cx != tx) dx = (cx < tx) ? 1 : -1;
                dy = (cy < ty) ? s : -s;
            }
        }

        if (me.move(dx, dy)) me.update(dx, dy);
    }

    // A*

    // Standard A* Node
    public class Node {
        int col, row;
        int gCost;      // dist from start
        int hCost;      // heuristic (dist to end)
        int fCost;      // total (g + h)
        Node parent;

        public Node(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

    private List<Node> findPath(int startCol, int startRow, int endCol, int endRow) {
        if (!isValid(endCol, endRow) || isWall(endCol, endRow)) return new ArrayList<>();

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();
        
        Node startNode = new Node(startCol, startRow);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            // 1. Find node with lowest F cost
            Node current = openList.get(0);
            for (Node n : openList) {
                if (n.fCost < current.fCost) current = n;
            }

            openList.remove(current);
            closedList.add(current);

            // 2. Reached target?
            if (current.col == endCol && current.row == endRow) {
                return buildPath(current);
            }

            // 3. Check neighbors
            int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
            
            for (int[] d : dirs) {
                int nCol = current.col + d[0];
                int nRow = current.row + d[1];

                if (!isValid(nCol, nRow) || isWall(nCol, nRow)) continue;
                if (isInList(closedList, nCol, nRow)) continue;

                int newGCost = current.gCost + 1;
                Node neighbor = getFromList(openList, nCol, nRow);

                if (neighbor == null) {
                    neighbor = new Node(nCol, nRow);
                    neighbor.gCost = newGCost;
                    neighbor.hCost = Math.abs(nCol - endCol) + Math.abs(nRow - endRow);
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;
                    neighbor.parent = current;
                    openList.add(neighbor);
                } else if (newGCost < neighbor.gCost) {
                    neighbor.gCost = newGCost;
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;
                    neighbor.parent = current;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Node> buildPath(Node endNode) {
        List<Node> p = new ArrayList<>();
        Node cur = endNode;
        while (cur.parent != null) {
            p.add(0, cur);
            cur = cur.parent;
        }
        return p;
    }

    private boolean isInList(List<Node> list, int col, int row) {
        return getFromList(list, col, row) != null;
    }

    private Node getFromList(List<Node> list, int col, int row) {
        for (Node n : list) {
            if (n.col == col && n.row == row) return n;
        }
        return null;
    }

    // Attack & Utils

    private boolean tryAttack() {
        int ts = gp.getTileSize();
        int mx = me.getX() + ts/2;
        int my = me.getY() + ts/2;
        int tx = target.getX() + ts/2;
        int ty = target.getY() + ts/2;
        int margin = ts / 2;

        boolean alignX = Math.abs(mx - tx) < margin;
        boolean alignY = Math.abs(my - ty) < margin;

        if ((alignX || alignY) && hasLineOfSight(mx, my, tx, ty)) {
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

    private boolean hasLineOfSight(int x1, int y1, int x2, int y2) {
        int ts = gp.getTileSize();
        int steps = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) / (ts/2);
        if (steps == 0) return true;

        float xInc = (float)(x2 - x1) / steps;
        float yInc = (float)(y2 - y1) / steps;
        float cx = x1, cy = y1;

        for (int i = 0; i < steps; i++) {
            cx += xInc; cy += yInc;
            if (isWall((int)cx / ts, (int)cy / ts)) return false;
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
    public Tank getTank() { return me; }
    public Tank getTargetTank() { return target; }
    public GamePanel getGp() { return gp; }

    private Tank findTarget() {
        if (gp.getTankSet() == null) return null;
        for (Tank t : gp.getTankSet()) {
            if (t != me && t.getHp() > 0) return t;
        }
        return null;
    }    

    private boolean isValid(int col, int row) {
        return col >= 0 && col < gp.getMaxCol() / gp.getWindow().scale() && 
               row >= 0 && row < gp.getMaxRow() / gp.getWindow().scale();
    }
    
    private boolean isWall(int col, int row) {
        try { return gp.getMap()[row][col] == 1; } catch (Exception e) { return true; }
    }
    
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}