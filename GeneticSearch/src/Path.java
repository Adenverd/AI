import java.util.LinkedList;
import java.util.List;

public class Path {
    public static final double RADIANS = 2d * Math.PI;
    public static final int MAX_STEPS = 50;
    public static final Terrain terrain = new Terrain();

    public static final int startX = 100;
    public static final int startY = 100;
    public static final int endX = 400;
    public static final int endY = 400;

    private List<Leg> legs;

    public double getCost() {
        return cost;
    }

    private double cost;

    public Path(){
        cost = Double.POSITIVE_INFINITY;
        legs = new LinkedList<Leg>();
    }

    public void addLeg(Leg leg){
        legs.add(leg);
    }

    public void addRandomLeg(){
        double angle = RandomGenerator.randDouble(RADIANS);
        int steps = RandomGenerator.randInt(0, MAX_STEPS);
        legs.add(new Leg(angle, steps));
    }

    public void dropRandomLeg(){
        if(!legs.isEmpty()){
            legs.remove(RandomGenerator.randInt(0, legs.size() - 1));
        }
    }

    public void mutateRandomAngle(){
        if(!legs.isEmpty()){
            legs.get(RandomGenerator.randInt(0, legs.size() - 1)).mutateAngle();
        }
    }

    public void mutateRandomSteps(){
        if(!legs.isEmpty()){
            legs.get(RandomGenerator.randInt(0, legs.size() - 1)).mutateSteps();
        }
    }

    private double distanceFromGoal(double x, double y){
        return Math.sqrt(((endX - x) * (endX - x)) + ((endY - y) * (endY - y)));
    }

    public void travel(){
        double x = 100;
        double y = 100;
        double c = 0;

        for(Leg leg : legs){
            for(int i = 0; i < leg.steps; i++){
                x += Math.cos(leg.angle);
                y += Math.sin(leg.angle);
                c += terrain.getCost(x, y);
            }
        }

        if(!terrain.isValidLocation(x, y)){
            this.cost = Double.POSITIVE_INFINITY;
        }
        else{
            this.cost = c + (500d * distanceFromGoal(x, y));
        }
    }

    public static Path getHighestCost(Path p1, Path p2){
        p1.travel();
        p2.travel();

        return p1.cost > p2.cost ? p1 : p2;
    }

    public static Path mate(Path p1, Path p2){
        Path child = new Path();
        try{
            if(!p1.legs.isEmpty()){
                for(int i = 0; i < p1.legs.size()/2; i++){
                    child.addLeg(p1.legs.get(i));
                }
            }
            if(!p2.legs.isEmpty()){
                for(int i = p2.legs.size()/2; i < p2.legs.size(); i++){
                    child.addLeg(p2.legs.get(i));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return child;
    }
}
