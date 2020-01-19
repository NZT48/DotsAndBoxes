package dotbox;

import java.util.ArrayList;



public class EasyComputer extends Player {

	@Override
	public Line getNextMove(Board board, int color, int depth) {
        referenceColor = color;
        ArrayList<Line> moves = board.getAvailableMoves();
        int moveCount = moves.size();
        int nextMove = -1;
        
        for(int i =  0; i < moveCount; i++) {
            Board nextBoard = board.getNewBoard(moves.get(i), color);
            if(nextBoard.getBoxCount(4) > board.getBoxCount(4))// nextMove = i;
            	return moves.get(i);
        }

        if(nextMove == -1)
        	nextMove = (int)(Math.random() * moveCount);
        	
        return moves.get(nextMove);
	}

}
