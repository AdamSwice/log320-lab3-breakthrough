import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int BOARDSIZE = 8;
    private int[][] board = new int[BOARDSIZE][BOARDSIZE];
    public int playerType;
    public int AIColor;
    public int numberOfRedPiece=16;
    public int numberOfBlackPiece=16;
    private final int BLACK = 2;
    private final int RED = 4;
    private final int EMPTY = 0;

    public Board(String Board, int playerType){

        createBoard(Board);
        this.playerType = playerType;

        if(playerType == RED)
            AIColor=BLACK;
        else
            AIColor = RED;

    }
    public Board(int [][] board, int playerType,int AIColor){
        this.board=board;
        this.playerType = playerType;
        this.AIColor= AIColor;
    }
    public Board clone()
    {

        int[][] newBoard =CopyArray(board);
        Board b = new Board(newBoard,this.playerType,this.AIColor);
        return b;
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
        int piece = getPieceAt(fromRow, fromColumn);
        int pieceDestination = getPieceAt(toRow, toColumn);
        if (playerColor == BLACK){
            playerAdjustment = 1;
        } else {
            playerAdjustment = -1;
        }

        //checking if there is no horizontal movements
        if (fromRow == toRow) {
            return false;
        }
        if(fromColumn == toColumn && pieceDestination!=0){
            return false;
        }


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
            if(board[toRow][toColumn]==BLACK)
                numberOfBlackPiece--;
            else if(board[toRow][toColumn]==RED)
                numberOfRedPiece--;
            board[toRow][toColumn] = getPieceAt(fromRow, fromColumn);
            board[fromRow][fromColumn] = EMPTY;
            //didIwin();
            //TODO: voir si il faut mettre une methode qui determine si on a gagner
        }
    }

    public ArrayList<Board> getAllValidMoves(int color){
        ArrayList<Board> possibleMoves = new ArrayList<>();

        for (int row = 0; row < BOARDSIZE; row++){
            for (int column = 0; column < BOARDSIZE; column++){
                int piece = getPieceAt(row, column);
                if (piece == color){
                    possibleMoves.addAll(getValidMoves(row, column, color));
                }
            }
        }

        return possibleMoves;
    }

    public ArrayList<Board> getValidMoves(int row, int column, int color){

        ArrayList<Board> possibleMoves = new ArrayList<>();

        if (color == 4){
            //Diagonal left
            this.moveAndReturnBoard(row, column, row-1, column-1, color, this.clone(), possibleMoves);
            //Forward
            this.moveAndReturnBoard(row, column, row-1, column, color, this.clone(), possibleMoves);
            //Diagonal right
            this.moveAndReturnBoard(row, column, row-1, column+1, color, this.clone(), possibleMoves);
        } else {
            //Diagonal left
            this.moveAndReturnBoard(row, column, row+1, column-1, color, this.clone(), possibleMoves);
            //Forward
            this.moveAndReturnBoard(row, column, row+1, column, color, this.clone(), possibleMoves);
            //Diagonal right
            this.moveAndReturnBoard(row, column, row+1, column+1, color, this.clone(), possibleMoves);
        }
        return possibleMoves;

    }

    private void moveAndReturnBoard(int fromRow, int fromColumn, int toRow, int toColumn, int playerColor, Board tempBoard, ArrayList<Board> possibleMoves){
        if (isMoveValid(fromRow, fromColumn, toRow, toColumn, playerColor)){
            if(tempBoard.getBoard()[toRow][toColumn]==BLACK)
                tempBoard.numberOfBlackPiece--;
            else if(tempBoard.getBoard()[toRow][toColumn]==RED)
                tempBoard.numberOfRedPiece--;
            tempBoard.modifyBoard(toRow,toColumn,getPieceAt(fromRow, fromColumn));
            tempBoard.modifyBoard(fromRow, fromColumn, EMPTY);
            possibleMoves.add(tempBoard);
        }
    }
    public int[][] CopyArray(int[][] original){
        int[][] newBoard = new int[BOARDSIZE][BOARDSIZE];
        for(int i = 0; i < BOARDSIZE; i++) {
            for(int j = 0; j< BOARDSIZE; j++) {
                newBoard[i][j] = original[i][j];
            }
        }
        return newBoard;
    }


    public int getPlayerType() {
        return playerType;
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
    public void modifyBoard(int i,int j,int value){
        board[i][j]=value;
    }



}
