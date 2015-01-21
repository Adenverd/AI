import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Image {
    public int width;
    public int height;

    BufferedImage image;

    int[][] colorArray;

    public Image(BufferedImage image){
        this.image = image;
        this.colorArray = GetColorArray(this.image);
    }

    public int getRed(int x, int y){
        return getRed(colorArray[x][y]);
    }

    public int getGreen(int x, int y){
        return getGreen(colorArray[x][y]);
    }

    public int getBlue(int x, int y){
        return getBlue(colorArray[x][y]);
    }

    private static int[][] GetColorArray(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static int getRed(int rgb){
        return (rgb >> 16) & 0x000000FF;
    }

    private static int getGreen(int rgb){
        return (rgb >>8 ) & 0x000000FF;
    }

    private static int getBlue(int rgb){
        return (rgb) & 0x000000FF;
    }
}
