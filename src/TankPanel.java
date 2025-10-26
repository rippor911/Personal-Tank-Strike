import java.awt.Graphics2D;

public interface TankPanel {
    public Tank getTank();

    public void update();

    public void draw(Graphics2D g2);
}
