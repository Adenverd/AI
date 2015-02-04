import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AstarSearcher {
    static final String inputFilePath = "AstarSearch/src/terrain.png";

    public static void main(String[] args){
        //compareOnImage();
        //System.out.println(new PuzzleState());
        compareOnPuzzle();
    }

    public static void compareOnPuzzle(){
        PuzzleState startState = new PuzzleState();
        PuzzleState ucsFinalState = UcsPuzzle(startState);
        //startState = new PuzzleState();
        //PuzzleState AstarFinalState = AstarPuzzle(startState);

        int x = 0;
    }

    public static void compareOnImage(){
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
                int heuristic = 14 * manhattanDistance(x, y, 400, 400);
                states[x][y] = new UcsState(Double.MAX_VALUE, heuristic, null, x, y);

            }
        }

        final UcsState startState = states[100][100];
        final UcsState endState = states[400][400];


        coloredUniformCostSearch(bufferedImage, states, startState, endState, width, height);
        coloredAstarSearch(bufferedImage, states, startState, endState, width, height);
    }

    /*
        Status: shit's broken. Frontier is growing way faster than pops, which isn't right.
     */
    private static PuzzleState AstarPuzzle(PuzzleState startState) {
        int pops = 0;
        Queue<PuzzleState> frontier = new PriorityQueue<PuzzleState>(100000, new StateHeuristicComparator());
        Map<PuzzleState, Double> visited = new HashMap<PuzzleState, Double>();

        startState.cost = 0d;
        startState.parent = null;
        startState.heuristic = manhattanDistance(startState.rowPieceOffsets[0], startState.columnPieceOffsets[0], -2, 4);

        frontier.add(startState);
        visited.put(startState, 0.);

        while (!frontier.isEmpty()) {
            PuzzleState curState = frontier.poll();

            pops++;
            if (pops % 20000 == 0) {
                System.out.println(pops + " " + curState.cost + " " + frontier.size());
            }
            if (curState.isSolved()) {
                System.out.println("bfs2=" + pops);
                return curState;
            }

            for (int piece = 0; piece < 11; piece++) {
                for (PuzzleState.Direction direction : PuzzleState.Direction.values()) {
                    PuzzleState childState = curState.tryMove(piece, direction);
                    if (childState != null) {
                        double newCost = visited.get(curState) + 1;
                        if (!visited.containsKey(childState) || newCost < visited.get(childState)) {
                            childState.parent = curState;
                            childState.cost = newCost;
                            childState.heuristic = manhattanDistance(childState.rowPieceOffsets[0], childState.columnPieceOffsets[0], -2, 4);
                            if (visited.containsKey(childState)) {
                                frontier.remove(childState);
                            }
                            visited.put(childState, childState.cost);
                            frontier.add(childState);
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Shit");
    }

    private static PuzzleState UcsPuzzle(PuzzleState startState){
        int pops = 0;
        Queue<PuzzleState> frontier = new LinkedList<PuzzleState>();
        Map<PuzzleState, Double> visited = new HashMap<PuzzleState, Double>();

        startState.cost = 0d;
        startState.parent = null;
        startState.heuristic = 1;

        frontier.add(startState);
        visited.put(startState, 0.);

        while (!frontier.isEmpty()) {
            PuzzleState curState = frontier.poll();

            pops++;
            if (pops % 20000 == 0) {
                System.out.println(pops + " " + curState.cost + " " + frontier.size());
            }
            if (curState.isSolved()) {
                System.out.println("bfs2=" + pops);
                return curState;
            }

            for (int piece = 0; piece < 11; piece++) {
                for (PuzzleState.Direction direction : PuzzleState.Direction.values()) {
                    PuzzleState childState = curState.tryMove(piece, direction);
                    if (childState != null) {
                        double newCost = visited.get(curState) + 1;
                        if (!visited.containsKey(childState) || newCost < visited.get(childState)) {
                            childState.parent = curState;
                            childState.cost = newCost;
                            childState.heuristic = 1;
                            if (visited.containsKey(childState)) {
                                frontier.remove(childState);
                            }
                            visited.put(childState, childState.cost);
                            frontier.add(childState);
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Shit");
    }

    private static UcsState coloredAstarSearch(BufferedImage image, UcsState[][] states, UcsState startState, UcsState endState, int width, int height){
        int pops = 0;
        PriorityQueue<UcsState> frontier = new PriorityQueue<UcsState>(width*height, new StateHeuristicComparator());
        Set<UcsState> visited = new HashSet<UcsState>();

        startState.cost = 0.0;
        startState.parent = null;

        frontier.add(startState);
        visited.add(startState);

        while(!frontier.isEmpty()){
            UcsState curState = frontier.poll();
            pops++;
            if (curState.equals(endState)){
                System.out.println("astar1=" + pops);
                return curState;
            }
            List<UcsState> children = getChildren(states, curState, width, height);

            for(UcsState child : children){
                int transitionCost = getGreen(image, child.x, child.y);

                if(visited.contains(child)){
                    if(curState.cost + transitionCost < child.cost){
                        child.cost = curState.cost + transitionCost;
                        child.parent = curState;
                        frontier.remove(child);
                        frontier.add(child);
                    }
                }
                else{
                    child.cost = curState.cost + transitionCost;
                    child.parent = curState;
                    frontier.add(child);
                }
                visited.add(child);
            }

        }
        throw new RuntimeException("Shit");
    }

    private static UcsState coloredUniformCostSearch(BufferedImage image, UcsState[][] states, UcsState startState, UcsState endState, int width, int height){
        int pops = 0;
        PriorityQueue<UcsState> frontier = new PriorityQueue<UcsState>(width*height, new StateCostComparator());
        Set<UcsState> visited = new HashSet<UcsState>();

        startState.cost = 0.0;
        startState.parent = null;

        frontier.add(startState);
        visited.add(startState);

        while(!frontier.isEmpty()){
            UcsState curState = frontier.poll();
            pops++;
            if (curState.equals(endState)){
                System.out.println("bfs1=" + pops);
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

        }
        throw new RuntimeException("Shit");
    }

    private static void colorShortestPath(BufferedImage image, UcsState startState, UcsState endState){
        UcsState curState = endState;
        while(!curState.equals(startState)){
            colorRed(image, curState.x, curState.y);
            curState = (UcsState) curState.parent;
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

    private static int manhattanDistance(UcsState curState, UcsState endState){
        return Math.abs(endState.x - curState.x) + Math.abs(endState.y - curState.y);
    }

    private static int manhattanDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
}
