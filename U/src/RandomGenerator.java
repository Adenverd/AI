import java.util.Random;

public class RandomGenerator {
    private static Random random = new Random(100);

    /***
     * Returns a random int between min and max, inclusive.
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max){
        return random.nextInt((max-min) + 1) + min;
    }

    /***
     * Returns the next double between 0.0 and max, inclusive.
     * @param max
     * @return
     */
    public static double randDouble(double max){
        return random.nextDouble() * max;
    }

}
