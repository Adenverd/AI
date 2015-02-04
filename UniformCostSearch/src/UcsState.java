import java.util.Comparator;

public class UcsState extends State{
    public int x;
    public int y;

    public UcsState(double cost, double heuristic, UcsState parent, int x, int y){
        this.cost = cost;
        this.heuristic = heuristic;
        this.parent = parent;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof UcsState){
            UcsState other = (UcsState) o;
            return (x == other.x) && (y == other.y);
        }
        return false;
    }
}

