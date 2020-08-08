import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Minmax {
    private int depth;
    private double amountOfMovesCalled;
    private int pruned = 0;
    private Board boardObject;
    private final int BOARDSIZE = 8;
    public final int BLACK = 2;
    public final int RED = 4;
    public final int EMPTY = 0;
    public final int WinValue = 500000;
    public final short PieceAlmostWinValue = 10;
    public final short PieceValue = 1300;
    public final short PieceDangerValue = 10;
    public final short PieceHighDangerValue = 100;
    public final short PieceAttackValue = 50;
    public final short PieceProtectionValue = 65;
    public final short PieceConnectionHValue = 35;
    public final short PieceConnectionVValue = 15;
    public final short PieceColumnHoleValue = 20;
    public final short PieceHomeGroundValue = 10;

    public Minmax(Board board, int depth){
        this.boardObject = board;
        this.depth = depth;
    }

    //TODO: fct qui kickstart l'algo de minmaxstart et qui dois retourner un string qui indique le move a faire une fois qu'on recoit
    // l'état de board que l'algo a choisit.
    public String makeMove(){
        Utilitaire util= new Utilitaire("Output");
        int [][] newBoardState;
        int [][] oldBoard = boardObject.getBoard();
        String positionInitial="";
        String positionFinale="";
        String move;
        newBoardState=minimaxStart(depth,true);

        for(int i = 0;i<BOARDSIZE;i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if(oldBoard[i][j] != newBoardState[i][j]){
                    if(newBoardState[i][j]==0){
                        positionInitial = ""+ i + j;

                    }
                    else{
                        positionFinale=""+ i + j;
                    }
                }
            }
        }
        move=util.getConvertedOutputMoveValues(positionInitial);
        move+=util.getConvertedOutputMoveValues(positionFinale);
       return move;
    }

    private int[][] minimaxStart(int depth, boolean humanPlayer){
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        Board clonedBoard = boardObject.clone();
        ArrayList<Board> possibleMoves;
        possibleMoves = clonedBoard.getAllValidMoves(boardObject.playerType);
        ArrayList<Double> heuristic = new ArrayList<>();

        for (int i = 0; i < possibleMoves.size(); i++){
            Board tempBoard =possibleMoves.get(i);
            //TODO:

            heuristic.add(minimax(tempBoard, depth - 1,humanPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;
        Random random = new Random();

        for (int i = heuristic.size() - 1; i >= 0; i--){
            if (heuristic.get(i) >= maxHeuristics){
                maxHeuristics = heuristic.get(i);
            }
        }

        for (int i = 0; i< heuristic.size(); i++){
            if (heuristic.get(i) < maxHeuristics){
                heuristic.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }
        //TODO : renvoyer l'indexe de qui sera retourner dans l'heuristique
        return possibleMoves.get(random.nextInt(possibleMoves.size())).getBoard();
    }

    private double minimax(Board board, int depth, boolean currentPlayer, double alpha, double beta){
        Board tempBoard = null;
        if (depth == 0 || board.playerWin || board.AIWin) {
            return getHeuristic(board,depth);
        }

        if(currentPlayer) {
            ArrayList<Board> possibleMoves = board.getAllValidMoves(board.playerType);
            double bestValue = Double.NEGATIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = possibleMoves.get(i);
//                System.out.println("This is a playerBoard "+depth);
//                tempBoard.printBoard();
//                System.out.println("-----------------------------------------------------------------");

                double value = minimax(tempBoard, depth - 1, !currentPlayer, alpha, beta);

                bestValue = Math.max(value, bestValue);
                alpha = Math.max(alpha, bestValue);
                if (alpha >= beta)
                    break;
            }
            return bestValue;

        }
        else{
            ArrayList<Board> possibleMoves = board.getAllValidMoves(board.AIColor);
            double bestValue = Double.POSITIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard =possibleMoves.get(i);
//                System.out.println("Enemy Board "+depth);
//                tempBoard.printBoard();
//                System.out.println("-----------------------------------------------------------------");
                double value = minimax(tempBoard, depth - 1, !currentPlayer, alpha, beta);

                bestValue = Math.min(value, bestValue);
                beta = Math.min(alpha, bestValue);
                if (alpha >= beta)
                    break;
            }
            return bestValue;
        }

    }

    private double getHeuristic(Board board,int depth){
        //TODO: doesnt make bad moves necessarily, but doesnt defend itself... WIP.
        int colorMoving=board.playerType;
        if(depth%2!=0){
            colorMoving=board.AIColor;
        }

        return stategieDeff(board,colorMoving);
    }




    public double stategieDeff(Board board,int colorMoving){
        double Points=0;
        int RemainingRedPieces = 0;
        int RemainingBlackPieces = 0;

        int gameBoard[][]=board.getBoard();
        for(int j=0;j<gameBoard[0].length;j++) {
            int BlackPiecesOnColumn = 0;
            int RedPiecesOnColumn = 0;

            for(int i=0;i<gameBoard.length;i++){
                if(gameBoard[i][j]==EMPTY)
                    continue;
                if (gameBoard[i][j]==RED) {
                    RedPiecesOnColumn++;
                    Points += GetPieceValue(gameBoard, i, j);
                    if (i == 0)
                        board.redWin = true;
                    else if (i == 1) {
                        boolean threatA = false;
                        boolean threatB = false;
                        if (j > 0)
                            threatA = (gameBoard[i - 1][0] == EMPTY);
                        if (j > 7)
                            threatB = (gameBoard[i + 1][0] == EMPTY);
                        if ((threatA && threatB))
                            //ici truc wierd
                            Points += PieceAlmostWinValue;
                    } else if (i == 7)
                        Points += PieceHomeGroundValue;
                }
                else{
                    BlackPiecesOnColumn++;
                    Points-= GetPieceValue(gameBoard, i, j);
                    if (i == 7)
                        board.blackWin = true;
                    else if (i == 6) {
                        boolean threatA = false;
                        boolean threatB = false;
                        if (j > 0)
                            threatA = (gameBoard[i - 1][7] == EMPTY);
                        if (j > 7)
                            threatB = (gameBoard[i + 1][7] == EMPTY);
                        if ((threatA && threatB))
                            //ici truc wierd
                            Points-= PieceAlmostWinValue;
                    } else if (i == 0)
                        Points -= PieceHomeGroundValue;

                }
            }
            if(RedPiecesOnColumn==0)
                Points -=PieceColumnHoleValue;
            if(BlackPiecesOnColumn==0)
                Points +=PieceColumnHoleValue;
        }
        if(board.blackPieces==0)
            board.redWin=true;
        if(board.blackPieces==0)
            board.blackWin=true;

        if(board.redWin)
            Points+=WinValue;
        if(board.blackWin)
            Points-=WinValue;
//        if(colorMoving==BLACK){
//            board.value=-board.value;
//        }
           return Points;
    }

    public int GetPieceValue(int[][] board, int i, int j){
        int value=PieceValue;
        int protectionValue=0;
        int attackValue=0;
        if(confirmHConnection(board,i,j))
            value+=PieceConnectionHValue;
        if(confirmVConnection(board,i,j))
            value+=PieceConnectionVValue;
        if(i<7 && i>0) {
            protectionValue = confirmprotectionValue(board, i, j);
            value += protectionValue;
        }

        attackValue=confirmAttackedValue(board,i,j);
        if(attackValue > 0 ) {
            value -= attackValue;
            if (protectionValue == 0)
                value -= attackValue;
        }else{
            if(protectionValue !=0){
                if(board[i][j]==RED){
                    if(i==1)
                        value+=PieceHighDangerValue;
                    else if(i==2)
                        value+=PieceDangerValue;
                }else if(board[i][j]==BLACK){
                    if(i==6)
                        value+=PieceHighDangerValue;
                    else if(i==5)
                        value+=PieceDangerValue;
                }
            }
        }

        if(board[i][j]==BLACK)
            value+= i*PieceDangerValue;
        else
            value+= (8-i)*PieceDangerValue;

        return value;

    }

    public boolean confirmHConnection(int[][] board, int i, int j){

        if (j > 0)
        {

            if (board[i][j-1] ==board[i][j])
            {
                return true;
            }
        }
        if (j < 7)
        {

            if (board[i][j+1] ==board[i][j])
            {
                return true;
            }
        }
        return false;
    }

    public boolean confirmVConnection(int[][] board, int i, int j){

        if (i > 0)
        {

            if (board[i-1][j] ==board[i][j])
            {
                return true;
            }
        }
        if (i < 7)
        {

            if (board[i+1][j] ==board[i][j])
            {
                return true;
            }
        }
        return false;
    }

    public int confirmprotectionValue(int[][] board, int i, int j){
        int protectionValue=0;
        if (j > 0)
        {
            if(board[i][j]==RED) {
                if (board[i + 1][j - 1] == board[i][j])
                    protectionValue += PieceProtectionValue;
            }else if(board[i][j]==BLACK){
                if (board[i - 1][j - 1] == board[i][j])
                    protectionValue += PieceProtectionValue;
            }
        }
        if (j < 7)
        {
            if(board[i][j]==RED) {
                if (board[i + 1][j + 1] == board[i][j])
                    protectionValue += PieceProtectionValue;
            }else if(board[i][j]==BLACK){
                if (board[i - 1][j + 1] == board[i][j])
                    protectionValue += PieceProtectionValue;
            }
        }
        return protectionValue;
    }

    public int confirmAttackedValue(int[][] board, int i, int j){
        int attackedValue=0;
        if (j > 0)
        {

            if(board[i][j]==RED) {
                if(i>0) {
                    if (board[i - 1][j - 1] == BLACK)
                        attackedValue += PieceAttackValue;
                }
            }else if(board[i][j]==BLACK){
                if(i<7) {
                    if (board[i + 1][j - 1] == RED)
                        attackedValue += PieceAttackValue;
                }
            }
        }
        if (j < 7)
        {
            if(board[i][j]==RED) {
                if(i>0) {
                    if (board[i - 1][j + 1] == BLACK)
                        attackedValue += PieceAttackValue;
                }
            }else if(board[i][j]==BLACK){
                if(i<7) {
                    if (board[i + 1][j + 1] == RED)
                        attackedValue += PieceAttackValue;
                }
            }
        }
        return attackedValue;
    }
    public void updateBoard(Board boardObject) {
        this.boardObject = boardObject;
    }
    public boolean onBoard(int i,int j) {
        return i>0 && i<8 && j>0 && j<8;
    }
}
