import java.util.Arrays;
import java.util.List;

public class PuzzleState extends State{
    public static enum Direction{
        LEFT, RIGHT, UP, DOWN
    }

    public static final PuzzlePiece PIECE0 = new PuzzlePiece(0, '0', new Pair<Integer, Integer>(3, 1), new Pair<Integer, Integer>(3, 2), new Pair<Integer, Integer>(4, 1), new Pair<Integer, Integer>(4, 2));
    public static final PuzzlePiece PIECE1 = new PuzzlePiece(1, '1', new Pair<Integer, Integer>(5, 1), new Pair<Integer, Integer>(6, 1), new Pair<Integer, Integer>(6, 2));
    public static final PuzzlePiece PIECE2 = new PuzzlePiece(2, '2', new Pair<Integer, Integer>(5, 2), new Pair<Integer, Integer>(5, 3), new Pair<Integer, Integer>(6, 3));
    public static final PuzzlePiece PIECE3 = new PuzzlePiece(3, '3', new Pair<Integer, Integer>(7, 4), new Pair<Integer, Integer>(7, 5), new Pair<Integer, Integer>(8, 5));
    public static final PuzzlePiece PIECE4 = new PuzzlePiece(4, '4', new Pair<Integer, Integer>(7, 6), new Pair<Integer, Integer>(7, 7), new Pair<Integer, Integer>(8, 6));
    public static final PuzzlePiece PIECE5 = new PuzzlePiece(5, '5', new Pair<Integer, Integer>(7, 3), new Pair<Integer, Integer>(8, 3), new Pair<Integer, Integer>(8, 4));
    public static final PuzzlePiece PIECE6 = new PuzzlePiece(6, '6', new Pair<Integer, Integer>(4, 5), new Pair<Integer, Integer>(5, 4), new Pair<Integer, Integer>(5, 5), new Pair<Integer, Integer>(6, 5));
    public static final PuzzlePiece PIECE7 = new PuzzlePiece(7, '7', new Pair<Integer, Integer>(4, 6), new Pair<Integer, Integer>(5, 6), new Pair<Integer, Integer>(5, 7), new Pair<Integer, Integer>(6, 6));
    public static final PuzzlePiece PIECE8 = new PuzzlePiece(8, '8', new Pair<Integer, Integer>(5, 8), new Pair<Integer, Integer>(6, 7), new Pair<Integer, Integer>(6, 8));
    public static final PuzzlePiece PIECE9 = new PuzzlePiece(9, '9', new Pair<Integer, Integer>(2, 6), new Pair<Integer, Integer>(3, 5), new Pair<Integer, Integer>(3, 6));
    public static final PuzzlePiece PIECEA = new PuzzlePiece(10, 'a', new Pair<Integer, Integer>(1, 5), new Pair<Integer, Integer>(1, 6), new Pair<Integer, Integer>(2, 5));


    public static final PuzzlePiece[] STARTING_PIECES = new PuzzlePiece[]{ PIECE0, PIECE1, PIECE2, PIECE3, PIECE4, PIECE5, PIECE6, PIECE7, PIECE8, PIECE9,  PIECEA};
    public int[] rowPieceOffsets;
    public int[] columnPieceOffsets;
    public boolean[][] occupiedBoard;

    public PuzzleState(){
        this.rowPieceOffsets = new int[]{0,0,0,0,0,0,0,0,0,0,0};
        this.columnPieceOffsets = new int[]{0,0,0,0,0,0,0,0,0,0,0};
        this.occupiedBoard = makeOccupiedBoard();

    }

    /**
     * Deep copy other
     * @param other
     */
    public PuzzleState(PuzzleState other){
        this.rowPieceOffsets = new int[11];
        this.columnPieceOffsets = new int[11];
        for(int x = 0; x < 11; x++){
            this.rowPieceOffsets[x] = other.rowPieceOffsets[x];
        }
        for(int x = 0; x < 11; x++){
            this.columnPieceOffsets[x] = other.columnPieceOffsets[x];
        }
        this.occupiedBoard = Arrays.copyOf(other.occupiedBoard, other.occupiedBoard.length);
    }

