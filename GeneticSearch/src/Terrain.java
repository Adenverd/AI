import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    File file;
    BufferedImage image;

    public Terrain(File file) throws IOException{
        this.file = file;
        this.image = ImageIO.read(this.file);
    }

    public boolean isValidLocation(double x, double y){
        int right = image.getWidth();
        int bottom = image.getHeight();

        return (x >= 0 && x <= right) && (y >=0 && y <= bottom);
    }

    public int getCost(int x, int y){
        Color c = new Color(image.getRGB(x, y));
        return c.getGreen();
    }

    public int getCost(double x, double y){
        Color c = new Color(image.getRGB((int) x, (int) y));
        return c.getGreen();
    }
}
