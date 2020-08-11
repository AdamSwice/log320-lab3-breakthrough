import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Minmax {
    private int depth;
    private Board boardObject;
    private final int BOARDSIZE = 8;
    public final int BLACK = 2;
    public final int RED = 4;
    public final int EMPTY = 0;
    public final int WinValue = 500000;
    public final short PieceColumnHoleValue = 500;
    public final int PieceHomeGroundValue = 8000;

    public final short AttackedPieceValue = 200;
    public final short PieceValue = 5000;
    public final int PieceProtectionValue = 150;
    public final int PieceConnectionVValue = 25;
    public final int PieceConnectionHValue = 40;


    public Minmax(Board board, int depth) {
        this.boardObject = board;
        this.depth = depth;
    }

    public String makeMove() {
        Utilitaire util = new Utilitaire("Output");
        int[][] newBoardState;
        int[][] oldBoard = boardObject.getBoard();
        String positionInitial = "";
        String positionFinale = "";
        String move;
        newBoardState = minimaxStart(depth, true);

        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                if (oldBoard[i][j] != newBoardState[i][j]) {
                    if (newBoardState[i][j] == 0) {
                        positionInitial = "" + i + j;

                    } else {
                        positionFinale = "" + i + j;
                    }
                }
            }
        }
        move = util.getConvertedOutputMoveValues(positionInitial);
        move += util.getConvertedOutputMoveValues(positionFinale);
        return move;
    }

    private int[][] minimaxStart(int depth, boolean humanPlayer) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        Board clonedBoard = boardObject.clone();
        ArrayList<Board> possibleMoves;
        possibleMoves = clonedBoard.getAllValidMoves(boardObject.playerType);
        ArrayList<Double> heuristic = new ArrayList<>();

        for (int i = 0; i < possibleMoves.size(); i++) {
            Board tempBoard = possibleMoves.get(i);

            heuristic.add(minimax(tempBoard, depth - 1, !humanPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;
        Random random = new Random();

        for (int i = heuristic.size() - 1; i >= 0; i--) {
            if (heuristic.get(i) >= maxHeuristics) {
                maxHeuristics = heuristic.get(i);
            }
        }
        System.out.println(maxHeuristics);
        for (int i = 0; i < heuristic.size(); i++) {
            if (heuristic.get(i) < maxHeuristics) {
                heuristic.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }
        return possibleMoves.get(random.nextInt(possibleMoves.size())).getBoard();
    }

    private double minimax(Board board, int depth, boolean currentPlayer, double alpha, double beta) {
        Board tempBoard = null;
        if (depth == 0 || board.playerWin || board.AIWin) {
            return getHeuristic(board, depth, boardObject.playerType);
        }

        if (currentPlayer) {
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

        } else {
            ArrayList<Board> possibleMoves = board.getAllValidMoves(board.AIColor);
            double bestValue = Double.POSITIVE_INFINITY;

            //Home player is always MAX
            for (int i = 0; i < possibleMoves.size(); i++) {
                tempBoard = possibleMoves.get(i);

                double value = minimax(tempBoard, depth - 1, currentPlayer, alpha, beta);

                bestValue = Math.min(value, bestValue);
                beta = Math.min(alpha, bestValue);
                if (alpha >= beta)
                    break;
            }
            return bestValue;
        }

    }

    private double getHeuristic(Board board, int depth, int playerColor) {

        return stategieDeff(board, depth, playerColor);
    }


    public double stategieDeff(Board board, int depth, int playerColor) {
        double Points = 0;

        int gameBoard[][] = board.getBoard();
        for (int j = 0; j < gameBoard[0].length; j++) {
            int BlackPiecesOnColumn = 0;
            int RedPiecesOnColumn = 0;

            for (int i = 0; i < gameBoard.length; i++) {
                if (gameBoard[i][j] == EMPTY)
                    continue;
                if (gameBoard[i][j] == RED) {
                    RedPiecesOnColumn++;
                    Points += GetPieceValue(board, i, j, RED);
                    if (i == 0) board.redWin = true;
                    if (i == 7) Points += PieceHomeGroundValue;
                } else {
                    BlackPiecesOnColumn++;
                    Points -= GetPieceValue(board, i, j, BLACK);
                    if (i == 7) board.blackWin = true;
                    if (i == 0) Points -= PieceHomeGroundValue;
                }

            }
            if (RedPiecesOnColumn == 0)
                Points -= PieceColumnHoleValue;

            if (BlackPiecesOnColumn == 0)
                Points += PieceColumnHoleValue;

        }

        if (board.blackPieces == 0)
            board.redWin = true;
        if (board.redPieces == 0)
            board.blackWin = true;

        if (board.redWin)
            Points += WinValue;// depth *
        if (board.blackWin)
            Points -= WinValue;//depth *

        if (playerColor == BLACK) Points = (-1) * (Points);

        return Points;
    }


    public int GetPieceValue(Board boardObject, int i, int j, int playerColor) {
        int value = 0;
        int board[][] = boardObject.getBoard();
        //boardObject.printBoard();
        if (playerColor == RED) {
            value = PieceValue;
        } else {
            value = PieceValue;
        }
        int protectionValue = 0;
        int attackValue = confirmAttackedValue(board, i, j);

        if (confirmHConnection(board, i, j)) {
            value += PieceConnectionHValue;
        }
        if (confirmVConnection(board, i, j)) {
            value += PieceConnectionVValue;
        }
        if (i < 7 && i > 0) {
            protectionValue = confirmprotectionValue(board, i, j);
            if (playerColor == RED) {
                value += protectionValue;
            } else {
                value += protectionValue;
            }
        }

        if (attackValue > 0) {
            value -= attackValue;
        } else {
            value += 100;
        }

        return value;
    }

    public boolean confirmHConnection(int[][] board, int i, int j) {

        if (j > 0) {

            if (board[i][j - 1] == board[i][j]) {
                return true;
            }
        }
        if (j < 7) {

            if (board[i][j + 1] == board[i][j]) {
                return true;
            }
        }
        return false;
    }

    public boolean confirmVConnection(int[][] board, int i, int j) {

        if (i > 1) {

            if (board[i - 1][j] == board[i][j]) {
                return true;
            }
        }
        if (i < 6) {

            if (board[i + 1][j] == board[i][j]) {
                return true;
            }
        }
        return false;
    }

    public int confirmprotectionValue(int[][] board, int i, int j) {
        int protectionValue = 0;
        if (j > 0) {
            if (board[i][j] == RED) {
                int protectedRow = Math.max(i - 1, 0);
                if (board[protectedRow][j - 1] == board[i][j])
                    protectionValue += i * PieceProtectionValue;
            } else if (board[i][j] == BLACK) {
                int protectedRow = Math.min(i + 1, 7);
                if (board[protectedRow][j - 1] == board[i][j])
                    protectionValue += (7 - i) * PieceProtectionValue;
            }
        }
        if (j < 7) {
            if (board[i][j] == RED) {
                int protectedRow = Math.max(i - 1, 0);
                if (board[protectedRow][j + 1] == board[i][j])
                    protectionValue += i * PieceProtectionValue;
            } else if (board[i][j] == BLACK) {
                int protectedRow = Math.min(i + 1, 7);
                if (board[protectedRow][j + 1] == board[i][j])
                    protectionValue += (7 - i) * PieceProtectionValue;
            }
        }
        return protectionValue;
    }

    public int confirmAttackedValue(int[][] board, int i, int j) {
        int attackedValue = 0;
        if (j > 0) {

            if (board[i][j] == RED) {
                if (i >= 0) {
                    int attackersRow = Math.max(i - 1, 0);
                    if (board[attackersRow][j - 1] == BLACK && i == 1) {
                        attackedValue += i * WinValue;
                    } else if (board[attackersRow][j - 1] == BLACK && i == 7) {
                        attackedValue += i * WinValue;
                    } else if (board[attackersRow][j - 1] == BLACK) {
                        attackedValue += (10 - i) * AttackedPieceValue;
                    }
                }
            } else if (board[i][j] == BLACK) {
                if (i <= 7) {
                    int attackersRow = Math.min(i + 1, 7);
                    if (board[attackersRow][j - 1] == RED && i == 6) {
                        attackedValue += (7 - i) * WinValue;
                    } else if (board[attackersRow][j - 1] == RED && i == 0) {
                        attackedValue += (7 - i) * WinValue;
                    } else if (board[attackersRow][j - 1] == RED) {
                        attackedValue += (i + 3) * AttackedPieceValue;
                    }

                }
            }
        }
        if (j < 7) {
            if (board[i][j] == RED) {
                if (i >= 0) {
                    int attackersRow = Math.max(i - 1, 0);
                    if (board[attackersRow][j + 1] == BLACK && i == 1) {
                        attackedValue += i * WinValue;
                    } else if (board[attackersRow][j + 1] == BLACK && i == 7) {
                        attackedValue += i * WinValue;
                    } else if (board[attackersRow][j + 1] == BLACK) {
                        attackedValue += (10 - i) * AttackedPieceValue;
                    }

                }
            } else if (board[i][j] == BLACK) {
                if (i <= 7) {
                    int attackersRow = Math.min(i + 1, 7);
                    if (board[attackersRow][j + 1] == RED && i == 6) {
                        attackedValue += (7 - i) * WinValue;
                    } else if (board[attackersRow][j + 1] == RED && i == 0) {
                        attackedValue += (7 - i) * WinValue;
                    } else if (board[attackersRow][j + 1] == RED) {
                        attackedValue += (i + 3) * AttackedPieceValue;
                    }
                }
            }
        }
        return attackedValue;
    }
}
