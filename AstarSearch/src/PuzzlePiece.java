import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PuzzlePiece {
    public List<Pair<Integer, Integer>> indices;
    public char symbol;
    public int number;

    public PuzzlePiece(int number, char c, Pair<Integer, Integer>... indices){
        this.number = number;
        this.symbol = c;
        this.indices = new ArrayList<Pair<Integer, Integer>>();

        for(Pair<Integer, Integer> pair : indices){
            this.indices.add(new Pair<Integer, Integer>(pair.getKey(), pair.getValue()));
        }

    }
}
