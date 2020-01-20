package etf.dotsandboxes.tn160392d;

import java.util.ArrayList;

public class EasyComputer implements Player {

	@Override
	public Line getNextMove(Board board, int color, int depth) {

		ArrayList<Line> moves = board.getAvailableMoves();
		int moveCount = moves.size();
		int nextMove = -1;

		for (int i = 0; i < moveCount; i++) {
			Board nextBoard = board.getNewBoard(moves.get(i), color);
			if (nextBoard.getBoxCount(4) > board.getBoxCount(4))
				return moves.get(i); // Return the move that closes box
		}
		
		if (nextMove == -1)
			nextMove = (int) (Math.random() * moveCount);

		return moves.get(nextMove);
	}

}
