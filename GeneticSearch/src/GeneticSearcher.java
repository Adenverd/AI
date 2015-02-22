import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sawyer on 2/21/15.
 */
public class GeneticSearcher {
    public static List<Path> population;

    public static void main(String[] args) throws IOException{
        population = new ArrayList<Path>(32);
        for(int i = 0; i < 30; i++){
            population.add(new Path());
        }
        try{
            go();
        }catch (RuntimeException e){
            System.out.println(e.getStackTrace());
        }

    }

    public static void go() {
        int events = 0;
        int n = 10000;
        int adds = 0, drops = 0, mutateAngles = 0, mutateSteps = 0, mates = 0, fights = 0;
        while (true) {
            int c = RandomGenerator.randInt(0, 29);
            Path path = population.get(c);
            double d = RandomGenerator.randDouble(1d);
            if (d < .3) {
                adds++;
                path.addRandomLeg();
            }
            else if (d < .4) {
                drops++;
                path.dropRandomLeg();
            } else if (d < .5) {
                mutateAngles++;
                path.mutateRandomAngle();
            } else if (d < .6) {
                mutateSteps++;
                path.mutateRandomSteps();
            } else {
                int c2 = RandomGenerator.randInt(0, 29);
                while (c2 == c) {
                    c2 = RandomGenerator.randInt(0, 29);
                }
                Path path2 = population.get(c2);
                population.remove(Path.getHighestCost(path, path2));
                population.add(mateRandom());
                fights++;

            }
            events++;

            if (events % n == 0) {
                double bestFitness = findLowestCost();
                if(bestFitness == 0.0){
                    int i = 3; //lolwat
                }
                System.out.println(events + "\t" + bestFitness);
            }
        }
    }

    private static double findLowestCost() {
        double bestFitness = Double.MAX_VALUE;
        for (Path c : population){
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

    private static Path mateRandom(){
        int c1 = RandomGenerator.randInt(0, population.size() - 1);
        int c2 = RandomGenerator.randInt(0, population.size() - 1);
        while (c2 == c1){
            c2 = RandomGenerator.randInt(0, population.size() - 1);
        }

        Path path1 = population.get(c1);
        Path path2 = population.get(c2);

        return Path.mate(path1, path2);
    }
}
