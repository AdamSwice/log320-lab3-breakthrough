import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int BOARDSIZE = 8;
    private int[][] board = new int[BOARDSIZE][BOARDSIZE];
    public int playerType;
    private final int BLACK = 2;
    private final int RED = 4;
    private final int EMPTY = 0;

    public Board(String Board, int playerType){
        createBoard(Board);
        this.playerType = playerType;
    }

    private void createBoard(String board){
        String[] boardValues;
        boardValues = board.split(" ");
        int x=0,y=0;
        for(int i=0; i<boardValues.length;i++){
            this.board[x][y] = Integer.parseInt(boardValues[i]);
            y++;
            if(y == 8){
                y = 0;
                x++;
            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }


    public boolean isValidLocation(int row, int column) {
        if (row < 0 || row >= 8 || column < 0 || column >= 8){
            return false;
        } else {
            return true;
        }
    }

    public boolean isMoveValid(int fromRow, int fromColumn, int toRow, int toColumn, int playerColor){

        int playerAdjustment = 0;
        //Checking if locations from and to are valid in the first place
        if(!isValidLocation(fromRow, fromColumn) || !isValidLocation(toRow, toColumn)){
            return false;
        }

        if (playerColor == BLACK){
            playerAdjustment = 1;
        } else {
            playerAdjustment = -1;
        }

        //checking if there is no horizontal movements
        if (fromRow == toRow) {
            return false;
        }

        int piece = getPieceAt(fromRow, fromColumn);
        //Checking if movement options are valid as black (2)
        if (piece == BLACK && playerColor == BLACK){
            if (toRow != fromRow + playerAdjustment){
                 return false;
            } else if (getPieceAt(toRow, toColumn) == 0){
                return true;
            } else if (getPieceAt(toRow, toColumn) == RED) {
                return true;
            }
        } else if (piece == RED && playerColor == RED){
            if ( toRow != fromRow + playerAdjustment){
                return false;
            } else if (getPieceAt(toRow, toColumn) == 0){
                return true;
            } else if (getPieceAt(toRow, toColumn) == BLACK){
                return true;
            }
        }
        return false;
    }

    public void move(int fromRow, int fromColumn, int toRow, int toColumn, int playerColor){
        if (isMoveValid(fromRow, fromColumn, toRow, toColumn, playerColor)){
            board[toRow][toColumn] = getPieceAt(fromRow, fromColumn);
            board[fromRow][fromColumn] = EMPTY;
            //didIwin();
            //TODO: voir si il faut mettre une methode qui determine si on a gagner
        }
    }

    public ArrayList<int[][]> getAllValidMoves(){
        ArrayList<int[][]> possibleMoves = new ArrayList<>();

        for (int row = 0; row < BOARDSIZE; row++){
            for (int column = 0; column < BOARDSIZE; column++){
                int piece = getPieceAt(row, column);
                if (piece == playerType){
                    possibleMoves.addAll(getValidMoves(row, column));
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<int[][]> getValidMoves(int row, int column){
        int[][] tempBoard = board;
        ArrayList<int[][]> possibleMoves = new ArrayList<>();

        if (playerType == 4){
            //Diagonal left
            this.moveAndReturnBoard(row, column, row-1, column-1, playerType, tempBoard, possibleMoves);
            //Forward
            this.moveAndReturnBoard(row, column, row-1, column, playerType, tempBoard, possibleMoves);
            //Diagonal right
            this.moveAndReturnBoard(row, column, row-1, column+1, playerType, tempBoard, possibleMoves);
        } else {
            //Diagonal left
            this.moveAndReturnBoard(row, column, row+1, column-1, playerType, tempBoard, possibleMoves);
            //Forward
            this.moveAndReturnBoard(row, column, row+1, column, playerType, tempBoard, possibleMoves);
            //Diagonal right
            this.moveAndReturnBoard(row, column, row+1, column+1, playerType, tempBoard, possibleMoves);
        }
        return possibleMoves;

    }

    private void moveAndReturnBoard(int fromRow, int fromColumn, int toRow, int toColumn, int playerColor, int[][] tempBoard, ArrayList<int[][]> possibleMoves){
        if (isMoveValid(fromRow, fromColumn, toRow, toColumn, playerColor)){
            tempBoard[toRow][toColumn] = getPieceAt(fromRow, fromColumn);
            tempBoard[fromRow][fromColumn] = EMPTY;
            possibleMoves.add(tempBoard);
        }
    }



    private int getPieceAt(int row, int column) {
        return this.board[row][column];
    }

    public void printBoard(){
        System.out.println(Arrays.deepToString(this.board).replace("], ", "]\n"));
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
