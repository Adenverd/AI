import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class UniformCostSearcher {

    static final String inputFilePath = "./terrain.png";
    static final String outputFilePath = "./path.png";

    public static void main(String[] args){

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(inputFilePath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        byte[] terrain =
                ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        UcsState[][] states = new UcsState[width][height];

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                states[x][y] = new UcsState(Double.MAX_VALUE, null, x, y);
            }
        }

        final UcsState startState = states[100][100];
        final UcsState endState = states[400][400];

        coloredUniformCostSearch(bufferedImage, states, startState, endState, width, height, terrain);

        File output = new File(outputFilePath);
        try{
            ImageIO.write(bufferedImage, "png", output);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private static UcsState coloredUniformCostSearch(BufferedImage image, UcsState[][] states, UcsState startState, UcsState endState, int width, int height, byte[] terrain){
        PriorityQueue<UcsState> frontier = new PriorityQueue<UcsState>(width*height, new UcsStateCostComparator());
        Set<UcsState> visited = new HashSet<UcsState>();

        startState.cost = 0.0;
        startState.parent = null;

        frontier.add(startState);
        visited.add(startState);

        int iterations = 0;
        while(!frontier.isEmpty()){
            UcsState curState = frontier.poll();
            if (curState.equals(endState)){
                return curState;
            }
            List<UcsState> children = getChildren(states, curState, width, height);

            for(UcsState child : children){
                int transitionCost = getGreen(terrain, child.x, child.y, width);

                if(visited.contains(child)){
                    if(curState.cost + transitionCost < child.cost){
                        child.cost = curState.cost + transitionCost;
                        child.parent = curState;
                    }
                }
                else{
                    child.cost = curState.cost + transitionCost;
                    child.parent = curState;
                    frontier.add(child);
                }
            }

            if(iterations%5000 < 1000){
                setGreen(image, terrain, width, curState.x, curState.y, 255);
            }
            iterations++;
        }
        throw new RuntimeException("Shit");
    }


    private static List<UcsState> getChildren (UcsState[][] states, UcsState parent, int width, int height){
        List<UcsState> children = new ArrayList<UcsState>();

        //right child
        if(parent.x < width - 1) children.add(states[parent.x + 1][parent.y]);
        //left child
        if(parent.x > 0) children.add(states[parent.x - 1][parent.y]);
        //up child
        if(parent.y > 0) children.add(states[parent.x][parent.y - 1]);
        //down child
        if(parent.y < height - 1) children.add(states[parent.x][parent.y + 1]);

        return children;
    }

    private static int getAlpha(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int alpha_channel = 0; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + alpha_channel];
    }

    private static int getRed(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int red_channel = 3; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + red_channel];
    }

    private static int getGreen(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int green_channel = 2; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + green_channel];
    }

    private static int getBlue(byte[] terrain, int x, int y, int width){
        int channel_count = 4;
        int blue_channel = 1; // 0=alpha, 1=blue, 2=green, 3=red
        return terrain[channel_count * (y * width + x) + blue_channel];
    }

    private static void setGreen(BufferedImage image, byte[] terrain, int width, int x, int y, int green){
        int r = getRed(terrain, x, y, width);
        int b = getBlue(terrain, x, y, width);
        int a = getAlpha(terrain, x, y, width);
        int color = (a <<24) | (r << 16) | (green << 8) | b;

        image.setRGB(x, y, color);
    }


}
