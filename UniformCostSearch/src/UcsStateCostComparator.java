import java.util.Comparator;

public class UcsStateCostComparator implements Comparator<UcsState> {
    @Override
    public int compare(UcsState o1, UcsState o2) {
        return Double.compare(o1.cost, o2.cost);
    }
}
