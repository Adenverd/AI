import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    public static double RADIANS = 2d * Math.PI;
    public static int MAX_STEPS = 707; //image size is 500x500, diagonal is 707

    public List<Pair<Double, Integer>> plan;
    public Double cost;

    public Chromosome(){
        plan = new ArrayList<Pair<Double, Integer>>();
        cost = 0.0;
        createPlan();
    }

    private void createPlan() {
        for (int i = 0; i < plan.size(); i++) {
            Pair<Double, Integer> action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), RandomGenerator.randInt(0, MAX_STEPS));
            plan.add(action);
        }
    }

    public void addRandomAction(){
        Pair<Double, Integer> action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), RandomGenerator.randInt(0, MAX_STEPS));
        plan.add(action);
    }

    public void dropRandomAction(){
        if(plan.size() <= 0){
            //TODO:SCA: what to do when plan is empty?
            return;
        }
        else{
            plan.remove(RandomGenerator.randInt(0, plan.size()));
        }
    }

    public void mutateRandomAngle(){
        if(plan.size() <= 0){
            //TODO:SCA: what to do when plan is empty?
            return;
        }
        else{
            int index = RandomGenerator.randInt(0, plan.size());
            Pair<Double, Integer> action = plan.remove(index);
            action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), action.getValue());
            plan.add(index, action);
        }
    }

    public void mutateRandomStepCount(){
        if(plan.size() <= 0){
            //TODO:SCA: what to do when plan is empty?
            return;
        }
        else{
            int index = RandomGenerator.randInt(0, plan.size());
            Pair<Double, Integer> action = plan.remove(index);
            action = new Pair<Double, Integer>(action.getKey(), RandomGenerator.randInt(0, MAX_STEPS));
            plan.add(index, action);
        }
    }

    public Chromosome fight(Chromosome other, double underdogChance){
        double outcome = RandomGenerator.randDouble(1.0d);
        if(this.cost < other.cost){ //this is the underdog
            if(outcome < underdogChance){
                return this;
            }
            return other;
        }
        else{ //other is the underdog
            if(outcome < underdogChance){
                return other;
            }
            return this;
        }
    }
}
