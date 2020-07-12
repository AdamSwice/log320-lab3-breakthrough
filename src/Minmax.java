import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Minmax {
    private int depth;
    private double amountOfMovesCalled;
    private int pruned = 0;
    private Board boardObject;

    public Minmax(Board board, int depth){
        this.boardObject = board;
        this.depth = depth;
    }

    //TODO: fct qui kickstart l'algo de minmaxstart et qui dois retourner un string qui indique le move a faire une fois qu'on recoit
    // l'état de board que l'algo a choisit.
    public String makeMove(){
        return "yo";
    }

    private int[][] minimaxStart(int depth){
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        ArrayList<int[][]> possibleMoves;
        possibleMoves = this.boardObject.getAllValidMoves();
        ArrayList<Double> heuristic = new ArrayList<>();

        for (int i = 0; i < possibleMoves.size(); i++){
            Board tempBoard = boardObject;
            tempBoard.setBoard(possibleMoves.get(i));
            //TODO:
            heuristic.add(minimax(tempBoard, depth - 1, alpha, beta));
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
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    private double minimax(Board board, int depth, double alpha, double beta){
        if (depth == 0){
            return getHeuristic();
        }
        ArrayList<int[][]> possibleMoves = board.getAllValidMoves();
        Board tempBoard = null;

        double init = Double.NEGATIVE_INFINITY;

        //Home player is always MAX
        for (int i = 0; i < possibleMoves.size(); i++){
            tempBoard = board;
            tempBoard.setBoard(possibleMoves.get(i));

            double result = minimax(tempBoard, depth - 1, alpha, beta);

            init = Math.max(result, init);
            alpha = Math.max(alpha, init);

        }
        return init;
    }

    private double getHeuristic(){
        Random rand = new Random();
        return rand.nextInt((100) + 1);
    }

}
