package Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class Images {
    public static HashMap<String, BufferedImage> images;
    static{
        images = new HashMap<>();

        String[] imageNames = new String[]{
                //Buildings
                "building_castle",
                "building_farm",
                "building_tower",
                "building_village",
                "building_woodmill",

                //Units
                "unit_archer_mp",
                "unit_archer_player",
                "unit_cavalry_mp",
                "unit_cavalry_player",
                "unit_warrior_mp",
                "unit_warrior_player",

                //Cursors
                "cursor_attack",
                "cursor_select",
        };

        for(String imageName : imageNames){
            images.put(imageName, readImg(imageName));
        }
    }

    public static URL buildImageFile(String file){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource("./Resources/" + file + ".png");
    }

    public static BufferedImage readImg(String file){
        BufferedImage img = null;
        try {
            URL url = buildImageFile(file);
            img =  ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        return img;
    }
}