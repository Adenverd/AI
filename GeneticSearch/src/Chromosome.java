import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    public static double RADIANS = 2d * Math.PI;
    public static int MAX_STEPS = 707; //image size is 500x500, diagonal is 707

    public List<Pair<Double, Integer>> plan;

    public double cost;
    public double x;
    public double y;

    public Chromosome(double startX, double startY){
        plan = new ArrayList<Pair<Double, Integer>>();
        cost = 0d;
        if (!updateLocation(startX, startY)) throw new RuntimeException("Invalid starting location for a chromosome");
        //createPlan();
    }

//    private void createPlan() {
//        for (int i = 0; i < plan.size(); i++) {
//            Pair<Double, Integer> action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), RandomGenerator.randInt(0, MAX_STEPS));
//            plan.add(action);
//        }
//    }


    public void addRandomAction(){
        Pair<Double, Integer> action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), RandomGenerator.randInt(0, MAX_STEPS));
        plan.add(action);
    }

    public void dropRandomAction(){
        if(plan.size() == 0){
            //TODO:SCA: what to do when plan is empty?
            return;
        }
        else{
            plan.remove(RandomGenerator.randInt(0, plan.size()));
        }
    }

    public void mutateRandomAngle(){
        if(plan.size() == 0){
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

    //TODO:SCA:
    //public boolean step(); //use trig and shit
    //public boolean doPlan();

    //returns true if moving to a valid location, false if not
    public boolean updateLocation(double x, double y){
        if(!GeneticSearcher.terrain.isValidLocation(x, y)) return false;

        int intX = (int)x;
        int intY = (int)y;

        if((int)this.x != intX || (int)this.y != intY){
            this.x = x;
            this.y = y;
            updateCost();
        }
        else{
            this.x = x;
            this.y = y;
        }

        return true;
    }

    private double updateCost(){
        this.cost += GeneticSearcher.terrain.getCost(this.x, this.y);
        return this.cost;
    }

    public static Chromosome fight(Chromosome c1, Chromosome c2, double underdogChance){
        double outcome = RandomGenerator.randDouble(1.0d);
        if(c1.cost < c2.cost){ //c1 is the underdog
            if(outcome < underdogChance){
                return c1;
            }
            return c2;
        }
        else{ //c2 is the underdog
            if(outcome < underdogChance){
                return c2;
            }
            return c1;
        }
    }

    public static Chromosome mate(Chromosome c1, Chromosome c2){
        Chromosome child = new Chromosome(c1.x, c2.y);

        child.plan = new ArrayList<Pair<Double, Integer>>();
        child.plan.addAll(c1.plan.subList(0, c1.plan.size()/2));
        child.plan.addAll(c2.plan.subList(c2.plan.size()/2, c2.plan.size()-1));

        return child;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
