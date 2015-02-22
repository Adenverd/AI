import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    public static final String inputFilePath = "GeneticSearch/src/terrain.png";

    BufferedImage image;
    int right;
    int bottom;

    public Terrain(){
        try{
            this.image = ImageIO.read(new File(inputFilePath));
        } catch(Exception e){
            e.printStackTrace();
        }

        right = image.getWidth();
        bottom = image.getHeight();
    }

    public boolean isValidLocation(double x, double y){
        return (x >= 0 && x <= right) && (y >=0 && y <= bottom);
    }

    public int getCost(int x, int y){
        if(isValidLocation(x, y)){
            Color c = new Color(image.getRGB(x, y));
            return c.getGreen();
        }
        return Integer.MAX_VALUE;
    }

    public int getCost(double x, double y){
        if(isValidLocation(x, y)){
            Color c = new Color(image.getRGB((int) x, (int) y));
            return c.getGreen();
        }
        throw new RuntimeException("Can't get cost of invalid location!");
    }
}
