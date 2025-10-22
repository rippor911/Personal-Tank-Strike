public class Tank {
    private int x;
    private int y;
    private int speed;
    private int hp;
    private int score;
    private int initHp = 3;
    private int initSpeed = 4;
    private Item item;
    private int maxRow;
    private int maxCol;
    private int tileSize;

    public Tank(int x,int y,int maxRow,int maxCol,int tileSize) {
        this.x = x;
        this.y = y;
        score = 0;
        hp = initHp;
        speed = initSpeed;
        item = null;
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        this.tileSize = tileSize;
    }

    //action section:

    public boolean move(int dx,int dy) {
        //TO DO: wall check!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //Frame

        if (x + dx < 0 || x + dx > maxCol - tileSize || y + dy < 0 || y + dy > maxRow - tileSize) {
            return false;
        }

        return true;
    }

    public void shoot() {

    }

    public void update(int dx,int dy) {
        x += dx;
        y += dy;
    }

    //getter:

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }    

}
