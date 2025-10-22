import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TileManager {
    private GamePanel gp;
    private ArrayList<Tile> tiles;
    private long seed;
    private int num;

    public TileManager(GamePanel gp) throws IOException {
        this.gp = gp;
        tiles = new ArrayList<>();
        buildMap();
    }

    public void buildMap() throws IOException {
        seed = System.nanoTime();
        Random rand = new Random(seed);

        if (gp.getMaxCol() == 0 || gp.getMaxRow() == 0) {
            System.err.println("/ 0 !!!");
            return;
        }

        num = rand.nextInt() % (gp.getMaxCol() * gp.getMaxRow() / 3 + 1) + 1;

        Integer i = 0;
        Integer x = 0;
        Integer y = 0;
        for (i = 0; i < num; i = i + 1) {
            x = rand.nextInt() % (gp.getMaxCol()) * gp.getTileSize();
            y = rand.nextInt() % (gp.getMaxRow()) * gp.getTileSize();
            Wall wal = new Wall(x, y);
            tiles.add(wal);
        }
    }

    public void update() {

    }

    public void draw(Graphics2D g2) {
        Integer i = 0;

        for (i = 0; i < num; i += 1) {
            Tile tile = tiles.get(i);
            BufferedImage image = tile.getImage();

            if (image != null) {
                g2.drawImage(image, tile.getX(), tile.getY()
                    ,gp.getTileSize(),gp.getTileSize(), null);

            } else {
                g2.setColor(Color.red);
                g2.fillRect(tile.getX(), tile.getY(), gp.getTileSize(), gp.getTileSize());
            }            
        }
    }
}
