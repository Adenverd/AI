public class Leg {
    public static final double RADIANS = 2d * Math.PI;
    public static final int MAX_STEPS = 50;
    public double angle;
    public int steps;

    public Leg(double a, int s){
        this.angle = a;
        this.steps = s;
    }

    public void mutateAngle(){
        angle = RandomGenerator.randDouble(RADIANS);
    }

    public void mutateSteps(){
        steps = RandomGenerator.randInt(1, MAX_STEPS);
    }
}
