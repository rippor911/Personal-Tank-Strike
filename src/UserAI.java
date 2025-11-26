import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class UserAI implements TankPanel {

    private Tank myTank;
    private Tank targetTank;
    private GamePanel gp;
    private int dx = 0;
    private int dy = 0;
    private Random random;
    private int randomTargetGridX;
    private int randomTargetGridY;
    private long lastTargetChangeTime;
    private int targetChangeInterval = 10000;
    private long lastMoveTime;
    private int moveInterval = 400;
    private boolean avoidingWall = false;
    private static final int[][] ADJ_DIRS = {
            { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },

    };

    public UserAI(int x, int y, GamePanel gp) throws IOException {
        this.gp = gp;
        this.myTank = new Tank(x, y, gp);
        this.random = new Random();
        this.lastTargetChangeTime = System.currentTimeMillis();
        this.lastMoveTime = System.currentTimeMillis();
        resetRandomTarget();
        dx = 0;
        dy = 0;
    }

    @Override
    public void update() {

        targetTank = findTargetTank();
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTargetChangeTime >= targetChangeInterval) {
            resetRandomTarget();
            avoidingWall = false;
            lastTargetChangeTime = currentTime;
        }
        if (currentTime - lastMoveTime >= moveInterval) {
            if (targetTank != null && !avoidingWall) {
                moveToTargetTank();
            } else {
                randomMoveWithAvoidWall();
            }
        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage tankImage = myTank.getTankImage(dx, dy);

        if (tankImage != null) {
            g2.drawImage(tankImage, myTank.getX(), myTank.getY(), 
                gp.getTileSize(), gp.getTileSize(), null);

        } else {
            g2.setColor(Color.red);
            g2.fillRect(myTank.getX(), myTank.getY(), gp.getTileSize(), gp.getTileSize());
        }
    }

    private Tank findTargetTank() {
        if (gp.getTankSet() == null) {
            return null;
        }
        for (Tank tank : gp.getTankSet()) {
            if (tank != myTank && tank.getHp() > 0) {
                return tank;
            }
        }
        return null;
    }

    private void moveToTargetTank() {
        int tileSize = gp.getTileSize();
        int targetGridX = targetTank.getX() / tileSize;
        int targetGridY = targetTank.getY() / tileSize;
        int tankSpeed = myTank.getSpeed() / 2;

        dx = 0;
        dy = 0;

        int targetPixelX = targetGridX * tileSize;
        int targetPixelY = targetGridY * tileSize;
        int currentX = myTank.getX();
        int currentY = myTank.getY();        

        if (currentX < targetPixelX - tankSpeed) {
            dx = tankSpeed;
        } else if (currentX > targetPixelX + tankSpeed) {
            dx = -tankSpeed;
        }

        if (currentY < targetPixelY - tankSpeed) {
            dy = tankSpeed;
        } else if (currentY > targetPixelY + tankSpeed) {
            dy = -tankSpeed;
        }

        if (myTank.move(dx, dy)) {
            myTank.update(dx, dy);
            return;
        }

        if (isTopRightBlocked(currentX, currentY, tankSpeed)) {
            if (tryTopRightEscape(tankSpeed)) {
                avoidingWall = true;
                return;
            }
        }
        for (int[] adjDir : ADJ_DIRS) {
            int adjDx = adjDir[0] * tankSpeed;
            int adjDy = adjDir[1] * tankSpeed;
            if (myTank.move(adjDx, adjDy)) {
                myTank.update(adjDx, adjDy);
                dx = adjDx;
                dy = adjDy;
                break;
            }
        }

    }

    private void randomMoveWithAvoidWall() {
        int tileSize = gp.getTileSize();

        int tankSpeed = myTank.getSpeed();

        dx = 0;
        dy = 0;

        int targetPixelX = randomTargetGridX * tileSize;
        int targetPixelY = randomTargetGridY * tileSize;
        int currentX = myTank.getX();
        int currentY = myTank.getY();        

        if (currentX < targetPixelX - tankSpeed) {
            dx = tankSpeed;
        } else if (currentX > targetPixelX + tankSpeed) {
            dx = -tankSpeed;
        }

        if (currentY < targetPixelY - tankSpeed) {
            dy = tankSpeed;
        } else if (currentY > targetPixelY + tankSpeed) {
            dy = -tankSpeed;
        }

        if (myTank.move(dx, dy)) {
            myTank.update(dx, dy);
            return;
        }

        for (int[] adjDir : ADJ_DIRS) {
            int adjDx = adjDir[0] * tankSpeed;
            int adjDy = adjDir[1] * tankSpeed;
            if (myTank.move(adjDx, adjDy)) {
                myTank.update(adjDx, adjDy);
                dx = adjDx;
                dy = adjDy;
                return;
            }
        }

        int shakeDir = random.nextInt(4);
        int shakeDx = ADJ_DIRS[shakeDir][0] * 1;
        int shakeDy = ADJ_DIRS[shakeDir][1] * 1;
        if (myTank.move(shakeDx, shakeDy)) {
            myTank.update(shakeDx, shakeDy);
            dx = shakeDx;
            dy = shakeDy;
        }
    }

    private boolean isTopRightBlocked(int currentX, int currentY, int tankSpeed) {
        return !myTank.move(4 * tankSpeed, 0) && !myTank.move(0, -4 * tankSpeed);
    }

    private boolean tryTopRightEscape(int tankSpeed) {
        if (myTank.move(-4 * tankSpeed, 0)) {
            myTank.update(-4 * tankSpeed, 0);
            dx = -4 * tankSpeed;
            dy = 0;
            return true;
        } else if (myTank.move(0, 4 * tankSpeed)) {
            myTank.update(0, 4 * tankSpeed);
            dx = 0;
            dy = tankSpeed;
            return true;
        }
        return false;
    }

    private void resetRandomTarget() {
        int maxGridCol = gp.getMap()[0].length;
        int maxGridRow = gp.getMap().length;
        int[][] map = gp.getMap();

        while (true) {
            int randomGridX = random.nextInt(maxGridCol);
            int randomGridY = random.nextInt(maxGridRow);

            if (map[randomGridY][randomGridX] == 0) {
                this.randomTargetGridX = randomGridX;
                this.randomTargetGridY = randomGridY;
                break;
            }
        }
    }

    @Override
    public Tank getTank() {
        return myTank;
    }

    public Tank getTargetTank() {
        return targetTank;
    }

    public GamePanel getGp() {
        return gp;
    }
}