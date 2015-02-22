import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sawyer on 2/21/15.
 */
public class GeneticSearcher {
    public static List<Path> population;
    public static BufferedImage output;

    public static void main(String[] args) throws IOException{
        population = new ArrayList<Path>(32);
        output = null;
        try{
            output = ImageIO.read(new File(Terrain.outputFilePath));
        } catch(IOException e){
            int j = 3;
        }
        for(int i = 0; i < 30; i++){
            population.add(new Path());
        }
        try{
            go();
        }catch (RuntimeException e){
            e.printStackTrace();
        }

    }

    public static void go() {
        int events = 0;
        int n = 500;
        int adds = 0, drops = 0, mutateAngles = 0, mutateSteps = 0, mates = 0, fights = 0;
        while (true) {
            int c = RandomGenerator.randInt(0, population.size() - 1);
            Path path = population.get(c);
            double d = RandomGenerator.randDouble(1d);
            if (d < .1) {
                adds++;
                if(!path.addRandomLeg()){
                    replacePathWithRandomMate(path);
                }
            }
            else if (d < .15) {
                drops++;
                if (!path.dropRandomLeg()) {
                    replacePathWithRandomMate(path);
                }
            } else if (d < .2) {
                mutateAngles++;
                if(!path.mutateRandomAngle()){
                    replacePathWithRandomMate(path);
                }
            } else if (d < .25) {
                mutateSteps++;
                if(!path.mutateRandomSteps()){
                    replacePathWithRandomMate(path);
                }
            } else if (d < 1){
                int c2 = RandomGenerator.randInt(0, population.size() - 1);
                while (c2 == c) {
                    c2 = RandomGenerator.randInt(0, population.size() - 1);
                }
                Path path2 = population.get(c2);
                Path weakest = Path.tournament(path, path2, .001);
                replacePathWithRandomMate(weakest);
                fights++;
            }
            events++;

            if (events % n == 0) {
                double bestFitness = findLowestCost();
                //drawPaths();

                System.out.println(events + "\t" + bestFitness);
            }
        }
    }

    private static double findLowestCost() {
        double bestFitness = Double.MAX_VALUE;
        for (int i = 0; i < population.size(); i++){
            Path c = population.get(i);
            //c.travel();
            double fitness = c.getCost();
            if (fitness < bestFitness){
                bestFitness = c.getCost();
            }
        }

        return bestFitness;
    }

    private static Path findFittest(){
        Path best = null;
        double bestFitness = Double.MAX_VALUE;
        for (Path c : population){
            double fitness = c.getCost();
            if (fitness < bestFitness){
                best = c;
                bestFitness = c.getCost();
            }
        }

        return best;
    }

    public static void replacePathWithRandomMate(Path replace) {
        Path path, path1, path2;
        population.remove(replace);
        int c1 = RandomGenerator.randInt(0, population.size() - 1);
        int c2 = RandomGenerator.randInt(0, population.size() - 1);
        while (c2 == c1) {
            c2 = RandomGenerator.randInt(0, population.size() - 1);
        }

        path1 = population.get(c1);
        path2 = population.get(c2);
        path = Path.mate(path1, path2);
        if (!path.travel()) {
            path = new Path();
        }

        population.add(path);
    }

    public static void removePath(Path path){
        population.remove(path);
    }

    public static void drawPaths(){
        for(int i = 0; i < population.size(); i++){
            population.get(i).draw(output);
        }
        File outputFile = new File(Terrain.outputFilePath);
        try{
            ImageIO.write(output, "png", outputFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
