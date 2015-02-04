import java.util.Comparator;

public class StateCostComparator implements Comparator<State> {
    @Override
    public int compare(State o1, State o2) {
        return Double.compare(o1.cost, o2.cost);
    }
}
