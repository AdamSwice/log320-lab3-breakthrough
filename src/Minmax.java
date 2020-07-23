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
        ArrayList<int[][]> possibleMoves;
        possibleMoves = clonedBoard.getAllValidMoves(boardObject.playerType);
        ArrayList<Double> heuristic = new ArrayList<>();

        for (int i = 0; i < possibleMoves.size(); i++){
            Board tempBoard = boardObject.clone();
            tempBoard.setBoard(possibleMoves.get(i));
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
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    private double minimax(Board board, int depth, boolean currentPlayer, double alpha, double beta){
        double init=0;
        Board tempBoard = null;
        if (depth == 0) {
            return getHeuristic();
        }

        if(currentPlayer) {
            ArrayList<int[][]> possibleMoves = board.getAllValidMoves(board.playerType);
            init = Double.NEGATIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board;
                tempBoard.setBoard(possibleMoves.get(i));

                double result = minimax(tempBoard, depth - 1, !currentPlayer, alpha, beta);

                init = Math.max(result, init);
                alpha = Math.max(alpha, init);
                if (alpha >= beta)
                    break;
            }

        }
        else{
            ArrayList<int[][]> possibleMoves = board.getAllValidMoves(board.AIColor);
            init = Double.POSITIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = board;
                tempBoard.setBoard(possibleMoves.get(i));

                double result = minimax(tempBoard, depth - 1, !currentPlayer, alpha, beta);

                init = Math.max(result, init);
                alpha = Math.max(alpha, init);
                if (alpha >= beta)
                    break;
            }
        }
        return init;
    }

    private double getHeuristic(){
        Random rand = new Random();
        return rand.nextInt((100) + 1);
    }

    public void updateBoard(Board boardObject) {
        this.boardObject = boardObject;
    }
}
