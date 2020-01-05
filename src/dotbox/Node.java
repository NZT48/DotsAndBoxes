package dotbox;



public class Node {
    private Board board ;
    private Node parent ;
    private int color, utility ;
    private Line Line ;
    public final static int MIN = -1000000000 ;

    public Node(Board board, int color, Node parent, Line Line) {
        this.board = board;
        this.color = color;
        this.parent = parent ;
        this.utility = MIN ;
        this.Line = Line ;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Node getParent() {
        return parent;
    }

    public int getColor() {
        return color;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility ;
    }

    public Line getLine() {
        return this.Line ;
    }

    public void setLine(Line Line) {
        this.Line = Line ;
    }

}
