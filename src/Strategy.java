public class Strategy {
    private Board board;
    private String dernierCoup;

    public Strategy(Board board){
        this.board = board;
    }

    public void coupAdversaire(String dernierCoup){
        this.dernierCoup = dernierCoup;
    }

    //TODO: Generateur de mouvements
    public void executeMove(){
//        minmaxTree = Minimax(this.board);
//        decisions = minmaxTree.getValue();
//        destination = decisions[0];
//        moveDirection = decisions[1];
    }

    public Board getBoard() {
        return board;
    }


    public String getDernierCoup() {
        return dernierCoup;
    }
}
