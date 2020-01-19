package dotbox;

//import java.awt.Color;
import java.util.ArrayList;

public class MediumComputer extends Player {

    final static int MIN=-1000000000, MAX=1000000000 ;

	
	@Override
	public Line getNextMove(Board board, int color, int depth) {
        referenceColor = color;
        ArrayList<Line> moves = board.getAvailableMoves();
        int moveCount = moves.size();
        int nextMove = -1;
        int bestScore = MIN;
        
        for(int i = 0; i<moveCount; i++) {
            Board nextBoard = board.getNewBoard(moves.get(i), color);
            if(nextBoard.getBoxCount(4) > board.getBoxCount(4)) { 
            	nextMove = i;
            	break; // mozemo da zatvorimo pravougaonik
            }
            ABtmp tmp = alphabeta(nextBoard, depth, MIN, MAX,false, color);
            if(tmp.getWeight() > bestScore) {
            	nextMove = i;
            	//System.out.println("\n The value of getWeight is " + tmp.getWeight());
            	bestScore = tmp.getWeight();
            }
        }

        return moves.get(nextMove);
	}
	
	class ABtmp {
		private Line line;
		private int weight;
		
		ABtmp(Line line, int w){
			this.line = line; weight = w;
		}
		
		public Line getLine() {
			return line;
		}
		public void setLine(Line ln) {
			this.line = ln;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
		
		
	}
	
	ABtmp alphabeta(Board board, int depth, int a, int b, boolean max, int color) {
		
		ArrayList<Line> moves = board.getAvailableMoves();
        int moveCount = moves.size();
        //int nextMove = -1;
		
		if(depth == 0 || moves.size() == 0)
			return new ABtmp(null,heuristic(board, color));
		
		//max player - the first call is always max player - uvedena predpostavka proveriti kasnije
		if(max) {
			//ABtmp retVal = new ABtmp(null, MIN);
			
			// prolazi sve sinove
	        for(int i = 0; i < moveCount; i++) {
	        	//formira novi board
	            Board nextBoard = board.getNewBoard(moves.get(i), color);
	            
	            
	            ABtmp tmp = alphabeta(nextBoard, depth - 1, a, b, false, (color == Board.RED) ? Board.BLUE : Board.RED );
	            a = Math.max(a, tmp.getWeight());
	            
	            if( a >= b )
	            	break;
	        }
	        
	        return new ABtmp(null, a);
			
		}else { // min player
	        
	        for(int i = 0; i < moveCount; i++) {
	            Board nextBoard = board.getNewBoard(moves.get(i), color);
	            
	            ABtmp tmp = alphabeta(nextBoard, depth - 1, a, b, true, (color == Board.RED) ? Board.BLUE : Board.RED );
	            b = Math.min(b, tmp.getWeight());
	            
	            if( a >= b )
	            	break;
	        }
	        
	        return new ABtmp(null, b);
		}
		
		//return 0;
	}
	
}
