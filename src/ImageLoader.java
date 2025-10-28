import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage getImage(int id) {
        switch (id) {
            case 0:
                try {
                    return ImageIO.read(ImageLoader.class.getResource("/images/grass.png"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            case 1:
                try {
                    return ImageIO.read(ImageLoader.class.getResource("/images/wall.png"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }                
            default:
                return null;
        }
    }
}
