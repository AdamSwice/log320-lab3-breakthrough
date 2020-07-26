import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Minmax {
    private int depth;
    private double amountOfMovesCalled;
    private int pruned = 0;
    private Board boardObject;
    private final int BOARDSIZE = 8;

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
        if (depth == 0) {
            return getHeuristic(board);
        }

        if(currentPlayer) {
            ArrayList<Board> possibleMoves = board.getAllValidMoves(board.playerType);
            double bestValue = Double.NEGATIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = possibleMoves.get(i);
                System.out.println("This is a playerBoard "+depth);
                tempBoard.printBoard();
                System.out.println("-----------------------------------------------------------------");

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
                System.out.println("Enemy Board "+depth);
                tempBoard.printBoard();
                System.out.println("-----------------------------------------------------------------");
                double value = minimax(tempBoard, depth - 1, !currentPlayer, alpha, beta);

                bestValue = Math.min(value, bestValue);
                beta = Math.min(alpha, bestValue);
                if (alpha >= beta)
                    break;
            }
            return bestValue;
        }

    }

    private double getHeuristic(Board board){

        return board.numberOfRedPiece-board.numberOfBlackPiece;
    }



    public void updateBoard(Board boardObject) {
        this.boardObject = boardObject;
    }
}
