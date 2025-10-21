import java.util.ArrayList;

public class Tank {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int hp;
    private int score;
    private int initHp = 3;
    private ArrayList<Item> items;

    public Tank(int x,int y) {
        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
        score = 0;
        hp = initHp;
        items = new ArrayList<>();
    }

    public void move() {

    }

    public void shoot() {

    }

}