    /**
     * Assumes current state is valid
     * @param piece
     * @param direction
     * @return
     */
    public PuzzleState tryMove(int piece, Direction direction) {
        if(isValidMove(piece, direction)){
            //valid move, return a new PuzzleState
            PuzzleState newState = new PuzzleState(this);
            newState.doMove(piece, direction);

            return newState;
        }

        return null;
    }

    public boolean isSolved(){
        return this.rowPieceOffsets[0] == -2 && this.columnPieceOffsets[0] == 4;
    }

    public boolean isSolved_TEST(){
        return this.rowPieceOffsets[0] == -2 && this.columnPieceOffsets[0] == 2;
    }



    /**
     * Returns true if valid move, else false
     */
    private boolean isValidMove(int piece, Direction direction){
        for (Pair<Integer, Integer> index : STARTING_PIECES[piece].indices) {
            int curRow = index.getKey() + rowPieceOffsets[piece];
            int curColumn = index.getValue() + columnPieceOffsets[piece];
            switch (direction) {
                case UP: {
                    if (curRow - 1 >= 0) {
                        if (this.occupiedBoard[curRow - 1][curColumn] == true){
                            if (!isSamePiece(piece, curRow - 1, curColumn)){
                                return false;
                            }
                        }
                    }
                    else{
                        return false;
                    }
                    break;
                }
                case DOWN: {
                    if (curRow + 1 < 10) {
                        if (this.occupiedBoard[curRow + 1][curColumn] == true){
                            if (!isSamePiece(piece, curRow + 1, curColumn)){
                                return false;
                            }
                        }
                    }
                    else{
                        return false;
                    }
                    break;
                }
                case LEFT: {
                    if (curColumn - 1 >= 0) {
                        if (this.occupiedBoard[curRow][curColumn - 1] == true){
                            if (!isSamePiece(piece, curRow, curColumn - 1)){
                                return false;
                            }
                        }
                    }
                    else{
                        return false;
                    }
                    break;
                }
                case RIGHT: {
                    if (curColumn + 1 < 10) {
                        if (this.occupiedBoard[curRow][curColumn + 1] == true){
                            if (!isSamePiece(piece, curRow, curColumn + 1)){
                                return false;
                            }
                        }
                    }
                    else{
                        return false;
                    }
                    break;
                }
                default:{
                    throw new RuntimeException("Unknown direction");
                }
            }
        }

        return true;
    }

    private void doMove(int piece, Direction direction) {
        Pair<Integer, Integer> newPieceOffsets = null;


        switch (direction) {
            case UP: {
                newPieceOffsets = new Pair<Integer, Integer>(rowPieceOffsets[piece] - 1, columnPieceOffsets[piece]);
                break;
            }
            case DOWN: {
                newPieceOffsets = new Pair<Integer, Integer>(rowPieceOffsets[piece] + 1, columnPieceOffsets[piece]);
                break;
            }
            case LEFT: {
                newPieceOffsets = new Pair<Integer, Integer>(rowPieceOffsets[piece], columnPieceOffsets[piece] - 1);
                break;
            }
            case RIGHT: {
                newPieceOffsets = new Pair<Integer, Integer>(rowPieceOffsets[piece], columnPieceOffsets[piece] + 1);
                break;
            }
        }
        if(newPieceOffsets == null) throw new RuntimeException("Unknown Direction");

        this.rowPieceOffsets[piece] = newPieceOffsets.getKey();
        this.columnPieceOffsets[piece] = newPieceOffsets.getValue();
        this.occupiedBoard = this.makeOccupiedBoard();
    }

