package com.util;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage getImage(int id) {
        switch (id) {
            case 0:
                try {
                    return ImageIO.read(ImageLoader.class.getResource("/com/util/images/grass.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            case 1:
                try {
                    return ImageIO.read(ImageLoader.class.getResource("/com/util/images/wall.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }                
            default:
                return null;
        }
    }

    public static BufferedImage loadImage(String path) {
        try {
            path = "/com/util" + path;
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
