import java.util.Comparator;

public class StateHeuristicComparator implements Comparator<State>{
    @Override
    public int compare(State o1, State o2) {
        return Double.compare(o1.cost + o1.heuristic, o2.cost + o2.heuristic);
    }
}
