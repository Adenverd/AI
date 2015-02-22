public class Leg {
    public static final double RADIANS = 2d * Math.PI;
    public double angle;
    public int steps;

    public Leg(double a, int s){
        this.angle = a;
        this.steps = s;
    }

    public Leg(Leg leg){
        this.angle = leg.angle;
        this.steps = leg.steps;
    }

    public void mutateAngle(){
        angle = RandomGenerator.randDouble(RADIANS);
    }

    public void mutateSteps(){
        steps = RandomGenerator.randInt(1, Path.MAX_STEPS);
    }
}
