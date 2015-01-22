import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Set;

public class UniformCostSearcher {

    public static void main(String[] args){

        final UcsState startState = new UcsState(0, null, 100, 100);
        final UcsState endState = new UcsState(0, null, 400, 400);

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File("strawberry.jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        byte[] terrain =
                ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        coloredUniformCostSearch(bufferedImage, startState, endState, width, height, terrain);

    }

    private static void coloredUniformCostSearch(BufferedImage image, UcsState startState, UcsState endState, int width, int height, byte[] terrain){
        PriorityQueue<UcsState> frontier = new PriorityQueue<UcsState>(width*height, new UcsStateCostComparator());
        Set<UcsState>


    }

    private int getAlpha(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int alpha_channel = 0; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + alpha_channel];
    }

    private int getRed(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int red_channel = 3; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + red_channel];
    }

    private int getGreen(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int green_channel = 2; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + green_channel];
    }

    private int getBlue(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int blue_channel = 1; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + blue_channel];
    }

    private void setGreen(BufferedImage image, byte[] terrain, int width, int x, int y, int green){
        int r = getRed(terrain, x, y, width);
        int b = getBlue(terrain, x, y, width);
        int a = getAlpha(terrain, x, y, width);
        int color = (a <<24) | (r << 16) | (green << 8) | b;

        image.setRGB(x, y, color);
    }


}
