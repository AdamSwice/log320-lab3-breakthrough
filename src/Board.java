public class Board {
    private int[][] board = new int[8][8];

    public Board(String Board){
        createBoard(Board);
    }

    private void createBoard(String board){
        String[] boardValues;
        boardValues = board.split(" ");
        int x=0,y=0;
        for(int i=0; i<boardValues.length;i++){
            this.board[x][y] = Integer.parseInt(boardValues[i]);
            x++;
            if(x == 8){
                x = 0;
                y++;
            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }
}
