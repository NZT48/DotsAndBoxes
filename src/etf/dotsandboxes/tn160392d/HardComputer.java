package etf.dotsandboxes.tn160392d;

import java.util.ArrayList;


public class HardComputer extends MiniMaxPlayer {
	
	// For time limit
    private long startTime;
    private long maxMoveTime = 2000000000;

    // Huristics for Hard Comupter includes score difference, number of boxes with 3 and 2 lines
	protected int heuristic(final Board board, int color) {
		int value = 0;
		
		if (referenceColor == Board.RED)
			value = 20 * board.getRedScore() - 20 * board.getBlueScore();
		else
			value = 20 * board.getBlueScore() - 20 * board.getRedScore();
		
		if (referenceColor == color)
			value += 10 * board.getBoxCount(3) - 2 * board.getBoxCount(2);
		else
			value -= 10 * board.getBoxCount(3) - 2 * board.getBoxCount(2);
		
		// If it is end, add bonus points
		if (board.isComplete()) {
			if (board.getWinner() == referenceColor) {
				value += 1000;
			} else {
				value -= 1000;
			}
		}
		
		return value;
	}
	
    @Override
	public Line getNextMove(Board board, int color, int depth) {
		
		referenceColor = color;
		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int nextMove = -1;
		int bestScore = MIN;
		int thirdLineMove = -1; // Used to remember index of move that adds third line to box
		StringBuilder sb = null; // Used for dialog output in step by step mode
		
		if(hWindow != null) {
			sb = new StringBuilder();
		}

		// Used if we have multiple moves with best score
		ArrayList<Integer> sameScores = new ArrayList<Integer>();
		
		startTime = System.nanoTime();
		
		for (int i = 0; i < moveCount; i++) {
			
			Board nextBoard = board.getNewBoard(moves.get(i), color);
	        
			if (nextBoard.getBoxCount(4) > board.getBoxCount(4)) {
				nextMove = i;
				if(hWindow != null)  sb.append("\nBest heuristic! Closing the box with move "+ Player.convertToOutput(moves.get(i)));
				break; // Break because we can close the box
			}
			
			if (nextBoard.getBoxCount(3) > board.getBoxCount(3)) {
				thirdLineMove = i;
				if(hWindow != null)  sb.append("\nWorst heuristic! Move is adding third line " + Player.convertToOutput(moves.get(i)));
				continue; // We are not interested in heuristics of this move
			}
			
			LineAndValue tmp = null;
            if(!((System.nanoTime() - startTime) > maxMoveTime))
				tmp = alphabeta(nextBoard, depth, MIN, MAX, false, color);
            else
            	break; // Time ran out, stop
            
            if(hWindow != null)  sb.append("\nHeuristics of move: "+ Player.convertToOutput(moves.get(i)) + " is " + tmp.getValue());
			
            if (tmp.getValue() > bestScore) {
				nextMove = i;
				bestScore = tmp.getValue();
				// New best score, clear array with previous best scores
				sameScores.clear();
				sameScores.add(i);
			} else if(tmp.getValue() == bestScore) {
				sameScores.add(i);
			}
			
			
		}
		
		// Multiple moves with best score, choose random one
		if( sameScores.size() > 1) {
			int x = (int) (Math.random() * sameScores.size());
			nextMove = sameScores.get(x);
		}
		
   		// Write to dialog text box
		if(hWindow != null) {
			hWindow.setText(sb.toString());
		}
		
		// If there is no other move, we play a move where we add third line to box
		if (nextMove == -1) {
			nextMove = thirdLineMove;
		}

		return moves.get(nextMove);
	}

	LineAndValue alphabeta(Board board, int depth, int a, int b, boolean max, int color) {

		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int bestMove = -1;

   		// No possible moves or the maximum depth or time ran out
		if (depth == 0 || moves.size() == 0 || (System.nanoTime() - startTime) > maxMoveTime)
			return new LineAndValue(null, heuristic(board, color));

		if (max) {
			
			int bestScore = MIN;
			bestMove = -1;

			// Go through all possible moves
			for (int i = 0; i < moveCount; i++) {
				
				Board nextBoard = board.getNewBoard(moves.get(i), color);
				
				LineAndValue tmp = alphabeta(nextBoard, depth - 1, a, b, false, (color == Board.RED) ? Board.BLUE : Board.RED);
				
				if (tmp.getValue() > bestScore) {
					bestMove = i;
					bestScore = tmp.getValue();
				}

				a = Math.max(a, bestScore);

				if (a >= b)
					break; // Pruning
			}

			return new LineAndValue(moves.get(bestMove), bestScore);

		} else { // Minimum player
			int worstScore = MAX;
			bestMove = -1;

			// Go through all possible moves
			for (int i = 0; i < moveCount; i++) {
				Board nextBoard = board.getNewBoard(moves.get(i), color);

				LineAndValue tmp = alphabeta(nextBoard, depth - 1, a, b, true, (color == Board.RED) ? Board.BLUE : Board.RED);

				if (tmp.getValue() < worstScore) {
					bestMove = i;
					worstScore = tmp.getValue();
				}

				b = Math.min(b, worstScore);

				if (a >= b)
					break; // Pruning
			}

			return new LineAndValue(moves.get(bestMove), worstScore);
		}

	}
	
}
