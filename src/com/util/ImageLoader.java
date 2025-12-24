package com.util;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageLoader {
    private static final String BASE_PATH = "/com/util/images/";
    private static final String EXTENSION = ".png";

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

    public static BufferedImage loadImage(String name) {

        String path = BASE_PATH + name + EXTENSION;

        InputStream input = ImageLoader.class.getResourceAsStream(path);

        if (input == null) {
            System.err.println("找不到图片资源！");
            System.err.println("请确认文件存在" + path);
            return null; // 返回默认图，避免null
        }

        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
