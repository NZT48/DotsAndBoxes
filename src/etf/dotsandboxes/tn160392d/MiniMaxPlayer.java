package etf.dotsandboxes.tn160392d;

import java.util.ArrayList;

public abstract class MiniMaxPlayer implements Player{
	
	final static int MIN = -1000000000, MAX = 1000000000;

    protected int referenceColor;

    protected abstract int heuristic(final Board board, int color);
    
    @Override
	public Line getNextMove(Board board, int color, int depth) {
		
		referenceColor = color;
		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int nextMove = -1;
		int bestScore = MIN;
		int flagThree = -1;

		for (int i = 0; i < moveCount; i++) {
			Board nextBoard = board.getNewBoard(moves.get(i), color);
			if (nextBoard.getBoxCount(4) > board.getBoxCount(4)) {
				nextMove = i;
				break; // Break because we can close the box
			}
			if (nextBoard.getBoxCount(3) > board.getBoxCount(3)) {
				flagThree = i;
				continue;
			}
			LineAndValue tmp = alphabeta(nextBoard, depth, MIN, MAX, false, color);
			if (tmp.getValue() > bestScore) {
				nextMove = i;
				bestScore = tmp.getValue();
			}
		}

		// If there is no other move, we play a move where we add third line to box
		if (nextMove == -1) {
			nextMove = flagThree;
		}

		return moves.get(nextMove);
	}

	LineAndValue alphabeta(Board board, int depth, int a, int b, boolean max, int color) {

		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int bestMove = -1;

		if (depth == 0 || moves.size() == 0)
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

		} else { // Min player
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
