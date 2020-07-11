import java.util.Arrays;

public class Board {
    private int[][] board = new int[8][8];
    private int playerType;
    private final int BLACK = 2;
    private final int RED = 4;
    private final int EMPTY = 0;

    public Board(String Board, int playerType){
        createBoard(Board);
        this.playerType = playerType;
    }

    private void createBoard(String board){
        StringBuilder reversedBoard= new StringBuilder();
        reversedBoard.append(board);
        reversedBoard.reverse();
        String[] boardValues;
        String reversedBoardString = ""+reversedBoard;
        boardValues = reversedBoardString.split(" ");

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
        //Checking if locations from and to are valid in the first place
        if(!isValidLocation(fromRow, fromColumn) || !isValidLocation(toRow, toColumn)){
            return false;
        }

        //checking if there is no horizontal movements
        if (toColumn > fromColumn + 1 || toColumn < fromColumn - 1) {
            return false;
        }
        int piece = getPieceAt(fromRow, fromColumn);
        //Checking if movement options are valid as black (2)
        if (piece == BLACK && playerColor == BLACK){
            if (toRow != fromRow + 1){
                 return false;
            } else if (getPieceAt(toRow, toColumn) == 0){
                return true;
            } else if (getPieceAt(toRow, toColumn) == RED && toColumn != fromColumn) {
                return true;
            }
        } else if (piece == RED && playerColor == RED){
            if ( toRow != fromRow +1){
                return false;
            } else if (getPieceAt(toRow, toColumn) == 0){
                return true;
            } else if (getPieceAt(toRow, toColumn) == BLACK && toColumn != fromColumn){
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


    private int getPieceAt(int row, int column) {
        return this.board[row][column];
    }

    public void printBoard(){
        System.out.println(Arrays.deepToString(this.board).replace("], ", "]\n"));
    }
}
