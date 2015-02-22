/*
import javafx.util.Pair;

import java.util.LinkedList;

public class Path_DEPRECATED {
    public static final double RADIANS = 2d * Math.PI;
    public static final int MAX_STEPS = 5; //image size is 500x500, diagonal is 707
    public static final int MAX_PLAN_LENGTH = 100000;
    public static final int goalX = 400;
    public static final int goalY = 400;

    public LinkedList<Pair<Double, Integer>> steps;

    public double cost;
    public double x;
    public double y;

    public Path_DEPRECATED(double startX, double startY){
        steps = new LinkedList<Pair<Double, Integer>>();
        if(!GeneticSearcher.terrain.isValidLocation(startX, startY)) throw new RuntimeException("Invalid start location");
        this.x = startX;
        this.y = startY;
        cost = 0d;
    }


    public void addRandomAction(){
        if(steps.size() < MAX_PLAN_LENGTH){
            Pair<Double, Integer> action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), RandomGenerator.randInt(0, MAX_STEPS));
            steps.add(action);
        }
    }

    public void dropRandomAction(){
        if(steps.isEmpty()){
            return;
        }
        else{
            steps.remove(RandomGenerator.randInt(0, steps.size() - 1));
        }
    }

    public void mutateRandomAngle(){
        if(steps.isEmpty()){
            return;
        }
        else{
            int index = RandomGenerator.randInt(0, steps.size() - 1);
            Pair<Double, Integer> action = steps.remove(index);
            action = new Pair<Double, Integer>(RandomGenerator.randDouble(RADIANS), action.getValue());
            steps.add(index, action);
        }
    }

    public void mutateRandomStepCount(){
        if(steps.isEmpty()){
            return;
        }
        else{
            int index = RandomGenerator.randInt(0, steps.size() - 1);
            Pair<Double, Integer> action = steps.remove(index);
            action = new Pair<Double, Integer>(action.getKey(), RandomGenerator.randInt(0, MAX_STEPS));
            steps.add(index, action);
        }
    }

    public boolean step(){
        Pair<Double, Integer> action = steps.poll();
        if (action == null) {
            throw new RuntimeException("Can't step if steps is empty");
        }
        double movedX = this.x + action.getValue() * Math.cos(action.getKey());
        double movedY = this.y + action.getValue() * Math.sin(action.getKey());

        return updateLocation(movedX, movedY);
    }
    public boolean doPlan(){
        while(!steps.isEmpty()){
            if(!step()){
                return false;
            }
        }
        cost += 500d * getDistanceFromGoal();
        return true;
    }

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

*/
/*    //public double getFitness(){
        return this.getCost() + (500d * getDistanceFromGoal());
    }*//*


    private double getDistanceFromGoal(){
        return Math.sqrt(((goalX - this.x) * (goalX - this.x)) + ((goalY - this.y) * (goalY - this.y)));
    }

    //returns the loser
    public static Path_DEPRECATED fight(Path_DEPRECATED c1, Path_DEPRECATED c2, double underdogChance){
        double outcome = RandomGenerator.randDouble(1.0d);
        if(c1.getCost() > c2.getCost()){ //c1 is the underdog
            if(outcome < underdogChance){
                return c2;
            }
            return c1;
        }
        else{ //c2 is the underdog
            if(outcome < underdogChance){
                return c1;
            }
            return c2;
        }
    }

    //returns the loser
    public static Path_DEPRECATED fightWrong(Path_DEPRECATED c1, Path_DEPRECATED c2, double underdogChance){
        double outcome = RandomGenerator.randDouble(1.0d);
        if(c1.getCost() > c2.getCost()){ //c1 is the underdog
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

    public static Path_DEPRECATED mate(Path_DEPRECATED c1, Path_DEPRECATED c2){
        Path_DEPRECATED child = new Path_DEPRECATED(100, 100);

        child.steps = new LinkedList<Pair<Double, Integer>>();
        if(!c1.steps.isEmpty()){
            child.steps.addAll(c1.steps.subList(0, c1.steps.size()/2));
        }
        if(!c2.steps.isEmpty()){
                child.steps.addAll(c2.steps.subList(c2.steps.size()/2, c2.steps.size()-1));
        }


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
*/
