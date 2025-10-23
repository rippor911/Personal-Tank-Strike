import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.awt.image.BufferedImage;

public class TileManager {
    private GamePanel gp;
    private long seed;
    private int [][] map;
    private Tile [][] tiles;
    //private boolean written;

    public TileManager(GamePanel gp) throws IOException {
        this.gp = gp;
        Random random = new Random();
        seed = Math.abs(random.nextInt()) % 2;
        tiles = new Tile[gp.getMaxRow()][gp.getMaxCol()];
        map = new int[gp.getMaxRow()][gp.getMaxCol()];
        //written = false;
    }

    public void buildMap() throws IOException {
        String url = "/map/map" + ((char)(seed + '0')) + ".txt";
        try {
            InputStream is = getClass().getResourceAsStream(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.getMaxRow(); row += 1) {
                String str = br.readLine();
                for (int col = 0; col < gp.getMaxCol(); col += 1) {
                    map[row][col] = (int)(str.charAt(2 * col) - '0');
                    if (map[row][col] == 1) {
                        tiles[row][col] = new Wall(col * gp.getTileSize(), row * gp.getTileSize());
                    } else {
                        tiles[row][col] = new Grass(col * gp.getTileSize(), row * gp.getTileSize());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }

    public void draw(Graphics2D g2) {
        /*if (written) {
            return;
        }*/
        for (int row = 0; row < gp.getMaxRow(); row += 1) {
            for (int col = 0; col < gp.getMaxCol(); col += 1) {

                BufferedImage img = tiles[row][col].getImage();

                try {
                    g2.drawImage(img,tiles[row][col].getX(),tiles[row][col].getY()
                        ,gp.getTileSize(),gp.getTileSize(),null);                         
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
        //written = true;
    }

    //getter:

    public int[][] getMap() {
        return map;
    }
}