    private boolean[][] makeOccupiedBoard(){
        boolean[][] board = new boolean[10][10];

        //Draw black
        for (int row = 0, column = 0; row < 10; row++){
            board[row][column] = true;
        }
        for (int row = 0, column = 9; row < 10; row++){
            board[row][column] = true;
        }
        for (int row = 0, column = 0; column < 10; column++){
            board[row][column] = true;
        }
        for (int row = 9, column = 0; column < 10; column++){
            board[row][column] = true;
        }

        board[1][1] = board[1][2] = board[2][1] = true;
        board[1][7] = board[1][8] = board[2][8] = true;
        board[7][1] = board[8][1] = board[8][2] = true;
        board[7][8] = board[8][7] = board[8][8] = true;
        board[3][4] = board[4][3] = board[4][4] = true;

        //Draw pieces
        for (PuzzlePiece piece : STARTING_PIECES){
            int rowOffset = this.rowPieceOffsets[piece.number];
            int columnOffset = this.columnPieceOffsets[piece.number];

            for (Pair<Integer, Integer> index : piece.indices){
                if(board[index.getKey() + rowOffset][index.getValue() + columnOffset] != false) {
                    throw new RuntimeException("A piece is on top of another piece or in a black spot");
                }
                board[index.getKey() + rowOffset][index.getValue() + columnOffset] = true;
            }
        }

        return board;
    }

    private boolean isSamePiece(int piece, int row, int col){
        for (Pair<Integer, Integer> index : STARTING_PIECES[piece].indices) {
            Integer r = index.getKey() + this.rowPieceOffsets[piece];
            Integer c = index.getValue() + this.columnPieceOffsets[piece];
            if(r.equals(row) && c.equals(col)) return true;
        }
        return false;
    }

    @Override
    public String toString(){
        char[][] board = new char[10][10];
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++){
                board[x][y] = ' ';
            }
        }

        //Draw black
        for (int x = 0, y = 0; x < 10; x++){
            board[x][y] = '#';
        }
        for (int x = 0, y = 9; x < 10; x++){
            board[x][y] = '#';
        }
        for (int x = 0, y = 0; y < 10; y++){
            board[x][y] = '#';
        }
        for (int x = 9, y = 0; y < 10; y++){
            board[x][y] = '#';
        }

        board[1][1] = board[1][2] = board[2][1] = '#';
        board[1][7] = board[1][8] = board[2][8] = '#';
        board[7][1] = board[8][1] = board[8][2] = '#';
        board[7][8] = board[8][7] = board[8][8] = '#';
        board[3][4] = board[4][3] = board[4][4] = '#';

        //Draw pieces
        for (PuzzlePiece piece : STARTING_PIECES){
            int rowOffset = this.rowPieceOffsets[piece.number];
            int columnOffset = this.columnPieceOffsets[piece.number];

            for (Pair<Integer, Integer> index : piece.indices){
                if(board[index.getKey() + rowOffset][index.getValue() + columnOffset] != ' ') {
                    throw new RuntimeException("A piece is on top of another piece or in a black spot");
                }
                board[index.getKey() + rowOffset][index.getValue() + columnOffset] = piece.symbol;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < 10; x++){
            sb.append(new String(board[x]));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object object){
        if(object == null || object.getClass() != this.getClass()){
            return false;
        }
        PuzzleState other = (PuzzleState) object;
        for(int x = 0; x < this.rowPieceOffsets.length; x++){
            if (this.rowPieceOffsets[x] != other.rowPieceOffsets[x]) return false;
        }
        for(int x = 0; x < this.rowPieceOffsets.length; x++){
            if (this.columnPieceOffsets[x] != other.columnPieceOffsets[x]) return false;
        }

        return true;
    }

    @Override
    public int hashCode(){
        return 1013 * (Arrays.hashCode(rowPieceOffsets)) ^ 1009 * (Arrays.hashCode(columnPieceOffsets));
    }
}
