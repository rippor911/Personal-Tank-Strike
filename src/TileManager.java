import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class TileManager {
    private GamePanel gp;
    private int [][] map;
    private BufferedImage [] images;

    public TileManager(GamePanel gp) throws IOException {
        this.gp = gp;
        map = new int[gp.getMaxRow() + 5][gp.getMaxCol() + 5];   //add 5 to avoid going beyond
        images = new BufferedImage[5];

        int tileTypeNum = 2;
        initImages(tileTypeNum);            //Load Images
    }

    public void initImages(int n) {
        for (int i = 0; i < n; i += 1) {
            images[i] = ImageLoader.getImage(i);
        }
    }

    public void buildMap() throws IOException {
        map = MazeGenerator.generateMap(gp.getMaxRow(), gp.getMaxCol());
    }

    public void update() {
        
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.getMaxRow(); row += 1) {
            for (int col = 0; col < gp.getMaxCol(); col += 1) {

                BufferedImage img = images[map[row][col]];

                try {
                    g2.drawImage(img,col * gp.getTileSize(),row * gp.getTileSize()
                        ,gp.getTileSize(),gp.getTileSize(),null);                         
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
    }

    //getter:

    public int[][] getMap() {
        return map;
    }
}
