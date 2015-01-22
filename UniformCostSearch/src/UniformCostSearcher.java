import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UniformCostSearcher {

    static final String inputFilePath = "UniformCostSearch/src/terrain.png";
    static final String outputFilePath = "UniformCostSearch/src/path.png";

    public static void main(String[] args){

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(inputFilePath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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

        coloredUniformCostSearch(bufferedImage, states, startState, endState, width, height);

        colorShortestPath(bufferedImage, startState, endState);

        File output = new File(outputFilePath);
        try{
            ImageIO.write(bufferedImage, "png", output);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private static UcsState coloredUniformCostSearch(BufferedImage image, UcsState[][] states, UcsState startState, UcsState endState, int width, int height){
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
                int transitionCost = getGreen(image, child.x, child.y);

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
                visited.add(child);
            }

            if(iterations%5000 < 1000){
                colorGreen(image, curState.x, curState.y);
            }
            iterations++;
        }
        throw new RuntimeException("Shit");
    }

    private static void colorShortestPath(BufferedImage image, UcsState startState, UcsState endState){
        UcsState curState = endState;
        while(!curState.equals(startState)){
            colorRed(image, curState.x, curState.y);
            curState = curState.parent;
        }
        colorRed(image, curState.x, curState.y);
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

    private static int getGreen(BufferedImage image, int x, int y){
        Color c = new Color(image.getRGB(x, y));
        return c.getGreen();
    }

    private static void colorGreen(BufferedImage image, int x, int y){
        image.setRGB(x, y, 0xFF00FF00);
    }

    private static void colorRed(BufferedImage image, int x, int y){
        image.setRGB(x, y, 0xFFFF0000);
    }


}
