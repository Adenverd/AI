import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Path {
    public static final double RADIANS = 2d * Math.PI;
    public static final int MAX_STEPS = 10;
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
        cost = 999999999d;
        legs = new LinkedList<Leg>();
    }

    public void addLeg(Leg leg){
        legs.add(new Leg(leg));
        //return this.travel();
    }

    public boolean addRandomLeg() {
        double angle = RandomGenerator.randDouble(RADIANS);
        int steps = RandomGenerator.randInt(0, MAX_STEPS);
        Leg tLeg = new Leg(angle, steps);
        legs.add(tLeg);
        return this.travel();
    }

    public boolean dropRandomLeg(){
        if(!legs.isEmpty()){
            legs.remove(RandomGenerator.randInt(0, legs.size() - 1));
        }
        return this.travel();
    }

    public boolean mutateRandomAngle(){
        if(!legs.isEmpty()){
            legs.get(RandomGenerator.randInt(0, legs.size() - 1)).mutateAngle();
        }
        return this.travel();
    }

    public boolean mutateRandomSteps(){
        if(!legs.isEmpty()){
            legs.get(RandomGenerator.randInt(0, legs.size() - 1)).mutateSteps();
        }
        return this.travel();
    }

    private double distanceFromGoal(double x, double y){
        return Math.sqrt(((endX - x) * (endX - x)) + ((endY - y) * (endY - y)));
    }

    public boolean travel() {
        double x = startX;
        double y = startY;
        double c = 0;

        for (Leg leg : legs) {
            for (int i = 0; i < leg.steps; i++) {
                int tempX = (int) x;
                int tempY = (int) y;

                x += Math.cos(leg.angle);
                y -= Math.sin(leg.angle);
                if (tempX != (int) x || tempY != (int) y) { //if we've traveled to a new pixel
                    int legCost = terrain.getCost(x, y);
                    if(legCost == Terrain.INVALID_COST){
                        return false;
                    }
                    c += legCost;
                }
            }
        }
        this.cost = c + (500d * distanceFromGoal(x, y));
        return true;
    }

    public void draw(BufferedImage image){
        double x = startX;
        double y = startY;
        double c = 0;

        for (Leg leg : legs) {
            for (int i = 0; i < leg.steps; i++) {
                int tempX = (int) x;
                int tempY = (int) y;

                x += Math.cos(leg.angle);
                y -= Math.sin(leg.angle);
                if (tempX != (int) x || tempY != (int) y) { //if we've traveled to a new pixel
                    try{
                        colorGreen(image, (int)x , (int)y);
                    } catch(Exception e){
                        int j = 2; //lolwat
                    }

                    /*int legCost = terrain.getCost(x, y);
                    *//*if(legCost == Terrain.INVALID_COST){
                        return false;
                    }*//*
                    c += legCost;*/
                }
            }
        }
        //this.cost = c + (500d * distanceFromGoal(x, y));
        //return true;
    }

    private static void colorGreen(BufferedImage image, int x, int y){
        image.setRGB(x, y, 0xFF00FF00);
    }

    private static void colorRed(BufferedImage image, int x, int y){
        image.setRGB(x, y, 0xFFFF0000);
    }

    //returns highest cost/weakest
    public static Path tournament(Path p1, Path p2, double underdogChance){
        double d = RandomGenerator.randDouble(1);

        if(p1.cost > p2.cost){ //p1 weakest/underdog
            if(d < underdogChance)
                return p2;
            return p1;
        }
        else{
            if(d < underdogChance){
                return p1;
            }
            return p2;
        }
    }

    //TODO:SCA: getting stuck in a situation where (it seems like) no combination of from any of the paths will get you
    //a valid path. Pretty sure rounding shit is wrong.
    public static Path mate(Path p1, Path p2){
        Path child = new Path();
        double segment = .5; //RandomGenerator.randDouble(1);
        try{
            if(!p1.legs.isEmpty()){
                for(int i = 0; i <= (int)(p1.legs.size()*segment); i++){
                    child.addLeg(p1.legs.get(i));
                }
            }
            if(!p2.legs.isEmpty()){
                for(int i = (int)(p2.legs.size() * segment); i < p2.legs.size(); i++){
                    child.addLeg(p2.legs.get(i));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return child;
    }

    private static int round(double d){
        return (int)Math.round(d);
    }
}
